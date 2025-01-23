package com.example.newsapp

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import android.util.Log
import com.example.newsapp.FacebookAuthManager


class FacebookAuthManager {

    companion object {
        fun loginWithFacebook(
            context: Context,
            callbackManager: CallbackManager,
            onSuccess: () -> Unit,
            onError: (String) -> Unit
        ) {
            val loginManager = LoginManager.getInstance()
            loginManager.logInWithReadPermissions(context as Activity, listOf("email", "public_profile"))

            loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    handleFacebookAccessToken(result.accessToken, onSuccess, onError)
                }

                override fun onCancel() {
                    onError("Facebook login canceled.")
                }

                override fun onError(error: FacebookException) {
                    onError("Facebook login error: ${error.message}")
                }
            })
        }

        private fun handleFacebookAccessToken(
            token: AccessToken,
            onSuccess: () -> Unit,
            onError: (String) -> Unit
        ) {
            val credential = FacebookAuthProvider.getCredential(token.token)
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("FacebookAuthManager", "Login successful.")
                        onSuccess()
                    } else {
                        Log.e("FacebookAuthManager", "Login failed: ${task.exception?.message}")
                        onError(task.exception?.message ?: "Facebook login failed.")
                    }
                }
        }
    }
}
