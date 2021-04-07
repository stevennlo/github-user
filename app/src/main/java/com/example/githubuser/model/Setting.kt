package com.example.githubuser.model

data class Setting(
    val code: String,
    var name: String,
    var description: String,
    var isActive: Boolean = false
)