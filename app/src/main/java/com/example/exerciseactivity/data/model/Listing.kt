package com.example.exerciseactivity.data.model

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

data class Listing<T : Any>(
    val pagedList: LiveData<PagedList<T>>,
    val networkState: LiveData<NetworkState>,
    val initState: LiveData<NetworkState>,
    val totalCount: LiveData<Int>? = null,
    val refresh: () -> Unit,
)

data class NetworkState constructor(
    val status: Status,
    val msg: String? = null,
    val rCode: String? = null
) {
    companion object {
        val LOADED = NetworkState(Status.SUCCESS)
        val LOADING = NetworkState(Status.RUNNING)
        val NO_DATA = NetworkState(Status.NO_DATA)
        val NO_DATA_FILTER = NetworkState(Status.NO_DATA_FILTER)
        val NETWORK_ERROR = NetworkState(Status.NETWORK_ERROR)
        fun error(msg: String?, rCode: String? = null) = NetworkState(Status.FAILED, msg, rCode)
    }
}

enum class Status {
    NO_DATA,
    RUNNING,
    SUCCESS,
    FAILED,
    NO_DATA_FILTER,
    NETWORK_ERROR
}
