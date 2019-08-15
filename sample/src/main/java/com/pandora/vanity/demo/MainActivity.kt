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

package com.pandora.vanity.demo

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.webkit.CookieManager
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.pandora.vanity.VanityConstants
import com.pandora.vanity.model.VanityData
import android.webkit.CookieSyncManager

class MainActivity : AppCompatActivity() {

    companion object {
        private const val INSTAGRAM_PICTURE_REQUEST_CODE = 100
        private const val VANITY_DATA_INTENT_EXTRA = "vanity_data"
    }

    private lateinit var pictureImageView: ImageView
    private var vanityData: VanityData? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Clear any credentials so we don't auto login when attempting to retrieve
        // a profile picture
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().removeAllCookies(null)
            CookieManager.getInstance().flush()
        } else {
            val cookieSyncMngr = CookieSyncManager.createInstance(this)
            cookieSyncMngr.startSync()
            val cookieManager = CookieManager.getInstance()
            cookieManager.removeAllCookie()
            cookieManager.removeSessionCookie()
            cookieSyncMngr.stopSync()
            cookieSyncMngr.sync()
        }

        pictureImageView = findViewById(R.id.pictureImageView)
        pictureImageView.setBackgroundResource(R.drawable.ic_launcher_background)
        pictureImageView.setImageResource(R.drawable.ic_launcher_foreground)

        val editButton = findViewById<Button>(R.id.editButton)
        editButton.setOnClickListener {
            val intent = Intent(this, VanityActivity::class.java)
            intent.putExtra(VanityConstants.INTENT_EXTRA_CLIENT_ID, "<Your Instagram client id>")
            intent.putExtra(VanityConstants.INTENT_EXTRA_REDIRECT_URI, "<Your redirect uri>>")
            intent.putExtra(VanityConstants.INTENT_EXTRA_PICTURE_QUALITY, VanityConstants.HIGH_QUALITY)

            startActivityForResult(intent, INSTAGRAM_PICTURE_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == INSTAGRAM_PICTURE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val vanityData = data!!.getParcelableExtra<VanityData>(VanityConstants.RESULT_EXTRA_VANITY_DATA)
                vanityData?.let {
                    val bitmap = BitmapFactory.decodeByteArray(it.pictureData, 0, it.pictureData!!.size)
                    pictureImageView.setImageBitmap(bitmap)
                    this.vanityData = it
                }
            }
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        vanityData = savedInstanceState.getParcelable(VANITY_DATA_INTENT_EXTRA)
        vanityData?.let {
            val bitmap = BitmapFactory.decodeByteArray(it.pictureData, 0, it.pictureData!!.size)
            pictureImageView.setImageBitmap(bitmap)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(VANITY_DATA_INTENT_EXTRA, vanityData)
        super.onSaveInstanceState(outState)
    }
}
