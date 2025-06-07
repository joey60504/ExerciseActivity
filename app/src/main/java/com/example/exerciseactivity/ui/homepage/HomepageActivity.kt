package com.example.exerciseactivity.ui.homepage

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.exerciseactivity.R
import com.example.exerciseactivity.databinding.ActivityHomepageBinding
import com.example.exerciseactivity.ui.BaseActivity
import kotlin.math.max

class HomepageActivity : BaseActivity<ActivityHomepageBinding>() {
    override fun getViewBinding(): ActivityHomepageBinding {
        return ActivityHomepageBinding.inflate(layoutInflater)
    }
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setUpNavController()
        setUpStatusBarDisplay()
        setUpListener()
    }

    private fun setUpNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.layout_container) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)
        NavigationUI.setupWithNavController(binding.navigationView, navController)
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
            val statusHeight = getStatusBarHeight()
            binding.layoutConstraintlayout.layoutParams =
                (binding.layoutConstraintlayout.layoutParams as ViewGroup.MarginLayoutParams).apply {
                    setMargins(0, statusHeight, 0, 0)
                }

            insets
        }
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
    }

    private fun setUpListener() {
        binding.ucNav.ucImgNavLeft.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }
}