package com.example.customerapp.viewmodel

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.customerapp.model.User
import com.example.customerapp.service.ContentProviderLiveData
import com.example.customerapp.service.FavoriteProvider
import com.example.customerapp.service.GitHubApiService.Companion.getInstance
import com.example.customerapp.service.Status
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersTabViewModel : ViewModel() {
    private var _users = MutableLiveData<Status<List<User>>>()
    val users: LiveData<Status<List<User>>> get() = _users

    fun getUserFollowers(username: String) {
        viewModelScope.launch {
            val call = getInstance().getUserFollowers(username)
            call.enqueue(object : Callback<List<User>?> {
                override fun onResponse(call: Call<List<User>?>, response: Response<List<User>?>) {
                    if (response.code() == 200) {
                        _users.postValue(Status.success(response.body()))
                    } else {
                        _users.postValue(Status.error(response.message(), users.value?.data))
                    }
                }

                override fun onFailure(call: Call<List<User>?>, t: Throwable) {
                    _users.postValue(Status.error(null, users.value?.data))
                }
            })
        }
    }

    fun getIsFavorite(context: Context, id: Int): ContentProviderLiveData<Boolean> {
        return object : ContentProviderLiveData<Boolean>(
            context,
            FavoriteProvider.USERS_FAVORITE_URI
        ) {
            override suspend fun getContentProviderValue(): Boolean {
                val cursor =
                    context.contentResolver.query(
                        "${FavoriteProvider.USERS_FAVORITE_URI}/$id".toUri(),
                        null,
                        null,
                        null,
                        null
                    )
                val result = (cursor?.count ?: 0) != 0
                cursor?.close()
                return result
            }
        }
    }
}