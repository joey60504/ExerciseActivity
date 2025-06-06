package com.example.exerciseactivity.ui.homepage

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.exerciseactivity.databinding.ActivityHomepageBinding
import com.example.exerciseactivity.di.withFactory
import com.example.exerciseactivity.ui.BaseActivity
import com.example.exerciseactivity.ui.parkdetail.ParkDetailActivity
import javax.inject.Inject
import kotlin.math.max

class HomepageActivity : BaseActivity<ActivityHomepageBinding>() {
    override fun getViewBinding(): ActivityHomepageBinding {
        return ActivityHomepageBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var viewModelFactory: HomepageViewModel.Factory
    private val viewModel: HomepageViewModel by viewModels {
        withFactory(
            viewModelFactory, intent.extras
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setUpStatusBarDisplay()
        setUpListener()
        setUpViewModelObserve()
    }


    @SuppressLint("SetTextI18n")
    private fun setUpStatusBarDisplay() {
        binding.ucNav.ucImgNavLeft.visibility = View.VISIBLE
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
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun setUpViewModelObserve() {
        viewModel.showLoading.observe(this) {
            if (it) {
                binding.layoutParkLoading.visibility = View.VISIBLE
                binding.recyclerPark.visibility = View.GONE
            } else {
                binding.layoutParkLoading.visibility = View.GONE
                binding.recyclerPark.visibility = View.VISIBLE
            }
        }

        viewModel.displayPark.observe(this) {
            val parkAdapter = HomepageParkAdapter(
                it,
                onParkClick = { park ->
                    viewModel.onParkClick(park)
                }
            )
            binding.recyclerPark.apply {
                this.layoutManager = LinearLayoutManager(this@HomepageActivity)
                setHasFixedSize(true)
                this.adapter = parkAdapter
            }
        }

        viewModel.navigationToParkDetail.observe(this) {
            startActivity(Intent(this@HomepageActivity, ParkDetailActivity::class.java).apply {
                putExtras(
                    bundleOf(
                        "Park" to it
                    )
                )
            })
        }
    }
}

inline fun FragmentManager.inTransaction(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commit()
}