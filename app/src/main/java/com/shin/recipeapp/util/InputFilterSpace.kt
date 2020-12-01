package com.shin.recipeapp.util

import android.text.InputFilter
import android.text.Spanned

class InputFilterSpace : InputFilter {

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        for (i in start until end) {
            if (Character.isSpaceChar(source[i])) {
                return ""
            }
        }
        return null
    }

}