package com.example.githubuser.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide

object ImageUtil {
    fun getDrawable(context: Context, id: Int): Drawable? {
        return ResourcesCompat.getDrawable(context.resources, id, null)
    }

    fun loadImage(
        context: Context,
        imageUrl: String?,
        defaultResourceId: Int,
        imageView: ImageView
    ) {
        Glide.with(context)
            .load(imageUrl)
            .error(defaultResourceId)
            .fallback(defaultResourceId)
            .centerCrop()
            .into(imageView)
    }

    fun loadImageToBitmap(
        context: Context,
        imageUrl: String?,
        defaultResourceId: Int,
    ): Bitmap {
        return Glide.with(context)
            .asBitmap()
            .load(imageUrl)
            .error(defaultResourceId)
            .fallback(defaultResourceId)
            .submit()
            .get()
    }
}