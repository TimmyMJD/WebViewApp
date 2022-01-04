package com.app.webviewapp

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.webkit.WebSettings.LOAD_CACHE_ELSE_NETWORK
import android.webkit.WebView
import android.webkit.WebViewClient


class WebViewActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        webView = findViewById(R.id.wb_webView)
        webViewSetup()
        val lastPageURL = getLastPageURL()
        if (lastPageURL.isNotEmpty()) {
            webView.loadUrl(lastPageURL)
        } else {
            webView.loadUrl("https://www.google.com.ua/")
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun webViewSetup() {
        webView.apply {
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    val lastPageURL = url ?: ""
                    saveLastPageURL(lastPageURL)
                }
            }
            settings.run {
                javaScriptEnabled = true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    safeBrowsingEnabled = true

                }
                cacheMode = LOAD_CACHE_ELSE_NETWORK
            }
        }
    }

    private fun saveLastPageURL(lastPageURL: String) {
        val pref = getPreferences(Context.MODE_PRIVATE)
        val editor = pref.edit()

        editor.putString(LAST_PAGE_KEY, lastPageURL)
        editor.commit()
    }

    private fun getLastPageURL(): String {
        return getPreferences(Context.MODE_PRIVATE)
            .getString(LAST_PAGE_KEY, "") ?: ""
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) webView.goBack() else super.onBackPressed()
    }

    companion object {
        const val LAST_PAGE_KEY = "LAST_PAGE_KEY"
    }


}