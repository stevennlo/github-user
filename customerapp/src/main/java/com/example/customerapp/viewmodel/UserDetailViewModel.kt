package com.example.customerapp.viewmodel

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.customerapp.model.User
import com.example.customerapp.model.UserDetail
import com.example.customerapp.service.ContentProviderLiveData
import com.example.customerapp.service.FavoriteProvider.Companion.USERS_FAVORITE_URI
import com.example.customerapp.service.GitHubApiService.Companion.getInstance
import com.example.customerapp.service.Status
import com.example.customerapp.util.toContentValues
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailViewModel : ViewModel() {
    private var _user = MutableLiveData<Status<UserDetail>>()
    val user: LiveData<Status<UserDetail>> get() = _user

    fun getUserDetail(username: String) {
        viewModelScope.launch {
            val call = getInstance().getUser(username)
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
        viewModelScope.launch(Dispatchers.IO) {
            user.value?.data?.let { it ->
                val user =
                    User(
                        id = it.id,
                        username = it.username,
                        type = it.type,
                        avatarUrl = it.avatarUrl
                    )
                if (isFavorite) {
                    context.contentResolver.insert(USERS_FAVORITE_URI, user.toContentValues())
                } else {
                    context.contentResolver.delete(
                        "$USERS_FAVORITE_URI/${it.id}".toUri(),
                        null,
                        null
                    )
                }
            }
        }
    }

    fun getIsFavorite(context: Context, id: Int): ContentProviderLiveData<Boolean> {
        return object : ContentProviderLiveData<Boolean>(context, USERS_FAVORITE_URI) {
            override suspend fun getContentProviderValue(): Boolean {
                val cursor =
                    context.contentResolver.query(
                        "$USERS_FAVORITE_URI/$id".toUri(),
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