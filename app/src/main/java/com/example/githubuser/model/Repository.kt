package com.example.githubuser.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Repository(
    @SerialName("stargazers_count")
    val stargazersCount: Int? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("full_name")
    val fullName: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("owner")
    val owner: User? = null,
    @SerialName("forks_count")
    val forksCount: Int? = null
)