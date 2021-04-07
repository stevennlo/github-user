package com.example.githubuser.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.model.User
import com.example.githubuser.service.DatabaseService.Companion.getService

class FavoriteViewModel : ViewModel() {
    fun getAllUsers(context: Context): LiveData<List<User>> = getService(context).userDao().getAll()

    fun getIsFavorite(context: Context, username: String) =
        getService(context).userDao().getOneByUsername(username)
}