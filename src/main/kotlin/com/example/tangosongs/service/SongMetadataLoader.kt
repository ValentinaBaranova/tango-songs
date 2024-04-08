package com.example.tangosongs.service

import com.example.tangosongs.model.SongEntity
import com.example.tangosongs.repository.SongRepository
import com.mpatric.mp3agic.Mp3File
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File
import java.time.Instant
import java.util.*
import kotlin.time.measureTime

@Component
class SongMetadataLoader(
    private val songRepository: SongRepository,
    @Value("\${application.song-loader.path-to-files}")
    val pathToSongFiles: String
) {

    private val log = KotlinLogging.logger { }

    fun loadSongs(path: String = pathToSongFiles) {
        if (songRepository.existsBySourceFilenameIsNotNull()) {
            log.info { "Songs were already loaded" }
            return
        }
        log.info { "Starting loading songs metadata from files..." }
        val initialDirectory = File(path)
        val duration = measureTime {
            exploreDirectory(initialDirectory, "mp3")
        }
        log.info { "Songs metadata were loaded in $duration" }
    }

    private fun exploreDirectory(file: File, extension: String) {
        val files = file.listFiles()
        val songFiles = files?.mapNotNull { f ->
            if (f.isDirectory) {
                exploreDirectory(f, extension)
                null
            } else if (f.isFile && f.extension.equals(extension, ignoreCase = true)) {
                f
            } else {
                null
            }
        } ?: return
        val entities = songFiles.mapNotNull { loadSong(it) }
        songRepository.saveAll(entities)
    }

    fun loadSong(file: File): SongEntity? {
        val mp3File = Mp3File(file.path)

        if (mp3File.hasId3v2Tag()) {
            val id3v2Tag = mp3File.id3v2Tag
            val artistInfo = id3v2Tag.artist
            val artistInfoParts = artistInfo.split("-", limit = 2) // разбиваем строку на две части по первому дефису
            val orchestra = artistInfoParts.getOrElse(0) { artistInfo }
            val singers = artistInfoParts.getOrElse(1) { null }
            val releaseYear = extractDate(file.name) ?: run {
                log.info { "Release date not found for ${file.name}" }
                null
            }

            val songEntity = SongEntity(
                id = UUID.randomUUID(),
                name = id3v2Tag.title,
                orchestra = orchestra,
                singers = singers,
                releaseYear = releaseYear,
                sourceFilename = file.name,
                sourceLoadedAt = Instant.now()
            )
            return songEntity
        } else {
            log.info { "No ID3v2 tag found" }
        }
        return null
    }

    fun extractDate(inputString: String): Int? {
        val regex = "(\\d{4})".toRegex() // регулярное выражение для поиска даты в скобках
        val matchResult = regex.find(inputString) // ищем соответствие регулярному выражению в строке
        val yearString = matchResult?.groups?.get(1)?.value ?: return null
        return yearString.toInt()
    }
}