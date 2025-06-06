package com.example.exerciseactivity.ui.homepage

import androidx.lifecycle.ViewModel
import com.example.exerciseactivity.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@Suppress("UNUSED")
abstract class HomepageModule {
    @Binds
    @IntoMap
    @ViewModelKey(HomepageViewModel::class)
    internal abstract fun bindHomepageViewModel(viewModel: HomepageViewModel): ViewModel

}
