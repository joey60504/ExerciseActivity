package com.example.exerciseactivity.ui.homepage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.exerciseactivity.R
import com.example.exerciseactivity.databinding.FragmentMapBinding
import com.example.exerciseactivity.di.viewBinding
import com.example.exerciseactivity.di.withFactory
import com.example.exerciseactivity.ui.BaseFragment
import javax.inject.Inject

class HomepageMapFragment : BaseFragment(R.layout.fragment_map) {
    @Inject
    lateinit var viewModelFactory: HomepageViewModel.Factory
    private val viewModel: HomepageViewModel by activityViewModels {
        withFactory(
            viewModelFactory
        )
    }
    private val binding by viewBinding(FragmentMapBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initMap()
        setUpViewModelObserve()
    }

    private fun setUpViewModelObserve() {
        viewModel.displayMap.observe(viewLifecycleOwner) {
            binding.webviewMap.loadUrl(it)
        }
    }
}