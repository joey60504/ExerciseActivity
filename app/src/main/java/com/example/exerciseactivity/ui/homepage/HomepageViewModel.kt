package com.example.exerciseactivity.ui.homepage

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.exerciseactivity.data.GetParkDataUseCase
import com.example.exerciseactivity.data.response.Park
import com.example.exerciseactivity.ui.BaseViewModel
import com.example.exerciseactivity.di.LiveEvent
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomepageViewModel @Inject constructor(
    private val getParkDataUseCase: GetParkDataUseCase,
    handle: SavedStateHandle
) : BaseViewModel() {
    class Factory @Inject constructor(
        private val getParkDataUseCase: GetParkDataUseCase,
    ) : BaseViewModel.Factory<HomepageViewModel>() {
        override fun create(handle: SavedStateHandle): HomepageViewModel {
            return HomepageViewModel(
                getParkDataUseCase,
                handle
            )
        }
    }

    private val _showLoading = LiveEvent<Boolean>()
    val showLoading: LiveData<Boolean> = _showLoading

    private val _displayPark = LiveEvent<List<Park>>()
    val displayPark: LiveData<List<Park>> = _displayPark

    private val _displayTransport = LiveEvent<String>()
    val displayTransport: LiveData<String> = _displayTransport

    private val _displayMap = LiveEvent<String>()
    val displayMap: LiveData<String> = _displayMap

    private val _displayUcNavRight = LiveEvent<Boolean>()
    val displayUcNavRight: LiveData<Boolean> = _displayUcNavRight

    private val _displayDownload = LiveEvent<String>()
    val displayDownload: LiveData<String> = _displayDownload

    private val _navigationToParkDetail = LiveEvent<Park>()
    val navigationToParkDetail: LiveData<Park> = _navigationToParkDetail

    private var parkList: List<Park>? = null

    fun init() {
        _displayUcNavRight.postValue(false)
        _showLoading.postValue(true)
        if (parkList.isNullOrEmpty()) {
            viewModelScope.launch {
                getParkDataUseCase()
                    .onStart {
                        _showLoading.postValue(true)
                    }
                    .onCompletion {
                        _showLoading.postValue(false)
                    }.collect { parkResult ->
                        parkList = parkResult.parks
                        _displayPark.postValue(parkResult.parks)
                    }
            }
        } else {
            _displayPark.postValue(parkList ?: listOf())
            _showLoading.postValue(false)
        }
    }

    fun initTransport() {
        _displayUcNavRight.postValue(false)
        _displayTransport.postValue("https://www.zoo.gov.taipei/News_Content.aspx?n=9F0E68F3EC5B5751&sms=F3B2EF982C0582B3&s=01BD7568D718DAED")
    }

    fun initMap() {
        _displayUcNavRight.postValue(true)
        _displayMap.postValue("https://www.zoo.gov.taipei/News_Content.aspx?n=E8BB5E02604E3BC7&sms=F3B2EF982C0582B3&s=BC8193C264494D92")
    }

    fun onParkClick(park: Park) {
        _navigationToParkDetail.postValue(park)
    }

    fun onDownloadClick() {
        _displayDownload.postValue("https://www-ws.gov.taipei/001/Upload/432/relpic/21511/8040477/88855b76-085e-4d95-8b9e-7f7132d20ac9.jpg")
    }
}