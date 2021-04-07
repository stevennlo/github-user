package com.example.githubuser.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseSearchUsers(
    @SerialName("total_count")
    val totalCount: Int? = null,
    @SerialName("incomplete_results")
    val incompleteResults: Boolean? = null,
    @SerialName("items")
    val items: List<User>? = null
)