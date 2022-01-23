package com.juanpineda.mymovies

import android.app.Application

class MoviesApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initDI()
    }
}