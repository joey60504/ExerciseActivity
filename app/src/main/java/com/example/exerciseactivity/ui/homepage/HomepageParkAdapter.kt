package com.example.exerciseactivity.ui.homepage

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.signature.ObjectKey
import com.example.exerciseactivity.R
import com.example.exerciseactivity.data.response.Park
import com.example.exerciseactivity.databinding.ItemHomepageParkBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class HomepageParkAdapter(
    private val parkList: List<Park>,
    private val onParkClick: (Park) -> Unit
) : RecyclerView.Adapter<HomepageParkAdapter.HomepageParkViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomepageParkViewHolder {
        val view = ItemHomepageParkBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HomepageParkViewHolder(view)
    }

    override fun getItemCount(): Int {
        return parkList.size
    }

    override fun onBindViewHolder(holder: HomepageParkViewHolder, position: Int) {
        holder.bind(parkList[position])
    }

    inner class HomepageParkViewHolder(private val binding: ItemHomepageParkBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(park: Park) {
            binding.root.setOnClickListener {
                onParkClick(park)
            }
            val glideUrl = GlideUrl(
                park.picUrl.toHttps(),
                LazyHeaders.Builder()
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                    .addHeader("Referer", "https://www.zoo.gov.tw")
                    .build()
            )

            Glide.with(itemView.context)
                .load(glideUrl)
                .signature(ObjectKey(park.picUrl))
                .error(R.drawable.park_replace)
                .into(binding.imgHomepagePark)

            binding.txtParkName.text = park.name
            binding.txtParkDesc.text = park.info
            binding.txtParkCloseInfo.text =
                park.memo.ifBlank { itemView.context.getString(R.string.txt_no_closed_info) }
        }

        private fun String.toHttps(): String {
            return if (startsWith("http://")) {
                "https://" + removePrefix("http://")
            } else this
        }
    }
}