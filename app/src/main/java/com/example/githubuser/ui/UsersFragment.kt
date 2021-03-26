package com.example.githubuser.ui

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.adapter.UsersAdapter
import com.example.githubuser.databinding.FragmentUsersBinding
import com.example.githubuser.model.GitHubUser
import com.example.githubuser.model.User
import com.example.githubuser.util.AssetUtil
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class UsersFragment : Fragment() {
    private lateinit var users: ArrayList<User>
    private lateinit var mContext: Context
    private var _binding: FragmentUsersBinding? = null
    private val binding get() = _binding!!
    private val usersAdapter by lazy {
        UsersAdapter(this::toUserDetail)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mContext = requireContext()
        users = loadUsers()
        _binding = FragmentUsersBinding.inflate(inflater, container, false)
        binding.usersListRv.apply {
            usersAdapter.submitList(users)
            adapter = usersAdapter
            val itemDecoration =
                DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL).apply {
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
                LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        }
        return binding.root
    }

    private fun loadUsers(): ArrayList<User> {
        val usersJson = AssetUtil.loadJSONFromAsset(
            mContext.applicationContext as Application,
            "githubuser.json"
        )
        val data = Json.decodeFromString<GitHubUser>(usersJson)
        return data.users
    }

    private fun toUserDetail(user: User) {
        val toUserDetailFragment =
            UsersFragmentDirections.actionUsersFragmentToUserDetailFragment(user)
        findNavController().navigate(toUserDetailFragment)
    }
}