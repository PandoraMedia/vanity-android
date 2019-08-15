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

import com.pandora.vanity.model.InstagramUserInfo
import org.json.JSONException
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.assertEquals

class InstagramUserInfoTest {

    @Test
    fun test_instagramUserInfo_null_input() {
        try {
            InstagramUserInfo(null)
            Assert.fail("Should have thrown IllegalArgumentException exception")
        } catch (ex: JSONException) {
            Assert.fail("Should have thrown IllegalArgumentException exception")
        } catch (ex: IllegalArgumentException) {
        }
    }

    @Test
    fun test_instagramUserInfo_invalid_input() {
        try {
            InstagramUserInfo("{}")
            Assert.fail("Should have thrown JSONException exception")
        } catch (ex: IllegalArgumentException) {
            Assert.fail("Should have thrown JSONException exception")
        } catch (ex: JSONException) {
        }
    }

    @Test
    @Throws(JSONException::class, IllegalArgumentException::class)
    fun test_instagramUserInfo_success() {
        val json = "{\"user\": {\"hd_profile_pic_url_info\": {\"url\": \"http://test/picture.jpg\"}}}"

        val instagramUserInfo = InstagramUserInfo(json)
        assertEquals("http://test/picture.jpg", instagramUserInfo.profilePicture)
    }
}
