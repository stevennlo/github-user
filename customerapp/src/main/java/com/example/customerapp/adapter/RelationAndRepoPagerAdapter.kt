package com.example.customerapp.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.customerapp.ui.FollowersTabFragment
import com.example.customerapp.ui.FollowingTabFragment
import com.example.customerapp.ui.RepositoryTabFragment

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