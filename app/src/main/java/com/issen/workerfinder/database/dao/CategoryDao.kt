package com.issen.workerfinder.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.issen.workerfinder.database.models.Categories

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(mutableListOf: MutableList<Categories>)

    @Query("DELETE FROM category_table")
    fun deleteAll()

}