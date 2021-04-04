package com.example.githubuser.ui

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.githubuser.R
import com.example.githubuser.databinding.FragmentUserDetailBinding
import com.example.githubuser.service.Status
import com.example.githubuser.util.ImageUtil.loadImage
import com.example.githubuser.util.MessageType
import com.example.githubuser.util.NumberUtil
import com.example.githubuser.util.getColorFromAttr
import com.example.githubuser.viewmodel.UserDetailViewModel

class UserDetailFragment :
    BaseFragment<FragmentUserDetailBinding>(FragmentUserDetailBinding::inflate),
    View.OnClickListener {
    private lateinit var username: String
    private val args: UserDetailFragmentArgs by navArgs()
    private val viewModel: UserDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadData()
    }

    override fun runOnCreateView() {
        super.runOnCreateView()
        mActivity.supportActionBar?.title = username
        binding.apply {
            userDetailRefreshSrl.setColorSchemeColors(mContext.getColorFromAttr(R.attr.colorPrimary))
            userDetailRefreshSrl.setOnRefreshListener {
                loadData()
            }
            userDetailRepositoryCv.setOnClickListener(this@UserDetailFragment)
            userDetailFollowersCv.setOnClickListener(this@UserDetailFragment)
            userDetailFollowingCv.setOnClickListener(this@UserDetailFragment)
            viewModel.user.observe(viewLifecycleOwner, { response ->
                if (response.status == Status.StatusType.FAILED) {
                    showResult(
                        MessageType.ERROR,
                        subtitleMessage = response.message
                            ?: getString(R.string.unknown_error_message),
                    )
                } else {
                    showResult(MessageType.EXISTS)
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
                userDetailRefreshSrl.isRefreshing = false
            })
        }
    }

    private fun loadData() {
        username = args.username
        viewModel.getUserDetail(username)
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

    override fun getRootViewGroup(): ViewGroup {
        return binding.userDetailRootCl
    }
}