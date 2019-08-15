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

package com.pandora.vanity.model

import org.json.JSONException
import org.json.JSONObject

class InstagramUserInfo @Throws(JSONException::class)
constructor(json: String?) {

    companion object {
        private const val USER = "user"
        private const val HD_PROFILE_PIC_URL_INFO = "hd_profile_pic_url_info"
        private const val URL = "url"
    }

    var profilePicture: String? = null

    init {
        if (json == null) {
            throw IllegalArgumentException("Input json cannot be null")
        }

        val jsonObject = JSONObject(json)

        profilePicture = jsonObject.getJSONObject(USER).getJSONObject(HD_PROFILE_PIC_URL_INFO).getString(URL)
    }
}
