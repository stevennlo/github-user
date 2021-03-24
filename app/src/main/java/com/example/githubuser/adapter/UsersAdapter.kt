package com.example.githubuser.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuser.databinding.ComponentUserBinding
import com.example.githubuser.model.User
import com.example.githubuser.ui.UserDetailActivity
import com.example.githubuser.ui.UserDetailActivity.Companion.EXTRA_INDEX
import com.example.githubuser.ui.UserDetailActivity.Companion.EXTRA_USER
import com.example.githubuser.ui.UsersActivity.Companion.REQUEST_FOR_USER_CHANGE
import com.example.githubuser.util.AssetUtil.getDrawableId

class UsersAdapter : ListAdapter<User, UsersAdapter.ViewHolder>(UserDiffCallback()) {
    class ViewHolder(private val binding: ComponentUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: User,
            position: Int
        ) {
            binding.apply {
                compUserProfileSiv.apply {
                    val profileId = getDrawableId(this.context, item.avatar)
                    setImageResource(profileId)
                }
                compUserNameTv.text = item.name
                compUserLocationTv.text = item.location
                compUserFavoriteIv.visibility = if (item.isFavorite) View.VISIBLE else View.GONE
                compUserLayout.setOnClickListener {
                    val intent = Intent(it.context, UserDetailActivity::class.java)
                    intent.putExtra(EXTRA_USER, item)
                        .putExtra(EXTRA_INDEX, position)
                    (it.context as Activity).startActivityForResult(intent, REQUEST_FOR_USER_CHANGE)
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ComponentUserBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ComponentUserBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item, position)
    }
}

class UserDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.username == newItem.username
    }
}
