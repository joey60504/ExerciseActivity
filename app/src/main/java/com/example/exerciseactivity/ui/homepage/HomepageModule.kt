package com.example.exerciseactivity.ui.homepage

import androidx.lifecycle.ViewModel
import com.example.exerciseactivity.di.FragmentScoped
import com.example.exerciseactivity.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
@Suppress("UNUSED")
abstract class HomepageModule {
    @Binds
    @IntoMap
    @ViewModelKey(HomepageViewModel::class)
    internal abstract fun bindHomepageViewModel(viewModel: HomepageViewModel): ViewModel

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeParkFragment(): HomepageParkFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeMapFragment(): HomepageMapFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeTransportFragment(): HomepageTransportFragment
}
