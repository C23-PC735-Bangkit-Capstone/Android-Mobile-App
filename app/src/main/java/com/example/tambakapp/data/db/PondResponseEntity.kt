package com.example.tambakapp.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pond_table")
data class PondResponseEntity(
    @PrimaryKey val pond_id: Int,
    @ColumnInfo val user_id: Int,
    @ColumnInfo val pond_location: String,
    // @ColumnInfo var kincirViewIsVisible: Boolean
)