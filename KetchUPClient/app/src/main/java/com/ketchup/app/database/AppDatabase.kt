package com.ketchup.app.database


import android.content.Context
import androidx.room.*


@Database(entities = [Users::class, Chats::class, ChatEntries::class, ChatMembership::class, AdditionalChatGroupInformation::class, EntryAttachments::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    companion object{
        private var INSTANCE: AppDatabase? = null
        fun createInstance(context: Context): AppDatabase? {
            if(INSTANCE == null){
                INSTANCE = Room.databaseBuilder(context, AppDatabase::class.java,"ketchup-database" )
                    .allowMainThreadQueries().build()
            }
            return INSTANCE
        }
        fun getInstance(): AppDatabase? {
            return INSTANCE
        }
    }
}
