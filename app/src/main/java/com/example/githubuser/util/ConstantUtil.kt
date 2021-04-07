package com.example.githubuser.util

const val BASE_GITHUB_API_URL = "https://api.github.com/"
const val GITHUB_API_ACCESS_TOKEN = "c617db263726293d46f2a079bf2251399c8d9aa9"

const val DEFAULT_REQUEST_CODE = 0

const val SETTINGS_PREFERENCE_NAME = "SETTINGS_PREFERENCE"

const val ALARM_REQUEST_CODE = 1
const val EXTRA_ALARM_TITLE = "EXTRA_ALARM_TITLE"
const val EXTRA_ALARM_MESSAGE = "EXTRA_ALARM_MESSAGE"

enum class MessageType {
    WELCOME, LOAD, EXISTS, NOT_FOUND, ERROR
}