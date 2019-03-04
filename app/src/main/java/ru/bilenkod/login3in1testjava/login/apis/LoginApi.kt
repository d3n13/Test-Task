package ru.bilenkod.login3in1testjava.login.apis

import android.app.Activity
import android.content.Intent
import ru.bilenkod.login3in1testjava.login.AuthState

interface LoginApi {

    var authState: AuthState

    fun logout()

    fun initLoginFrom(activity: Activity)

    fun signInResult(requestCode: Int, resultCode: Int, data: Intent?)

    fun checkLogin()

    fun updateAuthState() {
        authState = authState.copy()
    }
}