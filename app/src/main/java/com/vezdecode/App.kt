package com.vezdecode

import android.app.Application

class App: Application() {
    companion object {
        private var instance: App? = null

        fun applicationContext(): App {
            return instance as App
        }
    }

    init {
        instance = this
    }
}