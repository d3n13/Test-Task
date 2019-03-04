package ru.bilenkod.login3in1testjava.login

import android.app.Activity
import android.content.Intent
import ru.bilenkod.login3in1testjava.login.apis.ApiHolder

class LoginProvider {
    private val apiHolder: ApiHolder = ApiHolder()

    val isLoggedIn = apiHolder.isLoggedIn
    val userAvatarUrl = apiHolder.userAvatarUrl
    val userName = apiHolder.userName

    fun logout() {
        apiHolder.logout()
    }

    fun signInResult(requestCode: Int, resultCode: Int, data: Intent?) {
        apiHolder.signInResult(requestCode, resultCode, data)
    }

    fun initLoginViaFrom(apiKey: Int, activity: Activity) {
        apiHolder.swapCurrentApi(apiKey)
        apiHolder.initLoginFrom(activity)
    }
}