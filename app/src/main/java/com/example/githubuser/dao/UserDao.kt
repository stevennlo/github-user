package com.example.githubuser.dao

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.githubuser.model.User
import com.example.githubuser.util.USERS_FAVORITE_TABLE_NAME

@Dao
interface UserDao {
    @Query("SELECT * FROM $USERS_FAVORITE_TABLE_NAME")
    fun getAll(): Cursor

    @Query("SELECT * FROM $USERS_FAVORITE_TABLE_NAME WHERE id = :id LIMIT 1")
    fun getOneByUsername(id: Int): Cursor

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(users: User): Long

    @Query("DELETE FROM $USERS_FAVORITE_TABLE_NAME WHERE id = :id")
    fun deleteUser(id: Int): Int
}