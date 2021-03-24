package com.example.githubuser.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.githubuser.R
import com.example.githubuser.databinding.ActivityUserDetailBinding
import com.example.githubuser.model.User
import com.example.githubuser.ui.UsersActivity.Companion.EXTRA_FAVORITE_RESULT
import com.example.githubuser.util.AssetUtil.getDrawableId
import com.example.githubuser.util.NumberUtil.prettyCount

class UserDetailActivity : AppCompatActivity() {
    companion object {
        val EXTRA_USER = "USER_DETAIL_EXTRA_USER"
        val EXTRA_INDEX = "USER_DETAIL_EXTRA_INDEX"
    }

    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var user: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        user = intent.getParcelableExtra<User>(EXTRA_USER) as User
        supportActionBar?.title = user.username
        binding.apply {
            val profileId = getDrawableId(applicationContext, user.avatar)
            userDetailProfileSiv.setImageResource(profileId)
            userDetailNameTv.text = user.name
            userDetailRepositoryTv.text = prettyCount(user.repository)
            userDetailFollowerTv.text = prettyCount(user.follower)
            userDetailFollowingTv.text = prettyCount(user.following)
            userDetailCompanyTv.text = user.company
            userDetailLocationTv.text = user.location
        }

        setContentView(binding.root)
    }

    override fun onNavigateUp(): Boolean {
        setActivityResult()
        return super.onNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.user_detail_menu, menu)
        setFavoriteIcon(menu.getItem(0), user.isFavorite)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.user_detail_favorite_item -> {
                toggleFavorite()
                setFavoriteIcon(item, user.isFavorite)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return false
    }

    private fun toggleFavorite() {
        user.isFavorite = user.isFavorite xor true
    }

    private fun setFavoriteIcon(item: MenuItem, isFavorite: Boolean) {
        val iconId =
            if (isFavorite) R.drawable.ic_heart_red_24dp else R.drawable.ic_heart_outlined_white_24dp
        item.setIcon(iconId)
    }

    private fun setActivityResult() {
        val resultIntent = Intent()
        val position = intent.getIntExtra(EXTRA_INDEX, 0)
        resultIntent.putExtra(EXTRA_INDEX, position)
            .putExtra(EXTRA_FAVORITE_RESULT, user.isFavorite)
        setResult(Activity.RESULT_OK, resultIntent)
    }

    override fun onBackPressed() {
        setActivityResult()
        super.onBackPressed()
    }
}