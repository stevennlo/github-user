package com.example.githubuser.util

import android.app.Application
import android.content.Context

object AssetUtil {
    fun loadJSONFromAsset(app: Application, fileName: String): String {
        return app.assets.open(fileName).bufferedReader().use {
            it.readText()
        }
    }

    fun getDrawableId(context: Context, fileName: String): Int {
        return context.resources.getIdentifier(
            fileName,
            "drawable",
            context.packageName
        )
    }
}