package com.example.githubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuser.databinding.ComponentRepositoryBinding
import com.example.githubuser.model.Repository
import com.example.githubuser.util.NumberUtil.prettyCount

class RepositoryAdapter :
    ListAdapter<Repository, RepositoryAdapter.ViewHolder>(RepositoryDiffCallback()) {
    class ViewHolder(private val binding: ComponentRepositoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Repository) {
            binding.apply {
                compRepositoryNameTv.text = item.fullName
                compRepositoryOwnerTv.text = item.owner?.username
                compRepositoryStarsTv.text = prettyCount(item.stargazersCount ?: 0)
                compRepositoryForksTv.text = prettyCount(item.forksCount ?: 0)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ComponentRepositoryBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item)
    }
}

class RepositoryDiffCallback : DiffUtil.ItemCallback<Repository>() {
    override fun areItemsTheSame(oldItem: Repository, newItem: Repository): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Repository, newItem: Repository): Boolean {
        return oldItem.id == newItem.id
    }
}