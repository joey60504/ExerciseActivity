package com.example.exerciseactivity.ui.parkdetail

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.paging.Config
import androidx.paging.toLiveData
import com.example.exerciseactivity.data.GetParkDataInfoUseCase
import com.example.exerciseactivity.data.model.Listing
import com.example.exerciseactivity.data.parameters.ParkDetailParameters
import com.example.exerciseactivity.data.response.Park
import com.example.exerciseactivity.data.response.ParkInfo
import com.example.exerciseactivity.di.LiveEvent
import com.example.exerciseactivity.ui.BaseViewModel
import javax.inject.Inject

class ParkDetailViewModel @Inject constructor(
    private val getParkDataInfoUseCase: GetParkDataInfoUseCase,
    handle: SavedStateHandle
) : BaseViewModel() {
    class Factory @Inject constructor(
        private val getParkDataInfoUseCase: GetParkDataInfoUseCase,
    ) : BaseViewModel.Factory<ParkDetailViewModel>() {
        override fun create(handle: SavedStateHandle): ParkDetailViewModel {
            return ParkDetailViewModel(
                getParkDataInfoUseCase,
                handle
            )
        }
    }

    private val _displayPark = LiveEvent<Park?>()
    val displayPark: LiveData<Park?> = _displayPark

    private val _navigationToAnimals = LiveEvent<ParkInfo?>()
    val navigationToAnimals: LiveData<ParkInfo?> = _navigationToAnimals

    private var searchParameter: MutableLiveData<ParkDetailParameters> = MutableLiveData()

    //觀察搜尋條件，搜尋條件改變，觸發取資料
    private val searchResult = searchParameter.map {
        getData(it)
    }

    //搜尋結果改變觸發result，改變畫面
    val result = searchResult.switchMap { it.pagedList }

    //改變 adapter
    val networkState = searchResult.switchMap { it.networkState }


    //改變 ui
    val initState = searchResult.switchMap { it.initState }

    init {
        searchParameter.value = ParkDetailParameters(
            scope = "resourceAquire",
            limit = 20,
            offset = 0,
            showStart = 1,
            park = handle.get<Park>("Park")
        )
    }

    @MainThread
    private fun getData(parameter: ParkDetailParameters): Listing<ParkInfo> {
        val parkDetailListDataSourceFactory = ParkDetailListDataSourceFactory(
            getParkDataInfoUseCase,
            parameter,
            onHeaderLoaded = { park ->
                _displayPark.postValue(park)
            }
        )
        val livePagedList = parkDetailListDataSourceFactory.toLiveData(
            config = Config(
                pageSize = 20,
                enablePlaceholders = false,
                initialLoadSizeHint = 20,
                prefetchDistance = 2
            )
        )
        return Listing(
            pagedList = livePagedList,
            networkState = parkDetailListDataSourceFactory.sourceLiveData.switchMap { it.networkState },
            initState = parkDetailListDataSourceFactory.sourceLiveData.switchMap { it.initialLoad },
            refresh = {
                parkDetailListDataSourceFactory.sourceLiveData.value?.invalidate()
            }
        )
    }

    fun onItemClick(parkInfo: ParkInfo?) {
        _navigationToAnimals.postValue(parkInfo)
    }
}