package com.example.tambakapp.data.db

import androidx.room.*

@Dao
interface PondResponseDao {
    @Insert
    suspend fun insert(pondResponseEntity: PondResponseEntity)

    @Update
    suspend fun update(pondResponseEntity: PondResponseEntity)

    @Delete
    suspend fun delete(pondResponseEntity: PondResponseEntity)

    @Query("SELECT * FROM pond_table")
    suspend fun getAllPondResponses(): List<PondResponseEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReplace(pondResponseEntity: PondResponseEntity)

    @Query("SELECT * FROM pond_table WHERE pond_id = :id")
    suspend fun getPondResponseById(id: Int): List<PondResponseEntity>

    @Query("DELETE FROM pond_table")
    suspend fun clearTable()

    // Add other specific queries or operations related to PondResponseEntity
}

@Dao
interface DeviceResponseDao {
    @Insert
    suspend fun insert(deviceResponseEntity: DeviceResponseEntity)

    @Update
    suspend fun update(deviceResponseEntity: DeviceResponseEntity)

    @Delete
    suspend fun delete(deviceResponseEntity: DeviceResponseEntity)

    @Query("SELECT * FROM device_table")
    suspend fun getAllDeviceResponses(): List<DeviceResponseEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReplace(deviceResponseEntity: DeviceResponseEntity)

    @Query("DELETE FROM device_table")
    suspend fun clearTable()

    // Add other specific queries or operations related to DeviceResponseEntity
}

@Dao
interface UserResponseDao {
    @Insert
    suspend fun insert(userResponseEntity: UserResponseEntity)

    @Update
    suspend fun update(userResponseEntity: UserResponseEntity)

    @Delete
    suspend fun delete(userResponseEntity: UserResponseEntity)

    @Query("SELECT * FROM user_table")
    suspend fun getAllUserResponses(): List<UserResponseEntity>

    // Add other specific queries or operations related to DeviceResponseEntity
}