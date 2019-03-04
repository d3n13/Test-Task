package ru.bilenkod.login3in1testjava.login.apis

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import ru.bilenkod.login3in1testjava.login.AuthState
import ru.bilenkod.login3in1testjava.ui.App
import ru.bilenkod.login3in1testjava.ui.ToastAt
import kotlin.properties.Delegates

class GoogleLoginApi(listener: Listener, private val appCtx: Context = App.instance) : LoginApi {

    override var authState: AuthState by Delegates.observable(AuthState()) { _, _, newValue ->
        listener.valueChanged(newValue)
    }

    private val lastSignedAccount: GoogleSignInAccount?
        get() = GoogleSignIn.getLastSignedInAccount(appCtx)
    private var mGoogleSignInClient: GoogleSignInClient
    private var mGoogleApiClient: GoogleApiClient

    init {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile()
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(appCtx, gso)
        mGoogleApiClient = GoogleApiClient.Builder(appCtx)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
        mGoogleApiClient.connect()
    }

    override fun logout() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient)
        checkLogin()
    }

    override fun checkLogin() {
        handleGoogleAccount(lastSignedAccount)
    }

    override fun initLoginFrom(activity: Activity) {
        val signInIntent = mGoogleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, 9001)
    }

    override fun signInResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 9001) {
            val completedTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                handleGoogleAccount(completedTask.getResult(ApiException::class.java))
            } catch (e: ApiException) {
                ToastAt().googleApiFail(e.localizedMessage)
            }
        }
    }

    private fun handleGoogleAccount(account: GoogleSignInAccount?) {
        if (account == null) {
            authState.isLoggedIn = false
        } else {
            authState.isLoggedIn = true
            updateUserInfo()
        }
        updateAuthState()
    }

    private fun updateUserInfo() {
        var currentAvatarUrl: String? = null
        var currentUserName: String? = null

        if (lastSignedAccount?.photoUrl != null)
            currentAvatarUrl = lastSignedAccount?.photoUrl.toString()

        if (lastSignedAccount?.displayName != null) {
            currentUserName = lastSignedAccount?.displayName
        }

        authState.userAvatarUrl = currentAvatarUrl
        authState.userName = currentUserName
        updateAuthState()
    }
}
