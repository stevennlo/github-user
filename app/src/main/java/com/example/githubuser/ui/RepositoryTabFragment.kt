package com.example.githubuser.ui

import android.content.Context
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
import com.example.githubuser.adapter.RepositoryAdapter
import com.example.githubuser.databinding.FragmentRepositoryTabBinding
import com.example.githubuser.service.Status
import com.example.githubuser.util.ImageUtil
import com.example.githubuser.viewmodel.RepositoryTabViewModel

class RepositoryTabFragment(private val username: String) : Fragment() {
    private lateinit var mContext: Context
    private var _binding: FragmentRepositoryTabBinding? = null
    private val binding: FragmentRepositoryTabBinding get() = _binding as FragmentRepositoryTabBinding
    private val viewModel: RepositoryTabViewModel by viewModels()
    private val reposAdapter = RepositoryAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mContext = requireContext()
        _binding = FragmentRepositoryTabBinding.inflate(inflater, container, false)
        binding.apply {
            viewModel.repos.observe(viewLifecycleOwner, { response ->
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
                            subtitleMessage = getString(R.string.no_repo_yet)
                        )
                    }
                    else -> {
                        showResult(MainActivity.MessageType.EXISTS)
                    }
                }
                reposAdapter.submitList(response.data)
                showProgressBar(false)
            })
            repositoryTabListRv.apply {
                adapter = reposAdapter
                val itemDecoration =
                    DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL).apply {
                        setDrawable(
                            ResourcesCompat.getDrawable(
                                resources,
                                R.drawable.space_item_decoration,
                                null
                            )!!
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
        viewModel.getUserRepos(username)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        showProgressBar(false)
        _binding = null
    }

    private fun showProgressBar(isVisible: Boolean) {
        if (isVisible) {
            binding.repositoryTabLoadingPb.visibility = View.VISIBLE
        } else {
            binding.repositoryTabLoadingPb.visibility = View.GONE
        }
    }

    private fun showResult(
        type: MainActivity.MessageType,
        titleMessage: String? = null,
        subtitleMessage: String? = null
    ) {
        binding.apply {
            when (type) {
                MainActivity.MessageType.EXISTS -> {
                    repositoryTabListRv.visibility = View.VISIBLE
                    messageTitleTv.visibility = View.GONE
                    messageSubtitleTv.visibility = View.GONE
                    messageImageIv.visibility = View.GONE
                }
                MainActivity.MessageType.NOT_FOUND -> {
                    messageTitleTv.text = titleMessage
                    messageSubtitleTv.text = subtitleMessage
                    messageImageIv.setImageDrawable(
                        ImageUtil.getDrawable(
                            mContext,
                            R.drawable.ic_not_found
                        )
                    )
                    repositoryTabListRv.visibility = View.GONE
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
                        ImageUtil.getDrawable(
                            mContext,
                            R.drawable.ic_something_wrong
                        )
                    )
                    repositoryTabListRv.visibility = View.GONE
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