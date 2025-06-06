package com.example.exerciseactivity.ui.animalsdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import com.example.exerciseactivity.data.response.ParkInfo
import com.example.exerciseactivity.di.LiveEvent
import com.example.exerciseactivity.ui.BaseViewModel
import javax.inject.Inject

class AnimalsDetailViewModel @Inject constructor(
    handle: SavedStateHandle
) : BaseViewModel() {
    class Factory @Inject constructor(
    ) : BaseViewModel.Factory<AnimalsDetailViewModel>() {
        override fun create(handle: SavedStateHandle): AnimalsDetailViewModel {
            return AnimalsDetailViewModel(
                handle
            )
        }
    }

    private val _displayAnimalsInfo = LiveEvent<ParkInfo?>()
    val displayAnimalsInfo: LiveData<ParkInfo?> = _displayAnimalsInfo

    init {
        val parkInfo = handle.get<ParkInfo?>("ParkInfo")
        _displayAnimalsInfo.postValue(parkInfo)
    }
}