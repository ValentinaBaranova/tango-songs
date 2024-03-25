package com.example.tangosongs.repository

import com.example.tangosongs.model.SongEntity
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SongRepository: JpaRepository<SongEntity, UUID> {

    fun findByTrackIdIsNull(): List<SongEntity>

    @Modifying
    @Transactional
    @Query("update SongEntity set trackId=:trackId where id=:id")
    fun updateTrackId(id: UUID, trackId: String)

}