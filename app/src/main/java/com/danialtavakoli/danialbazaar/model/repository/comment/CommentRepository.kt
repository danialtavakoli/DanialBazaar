package com.danialtavakoli.danialbazaar.model.repository.comment

import com.danialtavakoli.danialbazaar.model.data.Comment

interface CommentRepository {
    suspend fun getAllComments(productId: String): List<Comment>
    suspend fun addNewComment(productId: String, text: String, IsSuccess: (String) -> Unit)
}