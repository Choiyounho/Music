package com.pdf.music.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [MusicEntity::class, FolderEntity::class, BookmarkEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun musicDao(): MusicDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "music_db")
                .fallbackToDestructiveMigration()
                .build()
        }

    }

}