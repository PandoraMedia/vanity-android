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

import androidx.annotation.VisibleForTesting
import com.pandora.vanity.VanityConstants
import com.pandora.vanity.model.InstagramUser
import com.pandora.vanity.model.InstagramUserInfo
import com.pandora.vanity.model.VanityData
import com.pandora.vanity.util.VanityUtils

import java.io.ByteArrayOutputStream
import java.util.concurrent.Callable

/**
 * Implementation of AsyncTask designed to fetch data from the network.
 */
class DownloadTask: Callable<VanityData> {

    private var url: String
    private var quality: String
    private var isCancelled = false

    private var userRequest: Request? = null
    private var userInfoRequest: Request? = null
    private var pictureRequest: Request? = null

    constructor(url: String, quality: String) {
        this.url = url
        this.quality = quality
    }

    /**
     * Defines work to perform on the background thread.
     */
    override fun call(): VanityData? {
        var vanityData: VanityData? = null
        val profilePictureUrl: String?

        if (!isCancelled && url.isNotEmpty()) {

            if (userRequest == null) {
                userRequest = GETRequest(url)
            }

            val instagramUser = InstagramUser(userRequest!!.send().data!!.toString())

            if (quality == VanityConstants.DEFAULT_QUALITY || quality == VanityConstants.LOW_QUALITY) {
                profilePictureUrl = instagramUser.profilePicture
            } else {
                if (userInfoRequest == null) {
                    userInfoRequest = GETRequest(
                        VanityUtils.constructAltUrl(
                            VanityConstants.ENDPOINT_USERS_INFO.replace(
                                "<user id>",
                                instagramUser.id!!
                            )
                        )
                    )
                }
                val instagramUserInfo = InstagramUserInfo(userInfoRequest!!.send().data!!.toString())
                profilePictureUrl = instagramUserInfo.profilePicture
            }

            if (pictureRequest == null) {
                pictureRequest = GETRequest(profilePictureUrl)
            }

            vanityData = VanityData()
            vanityData.profilePictureUrl = profilePictureUrl
            vanityData.pictureData = (pictureRequest!!.send().data as ByteArrayOutputStream).toByteArray()
        }

        return vanityData
    }

    fun cancel() {
        isCancelled = true
    }

    // Methods for testing

    @VisibleForTesting
    fun setUserRequest(userRequest: Request) {
        this.userRequest = userRequest
    }

    @VisibleForTesting
    fun setUserInfoRequest(userInfoRequest: Request) {
        this.userInfoRequest = userInfoRequest
    }

    @VisibleForTesting
    fun setPictureRequest(pictureRequest: Request) {
        this.pictureRequest = pictureRequest
    }
}