package com.l.fininassignment

import android.annotation.SuppressLint
import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import com.google.gson.Gson
import io.realm.Realm
import io.realm.RealmConfiguration

val gson = Gson()

fun string(@StringRes id: Int): String = FlobizApplication.instance.resources.getString(id)

fun color(@ColorRes id: Int): Int = ResourcesCompat.getColor(FlobizApplication.instance.resources, id, null)

class FlobizApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(config)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @get:Synchronized
        lateinit var instance: Application
            private set

        val pref: SharedPreferences by lazy { PreferenceManager.getDefaultSharedPreferences(instance) }

    }
}