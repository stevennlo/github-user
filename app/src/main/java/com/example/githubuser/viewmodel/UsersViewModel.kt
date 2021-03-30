package com.example.githubuser.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuser.model.ResponseSearchUsers
import com.example.githubuser.model.User
import com.example.githubuser.service.GitHubApiService
import com.example.githubuser.service.Status
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsersViewModel : ViewModel() {
    private var _users = MutableLiveData<Status<List<User>>>()
    val users: LiveData<Status<List<User>>> get() = _users

    fun searchUser(keyword: String) {
        viewModelScope.launch {
            val call = GitHubApiService.getService().getSearchUsers(keyword)
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
}