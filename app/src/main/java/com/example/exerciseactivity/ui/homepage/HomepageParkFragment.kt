package com.example.exerciseactivity.ui.homepage

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.exerciseactivity.R
import com.example.exerciseactivity.databinding.FragmentParkBinding
import com.example.exerciseactivity.di.viewBinding
import com.example.exerciseactivity.di.withFactory
import com.example.exerciseactivity.ui.BaseFragment
import com.example.exerciseactivity.ui.parkdetail.ParkDetailActivity
import javax.inject.Inject

class HomepageParkFragment : BaseFragment(R.layout.fragment_park) {
    @Inject
    lateinit var viewModelFactory: HomepageViewModel.Factory
    private val viewModel: HomepageViewModel by activityViewModels {
        withFactory(
            viewModelFactory
        )
    }
    private val binding by viewBinding(FragmentParkBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.init()
        setUpViewModelObserve()
    }

    private fun setUpViewModelObserve() {
        viewModel.showLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.layoutParkLoading.root.visibility = View.VISIBLE
                binding.recyclerPark.visibility = View.GONE
            } else {
                binding.layoutParkLoading.root.visibility = View.GONE
                binding.recyclerPark.visibility = View.VISIBLE
            }
        }

        viewModel.displayPark.observe(viewLifecycleOwner) {
            val parkAdapter = HomepageParkAdapter(
                it,
                onParkClick = { park ->
                    viewModel.onParkClick(park)
                }
            )
            binding.recyclerPark.apply {
                this.layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                this.adapter = parkAdapter
            }
        }

        viewModel.navigationToParkDetail.observe(viewLifecycleOwner) {
            startActivity(Intent(requireContext(), ParkDetailActivity::class.java).apply {
                putExtras(
                    bundleOf(
                        "Park" to it
                    )
                )
            })
        }
    }
}