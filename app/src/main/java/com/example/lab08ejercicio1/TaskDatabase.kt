package com.example.lab08ejercicio1

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}