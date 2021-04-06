package com.example.githubuser.ui

import android.view.ViewGroup
import com.example.githubuser.databinding.FragmentFavoriteBinding
import com.example.githubuser.util.MessageType

class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>(FragmentFavoriteBinding::inflate) {
    override fun runOnCreateView() {
        super.runOnCreateView()
        showResult(MessageType.EXISTS)
    }

    override fun getRootViewGroup(): ViewGroup {
        return binding.root
    }
}