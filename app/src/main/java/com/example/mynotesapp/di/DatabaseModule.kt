package com.example.mynotesapp.di

import android.content.Context
import androidx.room.Room
import com.example.mynotesapp.data.NoteDao
import com.example.mynotesapp.data.NotesDatabase
import com.example.mynotesapp.data.TaskDao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideFirestoreInstance(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }


    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): NotesDatabase {
        return Room.databaseBuilder(
            context,
            NotesDatabase::class.java,
            "note_database"
        ).build()
    }

    @Provides
    fun provideNoteDao(database: NotesDatabase): NoteDao {
        return database.noteDao()
    }

    @Provides
    fun provideTaskDao(database: NotesDatabase): TaskDao {
        return database.taskDao()
    }

    @Provides
    @Singleton
    fun provideAuthService(
        @ApplicationContext context: Context,
    ): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
}
