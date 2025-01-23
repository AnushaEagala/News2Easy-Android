package com.example.newsapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider

object AuthManager {

    fun signInWithGoogle(
        context: Context,
        googleSignInLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>
    ) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(context, gso)
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    fun handleGoogleSignInResult(
        data: Intent,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener { authTask ->
                    if (authTask.isSuccessful) {
                        // Get the current signed-in user
                        val user = FirebaseAuth.getInstance().currentUser
                        user?.let {
                            // Log the user details
                            val email = it.email
                            val displayName = it.displayName
                            val uid = it.uid
                            Log.d(
                                "Google Sign-In",
                                "Email: $email, Display Name: $displayName, UID: $uid"
                            )
                        }
                        onSuccess()
                    } else {
                        onError(authTask.exception?.message ?: "Google Sign-In failed")
                    }
                }
        } catch (e: ApiException) {
            onError("Google Sign-In error: ${e.message}")
        }
    }

    fun loginWithFacebook(
        context: Context,
        callbackManager: CallbackManager,
        onSuccess: () -> Unit,
        onError: Any
    ) {

    }

    object AuthManager {

        fun loginWithFacebook(
            context: Context,
            callbackManager: CallbackManager,
            onSuccess: () -> Unit,
            onError: (String) -> Unit
        ) {
            val loginManager = LoginManager.getInstance()
            loginManager.logInWithReadPermissions(
                context as Activity,
                listOf("email", "public_profile")
            )

            loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    handleFacebookAccessToken(result.accessToken, onSuccess, onError)
                }

                private fun handleFacebookAccessToken(
                    accessToken: AccessToken,
                    onSuccess: () -> Unit,
                    onError: (String) -> Unit
                ) {

                }

                override fun onCancel() {
                    onError("Facebook login canceled.")
                }

                override fun onError(error: FacebookException) {
                    onError("Facebook login error: ${error.message}")
                }
            })
        }
    }
}
