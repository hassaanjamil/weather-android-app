package com.weather.app.data.local.dao

import androidx.room.*
import com.weather.app.data.remote.model.cities.Data

@Dao
interface CityDao {

    @Query("SELECT * FROM city")
    suspend fun getAll(): List<Data>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(city: Data)

    @Delete
    suspend fun delete(city: Data)

}