package com.jhy.giftmanagement.di

import android.content.Context
import androidx.room.Room
import com.jhy.giftmanagement.db.GiftInfoDao
import com.jhy.giftmanagement.db.GiftInfoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideGiftInfoDatabase(@ApplicationContext context: Context): GiftInfoDatabase =
        Room.databaseBuilder(context,GiftInfoDatabase::class.java,"giftinfo_database").fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideGiftInfoDao(giftInfoDatabase: GiftInfoDatabase) : GiftInfoDao = giftInfoDatabase.giftInfoDao()
}