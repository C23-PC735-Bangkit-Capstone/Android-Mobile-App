package com.example.tambakapp.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "device_table")
data class DeviceResponseEntity(
    @PrimaryKey val device_id: Int,
    @ColumnInfo val battery_strength: Int,
    @ColumnInfo val pond_id: Int,
    @ColumnInfo val paddlewheel_condition: String,
    @ColumnInfo val signal_strength: Int,
    @ColumnInfo val device_status: Boolean,
    @ColumnInfo val monitor_status: Boolean,
    // @ColumnInfo var kincirViewIsVisible: Boolean
)
