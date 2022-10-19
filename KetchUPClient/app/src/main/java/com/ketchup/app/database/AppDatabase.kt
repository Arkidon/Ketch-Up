package com.ketchup.app.database

import androidx.room.*

    @Database(entities = [Users::class], version = 1)
    abstract class AppDatabase : RoomDatabase() {
        abstract fun userDao(): UserDao
    }
