package com.example.exerciseactivity.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
@Suppress("UNUSED")
class AppModule {
    @Provides
    fun provideContext(application: MyApplication): Context {
        return application.applicationContext
    }
}