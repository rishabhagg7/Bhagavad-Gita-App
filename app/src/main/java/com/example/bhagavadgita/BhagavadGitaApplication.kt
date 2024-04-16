package com.example.bhagavadgita

import android.app.Application
import com.example.bhagavadgita.data.AppContainer
import com.example.bhagavadgita.data.DefaultAppContainer

class BhagavadGitaApplication: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}