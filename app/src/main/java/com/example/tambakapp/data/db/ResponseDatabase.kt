package com.example.tambakapp.data.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

@androidx.room.Database(entities = [PondResponseEntity::class, DeviceResponseEntity::class, UserResponseEntity::class], version = 2)
abstract class ResponseDatabase : RoomDatabase() {
    abstract fun pondResponseDao(): PondResponseDao
    abstract fun deviceResponseDao(): DeviceResponseDao
    abstract fun userResponseDao(): UserResponseDao

    // Implement the singleton pattern to get an instance of the database
    companion object {
        @Volatile
        private var INSTANCE: ResponseDatabase? = null

        fun getInstance(context: Context) : ResponseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ResponseDatabase::class.java,
                    "response_database"
                )
                    //.fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}