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

package com.pandora.vanity.util

import com.pandora.vanity.VanityConstants
import org.json.JSONException
import org.json.JSONObject

import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.HashMap

class VanityUtils {

    companion object {
        @Throws(UnsupportedEncodingException::class)
        fun constructAuthorizationUrl(clientId: String?, redirectUri: String?): String? {
            if (clientId == null || redirectUri == null) {
                return null
            }

            val url = StringBuffer()

            url.append(VanityConstants.INSTAGRAM_HOST)
            url.append(VanityConstants.ENDPOINT_OAUTH_AUTHORIZE)
            url.append("?").append(VanityConstants.PARAM_CLIENT_ID).append("=").append(clientId)
            url.append("&").append(VanityConstants.PARAM_REDIRECT_URI).append("=")
                .append(URLEncoder.encode(redirectUri, "UTF-8"))
            url.append("&").append(VanityConstants.PARAM_RESPONSE_TYPE).append("=token")

            return url.toString()
        }

        fun constructUrl(endpoint: String?, accessToken: String?): String? {
            if (endpoint == null || accessToken == null) {
                return null
            }

            val url = StringBuffer()

            url.append(VanityConstants.INSTAGRAM_HOST)
            url.append(VanityConstants.INSTAGRAM_API_VERSION)
            url.append(endpoint)
            url.append("?").append(VanityConstants.PARAM_ACCESS_TOKEN).append("=").append(accessToken)

            return url.toString()
        }

        fun constructAltUrl(endpoint: String?): String? {
            if (endpoint == null) {
                return null
            }

            val url = StringBuffer()

            url.append(VanityConstants.ALT_INSTAGRAM_HOST)
            url.append(endpoint)

            return url.toString()
        }

        private fun parseProfilePictureUrl(response: String?): String? {
            if (response == null) {
                return null
            }

            try {
                val json = JSONObject(response)

                return if (!json.has("data")) {
                    null
                } else json.getJSONObject("data").getString("profile_picture")

            } catch (ex: JSONException) {
                ex.printStackTrace()
            }

            return null
        }

        fun getProfilePictureUrlForQuality(url: String, pictureQuality: String): String {
            return url.replace(VanityConstants.DEFAULT_QUALITY, pictureQuality)
        }

        fun getQueryMap(query: String?): Map<String, String>? {
            if (query == null) {
                return null
            }

            val params = query.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            val map = HashMap<String, String>()

            for (param in params) {
                val name = param.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
                val value = param.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                map[name] = value
            }

            return map
        }
    }
}
