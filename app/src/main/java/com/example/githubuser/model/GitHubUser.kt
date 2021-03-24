package com.example.githubuser.model

import kotlinx.serialization.Serializable

@Serializable
data class GitHubUser(val users: ArrayList<User>)