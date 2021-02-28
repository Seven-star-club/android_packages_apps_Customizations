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
package com.android.settings.lhosextras.custom.views

import android.content.Context
import android.content.om.IOverlayManager
import android.os.ServiceManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.android.settings.lhosextras.R
import com.android.settings.lhosextras.custom.sections.themes.FontPackAdapter
import com.android.settings.lhosextras.custom.utils.ItemRecyclerSpacer
import com.android.settings.lhosextras.system.OverlayController

class FontPackController(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    init {
        LayoutInflater.from(mContext).inflate(
            R.layout.item_fontpack_control, this, true
        )
        val overlayController = OverlayController(
            OverlayController.Categories.FONT_CATEGORY,
            mContext.packageManager,
            IOverlayManager.Stub
                .asInterface(ServiceManager.getService(Context.OVERLAY_SERVICE))
        )
        val recycler = findViewById<RecyclerView>(R.id.shapesRecycler)
        val fonts = overlayController.FontPacks().getFontPacks(context!!)
        val adapter = FontPackAdapter(overlayController, fonts)
        recycler.adapter = adapter
        recycler.addItemDecoration(
            ItemRecyclerSpacer(
                resources.getDimension(R.dimen.recyclerSpacer),
                0,
                false
            )
        )
        recycler.addItemDecoration(
            ItemRecyclerSpacer(
                resources.getDimension(R.dimen.recyclerSpacerBig),
                adapter.itemCount - 1,
                true
            )
        )
        val lm = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recycler.layoutManager = lm
        val smoothScroller: RecyclerView.SmoothScroller = object : LinearSmoothScroller(context) {
            override fun getVerticalSnapPreference(): Int = SNAP_TO_END
        }
        for (i in 0 until fonts.size) {
            if (fonts[i].selected)
                smoothScroller.targetPosition = i
        }
        lm.startSmoothScroll(smoothScroller)
        visibility = if (overlayController.isAvailable()) VISIBLE else GONE
    }
}