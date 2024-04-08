package com.example.tangosongs.service

import com.example.tangosongs.model.SongEntity
import com.example.tangosongs.repository.SongRepository
import com.mpatric.mp3agic.Mp3File
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatterBuilder
import java.util.*
import java.util.function.Consumer

@Component
class SongMetadataLoader(
    private val songRepository: SongRepository,
    @Value("\${application.song-loader.path-to-files}")
    val pathToSongFiles: String
) {

    private val log = KotlinLogging.logger { }

    private val releaseDateFormatter = DateTimeFormatterBuilder()
        .appendPattern("dd-MM-yyyy")
        .toFormatter()

    fun loadSongs(path: String = pathToSongFiles) {
        //cleanup
        val initialDirectory = File(path)
        exploreDirectory(initialDirectory, "mp3") { loadSong(it) }
    }

    private fun exploreDirectory(file: File, extension: String, consumer: Consumer<File>) {
        val files = file.listFiles()
        files?.forEach { f ->
            if (f.isDirectory) {
                exploreDirectory(f, extension, consumer)
            } else if (f.isFile && f.extension.equals(extension, ignoreCase = true)) {
                consumer.accept(f)
            }
        }
    }

    fun loadSong(file: File) {
        if (songRepository.findBySourceFilename(file.name).isNotEmpty()) {
            return
        }
        val mp3File = Mp3File(file.path)

        if (mp3File.hasId3v2Tag()) {
            val id3v2Tag = mp3File.id3v2Tag

            val artistInfo = id3v2Tag.artist
            log.info { "Title: ${id3v2Tag.title} Artist: ${id3v2Tag.artist}" }
            val artistInfoParts = artistInfo.split("-", limit = 2) // разбиваем строку на две части по первому дефису
            val orchestra = artistInfoParts.getOrElse(0) { artistInfo }
            val singers = artistInfoParts.getOrElse(1) { null }
            val releaseDate = extractDate(file.name) ?: run {
                log.info { "Release date not found for ${file.name}" }
                null
            }

            val songEntity = SongEntity(
                id = UUID.randomUUID(),
                name = id3v2Tag.title,
                orchestra = orchestra,
                singers = singers,
                releaseDate = releaseDate,
                sourceFilename = file.name,
                sourceLoadedAt = Instant.now()
            )
            songRepository.save(songEntity)
        } else {
            log.info { "No ID3v2 tag found" }
        }
    }

    fun extractDate(inputString: String): LocalDate? {
        val regex = "\\((\\d{2}-\\d{2}-\\d{4})\\)".toRegex() // регулярное выражение для поиска даты в скобках
        val matchResult = regex.find(inputString) // ищем соответствие регулярному выражению в строке
        val dateString = matchResult?.groups?.get(1)?.value ?: return null
        return LocalDate.parse(dateString, releaseDateFormatter)
    }
}