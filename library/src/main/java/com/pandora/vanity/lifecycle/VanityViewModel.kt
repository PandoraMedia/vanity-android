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

package com.pandora.vanity.lifecycle

import android.util.Log
import androidx.lifecycle.ViewModel
import com.pandora.vanity.model.VanityData
import com.pandora.vanity.network.DownloadTask
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class VanityViewModel : ViewModel() {

    companion object {
        val TAG = VanityViewModel::class.java.name
    }

    private var bin = CompositeDisposable()

    private val downstatusStatusPublisher = BehaviorSubject.create<VanityData?>()

    private var mDownloadTask: DownloadTask? = null
    private var isDownloading: Boolean = false

    fun getDownloadStatus(): Observable<VanityData?> {
        return downstatusStatusPublisher.serialize()
    }

    /**
     * Start non-blocking execution of DownloadTask.
     */
    fun startDownload(urlString: String, quality: String) {
        cancelDownload()
        mDownloadTask = DownloadTask(urlString, quality)
        val task = mDownloadTask as DownloadTask

        isDownloading = true

        bin.add(Single.fromCallable {
                task.call()
            }
            //.delay(10, TimeUnit.SECONDS) // add a delay, just for testing
            .map { vanityData ->
                isDownloading = false
                vanityData
            }.subscribeOn(Schedulers.io())
            .doOnError { Single.error<VanityData>(it) }
            .subscribe(
                { downstatusStatusPublisher.onNext(it) },
                {
                    Log.e(TAG, "Error while attempting to download profile info: ${it.message}")
                    downstatusStatusPublisher.onError(it)
                })
        )
    }

    /**
     * Cancel (and interrupt if necessary) any ongoing DownloadTask execution.
     */
    private fun cancelDownload() {
        mDownloadTask?.let {
            it.cancel()
        }
    }

    fun isDownloading(): Boolean {
        return isDownloading
    }

    override fun onCleared() {
        cancelDownload()
        bin.clear()
    }
}