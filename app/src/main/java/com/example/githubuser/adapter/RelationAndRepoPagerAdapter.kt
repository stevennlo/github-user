package com.example.githubuser.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.githubuser.ui.FollowersTabFragment
import com.example.githubuser.ui.FollowingTabFragment
import com.example.githubuser.ui.RepositoryTabFragment

class RelationAndRepoPagerAdapter(fragment: Fragment, private val username: String) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> RepositoryTabFragment(username)
            1 -> FollowersTabFragment(username)
            else -> FollowingTabFragment(username)
        }
    }
}