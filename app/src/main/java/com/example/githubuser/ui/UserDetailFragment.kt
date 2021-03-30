package com.example.githubuser.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.githubuser.R
import com.example.githubuser.databinding.FragmentUserDetailBinding
import com.example.githubuser.service.Status
import com.example.githubuser.util.ImageUtil.getDrawable
import com.example.githubuser.util.ImageUtil.loadImage
import com.example.githubuser.util.NumberUtil
import com.example.githubuser.viewmodel.UserDetailViewModel

class UserDetailFragment : Fragment(), View.OnClickListener {
    private lateinit var username: String
    private lateinit var mContext: Context
    private lateinit var mMainActivity: MainActivity
    private val args: UserDetailFragmentArgs by navArgs()
    private var _binding: FragmentUserDetailBinding? = null
    private val binding get() = _binding as FragmentUserDetailBinding
    private val viewModel: UserDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (mContext as AppCompatActivity).supportActionBar?.apply {
            title = username
        }
        _binding = FragmentUserDetailBinding.inflate(inflater, container, false)

        binding.apply {
            userDetailRepositoryCv.setOnClickListener(this@UserDetailFragment)
            userDetailFollowersCv.setOnClickListener(this@UserDetailFragment)
            userDetailFollowingCv.setOnClickListener(this@UserDetailFragment)
            viewModel.user.observe(viewLifecycleOwner, { response ->
                if (response.status == Status.StatusType.FAILED) {
                    showResult(
                        MainActivity.MessageType.ERROR,
                        subtitleMessage = response.message
                            ?: getString(R.string.unknown_error_message),
                    )
                } else {
                    val user = response.data
                    user?.avatarUrl.apply {
                        loadImage(
                            mContext,
                            response.data?.avatarUrl,
                            R.drawable.ic_person_black_24dp,
                            userDetailProfileSiv
                        )
                    }
                    userDetailNameTv.text = user?.name ?: "-"
                    userDetailRepositoryTv.text = NumberUtil.prettyCount(user?.publicRepos ?: 0)
                    userDetailFollowersTv.text = NumberUtil.prettyCount(user?.followers ?: 0)
                    userDetailFollowingTv.text = NumberUtil.prettyCount(user?.following ?: 0)
                    userDetailCompanyTv.text = user?.company ?: "-"
                    userDetailLocationTv.text = user?.location ?: "-"
                }
                mMainActivity.showProgressBar(false)
            })
        }


        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = requireContext()
        mMainActivity = mContext as MainActivity
        loadData()
    }

    private fun loadData() {
        username = args.username
        viewModel.getUserDetail(username)
        mMainActivity.showProgressBar(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mMainActivity.showProgressBar(false)
        _binding = null
    }

    private fun toRelationAndRepo(username: String, tab: Int) {
        val toRelationAndRepoFragment =
            UserDetailFragmentDirections.actionUserDetailFragmentToRelationAndRepoFragment(
                username = username,
                tab = tab
            )
        findNavController().navigate(toRelationAndRepoFragment)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.user_detail_repository_cv -> toRelationAndRepo(username, 0)
            R.id.user_detail_followers_cv -> toRelationAndRepo(username, 1)
            R.id.user_detail_following_cv -> toRelationAndRepo(username, 2)
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
                    userDetailContainerCl.visibility = View.VISIBLE
                    messageTitleTv.visibility = View.GONE
                    messageSubtitleTv.visibility = View.GONE
                    messageImageIv.visibility = View.GONE
                }
                MainActivity.MessageType.ERROR -> {
                    messageTitleTv.text =
                        titleMessage
                    messageSubtitleTv.text = subtitleMessage
                    messageImageIv.setImageDrawable(
                        getDrawable(
                            mContext,
                            R.drawable.ic_something_wrong
                        )
                    )
                    userDetailContainerCl.visibility = View.GONE
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