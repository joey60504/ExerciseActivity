package com.example.exerciseactivity.ui.parkdetail

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.exerciseactivity.R
import com.example.exerciseactivity.databinding.ActivityParkDetailBinding
import com.example.exerciseactivity.di.withFactory
import com.example.exerciseactivity.ui.BaseActivity
import javax.inject.Inject
import kotlin.math.max
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.exerciseactivity.data.model.NetworkState
import com.example.exerciseactivity.data.model.Status
import com.example.exerciseactivity.ui.animalsdetail.AnimalsDetailActivity

class ParkDetailActivity : BaseActivity<ActivityParkDetailBinding>() {

    @Inject
    lateinit var viewModelFactory: ParkDetailViewModel.Factory
    private val viewModel: ParkDetailViewModel by viewModels {
        withFactory(
            viewModelFactory, intent.extras
        )
    }

    private lateinit var ticketAdapter: ParkDetailListAdapter

    override fun getViewBinding(): ActivityParkDetailBinding {
        return ActivityParkDetailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        ticketAdapter = ParkDetailListAdapter(
            onItemClick = { parkInfo ->
                viewModel.onItemClick(parkInfo)
            },
            onWebClick = { park ->
                val intent = Intent(Intent.ACTION_VIEW, park.url.toUri())
                intent.addCategory(Intent.CATEGORY_BROWSABLE)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.setPackage(null)
                startActivity(intent)
            }
        )
        binding.recyclerParkDetailInfo.apply {
            this.layoutManager = LinearLayoutManager(this@ParkDetailActivity)
            setHasFixedSize(true)
            this.adapter = ticketAdapter
        }
        setUpStatusBarDisplay()
        setUpListener()
        setUpViewModelObserve()
    }

    @SuppressLint("SetTextI18n")
    private fun setUpStatusBarDisplay() {
        binding.ucNav.ucImgNavLeft.visibility = View.VISIBLE
        binding.ucNav.ucImgNavLeft.setImageResource(R.drawable.ic_left_arrow)
        //StatusBar
        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                max(ime.bottom, systemBars.bottom)
            )
            insets
        }
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
    }

    private fun setUpListener() {
        binding.ucNav.ucImgNavLeft.setOnClickListener {
            finish()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUpViewModelObserve() {
        viewModel.displayPark.observe(this) { park ->
            park?.let {
                binding.ucNav.ucTxtNav.text = park.name
                ticketAdapter.setHeaderData(it)
            }
        }
        viewModel.initState.observe(this) { state ->
            when (state) {
                NetworkState.LOADING -> {
                    binding.layoutParkLoading.root.visibility = View.VISIBLE
                    binding.recyclerParkDetailInfo.visibility = View.GONE
                    binding.recyclerParkDetailInfo.post {
                        binding.recyclerParkDetailInfo.scrollToPosition(0)
                    }
                }

                NetworkState.LOADED -> {
                    binding.layoutParkLoading.root.visibility = View.GONE
                    binding.recyclerParkDetailInfo.visibility = View.VISIBLE
                }

                NetworkState.NETWORK_ERROR -> {
                    AlertDialog.Builder(this)
                        .setTitle(getString(R.string.error))
                        .setPositiveButton(getString(R.string.ok)) { _, _ ->
                            finish()
                        }
                        .show()
                }

                else -> {
                    if (state.status == Status.FAILED) {
                        Log.d("error", state.msg.toString())
                    }
                }
            }
        }


        viewModel.result.observe(this) {
            ticketAdapter.submitList(it)
        }

        viewModel.networkState.observe(this) { networkState ->
            ticketAdapter.setNetworkState(networkState)
        }

        viewModel.navigationToAnimals.observe(this) {
            startActivity(Intent(this@ParkDetailActivity, AnimalsDetailActivity::class.java).apply {
                putExtras(
                    bundleOf(
                        "ParkInfo" to it
                    )
                )
            })
        }
    }
}