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

package com.pandora.vanity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.contrib.ActivityResultMatchers
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.pandora.vanity.activity.VanityActivity
import com.pandora.vanity.model.VanityData
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThat

@RunWith(AndroidJUnit4::class)
class VanityActivityTest {

    @get:Rule
    var activity = ActivityTestRule(
        VanityActivity::class.java,
        true,
        false
    )

    @Test
    fun test_VanityActivity_invalid_intent_extras() {
        activity.launchActivity(Intent())
        assertThat<Instrumentation.ActivityResult>(
            activity.activityResult,
            ActivityResultMatchers.hasResultCode(Activity.RESULT_CANCELED)
        )
    }

    @Test
    fun test_VanityActivity_on_picture_obtained() {
        val intent = Intent()
        intent.putExtra(VanityConstants.INTENT_EXTRA_CLIENT_ID, "1234")
        intent.putExtra(VanityConstants.INTENT_EXTRA_REDIRECT_URI, "https://github.com/wseemann/Vanity")

        activity.launchActivity(intent)

        val vanityData = VanityData()
        vanityData.pictureData = ByteArray(2)
        vanityData.profilePictureUrl = "http://someurl"

        activity.activity.runOnUiThread { activity.activity.updateFromDownload(vanityData) }
        assertThat<Instrumentation.ActivityResult>(activity.activityResult, ActivityResultMatchers.hasResultCode(Activity.RESULT_OK))

        val resultIntent = activity.activityResult.resultData
        val returnedVanityData = resultIntent.getParcelableExtra<VanityData>(VanityConstants.RESULT_EXTRA_VANITY_DATA)

        assertNotNull(resultIntent)
        assertNotNull(returnedVanityData)
        assertEquals("http://someurl", vanityData.profilePictureUrl)
        assertEquals(2, vanityData.pictureData?.size)
    }
}
