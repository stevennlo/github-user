package com.example.githubuser.ui

import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.adapter.UsersAdapter
import com.example.githubuser.databinding.FragmentFavoriteBinding
import com.example.githubuser.util.ImageUtil
import com.example.githubuser.util.MessageType
import com.example.githubuser.viewmodel.FavoriteViewModel

class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>(FragmentFavoriteBinding::inflate) {
    private val viewModel: FavoriteViewModel by viewModels()
    private val usersAdapter by lazy {
        UsersAdapter(this::toUserDetail, this::isFavoriteUser)
    }

    override fun runOnCreateView() {
        super.runOnCreateView()
        binding.apply {
            favoriteListRv.apply {
                adapter = usersAdapter
                val itemDecoration =
                    DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL).apply {
                        ImageUtil.getDrawable(mContext, R.drawable.space_item_decoration)?.let {
                            setDrawable(it)
                        }
                    }
                addItemDecoration(itemDecoration)
                viewModel.getAllUsers(mContext).observe(viewLifecycleOwner, {
                    if (it.isEmpty()) {
                        showResult(
                            MessageType.NOT_FOUND,
                            subtitleMessage = getString(R.string.no_one_here)
                        )
                    } else {
                        usersAdapter.submitList(it)
                        showResult(MessageType.EXISTS)
                    }
                })
            }
        }
    }

    private fun toUserDetail(username: String) {
        val toUserDetailFragment =
            FavoriteFragmentDirections.actionFavoriteFragmentToUserDetailFragment(
                username
            )
        findNavController().navigate(toUserDetailFragment)
    }

    private fun isFavoriteUser(
        @Suppress("UNUSED_PARAMETER") username: String,
        favoriteIcon: ImageView
    ) {
        favoriteIcon.isVisible = true
    }

    override fun getRootViewGroup(): ViewGroup {
        return binding.root
    }
}