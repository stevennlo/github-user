package com.example.customerapp.dao

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.customerapp.model.User
import com.example.customerapp.util.USERS_FAVORITE_TABLE_NAME

@Dao
interface UserDao {
    @Query("SELECT * FROM $USERS_FAVORITE_TABLE_NAME")
    fun getAll(): Cursor

    @Query("SELECT * FROM $USERS_FAVORITE_TABLE_NAME WHERE username = :username")
    fun getOneByUsername(username: String): Cursor

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(users: User): Long

    @Query("DELETE FROM $USERS_FAVORITE_TABLE_NAME WHERE username = :username")
    fun deleteUser(username: String): Int
}