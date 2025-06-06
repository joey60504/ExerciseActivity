package com.example.exerciseactivity.di

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class MyApplication: DaggerApplication() {
    // DaggerAppComponent 用於提供注入
    private val component: AndroidInjector<MyApplication> by lazy {
        DaggerAppComponent.factory().create(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return component
    }
}
