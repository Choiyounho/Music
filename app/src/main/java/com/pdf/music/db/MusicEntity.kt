package com.pdf.music.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MusicEntity(
    val title: String,
    val path: String,
    val folderName: String,
    val thumbnail: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
)