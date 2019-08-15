/*
 * Vanity: An Android library to download Instagram profile pictures.
 *
 * Copyright 2019 Pandora Media, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pandora.vanity.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.pandora.vanity.VanityConstants
import com.pandora.vanity.R
import com.pandora.vanity.lifecycle.VanityViewModel
import com.pandora.vanity.util.VanityUtils
import com.pandora.vanity.model.VanityData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

import java.io.UnsupportedEncodingException
import java.net.MalformedURLException
import java.net.URL

@SuppressLint("SetJavaScriptEnabled")
open class VanityActivity : AppCompatActivity() {

    companion object {
        private val TAG = VanityActivity::class.java.name
    }

    private lateinit var viewModel: VanityViewModel
    private var bin = CompositeDisposable()

    private lateinit var mProgressBar: ProgressBar
    private lateinit var mWebView: WebView

    private var pictureQuality: String = VanityConstants.DEFAULT_QUALITY

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.auth_activity)

        setResult(Activity.RESULT_CANCELED)

        mProgressBar = findViewById(R.id.progressBar)
        mWebView = findViewById(R.id.webview)

        viewModel = ViewModelProviders.of(this)[VanityViewModel::class.java]

        bindStreams()

        if (viewModel.isDownloading()) {
            mWebView.visibility = View.GONE
            mProgressBar.visibility = View.VISIBLE
        } else {
            val clientId = intent.getStringExtra(VanityConstants.INTENT_EXTRA_CLIENT_ID)
            val redirectUri = intent.getStringExtra(VanityConstants.INTENT_EXTRA_REDIRECT_URI)
            if (intent.hasExtra(VanityConstants.INTENT_EXTRA_PICTURE_QUALITY)) {
                val quality = intent.getStringExtra(VanityConstants.INTENT_EXTRA_PICTURE_QUALITY)
                if (quality != null) {
                    pictureQuality = quality
                }
            }

            if (!hasValidIntentExtras(clientId, redirectUri)) {
                processError("Missing required intent extras, exiting...")
                return
            }

            mWebView.let { webView ->
                val settings = webView.settings
                settings.javaScriptEnabled = true
                webView.webViewClient = buildWebViewClient(redirectUri)
            }

            try {
                mWebView.loadUrl(VanityUtils.constructAuthorizationUrl(clientId, redirectUri))
            } catch (ex: UnsupportedEncodingException) {
                processError(ex.message)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bin.clear()
    }

    private fun buildWebViewClient(redirectUri: String): WebViewClient {
        return object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                if (url.startsWith(redirectUri)) {
                    processRedirect(url)
                }
                return false
            }
        }
    }

    private fun startDownload(url: String, pictureQuality: String) {
        viewModel.startDownload(url, pictureQuality)
    }

    private fun bindStreams() {
        bin.add(viewModel.getDownloadStatus()
            .subscribeOn(Schedulers.io())
            .doOnError { updateFromDownload(null) }
            .subscribe(
                { updateFromDownload(it) },
                { Log.d(TAG, "Error while attempting to download profile info: ${it.message}") })
        )
    }

    @VisibleForTesting
    fun updateFromDownload(result: VanityData?) {
        // Update your UI here based on result of download.

        if (result == null) {
            finish()
        }

        val intent = Intent()
        intent.putExtra(VanityConstants.RESULT_EXTRA_VANITY_DATA, result)

        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun processRedirect(redirectUrl: String) {
        try {
            val url = URL(redirectUrl)

            val parameters = VanityUtils.getQueryMap(url.query)

            if (parameters != null && parameters.containsKey(VanityConstants.PARAM_ERROR)) {
                processError(parameters[VanityConstants.PARAM_ERROR_DESCRIPTION])
            } else {
                val accessToken = url.ref.replace(VanityConstants.PARAM_ACCESS_TOKEN + "=", "")
                Log.d(TAG, "access_token = $accessToken")

                mWebView.visibility = View.GONE
                mProgressBar.visibility = View.VISIBLE

                val apiUrl = VanityUtils.constructUrl(VanityConstants.ENDPOINT_USERS_SELF, accessToken)

                if (apiUrl != null) {
                    startDownload(apiUrl, pictureQuality)
                } else {
                    throw MalformedURLException()
                }
            }
        } catch (ex: MalformedURLException) {
            processError(ex.message)
        }

    }

    private fun hasValidIntentExtras(clientId: String?, redirectUri: String?): Boolean {
        if (clientId == null || redirectUri == null) {
            return false
        }

        return true
    }

    private fun processError(errorMessage: String?) {
        errorMessage?.let {
            Log.e(TAG, errorMessage)
        }
        finish()
    }
}
