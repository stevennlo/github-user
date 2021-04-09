package com.example.githubuser.service

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.githubuser.util.DATABASE_AUTHORITY
import com.example.githubuser.util.DATABASE_SCHEME
import com.example.githubuser.util.USERS_FAVORITE_TABLE_NAME
import com.example.githubuser.util.toUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteProvider : ContentProvider() {
    companion object {
        private const val USER = 1
        private const val USER_ID = 2
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(DATABASE_AUTHORITY, USERS_FAVORITE_TABLE_NAME, USER)
            addURI(DATABASE_AUTHORITY, "$USERS_FAVORITE_TABLE_NAME/#", USER_ID)
        }
        val USERS_FAVORITE_URI: Uri = Uri.Builder().scheme(DATABASE_SCHEME)
            .authority(DATABASE_AUTHORITY)
            .appendPath(USERS_FAVORITE_TABLE_NAME)
            .build()
    }

    private lateinit var mContext: Context
    private lateinit var databaseService: DatabaseService

    override fun onCreate(): Boolean {
        mContext = context as Context
        databaseService = DatabaseService.getInstance(mContext)
        return true
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val inserted = if (uriMatcher.match(uri) == USER) {
            values?.let {
                val data = it.toUser()
                databaseService.userDao().insertUser(data)
            } ?: 0
        } else 0

        mContext.contentResolver?.notifyChange(USERS_FAVORITE_URI, null)
        refreshWidgetUser()

        return Uri.parse("$USERS_FAVORITE_URI/$inserted")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted = if (uriMatcher.match(uri) == USER_ID) {
            uri.lastPathSegment?.toInt()?.let {
                databaseService.userDao().deleteUser(it)
                1
            } ?: 0
        } else 0
        mContext.contentResolver?.notifyChange(USERS_FAVORITE_URI, null)
        refreshWidgetUser()

        return deleted
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when (uriMatcher.match(uri)) {
            USER -> databaseService.userDao().getAll()
            USER_ID -> uri.lastPathSegment?.toInt()
                ?.let { databaseService.userDao().getOneByUsername(it) }
            else -> null
        }
    }

    private fun refreshWidgetUser() {
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }

    override fun getType(uri: Uri): String? {
        return null
    }
}

abstract class ContentProviderLiveData<T>(
    private val context: Context,
    private val uri: Uri,
) : MutableLiveData<T>() {
    private lateinit var observer: ContentObserver

    init {
        CoroutineScope(Dispatchers.IO).launch {
            postValue(getContentProviderValue())
        }
    }

    override fun onActive() {
        observer = object : ContentObserver(null) {
            override fun onChange(self: Boolean) {
                CoroutineScope(Dispatchers.IO).launch {
                    postValue(getContentProviderValue())
                }
            }
        }

        context.contentResolver.registerContentObserver(uri, true, observer)
    }

    override fun onInactive() {
        context.contentResolver.unregisterContentObserver(observer)
    }

    abstract suspend fun getContentProviderValue(): T
}