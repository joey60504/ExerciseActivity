package com.example.exerciseactivity.ui.animalsdetail

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.bumptech.glide.Glide
import com.example.exerciseactivity.R
import com.example.exerciseactivity.databinding.ActivityAnimalsDetailBinding
import com.example.exerciseactivity.di.withFactory
import com.example.exerciseactivity.ui.BaseActivity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.math.max

class AnimalsDetailActivity : BaseActivity<ActivityAnimalsDetailBinding>() {

    @Inject
    lateinit var viewModelFactory: AnimalsDetailViewModel.Factory
    private val viewModel: AnimalsDetailViewModel by viewModels {
        withFactory(
            viewModelFactory, intent.extras
        )
    }

    override fun getViewBinding(): ActivityAnimalsDetailBinding {
        return ActivityAnimalsDetailBinding.inflate(layoutInflater)
    }

    @RequiresApi(Build.VERSION_CODES.O)
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUpViewModelObserve() {
        viewModel.displayAnimalsInfo.observe(this) { parkInfo ->
            parkInfo?.let {
                //圖片
                Glide.with(this@AnimalsDetailActivity)
                    .load(it.pic01Url)
                    .error(R.drawable.ic_img_error)
                    .into(binding.imgAnimals)
                //名稱
                if (it.nameCh.isBlank() && it.nameEn.isBlank()) {
                    binding.layoutAnimalsName.visibility = View.GONE
                } else {
                    binding.layoutAnimalsName.visibility = View.VISIBLE
                    binding.ucNav.ucTxtNav.text = it.nameCh
                    binding.txtAnimalsName.text = it.nameCh
                    binding.txtAnimalsEName.text = it.nameEn
                }
                //種類
                if (it.phylum.isBlank() && it.animalClass.isBlank() && it.order.isBlank() && it.family.isBlank()) {
                    binding.layoutAnimalsCategory.visibility = View.GONE
                } else {
                    binding.layoutAnimalsCategory.visibility = View.VISIBLE
                    binding.txtAnimalsCategory.text = this.getString(
                        R.string.txt_animals_desc,
                        it.phylum,
                        it.animalClass,
                        it.order,
                        it.family
                    )
                }
                //分佈
                if (it.distribution.isBlank()) {
                    binding.layoutAnimalsLocation.visibility = View.GONE
                } else {
                    binding.layoutAnimalsLocation.visibility = View.VISIBLE
                    binding.txtAnimalsLocation.text = it.distribution
                }
                //特色
                if (it.feature.isBlank()) {
                    binding.layoutAnimalsFeature.visibility = View.GONE
                } else {
                    binding.layoutAnimalsFeature.visibility = View.VISIBLE
                    binding.txtAnimalsFeature.text = it.feature
                }
                //行為
                if (it.behavior.isBlank()) {
                    binding.layoutAnimalsBehavior.visibility = View.GONE
                } else {
                    binding.layoutAnimalsBehavior.visibility = View.VISIBLE
                    binding.txtAnimalsBehavior.text = it.behavior
                }
                //最後更新時間
                val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")
                val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val dateTime = LocalDateTime.parse(it.importDate.date, inputFormatter)
                binding.txtAnimalsLastUpdateDate.text =
                    this.getString(
                        R.string.txt_animals_last_update_date,
                        outputFormatter.format(dateTime)
                    ).ifBlank { "" }
            }
        }
    }
}