package ru.bilenkod.login3in1testjava.login.apis

import android.app.Activity
import android.content.Intent
import ru.bilenkod.login3in1testjava.login.AuthState

class EmptyLoginApi: LoginApi {

    override var authState: AuthState = AuthState()

    override fun logout() {
    }

    override fun checkLogin() {
    }

    override fun initLoginFrom(activity: Activity) {
    }

    override fun signInResult(requestCode: Int, resultCode: Int, data: Intent?) {
    }
}