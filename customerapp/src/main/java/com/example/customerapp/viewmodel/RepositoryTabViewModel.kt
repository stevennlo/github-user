package com.example.customerapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.customerapp.model.Repository
import com.example.customerapp.service.GitHubApiService.Companion.getInstance
import com.example.customerapp.service.Status
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RepositoryTabViewModel : ViewModel() {
    private var _repos = MutableLiveData<Status<List<Repository>>>()
    val repos: LiveData<Status<List<Repository>>> get() = _repos

    fun getUserRepos(username: String) {
        viewModelScope.launch {
            val call = getInstance().getUserRepos(username)
            call.enqueue(object : Callback<List<Repository>?> {
                override fun onResponse(
                    call: Call<List<Repository>?>,
                    response: Response<List<Repository>?>
                ) {
                    if (response.code() == 200) {
                        _repos.postValue(Status.success(response.body()))
                    } else {
                        _repos.postValue(Status.error(response.message(), repos.value?.data))
                    }
                }

                override fun onFailure(call: Call<List<Repository>?>, t: Throwable) {
                    _repos.postValue(Status.error(null, repos.value?.data))
                }
            })
        }
    }
}