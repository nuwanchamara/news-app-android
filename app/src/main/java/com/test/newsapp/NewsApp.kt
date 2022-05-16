package com.test.newsapp

import android.app.Application
import android.content.Context

class NewsApp : Application() {

    companion object {
        lateinit var instance: NewsApp
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        appContext = applicationContext
    }


}