package com.example.githubuser.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.githubuser.databinding.ActivityUserDetailBinding
import com.example.githubuser.model.User

class UserDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityUserDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}