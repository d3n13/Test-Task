package ru.bilenkod.login3in1testjava.login.apis

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import ru.bilenkod.login3in1testjava.login.AuthState

class ApiHolder : LoginApi, Listener {

    companion object {
        const val VK_API = 0
        const val FACEBOOK_API = 1
        const val GOOGLE_API = 2
    }

    override var authState: AuthState = AuthState()

    override fun valueChanged(newValue: Any) {
        val newAuthInfo = newValue as AuthState

        isLoggedIn.postValue(newAuthInfo.isLoggedIn)
        userAvatarUrl.postValue(newAuthInfo.userAvatarUrl)
        userName.postValue(newAuthInfo.userName)
    }

    private var currentApi: LoginApi = EmptyLoginApi()
    val isLoggedIn = MutableLiveData<Boolean>()
    val userAvatarUrl = MutableLiveData<String?>()
    val userName = MutableLiveData<String?>()

    private val apiMap = mapOf(
            VK_API to VkLoginApi(this),
            FACEBOOK_API to FbLoginApi(this),
            GOOGLE_API to GoogleLoginApi(this)
    )

    init {
        initialLoginCheck()
    }

    override fun logout() {
        currentApi.logout()
    }

    override fun initLoginFrom(activity: Activity) {
        currentApi.initLoginFrom(activity)
    }

    override fun signInResult(requestCode: Int, resultCode: Int, data: Intent?) {
        currentApi.signInResult(requestCode, resultCode, data)
    }

    override fun checkLogin() {
        currentApi.checkLogin()
    }

    private fun initialLoginCheck() {
        for (thisApi in apiMap.values) {
            thisApi.checkLogin()
            val apiAuthInfo = thisApi.authState
            if (apiAuthInfo.isLoggedIn) {
                currentApi = thisApi
                updateAuthState()
                break
            }
        }
    }

    fun swapCurrentApi(apiKey: Int) {
        if (apiMap[apiKey] != null)
            currentApi = apiMap[apiKey]!!
        else
            throw IllegalArgumentException("apiKey $apiKey " +
                    "Is invalid: No such Api declared in " +
                    "${this.javaClass.simpleName} class")
    }
}

interface Listener {
    fun valueChanged(newValue: Any)
}