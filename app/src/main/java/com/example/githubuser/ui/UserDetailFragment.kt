package com.example.githubuser.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.githubuser.databinding.FragmentUserDetailBinding
import com.example.githubuser.model.User
import com.example.githubuser.util.AssetUtil
import com.example.githubuser.util.NumberUtil

class UserDetailFragment : Fragment() {
    private lateinit var user: User
    private lateinit var mContext: Context
    private val args: UserDetailFragmentArgs by navArgs()
    private var _binding: FragmentUserDetailBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mContext = requireContext()
        user = args.user
        (mContext as AppCompatActivity).supportActionBar?.title = user.username
        _binding = FragmentUserDetailBinding.inflate(inflater, container, false)

        binding.apply {
            val profileId = AssetUtil.getDrawableId(mContext, user.avatar)
            userDetailProfileSiv.setImageResource(profileId)
            userDetailNameTv.text = user.name
            userDetailRepositoryTv.text = NumberUtil.prettyCount(user.repository)
            userDetailFollowerTv.text = NumberUtil.prettyCount(user.follower)
            userDetailFollowingTv.text = NumberUtil.prettyCount(user.following)
            userDetailCompanyTv.text = user.company
            userDetailLocationTv.text = user.location
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}