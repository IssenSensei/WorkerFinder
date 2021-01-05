package com.issen.workerfinder.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.issen.workerfinder.database.models.Comments

@Dao
interface CommentDao {

    @Insert
    suspend fun insert(mutableListOf: MutableList<Comments>)

    @Query("SELECT AVG(rating) from comment_table where commentedUserId = :userId and commentByWorker = 1")
    suspend fun getRatingAsUser(userId: String): Float

    @Query("SELECT AVG(rating) from comment_table where commentedUserId = :userId and commentByWorker = 0")
    suspend fun getRatingAsWorker(userId: String): Float

    @Query("DELETE FROM comment_table")
    suspend fun deleteAll()

    @Insert
    suspend fun addRating(comment: Comments)

}