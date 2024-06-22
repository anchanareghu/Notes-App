package com.example.mynotesapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Note::class], version = 2)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: NotesDatabase? = null

        fun getDatabase(context: Context): NotesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotesDatabase::class.java,
                    "note_database"
                )
                    .fallbackToDestructiveMigration()  // to avoid migration issues
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}