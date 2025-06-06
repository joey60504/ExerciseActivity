package com.example.exerciseactivity.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exerciseactivity.di.ViewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

abstract class BaseViewModel : ViewModel() {
    protected var jobs = mutableListOf<Job>()
    abstract class Factory<V : BaseViewModel>() : ViewModelFactory<V>

    override fun onCleared() {
        cancelJobs()
        viewModelScope.cancel()
    }

    protected fun cancelJobs() {
        jobs.map {
            it.cancel()
        }
        jobs.clear()
    }
}