package com.shin.recipeapp.localDb.sharePrefs

import android.content.Context
import android.content.SharedPreferences

class SharePreferenceManager private constructor(private val mCtx: Context) {

    companion object {
        private const val SHARED_NAME = "recipe_share_pref"
        private var mInstance: SharePreferenceManager? = null

        @Synchronized
        fun getInstance(mCtx: Context): SharePreferenceManager {
            if (mInstance == null) {
                mInstance = SharePreferenceManager(mCtx)
            }
            return mInstance as SharePreferenceManager
        }
    }

    val isNewUser: Boolean
        get() {
            val sharedPreferences =
                mCtx.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean("newUser", true)
        }

    fun setNewUser(isNewUser: Boolean) {
        val sharedPreferences: SharedPreferences =
            mCtx.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("newUser", isNewUser)
        editor.apply()
    }

    fun clear() {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

}