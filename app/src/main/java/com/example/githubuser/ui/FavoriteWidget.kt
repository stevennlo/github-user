package com.example.githubuser.ui

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import com.example.githubuser.R
import com.example.githubuser.service.FavoriteWidgetService
import com.example.githubuser.util.EXTRA_USER_ITEM
import com.example.githubuser.util.FAVORITE_WIDGET_EACH_ACTION

class FavoriteWidget : AppWidgetProvider() {
    companion object {
        private fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val intent = Intent(context, FavoriteWidgetService::class.java).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                data = toUri(Intent.URI_INTENT_SCHEME).toUri()
            }
            val itemClickIntent = Intent(context, FavoriteWidget::class.java).apply {
                action = FAVORITE_WIDGET_EACH_ACTION
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()
            }
            val itemClickPendingIntent = PendingIntent.getBroadcast(
                context, 0,
                itemClickIntent, PendingIntent.FLAG_UPDATE_CURRENT
            )
            val views = RemoteViews(context.packageName, R.layout.favorite_widget).apply {
                setRemoteAdapter(R.id.favorite_list_sv, intent)
                setEmptyView(R.id.favorite_list_sv, R.id.favorite_empty_ll)
                setPendingIntentTemplate(R.id.favorite_list_sv, itemClickPendingIntent)
            }
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        fun sendRefreshBroadcast(context: Context) {
            val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE).apply {
                component = ComponentName(context, FavoriteWidget::class.java)
            }
            context.sendBroadcast(intent)
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            AppWidgetManager.ACTION_APPWIDGET_UPDATE -> {
                val component = ComponentName(
                    context,
                    FavoriteWidget::class.java
                )
                AppWidgetManager.getInstance(context).apply {
                    notifyAppWidgetViewDataChanged(
                        getAppWidgetIds(component),
                        R.id.favorite_list_sv
                    )
                }
            }
            FAVORITE_WIDGET_EACH_ACTION -> {
                val username = intent.getStringExtra(EXTRA_USER_ITEM)
                NavDeepLinkBuilder(context)
                    .setGraph(R.navigation.favorite_nav)
                    .setDestination(R.id.userDetailFragment)
                    .setArguments(bundleOf("username" to username))
                    .createPendingIntent()
                    .send()
            }
        }
        super.onReceive(context, intent)
    }
}