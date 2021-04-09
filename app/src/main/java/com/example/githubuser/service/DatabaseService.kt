package com.example.githubuser.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.githubuser.dao.UserDao
import com.example.githubuser.model.User
import com.example.githubuser.util.DATABASE_NAME

@Database(entities = [User::class], version = 1)
abstract class DatabaseService : RoomDatabase() {
    companion object {
        private var mDbService: DatabaseService? = null
        fun getInstance(context: Context): DatabaseService {
            if (mDbService == null) {
                mDbService =
                    Room.databaseBuilder(context, DatabaseService::class.java, DATABASE_NAME)
                        .build()
            }

            return mDbService as DatabaseService
        }
    }

    abstract fun userDao(): UserDao
}