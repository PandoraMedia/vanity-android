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

class InstagramUser @Throws(JSONException::class, IllegalArgumentException::class)
constructor(json: String?) {

    companion object {
        private const val DATA = "data"
        private const val ID = "id"
        private const val USERNAME = "username"
        private const val PROFILE_PICTURE = "profile_picture"
    }

    var id: String? = null
    var username: String? = null
    var profilePicture: String? = null

    init {
        if (json == null) {
            throw IllegalArgumentException("Input json cannot be null")
        }

        val jsonObject = JSONObject(json)

        id = jsonObject.getJSONObject(DATA).getString(ID)
        username = jsonObject.getJSONObject(DATA).getString(USERNAME)
        profilePicture = jsonObject.getJSONObject(DATA).getString(PROFILE_PICTURE)
    }
}
