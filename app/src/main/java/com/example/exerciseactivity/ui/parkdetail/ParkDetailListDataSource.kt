package com.example.exerciseactivity.ui.parkdetail

import com.example.exerciseactivity.data.GetParkDataInfoUseCase
import com.example.exerciseactivity.data.model.NetworkState
import com.example.exerciseactivity.data.parameters.ParkDetailParameters
import com.example.exerciseactivity.data.response.Park
import com.example.exerciseactivity.data.response.ParkInfo
import com.example.exerciseactivity.ui.BaseDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ParkDetailListDataSource(
    private val parameter: ParkDetailParameters,
    private val getParkDataInfoUseCase: GetParkDataInfoUseCase,
    private val onHeaderLoaded: (Park) -> Unit
) : BaseDataSource<Int, ParkInfo>() {

    companion object {
        private const val PAGE_SIZE = 20
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, ParkInfo>
    ) {
        initialLoad.postValue(NetworkState.LOADING)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // 告訴 adapter 設定 header
                parameter.park?.let {
                    withContext(Dispatchers.Main) {
                        onHeaderLoaded(it)
                    }
                }

                val (resultList, nextKey) = loadFilteredPage(0)
                withContext(Dispatchers.Main) {
                    callback.onResult(resultList, null, nextKey)
                    initialLoad.postValue(NetworkState.LOADED)
                    networkState.postValue(NetworkState.LOADED)
                }
            } catch (e: Throwable) {
                withContext(Dispatchers.Main) { handleError(e) }
            }
        }
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, ParkInfo>
    ) {
        networkState.postValue(NetworkState.LOADING)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val (resultList, nextKey) = loadFilteredPage(params.key)
                withContext(Dispatchers.Main) {
                    callback.onResult(resultList, nextKey)
                    networkState.postValue(NetworkState.LOADED)
                }
            } catch (e: Throwable) {
                withContext(Dispatchers.Main) { handleError(e) }
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, ParkInfo>) {}

    private suspend fun loadFilteredPage(startOffset: Int): Pair<List<ParkInfo>, Int?> {
        var offset = startOffset
        var filteredResult: List<ParkInfo>? = null
        while (true) {
            parameter.offset = offset
            parameter.limit = PAGE_SIZE
            parameter.showStart = offset
            val data = getParkDataInfoUseCase(parameter).first()
            if (data.parks.isEmpty()) {
                break
            }
            val filtered = data.parks.filter { it.location == parameter.park?.name }
            if (filtered.isNotEmpty()) {
                filteredResult = filtered
                break
            }
            if (data.parks.size < PAGE_SIZE) {
                break
            }
            offset += PAGE_SIZE
        }
        val resultList = filteredResult ?: emptyList()
        val nextKey = if (resultList.isEmpty()) null else offset + PAGE_SIZE
        return resultList to nextKey
    }

    private fun handleError(throwable: Throwable) {
        if (throwable is UnknownHostException || throwable is SocketTimeoutException) {
            initialLoad.postValue(NetworkState.NETWORK_ERROR)
            networkState.postValue(NetworkState.NETWORK_ERROR)
        } else {
            val error = NetworkState.error(throwable.message)
            initialLoad.postValue(error)
            networkState.postValue(error)
        }
    }
}
