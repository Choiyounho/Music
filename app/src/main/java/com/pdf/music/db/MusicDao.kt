package com.pdf.music.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MusicDao {

    @Query("SELECT * from  musicentity")
    fun musics(): List<MusicEntity>

    @Insert
    fun insert(musicEntity: MusicEntity)

}