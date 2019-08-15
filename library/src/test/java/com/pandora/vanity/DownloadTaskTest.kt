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

import com.pandora.vanity.network.*
import org.junit.After
import org.junit.Test
import org.mockito.Mockito

import java.io.ByteArrayOutputStream
import java.io.IOException

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.mockito.Mockito.validateMockitoUsage
import org.junit.rules.ExpectedException
import org.junit.Rule

class DownloadTaskTest {

    @get:Rule
    var thrown = ExpectedException.none()

    @Test
    @Throws(IOException::class)
    fun test_downloadTask_failure() {
        val userRequestMock = Mockito.mock(GETRequest::class.java)
        Mockito.`when`(userRequestMock.send()).thenThrow(IOException::class.java)

        val downloadTask = DownloadTask("http://someurl", "")
        downloadTask.setUserRequest(userRequestMock)
        thrown.expect(IOException::class.java)
        val result = downloadTask.call()

        assertNotNull(result)
        assertNull(result)
    }

    @Test
    @Throws(IOException::class)
    fun test_downloadTask_default_quality_success() {
        val json = "{\"data\" : {\"id\" : \"1234\"," +
                "\"username\" : \"test\"," +
                "\"profile_picture\" : \"http://test/picture.jpg\"}}"

        val userResponse = Response()
        userResponse.data = json

        val userRequestMock = Mockito.mock(GETRequest::class.java)
        Mockito.`when`(userRequestMock.send()).thenReturn(userResponse)

        val pictureResponse = Response()
        pictureResponse.data = ByteArrayOutputStream()

        val pictureRequestMock = Mockito.mock(GETRequest::class.java)
        Mockito.`when`(pictureRequestMock.send()).thenReturn(pictureResponse)

        val userInfoRequest = Mockito.mock(GETRequest::class.java)

        val downloadTask = DownloadTask("http://someurl", VanityConstants.DEFAULT_QUALITY)
        downloadTask.setUserRequest(userRequestMock)
        downloadTask.setUserInfoRequest(userInfoRequest)
        downloadTask.setPictureRequest(pictureRequestMock)
        val result = downloadTask.call()

        assertNotNull(result)
        assertEquals("http://test/picture.jpg", result?.profilePictureUrl)
        assertEquals(0, result?.pictureData?.size)

        Mockito.verify<Request>(userInfoRequest, Mockito.never()).send()
    }

    @Test
    @Throws(IOException::class)
    fun test_downloadTask_high_quality_success() {
        val userJson = "{\"data\" : {\"id\" : \"1234\"," +
                "\"username\" : \"test\"," +
                "\"profile_picture\" : \"http://test/picture.jpg\"}}"

        val userInfoJson = "{\"user\": {\"hd_profile_pic_url_info\": {\"url\": \"http://test/picture1.jpg\"}}}"

        val userResponse = Response()
        userResponse.data = userJson

        val userRequestMock = Mockito.mock(GETRequest::class.java)
        Mockito.`when`(userRequestMock.send()).thenReturn(userResponse)

        val pictureResponse = Response()
        pictureResponse.data = ByteArrayOutputStream()

        val pictureRequestMock = Mockito.mock(GETRequest::class.java)
        Mockito.`when`(pictureRequestMock.send()).thenReturn(pictureResponse)

        val userInfoResponse = Response()
        userInfoResponse.data = userInfoJson

        val userInfoRequest = Mockito.mock(GETRequest::class.java)
        Mockito.`when`(userInfoRequest.send()).thenReturn(userInfoResponse)

        val downloadTask = DownloadTask("http://someurl", VanityConstants.HIGH_QUALITY)
        downloadTask.setUserRequest(userRequestMock)
        downloadTask.setUserInfoRequest(userInfoRequest)
        downloadTask.setPictureRequest(pictureRequestMock)
        val result = downloadTask.call()

        assertNotNull(result)
        assertEquals("http://test/picture1.jpg", result?.profilePictureUrl)
        assertEquals(0, result?.pictureData?.size)

        Mockito.verify<Request>(userInfoRequest).send()
    }

    @After
    fun validate() {
        validateMockitoUsage()
    }
}
