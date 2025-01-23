package com.example.newsapp.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

fun shareViaGmail(context: Context, subject: String, body: String) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "message/rfc822"
    intent.putExtra(Intent.EXTRA_EMAIL, arrayOf<String>()) // Optional: Specify recipient email addresses
    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
    intent.putExtra(Intent.EXTRA_TEXT, body)
    try {
        context.startActivity(Intent.createChooser(intent, "Send email using..."))
    } catch (e: android.content.ActivityNotFoundException) {
        Toast.makeText(context, "No email clients installed.", Toast.LENGTH_SHORT).show()
    }
}

fun shareViaSMS(context: Context, body: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:"))
    intent.putExtra("sms_body", body)
    context.startActivity(intent)
}

fun shareViaSocialMedia(context: Context, subject: String, body: String) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
    intent.putExtra(Intent.EXTRA_TEXT, body)
    context.startActivity(Intent.createChooser(intent, "Share via"))
}
