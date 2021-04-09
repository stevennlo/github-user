package com.example.githubuser.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.githubuser.util.USERS_FAVORITE_TABLE_NAME
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
@Entity(tableName = USERS_FAVORITE_TABLE_NAME)
data class User(
    @SerialName("id")
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    @SerialName("login")
    val username: String,
    @SerialName("type")
    val type: String? = null,
    @SerialName("avatar_url")
    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String? = null
) : Parcelable