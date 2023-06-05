package com.jhy.giftmanagement.di

import android.content.Context
import com.jhy.giftmanagement.db.GiftInfoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideGiftInfoDatabase(application: Context): GiftInfoDatabase {
        return GiftInfoDatabase.getDatabase(application)
    }
}