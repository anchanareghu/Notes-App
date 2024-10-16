package com.example.mynotesapp.di

import android.content.Context
import com.example.mynotesapp.auth.AuthenticationManager
import com.example.mynotesapp.firestore.FireStoreRepo
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

    @Provides
    @Singleton
    fun provideFirestoreRepository(firestore: FirebaseFirestore): FireStoreRepo {
        return FireStoreRepo(firestore)
    }
    @Provides
    @Singleton
    fun provideAuthenticationManager(@ApplicationContext context: Context): AuthenticationManager {
        return AuthenticationManager(context)
    }
}
