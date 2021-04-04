package com.example.githubuser.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuser.model.User
import com.example.githubuser.service.GitHubApiService.Companion.getService
import com.example.githubuser.service.Status
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersTabViewModel : ViewModel() {
    private var _users = MutableLiveData<Status<List<User>>>()
    val users: LiveData<Status<List<User>>> get() = _users

    fun getUserFollowers(username: String) {
        viewModelScope.launch {
            val call = getService().getUserFollowers(username)
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
}