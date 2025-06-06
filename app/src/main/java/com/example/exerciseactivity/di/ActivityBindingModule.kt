package com.example.exerciseactivity.di

import com.example.exerciseactivity.ui.animalsdetail.AnimalsDetailActivity
import com.example.exerciseactivity.ui.animalsdetail.AnimalsDetailModule
import com.example.exerciseactivity.ui.homepage.HomepageActivity
import com.example.exerciseactivity.ui.homepage.HomepageModule
import com.example.exerciseactivity.ui.main.MainActivity
import com.example.exerciseactivity.ui.parkdetail.ParkDetailActivity
import com.example.exerciseactivity.ui.parkdetail.ParkDetailModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
@Suppress("UNUSED")
abstract class ActivityBindingModule {
    @ActivityScoped
    @ContributesAndroidInjector
    internal abstract fun bindMainActivity(): MainActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [HomepageModule::class])
    internal abstract fun bindHomepageActivity(): HomepageActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [ParkDetailModule::class])
    internal abstract fun bindParkDetailActivity(): ParkDetailActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [AnimalsDetailModule::class])
    internal abstract fun bindAnimalsDetailActivity(): AnimalsDetailActivity
}