package com.example.githubuser.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.githubuser.R
import com.example.githubuser.adapter.RelationAndRepoPagerAdapter
import com.example.githubuser.databinding.FragmentRelationAndRepoBinding
import com.google.android.material.tabs.TabLayoutMediator

class RelationAndRepoFragment : Fragment() {
    private lateinit var username: String
    private lateinit var mContext: Context
    private val args: RelationAndRepoFragmentArgs by navArgs()
    private var _binding: FragmentRelationAndRepoBinding? = null
    private val binding: FragmentRelationAndRepoBinding get() = _binding as FragmentRelationAndRepoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mContext = requireContext()
        username = args.username
        (mContext as AppCompatActivity).supportActionBar?.title = username
        val tab = args.tab
        _binding = FragmentRelationAndRepoBinding.inflate(inflater, container, false)
        binding.apply {
            val relationAndRepoPagerAdapter =
                RelationAndRepoPagerAdapter(this@RelationAndRepoFragment, username)
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

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun toUserDetail(username: String) {
        val toUserDetailFragment =
            RelationAndRepoFragmentDirections.actionRelationAndRepoFragmentToUserDetailFragment(
                username
            )
        findNavController().navigate(toUserDetailFragment)
    }
}