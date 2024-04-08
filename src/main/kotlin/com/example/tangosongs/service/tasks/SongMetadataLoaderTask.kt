package com.example.tangosongs.service.tasks

import com.example.tangosongs.service.SongMetadataLoader
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class SongMetadataLoaderTask(
    private val songMetadataLoader: SongMetadataLoader
) {
    @Scheduled(fixedDelay = 10000, initialDelay = 5000)
    fun process() {
        songMetadataLoader.loadSongs()
    }
}