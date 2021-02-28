package com.android.settings.lhosextras.custom.sections.grid

typealias onGridApplied = ((grid: GridOption) -> Unit)?

class GridOptionCompat(val gridOption: GridOption) {
    var selected = false
    var listener: onGridApplied = null
}