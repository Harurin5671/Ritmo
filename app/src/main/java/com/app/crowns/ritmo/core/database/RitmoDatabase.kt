package com.app.crowns.ritmo.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.crowns.ritmo.feature.habit.data.local.HabitDao
import com.app.crowns.ritmo.feature.habit.data.local.HabitEntity

@Database(
    entities = [HabitEntity::class],
    version = 1,
    exportSchema = false
)
abstract class RitmoDatabase: RoomDatabase() {
    abstract fun habitDao(): HabitDao
}