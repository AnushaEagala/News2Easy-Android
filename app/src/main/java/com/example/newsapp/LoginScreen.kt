package com.example.newsapp

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.facebook.CallbackManager
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    onNavigateToHome: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    authViewModel: AuthViewModel
) {
    val authState by authViewModel.authState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val activity = context as? Activity
    val auth = FirebaseAuth.getInstance()
    val callbackManager = remember { CallbackManager.Factory.create() }

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data = result.data
        if (result.resultCode == Activity.RESULT_OK && data != null) {
            AuthManager.handleGoogleSignInResult(data, onSuccess = {
                onNavigateToHome()
            }, onError = { error: String? ->  // Explicitly specify type
                showToast(context, error)
            })
        }
    }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.login),
            contentDescription = "Login Image",
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text("Welcome back")

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email address") },
            modifier = Modifier.fillMaxWidth(0.8f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                authViewModel.login(email, password, onSuccess = {
                    onNavigateToHome()
                }, onError = { error: String? ->  // Explicitly specify type
                    showToast(context, error)
                })
            },
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Don't have an account? Sign Up",
            modifier = Modifier.clickable(onClick = onNavigateToSignUp)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text("Or sign in with", modifier = Modifier.align(Alignment.CenterHorizontally))

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Google sign-in button
            Image(
                painter = painterResource(id = R.drawable.google),
                contentDescription = "Google",
                modifier = Modifier
                    .size(60.dp)
                    .clickable {
                        AuthManager.signInWithGoogle(context, googleSignInLauncher)
                    }
            )

            // Facebook sign-in button
            Image(
                painter = painterResource(id = R.drawable.facebook),
                contentDescription = "Facebook",
                modifier = Modifier.size(60.dp).clickable {
                    AuthManager.loginWithFacebook(
                        context = context,
                        callbackManager = callbackManager,
                        onSuccess = {
                            onNavigateToHome()
                        },
                        onError = { error: String? ->  // Explicitly specify type
                            showToast(context, error)
                        }
                    )
                }
            )
        }
    }
}

fun showToast(context: Context, message: String?) {
    if (!message.isNullOrBlank()) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
