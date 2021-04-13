package com.example.customerapp.util

const val BASE_GITHUB_API_URL = "https://api.github.com/"
const val GITHUB_API_ACCESS_TOKEN = "c617db263726293d46f2a079bf2251399c8d9aa9"

const val DATABASE_NAME = "users.db"
const val DATABASE_AUTHORITY = "com.example.githubuser"
const val DATABASE_SCHEME = "content"

const val USERS_FAVORITE_TABLE_NAME = "users_favorite"
const val USERS_FAVORITE_USERNAME = "username"
const val USERS_FAVORITE_TYPE = "type"
const val USERS_FAVORITE_AVATAR_URL = "avatar_url"

enum class MessageType {
    WELCOME, LOAD, EXISTS, NOT_FOUND, ERROR
}