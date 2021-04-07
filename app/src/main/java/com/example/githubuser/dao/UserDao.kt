package com.example.githubuser.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.githubuser.model.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): LiveData<List<User>>

    @Query("SELECT * FROM user WHERE username = :username LIMIT 1")
    fun getOneByUsername(username: String): LiveData<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(vararg users: User)

    @Delete
    suspend fun deleteUsers(vararg users: User)
}