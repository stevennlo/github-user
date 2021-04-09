package com.example.githubuser.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.githubuser.R
import com.example.githubuser.databinding.FragmentUserDetailBinding
import com.example.githubuser.model.User
import com.example.githubuser.service.Status
import com.example.githubuser.util.ImageUtil.loadImage
import com.example.githubuser.util.MessageType
import com.example.githubuser.util.NumberUtil
import com.example.githubuser.viewmodel.UserDetailViewModel

class UserDetailFragment :
    BaseFragment<FragmentUserDetailBinding>(FragmentUserDetailBinding::inflate),
    View.OnClickListener {
    private lateinit var user: User
    private val args: UserDetailFragmentArgs by navArgs()
    private val viewModel: UserDetailViewModel by viewModels()
    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadData()
    }

    override fun runOnCreateView() {
        super.runOnCreateView()
        mActivity.supportActionBar?.title = user.username
        binding.apply {
            userDetailRefreshSrl.setOnRefreshListener {
                loadData()
            }
            userDetailRepositoryCv.setOnClickListener(this@UserDetailFragment)
            userDetailFollowersCv.setOnClickListener(this@UserDetailFragment)
            userDetailFollowingCv.setOnClickListener(this@UserDetailFragment)
            userDetailFavoriteFab.setOnClickListener(this@UserDetailFragment)
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
                    user?.let {
                        user.avatarUrl.apply {
                            loadImage(
                                mContext,
                                user.avatarUrl,
                                R.drawable.ic_person_black_24dp,
                                userDetailProfileSiv
                            )
                        }
                        userDetailNameTv.text = user.name ?: "-"
                        userDetailRepositoryTv.text = NumberUtil.prettyCount(user.publicRepos ?: 0)
                        userDetailFollowersTv.text = NumberUtil.prettyCount(user.followers ?: 0)
                        userDetailFollowingTv.text = NumberUtil.prettyCount(user.following ?: 0)
                        userDetailCompanyTv.text = user.company ?: "-"
                        userDetailLocationTv.text = user.location ?: "-"
                    }
                }
                userDetailRefreshSrl.isRefreshing = false
            })

            viewModel.getIsFavorite(mContext, user.id).observe(viewLifecycleOwner, {
                isFavorite = it
                setFavoriteIcon(it)
            })
        }
        setHasOptionsMenu(true)
    }

    private fun setFavoriteIcon(isFavorite: Boolean) {
        val iconId =
            if (isFavorite) R.drawable.ic_favorite_white_24dp else R.drawable.ic_favorite_border_white_24dp
        binding.userDetailFavoriteFab.setImageResource(iconId)
    }

    private fun loadData() {
        user = args.user
        viewModel.getUserDetail(user.username)
    }

    private fun toRelationAndRepo(user: User, tab: Int) {
        val toRelationAndRepoFragment =
            UserDetailFragmentDirections.actionUserDetailFragmentToRelationAndRepoFragment(
                user = user,
                tab = tab
            )
        findNavController().navigate(toRelationAndRepoFragment)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.user_detail_repository_cv -> toRelationAndRepo(user, 0)
            R.id.user_detail_followers_cv -> toRelationAndRepo(user, 1)
            R.id.user_detail_following_cv -> toRelationAndRepo(user, 2)
            R.id.user_detail_favorite_fab -> viewModel.setFavorite(mContext, isFavorite xor true)
        }
    }

    override fun getRootViewGroup(): ViewGroup {
        return binding.userDetailRootCl
    }
}