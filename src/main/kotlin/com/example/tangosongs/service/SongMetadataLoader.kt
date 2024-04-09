package com.example.tangosongs.service

import com.example.tangosongs.model.SongEntity
import com.example.tangosongs.repository.SongRepository
import com.example.tangosongs.service.tasks.SongMetadataLoaderTask
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
    @Value("\${application.scheduling.task.${SongMetadataLoaderTask.TASK_NAME}.path-to-files}")
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
        val regex = "(.*?) - (.*?) - (.*?) - (\\d{4})? - (.*?)".toRegex()
        val matchResult = regex.find(file.name)
        val orchestra = matchResult?.groups?.get(1)?.value ?: run {
            log.info { "Orchestra is empty, cannot load this song ${file.name}" }
            return null
        }
        val singers = matchResult?.groups?.get(2)?.value
        val title = matchResult?.groups?.get(3)?.value ?: run {
            log.info { "Title is empty, cannot load this song ${file.name}" }
            return null
        }
        val yearString = matchResult?.groups?.get(4)?.value
        val songEntity = SongEntity(
            id = UUID.randomUUID(),
            name = title,
            orchestra = orchestra,
            singers = singers,
            releaseYear = yearString?.toInt(),
            sourceFilename = file.name,
            sourceLoadedAt = Instant.now()
        )
        return songEntity
    }
}