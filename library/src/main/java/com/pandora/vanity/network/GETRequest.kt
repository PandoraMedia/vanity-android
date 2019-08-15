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

package com.pandora.vanity.network

import com.pandora.vanity.VanityConstants

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

open class GETRequest(url: String?) : Request(url) {

    @Throws(IOException::class)
    override fun send(): Response {
        lateinit var inputStream: InputStream
        var os: ByteArrayOutputStream? = null
        var len: Int?

        if (url == null) {
            throw IOException("URL cannot be null")
        }

        try {
            val uri = URL(this.url)

            if (conn == null) {
                conn = uri.openConnection() as HttpURLConnection
            }

            conn?.let {
                it.setRequestProperty("User-Agent", VanityConstants.USER_AGENT)
                it.connectTimeout = 10000
                it.readTimeout = 10000
                it.requestMethod = "GET"

                inputStream = it.inputStream
                it.connect()
            }

            val byteChunk = ByteArray(4096)

            len = inputStream.read(byteChunk)
            while (len!! > 0) {
                if (os == null) {
                    os = ByteArrayOutputStream()
                }

                os.write(byteChunk, 0, len)
                len = inputStream.read(byteChunk)
            }
        } finally {
            if (conn != null) {
                conn?.disconnect()
            }
        }

        val response = Response()
        response.data = os

        return response
    }
}