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

import com.pandora.vanity.network.GETRequest
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito

import java.io.ByteArrayInputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.nio.charset.StandardCharsets

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull

class RequestTest {

    @Test
    fun test_request_null_input() {
        try {
            val request = GETRequest(null)
            request.send()
            Assert.fail("Should have thrown IOException exception")
        } catch (ex: IOException) {
        }
    }

    @Test
    @Throws(IOException::class)
    fun test_request_success() {
        val json = "{}"

        val inputStream = ByteArrayInputStream(json.toByteArray(StandardCharsets.UTF_8))

        val httpURLConnectionMock = Mockito.mock(HttpURLConnection::class.java)
        Mockito.`when`(httpURLConnectionMock.inputStream).thenReturn(inputStream)

        val request = GETRequest("http://someurl")
        request.setHttpURLConnection(httpURLConnectionMock)
        val response = request.send()

        assertNotNull(response)
        assertNotNull(response.data)
        assertEquals(json, response.data.toString())
    }
}
