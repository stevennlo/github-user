package com.example.customerapp.ui

import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.consumerapp.R
import com.example.consumerapp.databinding.FragmentRepositoryTabBinding
import com.example.customerapp.adapter.RepositoryAdapter
import com.example.customerapp.service.Status
import com.example.customerapp.util.ImageUtil.getDrawable
import com.example.customerapp.util.MessageType
import com.example.customerapp.viewmodel.RepositoryTabViewModel

class RepositoryTabFragment(private val username: String) :
    BaseFragment<FragmentRepositoryTabBinding>(FragmentRepositoryTabBinding::inflate) {
    private val viewModel: RepositoryTabViewModel by viewModels()
    private val reposAdapter = RepositoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadData()
    }

    override fun runOnCreateView() {
        super.runOnCreateView()

        binding.apply {
            repositoryTabRefreshSrl.setOnRefreshListener {
                loadData()
            }
            viewModel.repos.observe(viewLifecycleOwner, { response ->
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
                            subtitleMessage = getString(R.string.no_repo_yet)
                        )
                    }
                    else -> {
                        showResult(MessageType.EXISTS)
                    }
                }
                repositoryTabRefreshSrl.isRefreshing = false
                reposAdapter.submitList(response.data)
            })
            repositoryTabListRv.apply {
                adapter = reposAdapter
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
        viewModel.getUserRepos(username)
    }

    override fun getRootViewGroup(): ViewGroup {
        return binding.repositoryTabRootCl
    }
}