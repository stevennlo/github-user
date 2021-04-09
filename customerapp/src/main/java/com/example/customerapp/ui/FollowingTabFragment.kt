package com.example.customerapp.ui

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.consumerapp.R
import com.example.consumerapp.databinding.FragmentFollowingTabBinding
import com.example.customerapp.adapter.UsersAdapter
import com.example.customerapp.service.Status
import com.example.customerapp.util.ImageUtil.getDrawable
import com.example.customerapp.util.MessageType
import com.example.customerapp.viewmodel.FollowingTabViewModel

class FollowingTabFragment(private val username: String) :
    BaseFragment<FragmentFollowingTabBinding>(FragmentFollowingTabBinding::inflate) {
    private val viewModel: FollowingTabViewModel by viewModels()
    private val usersAdapter by lazy {
        UsersAdapter(
            (parentFragment as RelationAndRepoFragment)::toUserDetail,
            this::isFavoriteUser
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            followingTabRefreshSrl.setOnRefreshListener {
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
                followingTabRefreshSrl.isRefreshing = false
                usersAdapter.submitList(response.data)
            })
            followingTabListRv.apply {
                adapter = usersAdapter
                val itemDecoration =
                    DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL).apply {
                        getDrawable(mContext, R.drawable.space_item_decoration)?.let {
                            setDrawable(it)
                        }
                    }
                addItemDecoration(itemDecoration)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadData()
    }

    private fun loadData() {
        viewModel.getUserFollowing(username)
    }

    private fun isFavoriteUser(id: Int, favoriteIcon: ImageView) {
        viewModel.getIsFavorite(mContext, id).observe(viewLifecycleOwner, {
            val isFavorite = it
            favoriteIcon.isVisible = isFavorite
        })
    }

    override fun getRootViewGroup(): ViewGroup {
        return binding.followingTabRootCl
    }
}