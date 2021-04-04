package com.example.githubuser.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuser.model.UserDetail
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
}