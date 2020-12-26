/*
 * Copyright (C) 2020 The dotOS Project
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
package com.android.settings.dotextras.custom.sections

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.android.settings.dotextras.R
import com.android.settings.dotextras.custom.sections.clock.*
import com.android.settings.dotextras.custom.utils.ItemRecyclerSpacer

class AODLockscreenSection : GenericSection() {

    private val EXTRA_CLOCK_FACE_NAME = "clock_face_name"
    private lateinit var mClockManager: BaseClockManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.section_aod_lock, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.clockfaceOptionsRecycler)
        recyclerView.isNestedScrollingEnabled = true
        val contentProviderClockProvider = ContentProviderClockProvider(requireActivity())
        mClockManager = object : BaseClockManager(
            ContentProviderClockProvider(requireActivity())) {
            override fun handleApply(option: Clockface?, callback: onHandleCallback) {
                callback?.invoke(true)
            }

            override fun lookUpCurrentClock(): String {
                return requireActivity().intent.getStringExtra(EXTRA_CLOCK_FACE_NAME)
            }
        }
        if (!mClockManager.isAvailable) {
            view.findViewById<LinearLayout>(R.id.clockfaceSection).visibility = View.GONE
            Log.e("ClockManager", "Not available")
        } else {
            mClockManager.fetchOptions({ options ->
                run {
                    if (options != null) {
                        val cm = ClockManager(requireContext().contentResolver,
                            contentProviderClockProvider)
                        val optionsCompat = ArrayList<ClockfaceCompat>()
                        for (option in options) {
                            optionsCompat.add(ClockfaceCompat(option))
                        }
                        recyclerView.adapter =
                            ClockfacePreviewRecyclerAdapter(cm, optionsCompat)
                        recyclerView.layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        recyclerView.addItemDecoration(
                            ItemRecyclerSpacer(resources.getDimension(R.dimen.recyclerSpacerBigger),
                                null,
                                false)
                        )
                        val snap = PagerSnapHelper()
                        snap.attachToRecyclerView(recyclerView)
                        for (i in 0 until optionsCompat.size) {
                            if (optionsCompat[i].clockface.isActive(cm))
                                recyclerView.scrollToPosition(i)
                        }

                    }
                }
            }, false)
        }
    }

}