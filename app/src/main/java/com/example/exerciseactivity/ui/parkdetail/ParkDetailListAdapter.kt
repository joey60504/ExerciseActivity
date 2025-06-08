package com.example.exerciseactivity.ui.parkdetail


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.exerciseactivity.R
import com.example.exerciseactivity.data.model.NetworkState
import com.example.exerciseactivity.data.response.Park
import com.example.exerciseactivity.data.response.ParkInfo
import com.example.exerciseactivity.databinding.ItemHomepageParkBinding
import com.example.exerciseactivity.databinding.ItemParkDetailHeader2Binding
import com.example.exerciseactivity.databinding.ItemParkDetailHeaderBinding
import com.example.exerciseactivity.databinding.ItemParkDetailLoadingBinding

class ParkDetailListAdapter(
    private val onItemClick: (ParkInfo?) -> Unit,
    private val onWebClick: (Park) -> Unit
) : PagedListAdapter<ParkInfo, RecyclerView.ViewHolder>(diffCallback) {

    private var networkState: NetworkState? = null
    private var headerPark: Park? = null

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_HEADER2 = 1
        private const val VIEW_TYPE_ITEM = 2
        private const val VIEW_TYPE_LOADING = 3

        private val diffCallback = object : DiffUtil.ItemCallback<ParkInfo>() {
            override fun areItemsTheSame(oldItem: ParkInfo, newItem: ParkInfo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ParkInfo, newItem: ParkInfo): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 -> VIEW_TYPE_HEADER
            position == 1 && hasItemData() -> VIEW_TYPE_HEADER2
            hasExtraRow() && position == itemCount - 1 -> VIEW_TYPE_LOADING
            else -> VIEW_TYPE_ITEM
        }
    }


    override fun getItemCount(): Int {
        val baseCount = super.getItemCount()
        val extra = if (hasExtraRow()) 1 else 0
        val header2Count = if (hasItemData()) 1 else 0
        return baseCount + 1 + header2Count + extra
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val binding = ItemParkDetailHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                HeaderViewHolder(binding)
            }

            VIEW_TYPE_HEADER2 -> {
                val binding = ItemParkDetailHeader2Binding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                HeaderViewHolder2(binding)
            }

            VIEW_TYPE_ITEM -> {
                val binding = ItemHomepageParkBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ParkViewHolder(binding)
            }

            VIEW_TYPE_LOADING -> {
                val binding =
                    ItemParkDetailLoadingBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                LoadingViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ParkViewHolder -> {
                val itemPosition = position - 2
                val parkInfo = getItem(itemPosition)
                holder.bind(parkInfo, onItemClick)
            }

            is HeaderViewHolder -> {
                headerPark?.let { holder.bind(it, onWebClick) }
            }
        }
    }

    fun setHeaderData(park: Park) {
        this.headerPark = park
        notifyItemChanged(0)
    }

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = networkState
        val hadExtraRow = hasExtraRow()
        networkState = newNetworkState
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    private fun hasItemData(): Boolean {
        return currentList?.isNotEmpty() == true
    }

    class HeaderViewHolder2(binding: ItemParkDetailHeader2Binding) :
        RecyclerView.ViewHolder(binding.root)

    class HeaderViewHolder(private val binding: ItemParkDetailHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(park: Park, onWebClick: (Park) -> Unit) {
            Glide.with(itemView.context)
                .load(park.picUrl)
                .error(R.drawable.ic_img_error)
                .into(binding.imgParkDetail)

            binding.txtParkDetailDesc.text = park.info
            binding.txtParkDetailCloseInfo.text =
                park.memo.ifBlank { itemView.context.getString(R.string.txt_no_closed_info) }
            binding.txtParkDetailCategory.text = park.category

            binding.txtParkDetailOpenInWeb.setOnClickListener {
                onWebClick(park)
            }
        }
    }

    class ParkViewHolder(private val binding: ItemHomepageParkBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ParkInfo?, onClick: (ParkInfo?) -> Unit) {
            item?.let {
                Glide.with(itemView.context)
                    .load(item.pic01Url)
                    .error(R.drawable.ic_img_error)
                    .into(binding.imgHomepagePark)

                binding.txtParkName.text = it.nameCh
                binding.txtParkDesc.text = itemView.context.getString(
                    R.string.txt_animals_desc,
                    it.phylum,
                    it.animalClass,
                    it.order,
                    it.family
                )
                binding.root.setOnClickListener {
                    onClick(item)
                }
            }
        }
    }

    class LoadingViewHolder(binding: ItemParkDetailLoadingBinding) :
        RecyclerView.ViewHolder(binding.root)
}