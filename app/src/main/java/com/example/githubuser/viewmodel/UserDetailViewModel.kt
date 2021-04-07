package com.example.githubuser.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuser.model.User
import com.example.githubuser.model.UserDetail
import com.example.githubuser.service.DatabaseService
import com.example.githubuser.service.GitHubApiService.Companion.getService
import com.example.githubuser.service.Status
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailViewModel : ViewModel() {
    private var _user = MutableLiveData<Status<UserDetail>>()
    val user: LiveData<Status<UserDetail>> get() = _user

    fun getUserDetail(username: String) {
        viewModelScope.launch {
            val call = getService().getUser(username)
            call.enqueue(object : Callback<UserDetail> {
                override fun onResponse(
                    call: Call<UserDetail>,
                    response: Response<UserDetail>
                ) {
                    if (response.code() == 200) {
                        _user.postValue(Status.success(response.body()))
                    } else {
                        _user.postValue(Status.error(response.message(), user.value?.data))
                    }
                }

                override fun onFailure(call: Call<UserDetail>, t: Throwable) {
                    _user.postValue(Status.error(null, user.value?.data))
                }
            })
        }
    }

    fun setFavorite(context: Context, isFavorite: Boolean) {
        viewModelScope.launch {
            user.value?.data?.let { it ->
                val user =
                    User(
                        username = it.username as String,
                        type = it.type,
                        avatarUrl = it.avatarUrl
                    )
                if (isFavorite) {
                    DatabaseService.getService(context).userDao().insertUsers(user)
                } else {
                    DatabaseService.getService(context).userDao().deleteUsers(user)
                }
                _user.postValue(Status.success(it))
            }
        }
    }

    fun getIsFavorite(context: Context, username: String) =
        DatabaseService.getService(context).userDao().getOneByUsername(username)
}