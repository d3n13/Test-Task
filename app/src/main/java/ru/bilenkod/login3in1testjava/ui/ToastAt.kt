package ru.bilenkod.login3in1testjava.ui

import android.content.Context
import android.widget.Toast
import ru.bilenkod.login3in1testjava.R

class ToastAt(private val ctx: Context = App.instance) {

    fun githubUserLoadingError(errorMessage: String) {
        showToastAt(errorMessage, prefix = getString(R.string.error_github))
    }

    fun googleApiFail(errorMessage: String) {
        showToastAt(errorMessage, prefix = getString(R.string.error_api_google))
    }

    fun facebookAuthError(errorMessage: String) {
        showToastAt(errorMessage, prefix = getString(R.string.error_facebook))
    }

    fun facebookAuthCancel() {
        showToastAt(message = getString(R.string.facebook_auth_cancelled),
                length = Toast.LENGTH_SHORT)
    }

    fun vkError(errorMessage: String?) {
        if (errorMessageExists(errorMessage))
            showToastAt(message = errorMessage!!, prefix = getString(R.string.error_vk))
    }

    private fun getString(id: Int) = ctx.getString(id)

    private fun errorMessageExists(errorMessage: String?) = !errorMessage.isNullOrBlank()

    private fun showToastAt(message: String,
                            length: Int = Toast.LENGTH_LONG,
                            prefix: String? = null) {
        val toastMessage =
                if (prefix == null)
                    message
                else
                    "$prefix $message"

        Toast.makeText(ctx, toastMessage, length).show()
    }
}