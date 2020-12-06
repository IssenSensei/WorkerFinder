package com.issen.workerfinder.database.dao

import androidx.room.Dao
import androidx.room.Insert
import com.issen.workerfinder.database.models.Comments

@Dao
interface CommentsDao {

    @Insert
    suspend fun insert(mutableListOf: MutableList<Comments>)


}