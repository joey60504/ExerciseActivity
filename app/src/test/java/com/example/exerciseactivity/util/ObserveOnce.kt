package com.example.exerciseactivity.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.observeOnce(onChange: (T) -> Unit) {
    val observer = object : Observer<T> {
        override fun onChanged(value: T) {
            onChange(value)
            removeObserver(this)
        }
    }
    observeForever(observer)
}