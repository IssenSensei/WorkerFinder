package com.issen.workerfinder.database.repositories

import com.issen.workerfinder.database.dao.CommentDao

class CommentRepository(private val commentDao: CommentDao){

    suspend fun getRatingAsWorker(userId: String) = commentDao.getRatingAsWorker(userId)
    suspend fun getRatingAsUser(userId: String) = commentDao.getRatingAsUser(userId)
}