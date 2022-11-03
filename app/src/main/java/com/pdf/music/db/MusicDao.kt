package com.pdf.music.db

import androidx.room.*

@Dao
interface MusicDao {

    @Query("SELECT * from  musicentity")
    fun musics(): List<MusicEntity>

    @Query("SELECT * from musicentity WHERE folderName = :folder ")
    fun musicsFolder(folder: String): List<MusicEntity>

    @Query("SELECT * from  folderentity")
    fun folders(): List<FolderEntity>

    @Query("SELECT * from bookmarkentity")
    fun bookmarks(): List<BookmarkEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMusic(musicEntity: MusicEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFolder(folderEntity: FolderEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBookmark(bookmarkEntity: BookmarkEntity)

    @Delete
    fun deleteFolder(folderEntity: FolderEntity)

}