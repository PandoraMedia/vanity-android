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

import com.pandora.vanity.util.VanityUtils
import org.junit.Test

import java.io.UnsupportedEncodingException
import java.net.MalformedURLException
import java.net.URL
import java.net.URLEncoder

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull

class VanityUtilsTest {
    @Test
    fun test_getQueryMap_null_input() {
        assertNull(VanityUtils.getQueryMap(null))
    }

    @Test
    fun test_getQueryMap() {
        val parameters = VanityUtils.getQueryMap("one=1&two=2&three=3")

        assertEquals(3, parameters!!.size.toLong())
        assertEquals(parameters["one"], "1")
        assertEquals(parameters["two"], "2")
        assertEquals(parameters["three"], "3")
    }

    @Test
    @Throws(UnsupportedEncodingException::class)
    fun test_constructAuthorizationUrl_null_inputs() {
        assertNull(VanityUtils.constructAuthorizationUrl(null, ""))
        assertNull(VanityUtils.constructAuthorizationUrl("", null))
    }

    @Test
    @Throws(MalformedURLException::class, UnsupportedEncodingException::class)
    fun test_constructAuthorizationUrl() {
        val clientId = "1234"
        val redirectUri = "http://someurl"

        val authorizationUrl = VanityUtils.constructAuthorizationUrl(clientId, redirectUri)
        val url = URL(authorizationUrl)

        assertEquals(VanityConstants.INSTAGRAM_HOST, url.protocol + "://" + url.host)

        val parameters = VanityUtils.getQueryMap(url.query)
        assertEquals(clientId, parameters!![VanityConstants.PARAM_CLIENT_ID])
        assertEquals(URLEncoder.encode(redirectUri, "UTF-8"), parameters[VanityConstants.PARAM_REDIRECT_URI])
    }

    @Test
    fun test_constructUrl_invalid_input() {
        assertNull(VanityUtils.constructUrl(null, ""))
        assertNull(VanityUtils.constructUrl("", null))
    }

    @Test
    fun test_constructUrl() {
        val accessToken = "1234"

        val url = VanityConstants.INSTAGRAM_HOST +
                VanityConstants.INSTAGRAM_API_VERSION +
                VanityConstants.ENDPOINT_USERS_SELF + "?" +
                VanityConstants.PARAM_ACCESS_TOKEN + "=" + accessToken

        assertEquals(url, VanityUtils.constructUrl(VanityConstants.ENDPOINT_USERS_SELF, accessToken))
    }
}
