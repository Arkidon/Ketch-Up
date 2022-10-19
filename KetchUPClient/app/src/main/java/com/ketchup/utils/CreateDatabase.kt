package com.ketchup.utils

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import com.ketchup.app.database.AppDatabase

class CreateDatabase{

    companion object {
        fun createBd(context: Context): AppDatabase {
            val db = Room.databaseBuilder(
                context,
                com.ketchup.app.database.AppDatabase::class.java, "ketchup-database"
            ).build()
            return db

        }
    }
}
