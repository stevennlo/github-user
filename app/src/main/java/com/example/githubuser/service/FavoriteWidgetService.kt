package com.example.githubuser.service

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Binder
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.example.githubuser.R
import com.example.githubuser.model.User
import com.example.githubuser.service.FavoriteProvider.Companion.USERS_FAVORITE_URI
import com.example.githubuser.util.EXTRA_USER_ITEM
import com.example.githubuser.util.ImageUtil
import com.example.githubuser.util.toListUser

class FavoriteWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory =
        FavoriteRemoteViewsFactory(this.applicationContext)
}

internal class FavoriteRemoteViewsFactory(private val mContext: Context) :
    RemoteViewsService.RemoteViewsFactory {
    private var list: List<User> = listOf()
    private var cursor: Cursor? = null

    override fun onCreate() {}

    override fun onDataSetChanged() {
        cursor?.close()
        val identityToken = Binder.clearCallingIdentity()
        cursor = mContext.contentResolver?.query(USERS_FAVORITE_URI, null, null, null, null)
        cursor?.let {
            list = it.toListUser()
        }

        Binder.restoreCallingIdentity(identityToken)
    }

    override fun onDestroy() {
        cursor?.close()
        list = listOf()
    }

    override fun getCount(): Int = list.size

    override fun getViewAt(position: Int): RemoteViews {
        val views = RemoteViews(mContext.packageName, R.layout.component_favorite_widget)
        if (!list.isNullOrEmpty()) {
            views.apply {
                list[position].let {
                    setImageViewBitmap(
                        R.id.comp_favorite_profile_siv, ImageUtil.loadImageToBitmap(
                            mContext,
                            it.avatarUrl,
                            R.drawable.ic_person_black_24dp,
                        )
                    )
                    setTextViewText(R.id.comp_favorite_name_tv, it.username)
                    val extras = bundleOf(
                        EXTRA_USER_ITEM to it.username
                    )
                    val fillInIntent = Intent().putExtras(extras)
                    setOnClickFillInIntent(R.id.comp_favorite_root_rl, fillInIntent)
                }
            }
        }

        return views
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = 0

    override fun hasStableIds(): Boolean = true
}