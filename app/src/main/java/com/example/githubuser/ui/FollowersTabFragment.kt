package com.example.githubuser.ui

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.adapter.UsersAdapter
import com.example.githubuser.databinding.FragmentFollowersTabBinding
import com.example.githubuser.service.Status
import com.example.githubuser.util.ImageUtil.getDrawable
import com.example.githubuser.util.MessageType
import com.example.githubuser.util.getColorFromAttr
import com.example.githubuser.viewmodel.FollowersTabViewModel

class FollowersTabFragment(private val username: String) :
    BaseFragment<FragmentFollowersTabBinding>(FragmentFollowersTabBinding::inflate) {
    private val viewModel: FollowersTabViewModel by viewModels()
    private val usersAdapter by lazy {
        UsersAdapter((parentFragment as RelationAndRepoFragment)::toUserDetail)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            followersTabRefreshSrl.setColorSchemeColors(mContext.getColorFromAttr(R.attr.colorPrimary))
            followersTabRefreshSrl.setOnRefreshListener {
                loadData()
            }
            viewModel.users.observe(viewLifecycleOwner, { response ->
                when {
                    response.status == Status.StatusType.FAILED -> {
                        showResult(
                            MessageType.ERROR,
                            subtitleMessage = response.message
                                ?: getString(R.string.unknown_error_message),
                        )
                    }
                    (response.data?.count() ?: 0) == 0 -> {
                        showResult(
                            MessageType.NOT_FOUND,
                            subtitleMessage = getString(R.string.no_one_here)
                        )
                    }
                    else -> {
                        showResult(MessageType.EXISTS)
                    }
                }
                followersTabRefreshSrl.isRefreshing = false
                usersAdapter.submitList(response.data)
            })
            followersTabListRv.apply {
                adapter = usersAdapter
                val itemDecoration =
                    DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL).apply {
                        getDrawable(mContext, R.drawable.space_item_decoration)?.let {
                            setDrawable(it)
                        }
                    }
                addItemDecoration(itemDecoration)
                layoutManager =
                    LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
            }
        }
    }

    private fun loadData() {
        viewModel.getUserFollowers(username)
    }

    override fun getRootViewGroup(): ViewGroup {
        return binding.followersTabRootCl
    }
}