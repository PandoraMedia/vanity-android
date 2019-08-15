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

import android.os.Parcel
import android.os.Parcelable

class VanityData() : Parcelable {

    var profilePictureUrl: String? = null
    var pictureData: ByteArray? = null

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        out.writeString(profilePictureUrl)
        out.writeInt(pictureData!!.size)
        out.writeByteArray(pictureData)
    }

    private constructor(parcel: Parcel) : this() {
        profilePictureUrl = parcel.readString()
        pictureData = ByteArray(parcel.readInt())
        parcel.readByteArray(pictureData!!)
    }

    companion object CREATOR : Parcelable.Creator<VanityData> {
        override fun createFromParcel(parcel: Parcel): VanityData {
            return VanityData(parcel)
        }

        override fun newArray(size: Int): Array<VanityData?> {
            return arrayOfNulls(size)
        }
    }
}
