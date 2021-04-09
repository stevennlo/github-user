package com.example.githubuser.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.githubuser.R
import com.example.githubuser.databinding.IncludeMessageBinding
import com.example.githubuser.databinding.IncludeProgressBinding
import com.example.githubuser.util.ImageUtil.getDrawable
import com.example.githubuser.util.MessageType

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<T : ViewBinding>(
    private val inflate: Inflate<T>
) : Fragment() {
    private var _binding: T? = null
    private var _progressBinding: IncludeProgressBinding? = null
    private var _messageBinding: IncludeMessageBinding? = null
    protected val binding get() = _binding as T
    private val progressBinding get() = _progressBinding as IncludeProgressBinding
    private val messageBinding get() = _messageBinding as IncludeMessageBinding
    protected lateinit var mContext: Context
    protected lateinit var mActivity: AppCompatActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = requireContext()
        mActivity = mContext as AppCompatActivity
    }

    open fun runOnCreateView() {}

    protected abstract fun getRootViewGroup(): ViewGroup

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = inflate.invoke(inflater, container, false)
        _messageBinding = IncludeMessageBinding.inflate(inflater, getRootViewGroup(), true)
        _progressBinding = IncludeProgressBinding.inflate(inflater, getRootViewGroup(), true)
        runOnCreateView()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _messageBinding = null
        _progressBinding = null
    }

    protected fun showResult(
        type: MessageType,
        titleMessage: String? = null,
        subtitleMessage: String? = null
    ) {
        when (type) {
            MessageType.LOAD -> {
                progressBinding.root.visibility = View.VISIBLE
                messageBinding.root.visibility = View.GONE
            }
            MessageType.EXISTS -> {
                progressBinding.root.visibility = View.GONE
                messageBinding.root.visibility = View.GONE
            }
            MessageType.NOT_FOUND, MessageType.ERROR, MessageType.WELCOME -> {
                progressBinding.root.visibility = View.GONE
                messageBinding.root.visibility = View.VISIBLE
                messageBinding.apply {
                    mainMessageTitleTv.apply {
                        text = titleMessage
                        isVisible = titleMessage !== null
                    }
                    mainMessageSubtitleTv.apply {
                        text = subtitleMessage
                        isVisible = subtitleMessage !== null
                    }

                    val drawableId = when (type) {
                        MessageType.WELCOME -> R.drawable.ic_search_welcome
                        MessageType.NOT_FOUND -> R.drawable.ic_not_found
                        else -> R.drawable.ic_something_wrong
                    }
                    mainMessageImageIv.apply {
                        setImageDrawable(
                            getDrawable(
                                mContext,
                                drawableId
                            )
                        )
                        visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}