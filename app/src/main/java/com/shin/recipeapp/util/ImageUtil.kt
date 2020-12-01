package com.shin.recipeapp.util

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.shin.recipeapp.R
import java.io.File

object ImageUtil {
    @SuppressLint("CheckResult")
    @BindingAdapter(value = ["path", "scaleType"], requireAll = false)
    @JvmStatic
    fun addImageWithPath(imageView: ImageView, path: String?, scaleType: String?) {
        val type: RequestOptions = if (scaleType !== null) {
            when (scaleType) {
                "fitCenter" -> RequestOptions().fitCenter()
                "centerCrop" -> RequestOptions().centerCrop()
                "centerInside" -> RequestOptions().centerInside()
                "circleCrop" -> RequestOptions().circleCrop()
                else -> RequestOptions().fitCenter()
            }
        } else {
            RequestOptions().fitCenter()
        }
        type.placeholder(R.drawable.recipe_place_holder)
        if (!path.isNullOrEmpty()) {
            val file = File(path)
            val imageUri: Uri = Uri.fromFile(file)

            path?.let {
                Glide.with(imageView.context)
                    .load(imageUri)
                    .apply(type)
                    .into(imageView)
            }
        } else {
            Glide.with(imageView.context)
                .load("")
                .apply(type)
                .into(imageView)
        }
    }

    @JvmStatic
    fun getPathImg(context: Context, uri: Uri?): String {
        var result: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? =
            uri?.let { context.contentResolver.query(it, projection, null, null, null) }
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val columnIndex: Int = cursor.getColumnIndexOrThrow(projection[0])
                result = cursor.getString(columnIndex)
            }
            cursor.close()
        }
        if (result == null) {
            result = "Error"
        }
        return result
    }

}