package com.example.tangosongs.service.tasks

import com.example.tangosongs.service.SpotifyTrackIdLoader
import com.example.tangosongs.service.tasks.SpotifyTask.Companion.TASK_NAME
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@ConditionalOnProperty(
    prefix = "application.scheduling.task",
    name = ["$TASK_NAME.enabled"],
    havingValue = "true",
    matchIfMissing = true
)
@Component
class SpotifyTask(
    val spotifyTrackIdLoader: SpotifyTrackIdLoader
) {

    @Scheduled(fixedDelay = 10000, initialDelay = 90000)
    fun process() {
        spotifyTrackIdLoader.fillTrackIds()
    }

    companion object {
        const val TASK_NAME = "spotify-task"
    }
}