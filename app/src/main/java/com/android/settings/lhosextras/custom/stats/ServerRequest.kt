package com.android.settings.lhosextras.custom.stats

class ServerRequest {
    private var operation: String? = null
    private var stats: StatsData? = null
    fun setOperation(operation: String?) {
        this.operation = operation
    }

    fun setStats(stats: StatsData?) {
        this.stats = stats
    }
}