package com.example.exerciseactivity.ui.parkdetail

import androidx.lifecycle.ViewModel
import com.example.exerciseactivity.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@Suppress("UNUSED")
abstract class ParkDetailModule {
    @Binds
    @IntoMap
    @ViewModelKey(ParkDetailViewModel::class)
    internal abstract fun bindParkDetailViewModel(viewModel: ParkDetailViewModel): ViewModel
}
