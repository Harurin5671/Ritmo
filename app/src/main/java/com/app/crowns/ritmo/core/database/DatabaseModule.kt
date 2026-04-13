package com.app.crowns.ritmo.core.database

import android.content.Context
import androidx.room.Room
import com.app.crowns.ritmo.feature.habit.data.local.HabitDao
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
    fun provideRitmoDatabase(
        @ApplicationContext context: Context,
    ): RitmoDatabase = Room.databaseBuilder(
        context,
        RitmoDatabase::class.java,
        "ritmo_db"
    ).build()

    @Provides
    fun provideHabitDao(db: RitmoDatabase): HabitDao = db.habitDao()
}