package com.example.githubuser.viewmodel

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuser.model.ResponseSearchUsers
import com.example.githubuser.model.User
import com.example.githubuser.service.ContentProviderLiveData
import com.example.githubuser.service.FavoriteProvider
import com.example.githubuser.service.GitHubApiService
import com.example.githubuser.service.Status
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsersViewModel : ViewModel() {
    private var _keyword = MutableLiveData<String>()
    private var _users = MutableLiveData<Status<List<User>>>()
    val keyword: LiveData<String> get() = _keyword
    val users: LiveData<Status<List<User>>> get() = _users

    fun searchUser(keyword: String) {
        viewModelScope.launch {
            _keyword.postValue(keyword)
            val call = GitHubApiService.getInstance().getSearchUsers(keyword)
            call.enqueue(object : Callback<ResponseSearchUsers> {
                override fun onResponse(
                    call: Call<ResponseSearchUsers>,
                    response: Response<ResponseSearchUsers>
                ) {
                    if (response.code() == 200) {
                        val responseSearchUsers: ResponseSearchUsers? = response.body()
                        responseSearchUsers?.items.apply {
                            _users.postValue(Status.success(this))
                        }
                    } else {
                        _users.postValue(Status.error(response.message(), users.value?.data))
                    }
                }

                override fun onFailure(call: Call<ResponseSearchUsers>, t: Throwable) {
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