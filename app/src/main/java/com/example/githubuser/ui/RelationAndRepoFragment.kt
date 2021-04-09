package com.example.githubuser.ui

import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.githubuser.R
import com.example.githubuser.adapter.RelationAndRepoPagerAdapter
import com.example.githubuser.databinding.FragmentRelationAndRepoBinding
import com.example.githubuser.util.MessageType
import com.google.android.material.tabs.TabLayoutMediator

class RelationAndRepoFragment :
    BaseFragment<FragmentRelationAndRepoBinding>(FragmentRelationAndRepoBinding::inflate) {
    private lateinit var username: String
    private val args: RelationAndRepoFragmentArgs by navArgs()

    override fun runOnCreateView() {
        super.runOnCreateView()
        val tab = args.tab
        username = args.username
        mActivity.supportActionBar?.title = username
        showResult(MessageType.EXISTS)
        binding.apply {
            val relationAndRepoPagerAdapter =
                RelationAndRepoPagerAdapter(this@RelationAndRepoFragment, username)
            relationAndRepoContentVp2.isUserInputEnabled = false
            relationAndRepoContentVp2.adapter = relationAndRepoPagerAdapter
            relationAndRepoContentVp2.setCurrentItem(tab, false)
            (relationAndRepoContentVp2.getChildAt(0) as ViewGroup).clipChildren = false
            TabLayoutMediator(relationAndRepoTabsTl, relationAndRepoContentVp2) { tab, position ->
                when (position) {
                    0 -> tab.text = getString(R.string.repositories_tab)
                    1 -> tab.text = getString(R.string.followers_tab)
                    2 -> tab.text = getString(R.string.following_tab)
                }
            }.attach()
        }
    }

    fun toUserDetail(username: String) {
        val toUserDetailFragment =
            RelationAndRepoFragmentDirections.actionRelationAndRepoFragmentToUserDetailFragment(
                username
            )
        findNavController().navigate(toUserDetailFragment)
    }

    override fun getRootViewGroup(): ViewGroup {
        return binding.root
    }
}