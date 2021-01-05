package com.issen.workerfinder.database.repositories

import com.issen.workerfinder.database.dao.CommentDao
import com.issen.workerfinder.database.models.Comments

class CommentRepository(private val commentDao: CommentDao){

    suspend fun getRatingAsWorker(userId: String) = commentDao.getRatingAsWorker(userId)
    suspend fun getRatingAsUser(userId: String) = commentDao.getRatingAsUser(userId)
    suspend fun addRating(comment: Comments) = commentDao.addRating(comment)
}