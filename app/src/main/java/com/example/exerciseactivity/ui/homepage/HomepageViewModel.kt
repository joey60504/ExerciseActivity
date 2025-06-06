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

    private val _navigationToParkDetail = LiveEvent<Park>()
    val navigationToParkDetail: LiveData<Park> = _navigationToParkDetail

    init {
        viewModelScope.launch {
            getParkDataUseCase()
                .onStart {
                    _showLoading.postValue(true)
                }
                .onCompletion {
                    _showLoading.postValue(false)
                }.collect { parkResult ->
                    _displayPark.postValue(parkResult.parks)
                }
        }
    }

    fun onParkClick(park: Park) {
        _navigationToParkDetail.postValue(park)
    }
}