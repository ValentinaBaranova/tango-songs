package com.example.tangosongs.service.tasks

import com.example.tangosongs.service.SongMetadataLoader
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@ConditionalOnProperty(
    prefix = "application.scheduling.task",
    name = ["${SpotifyTask.TASK_NAME}.enabled"],
    havingValue = "true",
    matchIfMissing = true
)
@Component
class SongMetadataLoaderTask(
    private val songMetadataLoader: SongMetadataLoader,
) {
    @Scheduled(fixedDelay = 10000, initialDelay = 5000)
    fun process() {
        songMetadataLoader.loadSongs()
    }

    companion object {
        const val TASK_NAME = "song-metadata-loader-task"
    }
}