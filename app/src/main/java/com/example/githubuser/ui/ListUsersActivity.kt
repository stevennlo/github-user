package com.example.githubuser.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.githubuser.R
import com.example.githubuser.databinding.ActivityListUsersBinding

class ListUsersActivity : AppCompatActivity() {
    lateinit var binding: ActivityListUsersBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_App)
        super.onCreate(savedInstanceState)

        binding = ActivityListUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}