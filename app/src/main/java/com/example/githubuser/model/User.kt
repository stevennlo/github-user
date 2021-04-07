package com.example.githubuser.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class User(
    @SerialName("login")
    @PrimaryKey
    val username: String,
    @SerialName("type")
    val type: String? = null,
    @SerialName("avatar_url")
    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String? = null
)