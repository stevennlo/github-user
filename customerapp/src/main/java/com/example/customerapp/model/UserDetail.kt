package com.example.customerapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDetail(
    @SerialName("id")
    val id: Int,
    @SerialName("login")
    val username: String,
    @SerialName("company")
    val company: String? = null,
    @SerialName("public_repos")
    val publicRepos: Int? = null,
    @SerialName("followers")
    val followers: Int? = null,
    @SerialName("avatar_url")
    val avatarUrl: String? = null,
    @SerialName("following")
    val following: Int? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("location")
    val location: String? = null,
    @SerialName("type")
    val type: String? = null,
)