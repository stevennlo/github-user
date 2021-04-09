package com.example.githubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuser.R
import com.example.githubuser.databinding.ComponentUserBinding
import com.example.githubuser.model.User
import com.example.githubuser.util.ImageUtil

class UsersAdapter(
    private val clickListener: (User) -> Unit,
    private val favoriteChangeListener: (Int, ImageView) -> Unit
) :
    ListAdapter<User, UsersAdapter.ViewHolder>(UserDiffCallback()) {
    class ViewHolder(
        private val binding: ComponentUserBinding,
        private val layoutClickListener: (User) -> Unit,
        private val favoriteChangeListener: (Int, ImageView) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: User
        ) {
            binding.apply {
                compUserProfileSiv.apply {
                    ImageUtil.loadImage(
                        this.context,
                        item.avatarUrl,
                        R.drawable.ic_person_black_24dp,
                        compUserProfileSiv
                    )
                }
                compUserNameTv.text = item.username
                root.setOnClickListener {
                    layoutClickListener.invoke(item)
                }
                compUserRoleTv.text = item.type
                favoriteChangeListener.invoke(item.id, compUserFavoriteIv)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ComponentUserBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding, clickListener, favoriteChangeListener)
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
