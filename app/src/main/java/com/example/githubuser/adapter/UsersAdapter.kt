package com.example.githubuser.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuser.databinding.ComponentUserBinding
import com.example.githubuser.model.User
import com.example.githubuser.util.AssetUtil.getDrawableId

class UsersAdapter(private val clickListener: (User) -> Unit) :
    ListAdapter<User, UsersAdapter.ViewHolder>(UserDiffCallback()) {
    class ViewHolder(
        private val binding: ComponentUserBinding,
        private val layoutClickListener: (User) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: User
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
                    layoutClickListener.invoke(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ComponentUserBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item)
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
