package com.pdf.music.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BookmarkEntity(
    val page: Int,
    val name: String,
    val path: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
)
