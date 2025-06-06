package com.example.exerciseactivity.ui

interface BaseCallback<T> {

    fun onResult(data: MutableList<T>) {

    }


    fun onRecommendHotResult(data: MutableList<T>) {

    }
}