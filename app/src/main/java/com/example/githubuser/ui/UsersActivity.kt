package com.example.githubuser.ui

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.adapter.UsersAdapter
import com.example.githubuser.databinding.ActivityUsersBinding
import com.example.githubuser.model.GitHubUser
import com.example.githubuser.model.User
import com.example.githubuser.ui.UserDetailActivity.Companion.EXTRA_INDEX
import com.example.githubuser.util.AssetUtil
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class UsersActivity : AppCompatActivity() {
    companion object {
        val REQUEST_FOR_USER_CHANGE = 1
        val EXTRA_FAVORITE_RESULT = "USERS_EXTRA_FAVORITE_RESULT"
        val USERS_STATE = "USERS_STATE"
    }

    private lateinit var binding: ActivityUsersBinding
    private lateinit var users: ArrayList<User>
    private var userAdapter: UsersAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_App)
        super.onCreate(savedInstanceState)
        binding = ActivityUsersBinding.inflate(layoutInflater)
        users = loadUsers()
        binding.usersListRv.apply {
            userAdapter = UsersAdapter()
            userAdapter?.submitList(users)
            adapter = userAdapter
            val itemDecoration =
                DividerItemDecoration(this@UsersActivity, LinearLayoutManager.VERTICAL).apply {
                    setDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.space_item_decoration,
                            null
                        )!!
                    )
                }
            addItemDecoration(itemDecoration)
            layoutManager =
                LinearLayoutManager(this@UsersActivity, LinearLayoutManager.VERTICAL, false)
        }

        setContentView(binding.root)
    }

    private fun loadUsers(): ArrayList<User> {
        val usersJson = AssetUtil.loadJSONFromAsset(
            application as Application,
            "githubuser.json"
        )
        val data = Json.decodeFromString<GitHubUser>(usersJson)
        return data.users
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_FOR_USER_CHANGE && resultCode == Activity.RESULT_OK) {
            val position = data?.getIntExtra(EXTRA_INDEX, 0) ?: 0
            val isFavorite = data?.getBooleanExtra(EXTRA_FAVORITE_RESULT, false) ?: false
            users[position].isFavorite = isFavorite
            userAdapter?.notifyItemChanged(position)
        }
    }
}