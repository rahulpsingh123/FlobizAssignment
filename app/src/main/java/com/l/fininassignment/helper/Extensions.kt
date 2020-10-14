package com.l.fininassignment.helper

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.SystemClock
import android.view.View
import com.l.fininassignment.helper.Constants.NULL_STRING


fun View.click(debounceTime: Long = 600L, action: (view: View) -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0

        override fun onClick(v: View) {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
            else action(v)
            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}


@SuppressLint("ApplySharedPref")
fun SharedPreferences.put(pair: Pair<String, Any>, commit: Boolean = false) {
    val editor = edit()
    val key = pair.first
    when (val value = pair.second) {
        is String -> editor.putString(key, value)
        is Int -> editor.putInt(key, value)
        is Boolean -> editor.putBoolean(key, value)
        is Long -> editor.putLong(key, value)
        is Float -> editor.putFloat(key, value)
        else -> error("Only primitive types can be stored in SharedPreferences")
    }

    if (commit) editor.commit()
    else editor.apply()
}


fun String?.isNullString(): Boolean {
    return this == null || this == NULL_STRING
}


fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}




