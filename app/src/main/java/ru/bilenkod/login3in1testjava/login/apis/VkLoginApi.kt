package ru.bilenkod.login3in1testjava.login.apis

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import com.vk.sdk.VKSdk
import com.vk.sdk.api.*
import org.json.JSONException
import ru.bilenkod.login3in1testjava.login.AuthState
import ru.bilenkod.login3in1testjava.ui.App
import ru.bilenkod.login3in1testjava.ui.ToastAt
import kotlin.properties.Delegates

class VkLoginApi(listener: Listener, appCtx: Context = App.instance) : LoginApi {

    override var authState: AuthState by Delegates.observable(AuthState()) { _, _, newValue ->
        listener.valueChanged(newValue)
    }

    val vkUserGetRequest = VKRequest("users.get",
            VKParameters.from(VKApiConst.FIELDS, "photo_400_orig"))

    val vkRequestListener = object : VKRequest.VKRequestListener() {
        override fun onComplete(response: VKResponse?) {
            if (response != null)
                vkResponseAction(response)
        }

        override fun onError(error: VKError) {
            ToastAt().vkError(error.errorMessage)
        }

        override fun attemptFailed(request: VKRequest?, attemptNumber: Int, totalAttempts: Int) {}
    }

    private val vkCallback = object : VKCallback<VKAccessToken> {
        override fun onResult(res: VKAccessToken) {
            vkUserGetRequest.executeWithListener(vkRequestListener)
        }

        override fun onError(error: VKError) {
            ToastAt().vkError(error.errorMessage)
        }
    }

    init {
        VKSdk.initialize(appCtx)
    }

    override fun logout() {
        VKSdk.logout()
        checkLogin()
    }

    override fun checkLogin() {
        if (VKSdk.isLoggedIn()) {
            updateUserInfo()
            authState.isLoggedIn = true
        } else {
            authState.isLoggedIn = false
        }

        updateAuthState()
    }

    override fun initLoginFrom(activity: Activity) {
        VKSdk.login(activity)
    }

    override fun signInResult(requestCode: Int, resultCode: Int, data: Intent?) {
        VKSdk.onActivityResult(requestCode, resultCode, data, vkCallback)
    }

    private fun updateUserInfo() {
        vkUserGetRequest.executeWithListener(vkRequestListener)
    }

    fun vkResponseAction(response: VKResponse) {
        try {
            val responseObject = extractVkResponseObject(response)

            authState.userAvatarUrl =
                    responseObject
                            .getString("photo_400_orig")

            authState.userName =
                    (responseObject
                            .getString("first_name")
                            + " " +
                            responseObject
                                    .getString("last_name"))

            authState.isLoggedIn = true

            updateAuthState()

        } catch (e: JSONException) {
        }
    }

    private fun extractVkResponseObject(response: VKResponse) =
            response.json
                    .getJSONArray("response")
                    .getJSONObject(0)
}