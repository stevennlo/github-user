package com.example.githubuser.ui

import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.githubuser.adapter.SettingsAdapter
import com.example.githubuser.databinding.FragmentSettingsBinding
import com.example.githubuser.util.MessageType
import com.example.githubuser.viewmodel.SettingsViewModel

class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {
    private val viewModel: SettingsViewModel by viewModels()
    private val settingsAdapter by lazy {
        SettingsAdapter(this::changeSetting)
    }

    override fun runOnCreateView() {
        super.runOnCreateView()
        showResult(MessageType.EXISTS)
        binding.apply {
            settingsListRv.adapter = settingsAdapter
            viewModel.settings.observe(viewLifecycleOwner, {
                settingsAdapter.submitList(it.values.toList())
            })
        }
    }

    private fun changeSetting(key: String, value: Boolean) {
        viewModel.changeSetting(key, value)
    }

    override fun getRootViewGroup(): ViewGroup {
        return binding.root
    }
}