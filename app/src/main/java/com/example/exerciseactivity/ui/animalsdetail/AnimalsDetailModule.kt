package com.example.exerciseactivity.ui.animalsdetail

import androidx.lifecycle.ViewModel
import com.example.exerciseactivity.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@Suppress("UNUSED")
abstract class AnimalsDetailModule {
    @Binds
    @IntoMap
    @ViewModelKey(AnimalsDetailViewModel::class)
    internal abstract fun bindAnimalsDetailViewModel(viewModel: AnimalsDetailViewModel): ViewModel
}
