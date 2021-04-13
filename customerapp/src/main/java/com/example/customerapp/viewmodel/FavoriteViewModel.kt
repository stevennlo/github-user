package com.example.customerapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.customerapp.model.User
import com.example.customerapp.service.ContentProviderLiveData
import com.example.customerapp.service.FavoriteProvider
import com.example.customerapp.util.toListUser

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