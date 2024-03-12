package com.gmart.gmovies.app

import android.app.Application
import android.content.Context
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MoviesApp : Application() {

    lateinit var instance: MoviesApp
        private set

    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
        instance = this
    }
    fun getContext(): Context {
        return instance
    }

}