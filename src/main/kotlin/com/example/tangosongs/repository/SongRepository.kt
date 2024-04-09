package com.example.tangosongs.repository

import com.example.tangosongs.model.SongEntity
import jakarta.transaction.Transactional
import org.springframework.data.domain.Limit
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.*

@Repository
interface SongRepository : JpaRepository<SongEntity, UUID>, JpaSpecificationExecutor<SongEntity> {

    @Query("SELECT s FROM SongEntity s WHERE (s.trackId IS NULL AND s.trackIdRequestedAt < :trackIdRequestedAtBefore) " +
            "OR s.trackIdRequestedAt IS NULL")
    fun findWithoutTrackId(limit: Limit, trackIdRequestedAtBefore: Instant): List<SongEntity>

    fun existsBySourceFilenameIsNotNull(): Boolean

    @Modifying
    @Transactional
    @Query("update SongEntity set trackId=:trackId, trackIdRequestedAt=:trackIdRequestedAt where id=:id")
    fun updateTrackId(id: UUID, trackId: String, trackIdRequestedAt: Instant = Instant.now())

    @Modifying
    @Transactional
    @Query("update SongEntity set trackIdRequestedAt=:trackIdRequestedAt where id=:id")
    fun updateTrackIdRequestedAt(id: UUID, trackIdRequestedAt: Instant = Instant.now())
}