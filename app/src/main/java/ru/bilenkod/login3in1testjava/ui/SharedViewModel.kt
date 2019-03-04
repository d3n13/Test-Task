package ru.bilenkod.login3in1testjava.ui

import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import ru.bilenkod.login3in1testjava.data.githubusersearch.GitHubSearchRepository

class SharedViewModel(appCtx: Application = App.instance) : AndroidViewModel(appCtx) {

    private val searchRepository = GitHubSearchRepository()
    private val loginProvider = App.loginProvider

    val isLoggedIn = loginProvider.isLoggedIn
    val imageUrl = loginProvider.userAvatarUrl
    val userName = loginProvider.userName

    var usersList = searchRepository.usersList

    fun provideUserList(searchString: String, delay: Boolean) {
        searchRepository.initSearchTask(searchString, delay)
    }

    fun handleOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        loginProvider.signInResult(requestCode, resultCode, data)
    }

    fun logout() {
        loginProvider.logout()
    }

    fun initLoginViaFrom(apiKey: Int, activity: Activity) {
        loginProvider.initLoginViaFrom(apiKey, activity)
    }
}