package com.example.exerciseactivity.ui

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.exerciseactivity.data.model.NetworkState

abstract class BaseDataSource<T : Any, U : Any> : PageKeyedDataSource<T, U>() {
    val networkState = MutableLiveData<NetworkState>()
    val initialLoad = MutableLiveData<NetworkState>()
}