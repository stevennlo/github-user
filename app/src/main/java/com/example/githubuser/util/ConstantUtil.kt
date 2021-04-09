package com.example.githubuser.util

import com.example.githubuser.BuildConfig

const val BASE_GITHUB_API_URL = "https://api.github.com/"
const val GITHUB_API_ACCESS_TOKEN = "c617db263726293d46f2a079bf2251399c8d9aa9"

const val DEFAULT_REQUEST_CODE = 0

const val SETTINGS_PREFERENCE_NAME = "SETTINGS_PREFERENCE"

const val ALARM_REQUEST_CODE = 1
const val EXTRA_ALARM_TITLE = "EXTRA_ALARM_TITLE"
const val EXTRA_ALARM_MESSAGE = "EXTRA_ALARM_MESSAGE"

const val DATABASE_NAME = "users.db"
const val DATABASE_AUTHORITY = BuildConfig.APPLICATION_ID
const val DATABASE_SCHEME = "content"

const val USERS_FAVORITE_TABLE_NAME = "users_favorite"
const val USERS_FAVORITE_USERNAME = "username"
const val USERS_FAVORITE_TYPE = "type"
const val USERS_FAVORITE_AVATAR_URL = "avatar_url"

const val EXTRA_USER_ITEM = "EXTRA_USER_ITEM"
const val FAVORITE_WIDGET_EACH_ACTION = "FAVORITE_WIDGET_EACH_ACTION"

enum class MessageType {
    WELCOME, LOAD, EXISTS, NOT_FOUND, ERROR
}