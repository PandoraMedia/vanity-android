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

interface VanityConstants {
    companion object {

        // API host
        const val INSTAGRAM_HOST = "https://api.instagram.com"
        const val ALT_INSTAGRAM_HOST = "https://i.instagram.com"
        const val INSTAGRAM_URL = "https://www.instagram.com/"

        // API version
        const val INSTAGRAM_API_VERSION = "/v1"

        // API endpoints
        const val ENDPOINT_USERS_SELF = "/users/self/"
        const val ENDPOINT_USERS_INFO = "/api/v1/users/<user id>/info/"
        const val ENDPOINT_OAUTH_AUTHORIZE = "/oauth/authorize/"

        // API query parameters
        const val PARAM_CLIENT_ID = "client_id"
        const val PARAM_REDIRECT_URI = "redirect_uri"
        const val PARAM_RESPONSE_TYPE = "response_type"
        const val PARAM_ACCESS_TOKEN = "access_token"

        // Authentication error parameters
        const val PARAM_ERROR = "error"
        const val PARAM_ERROR_REASON = "error_reason"
        const val PARAM_ERROR_DESCRIPTION= "error_description"

        // Intent extra string constants
        const val INTENT_EXTRA_REDIRECT_URI = "redirect_uri"
        const val INTENT_EXTRA_CLIENT_ID = "client_id"
        const val INTENT_EXTRA_PICTURE_QUALITY = "picture_quality"

        // Result extra string constants
        const val RESULT_EXTRA_VANITY_DATA = "vanity_data"

        // Profile picture quality
        const val LOW_QUALITY = "low"
        const val HIGH_QUALITY = "high"
        const val DEFAULT_QUALITY = LOW_QUALITY

        // Library user agent
        const val USER_AGENT = "Vanity"
    }
}
