package com.example.tambakapp.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserResponseEntity(
    @PrimaryKey val user_id: Int,
    @ColumnInfo val user_infos: String,
    @ColumnInfo val alarm_sound: String,
    @ColumnInfo val notification_sound: String,
    @ColumnInfo val contacts: String
)
