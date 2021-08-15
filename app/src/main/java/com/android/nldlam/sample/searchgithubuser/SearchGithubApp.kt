package com.android.nldlam.sample.searchgithubuser

import android.app.Application

class SearchGithubApp : Application() {
    companion object {
        lateinit var instance: SearchGithubApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}