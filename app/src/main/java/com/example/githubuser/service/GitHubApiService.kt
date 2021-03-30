package com.example.githubuser.service

import com.example.githubuser.model.Repository
import com.example.githubuser.model.ResponseSearchUsers
import com.example.githubuser.model.User
import com.example.githubuser.model.UserDetail
import com.example.githubuser.util.BASE_GITHUB_API_URL
import com.example.githubuser.util.GITHUB_API_ACCESS_TOKEN
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApiService {
    companion object {
        @ExperimentalSerializationApi
        fun getService(): GitHubApiService {
            val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val okHttp = OkHttpClient.Builder()
                .addInterceptor(TokenInterceptor(GITHUB_API_ACCESS_TOKEN))
                .addInterceptor(logger)
                .build()
            val contentType = "application/json".toMediaType()
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_GITHUB_API_URL)
                .addConverterFactory(Json {
                    ignoreUnknownKeys = true
                }.asConverterFactory(contentType))
                .client(okHttp)
                .build()

            return retrofit.create(GitHubApiService::class.java)
        }
    }

    @GET("search/users")
    fun getSearchUsers(
        @Query("q") keyword: String
    ): Call<ResponseSearchUsers>

    @GET("users/{username}")
    fun getUser(
        @Path("username") username: String
    ): Call<UserDetail>

    @GET("users/{username}/followers")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<List<User>?>

    @GET("users/{username}/following")
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<List<User>?>

    @GET("users/{username}/repos")
    fun getUserRepos(
        @Path("username") username: String
    ): Call<List<Repository>?>
}

class TokenInterceptor(private var token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request()
        val basicReq = req.newBuilder()
            .addHeader("Authorization", "token $token")
            .build()

        return chain.proceed(basicReq)
    }
}

class Status<T>(val status: StatusType, val data: T?, val message: String?) {
    enum class StatusType {
        SUCCESS, FAILED
    }

    companion object {
        fun <T> success(data: T?): Status<T> {
            return Status(StatusType.SUCCESS, data, null)
        }

        fun <T> error(message: String?, data: T?): Status<T> {
            return Status(StatusType.FAILED, data, message)
        }
    }
}