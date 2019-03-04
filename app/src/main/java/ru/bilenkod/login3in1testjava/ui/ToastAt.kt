package ru.bilenkod.login3in1testjava.ui

import android.content.Context
import android.widget.Toast

class ToastAt(private val ctx: Context = App.instance) {

    fun githubUserLoadingError(errorMessage: String) {
        showToastAt("Error while loading Github users: $errorMessage")
    }

    fun googleApiFail(errorMessage: String) {
        showToastAt("Google Api Error: $errorMessage")
    }

    fun facebookAuthError(errorMessage: String) {
        showToastAt("Facebook auth error: $errorMessage")
    }

    fun facebookAuthCancel() {
        showToastAt("Facebook auth cancelled", Toast.LENGTH_SHORT)
    }

    fun vkError(errorMessage: String?) {
        if (errorMessageExists(errorMessage))
            showToastAt("VK error: $errorMessage")
    }

    private fun errorMessageExists(errorMessage: String?) = !errorMessage.isNullOrBlank()

    private fun showToastAt(fullToastMessage: String, length: Int = Toast.LENGTH_LONG) {
        Toast.makeText(ctx, fullToastMessage, length).show()
    }

}