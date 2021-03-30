package com.example.githubuser.ui

import android.app.SearchManager
import android.content.Context
import android.content.Context.SEARCH_SERVICE
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.adapter.UsersAdapter
import com.example.githubuser.databinding.FragmentUsersBinding
import com.example.githubuser.service.Status
import com.example.githubuser.util.ImageUtil.getDrawable
import com.example.githubuser.viewmodel.UsersViewModel

class UsersFragment : Fragment() {
    private lateinit var mContext: Context
    private var _binding: FragmentUsersBinding? = null
    private val binding get() = _binding as FragmentUsersBinding
    private val usersAdapter by lazy {
        UsersAdapter(this::toUserDetail)
    }
    private val viewModel: UsersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mContext = requireContext()
        _binding = FragmentUsersBinding.inflate(inflater, container, false)
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
                            getString(R.string.no_one_here),
                            getString(R.string.try_to_search_another_text)
                        )
                    }
                    else -> {
                        showResult(MainActivity.MessageType.EXISTS)
                    }
                }
                usersAdapter.submitList(response.data)
                (mContext as MainActivity).showProgressBar(false)
            })
            usersListRv.apply {
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
        setHasOptionsMenu(true)
        return binding.root
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
                    return false
                }

                override fun onQueryTextChange(keyword: String): Boolean {
                    return false
                }
            })
        }
    }

    private fun searchUser(keyword: String) {
        (mContext as MainActivity).showProgressBar(true)
        viewModel.searchUser(keyword)
    }

    override fun onDestroy() {
        super.onDestroy()
        (mContext as MainActivity).showProgressBar(false)
        _binding = null
    }

    private fun toUserDetail(username: String) {
        val toUserDetailFragment =
            UsersFragmentDirections.actionUsersFragmentToUserDetailFragment(username)
        findNavController().navigate(toUserDetailFragment)
    }

    private fun showResult(
        type: MainActivity.MessageType,
        titleMessage: String? = null,
        subtitleMessage: String? = null
    ) {
        binding.apply {
            when (type) {
                MainActivity.MessageType.EXISTS -> {
                    usersListRv.visibility = View.VISIBLE
                    messageTitleTv.visibility = View.GONE
                    messageSubtitleTv.visibility = View.GONE
                    messageImageIv.visibility = View.GONE
                }
                MainActivity.MessageType.NOT_FOUND -> {
                    messageTitleTv.text = titleMessage
                    messageSubtitleTv.text = subtitleMessage
                    messageImageIv.setImageDrawable(
                        getDrawable(
                            mContext,
                            R.drawable.ic_not_found
                        )
                    )
                    usersListRv.visibility = View.GONE
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
                    usersListRv.visibility = View.GONE
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
