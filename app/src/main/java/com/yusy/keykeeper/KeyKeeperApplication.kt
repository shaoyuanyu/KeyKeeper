package com.yusy.keykeeper

import android.app.Application
import com.yusy.keykeeper.data.AppContainer
import com.yusy.keykeeper.data.AppDataContainer

class KeyKeeperApplication: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}