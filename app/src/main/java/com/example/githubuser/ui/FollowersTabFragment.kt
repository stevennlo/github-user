package com.example.githubuser.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.adapter.UsersAdapter
import com.example.githubuser.databinding.FragmentFollowersTabBinding
import com.example.githubuser.service.Status
import com.example.githubuser.util.ImageUtil.getDrawable
import com.example.githubuser.viewmodel.FollowersTabViewModel

class FollowersTabFragment(private val username: String) : Fragment() {
    private lateinit var mContext: Context
    private var _binding: FragmentFollowersTabBinding? = null
    private val binding: FragmentFollowersTabBinding get() = _binding as FragmentFollowersTabBinding
    private val viewModel: FollowersTabViewModel by viewModels()
    private val usersAdapter by lazy {
        UsersAdapter((parentFragment as RelationAndRepoFragment)::toUserDetail)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mContext = requireContext()
        _binding = FragmentFollowersTabBinding.inflate(inflater, container, false)
        binding.apply {
            viewModel.users.observe(viewLifecycleOwner, { response ->
                when {
                    response.status == Status.StatusType.FAILED -> {
                        showResult(
                            MainActivity.MessageType.ERROR,
                            subtitleMessage = response.message
                                ?: getString(R.string.unknown_error_message),
                        )
                    }
                    (response.data?.count() ?: 0) == 0 -> {
                        showResult(
                            MainActivity.MessageType.NOT_FOUND,
                            subtitleMessage = getString(R.string.no_one_here)
                        )
                    }
                    else -> {
                        showResult(MainActivity.MessageType.EXISTS)
                    }
                }
                usersAdapter.submitList(response.data)
                showProgressBar(false)
            })
            followersTabListRv.apply {
                adapter = usersAdapter
                val itemDecoration =
                    DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL).apply {
                        setDrawable(
                            ResourcesCompat.getDrawable(
                                resources,
                                R.drawable.space_item_decoration,
                                null
                            ) as Drawable
                        )
                    }
                addItemDecoration(itemDecoration)
                layoutManager =
                    LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
            }
        }
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getUserFollowers(username)
    }

    private fun showProgressBar(isVisible: Boolean) {
        if (isVisible) {
            binding.followersTabLoadingPb.visibility = View.VISIBLE
        } else {
            binding.followersTabLoadingPb.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        showProgressBar(false)
        _binding = null
    }

    private fun showResult(
        type: MainActivity.MessageType,
        titleMessage: String? = null,
        subtitleMessage: String? = null
    ) {
        binding.apply {
            when (type) {
                MainActivity.MessageType.EXISTS -> {
                    followersTabListRv.visibility = View.VISIBLE
                    messageTitleTv.visibility = View.GONE
                    messageSubtitleTv.visibility = View.GONE
                    messageImageIv.visibility = View.GONE
                }
                MainActivity.MessageType.NOT_FOUND -> {
                    messageTitleTv.text = titleMessage
                    messageSubtitleTv.text = subtitleMessage
                    messageImageIv.setImageDrawable(getDrawable(mContext, R.drawable.ic_not_found))
                    followersTabListRv.visibility = View.GONE
                    messageTitleTv.visibility =
                        if (titleMessage !== null) View.VISIBLE else View.GONE
                    messageSubtitleTv.visibility =
                        if (subtitleMessage !== null) View.VISIBLE else View.GONE
                    messageImageIv.visibility = View.VISIBLE
                }
                MainActivity.MessageType.ERROR -> {
                    messageTitleTv.text =
                        titleMessage
                    messageSubtitleTv.text = subtitleMessage
                    messageImageIv.setImageDrawable(
                        getDrawable(
                            mContext,
                            R.drawable.ic_something_wrong
                        )
                    )
                    followersTabListRv.visibility = View.GONE
                    messageTitleTv.visibility =
                        if (titleMessage !== null) View.VISIBLE else View.GONE
                    messageSubtitleTv.visibility =
                        if (subtitleMessage !== null) View.VISIBLE else View.GONE
                    messageImageIv.visibility = View.VISIBLE
                }
            }
        }
    }
}