package `in`.koreatech.koin.ui.store.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import `in`.koreatech.koin.R
import `in`.koreatech.koin.databinding.ItemStoreDetailEventBinding
import `in`.koreatech.koin.domain.model.store.ShopEvent

class StoreDetailEventRecyclerAdapter():
    ListAdapter<ShopEvent, RecyclerView.ViewHolder>(diffCallback) {

    inner class ViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(shopEvent: ShopEvent) {
            with(binding as ItemStoreDetailEventBinding) {
                storeDetailEvent.setOnClickListener {
                    storeDetailEvent.visibility = View.GONE
                    storeDetailEventExpand.visibility = View.VISIBLE
                }
                storeDetailViewSummary.setOnClickListener {
                    storeDetailEvent.visibility = View.VISIBLE
                    storeDetailEventExpand.visibility = View.GONE
                }
                storeDetailEventTitleTextview.text = shopEvent.title
                storeDetailEventTitleExpandTextview.text = shopEvent.title
                storeDetailEventTextview.text = shopEvent.content
                storeDetailEventExpandTextview.text = shopEvent.content
                storeDetailEventDateTextview.text = shopEvent.startDate + "-" + shopEvent.endDate
                storeDetailEventDateExpandTextview.text =
                    shopEvent.startDate + "-" + shopEvent.endDate
            }

            Glide.with(binding.root.context)
                .load(shopEvent.thumbnailImages?.getOrNull(0) ?: R.drawable.no_image)
                .into(binding.storeDetailEventImageview)
            Glide.with(binding.root.context)
                .load(shopEvent.thumbnailImages?.getOrNull(0) ?: R.drawable.no_event_thumbnail_image)
                .into(binding.storeDetailEventExpandImageview)

        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val binding = ItemStoreDetailEventBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(getItem(position))
    }


    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<ShopEvent>() {
            override fun areItemsTheSame(oldItem: ShopEvent, newItem: ShopEvent): Boolean {
                return oldItem.shopId == newItem.shopId
            }

            override fun areContentsTheSame(oldItem: ShopEvent, newItem: ShopEvent): Boolean {
                return oldItem == newItem
            }
        }
    }
}
