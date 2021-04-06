package com.example.githubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuser.databinding.ComponentSettingBinding
import com.example.githubuser.model.Setting

class SettingsAdapter(private val switchChangeListener: (String, Boolean) -> Unit) :
    ListAdapter<Setting, SettingsAdapter.ViewHolder>(SettingDiffCallback()) {
    class ViewHolder(
        private val binding: ComponentSettingBinding,
        private val switchChangeListener: (String, Boolean) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: Setting
        ) {
            binding.apply {
                settingNameTv.text = item.name
                settingDescriptionTv.text = item.description
                settingSwitchSm.apply {
                    isChecked = item.isActive
                    setOnCheckedChangeListener { _, isChecked ->
                        switchChangeListener.invoke(item.code, isChecked)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ComponentSettingBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding, switchChangeListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item)
    }
}

class SettingDiffCallback : DiffUtil.ItemCallback<Setting>() {
    override fun areItemsTheSame(oldItem: Setting, newItem: Setting): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Setting, newItem: Setting): Boolean {
        return oldItem.code == newItem.code
    }
}
