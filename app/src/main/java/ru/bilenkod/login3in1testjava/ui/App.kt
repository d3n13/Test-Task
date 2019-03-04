package ru.bilenkod.login3in1testjava.ui

import android.app.Application
import ru.bilenkod.login3in1testjava.login.LoginProvider

class App : Application() {

    companion object {
        lateinit var instance: App
            private set
        lateinit var loginProvider: LoginProvider
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        loginProvider = LoginProvider()
    }
}