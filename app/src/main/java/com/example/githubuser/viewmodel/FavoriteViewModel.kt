package com.example.githubuser.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.githubuser.model.User
import com.example.githubuser.service.ContentProviderLiveData
import com.example.githubuser.service.FavoriteProvider
import com.example.githubuser.util.toListUser

class FavoriteViewModel : ViewModel() {
    fun getAllUsers(context: Context): ContentProviderLiveData<List<User>> {
        return object : ContentProviderLiveData<List<User>>(
            context,
            FavoriteProvider.USERS_FAVORITE_URI
        ) {
            override suspend fun getContentProviderValue(): List<User> {
                val cursor =
                    context.contentResolver.query(
                        FavoriteProvider.USERS_FAVORITE_URI,
                        null,
                        null,
                        null,
                        null
                    )
                val result = cursor?.toListUser() ?: listOf()
                cursor?.close()
                return result
            }
        }
    }
}