package com.example.exerciseactivity.ui.homepage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.exerciseactivity.R
import com.example.exerciseactivity.data.response.Park
import com.example.exerciseactivity.databinding.ItemHomepageParkBinding

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
            Glide.with(itemView.context)
                .load(park.picUrl)
                .error(R.drawable.ic_img_error)
                .into(binding.imgHomepagePark)

            binding.txtParkName.text = park.name
            binding.txtParkDesc.text = park.info
            binding.txtParkCloseInfo.text =
                park.memo.ifBlank { itemView.context.getString(R.string.txt_no_closed_info) }
        }
    }
}