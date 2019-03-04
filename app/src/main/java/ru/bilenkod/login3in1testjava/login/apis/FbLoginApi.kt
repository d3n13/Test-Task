package ru.bilenkod.login3in1testjava.login.apis

import android.app.Activity
import android.content.Intent
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import org.json.JSONException
import ru.bilenkod.login3in1testjava.login.AuthState
import ru.bilenkod.login3in1testjava.ui.ToastAt
import kotlin.properties.Delegates

class FbLoginApi(listener: Listener) : LoginApi {

    override var authState: AuthState by Delegates.observable(AuthState()) { _, _, newValue ->
        listener.valueChanged(newValue)
    }

    private var callbackManager: CallbackManager = CallbackManager.Factory.create()

    init {
        facebookRegisterCallback()
    }

    override fun logout() {
        LoginManager.getInstance().logOut()
        checkLogin()
    }

    override fun checkLogin() {
        val accessToken = AccessToken.getCurrentAccessToken()
        authState.isLoggedIn = if (accessToken != null && !accessToken.isExpired) {
            executeRequest(accessToken)
            true
        } else {
            false
        }
        updateAuthState()
    }

    override fun initLoginFrom(activity: Activity) {
    }

    override fun signInResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    fun executeRequest(accessToken: AccessToken) {
        GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + accessToken.userId + "/",
                null,
                HttpMethod.GET,
                GraphRequest.Callback { response ->
                    authState.isLoggedIn = true
                    try {
                        authState.userName = response.jsonObject.getString("name")
                        authState.userAvatarUrl = ("https://graph.facebook.com/"
                                + accessToken.userId + "/picture?type=large")
                        updateAuthState()
                    } catch (e: JSONException) {

                    }
                }
        ).executeAsync()
    }

    private fun facebookRegisterCallback() {
        LoginManager.getInstance()
                .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        val accessToken = AccessToken.getCurrentAccessToken()
                        if (accessToken != null && !accessToken.isExpired) {
                            executeRequest(accessToken)
                        }
                    }

                    override fun onCancel() {
                        ToastAt().facebookAuthCancel()
                    }

                    override fun onError(error: FacebookException) {
                        ToastAt().facebookAuthError(error.localizedMessage)
                    }
                })
    }
}