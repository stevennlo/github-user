package com.example.githubuser.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.githubuser.dao.UserDao
import com.example.githubuser.model.User

@Database(entities = [User::class], version = 1)
abstract class DatabaseService : RoomDatabase() {
    companion object {
        private var mDbService: DatabaseService? = null
        fun getService(context: Context): DatabaseService {
            if (mDbService == null) {
                mDbService =
                    Room.databaseBuilder(context, DatabaseService::class.java, "users.db").build()
            }

            return mDbService as DatabaseService
        }
    }

    abstract fun userDao(): UserDao
}