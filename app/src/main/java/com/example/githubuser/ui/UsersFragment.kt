package com.example.githubuser.ui

import android.app.SearchManager
import android.content.Context.SEARCH_SERVICE
import android.view.Menu
import android.view.MenuInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.adapter.UsersAdapter
import com.example.githubuser.databinding.FragmentUsersBinding
import com.example.githubuser.service.Status
import com.example.githubuser.util.ImageUtil.getDrawable
import com.example.githubuser.util.MessageType
import com.example.githubuser.viewmodel.UsersViewModel

class UsersFragment : BaseFragment<FragmentUsersBinding>(FragmentUsersBinding::inflate) {
    private val usersAdapter by lazy {
        UsersAdapter(this::toUserDetail, this::isFavoriteUser)
    }
    private val viewModel: UsersViewModel by viewModels()

    override fun runOnCreateView() {
        super.runOnCreateView()
        showResult(
            MessageType.WELCOME,
            getString(R.string.welcome_text),
            getString(R.string.try_to_search_someone_text)
        )
        binding.apply {
            usersRefreshSrl.setOnRefreshListener {
                viewModel.keyword.value?.let {
                    searchUser(it)
                }
                if (viewModel.keyword.value === null) usersRefreshSrl.isRefreshing = false
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
                            getString(R.string.no_one_here),
                            getString(R.string.try_to_search_another_text)
                        )
                    }
                    else -> {
                        showResult(MessageType.EXISTS)
                    }
                }
                usersRefreshSrl.isRefreshing = false
                usersAdapter.submitList(response.data)
            })
            usersListRv.apply {
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
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.users_menu, menu)
        val searchView = menu.findItem(R.id.users_search_item).actionView as SearchView
        val searchManager = mContext.getSystemService(SEARCH_SERVICE) as SearchManager
        searchView.apply {
            maxWidth = Integer.MAX_VALUE
            isIconified = false
            setSearchableInfo(searchManager.getSearchableInfo((mContext as AppCompatActivity).componentName))
            queryHint = resources.getString(R.string.search_hint)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(keyword: String): Boolean {
                    searchUser(keyword)
                    showResult(MessageType.LOAD)
                    return false
                }

                override fun onQueryTextChange(keyword: String): Boolean {
                    return false
                }
            })
        }
    }

    private fun searchUser(keyword: String) {
        viewModel.searchUser(keyword)
    }

    private fun toUserDetail(username: String) {
        val toUserDetailFragment =
            UsersFragmentDirections.actionUsersFragmentToUserDetailFragment(username)
        findNavController().navigate(toUserDetailFragment)
    }

    private fun isFavoriteUser(username: String, favoriteIcon: ImageView) {
        viewModel.getIsFavorite(mContext, username).observe(viewLifecycleOwner, {
            val isFavorite = it
            favoriteIcon.isVisible = isFavorite
        })
    }

    override fun getRootViewGroup(): ViewGroup {
        return binding.usersRootCl
    }
}
