package com.example.githubuser.util

import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import com.example.githubuser.model.User

@ColorInt
fun Context.getColorFromAttr(
    @AttrRes attrColor: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): Int {
    theme.resolveAttribute(attrColor, typedValue, resolveRefs)
    return typedValue.data
}

fun Context.getSettingsPref(): SharedPreferences {
    return getSharedPreferences(SETTINGS_PREFERENCE_NAME, Context.MODE_PRIVATE)
}

fun User.toContentValues(): ContentValues {
    val values = ContentValues()
    values.apply {
        put(USERS_FAVORITE_USERNAME, username)
        put(USERS_FAVORITE_TYPE, type)
        put(USERS_FAVORITE_AVATAR_URL, avatarUrl)
    }
    return values
}

fun ContentValues.toUser(): User {
    return User(
        getAsString(USERS_FAVORITE_USERNAME),
        getAsString(USERS_FAVORITE_TYPE),
        getAsString(USERS_FAVORITE_AVATAR_URL)
    )
}

fun Cursor.toUser(): User {
    return User(
        getString(getColumnIndexOrThrow(USERS_FAVORITE_USERNAME)),
        getString(getColumnIndexOrThrow(USERS_FAVORITE_TYPE)),
        getString(getColumnIndexOrThrow(USERS_FAVORITE_AVATAR_URL))
    )
}

fun Cursor.toListUser(): List<User> {
    val users = mutableListOf<User>()
    apply {
        while (moveToNext()) {
            users.add(toUser())
        }
    }
    return users
}