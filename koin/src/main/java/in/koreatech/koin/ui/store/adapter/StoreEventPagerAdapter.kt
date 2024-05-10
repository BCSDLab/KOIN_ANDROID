package `in`.koreatech.koin.ui.store.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import `in`.koreatech.koin.R
import `in`.koreatech.koin.databinding.StoreEventCardBinding
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import `in`.koreatech.koin.domain.model.store.StoreEvent

class StoreEventPagerAdapter(): ListAdapter<StoreEvent,StoreEventPagerAdapter.StoreEventCardViewHolder>(
    diffCallback
){

    var onItemClickListener: OnItemClickListener? = null

    inner class StoreEventCardViewHolder(
        val binding: StoreEventCardBinding
    ): RecyclerView.ViewHolder(binding.root){
        val container = binding.storeEventContainer
        val eventStoreImage = binding.eventImageView
        val eventStoreName = binding.evnetStoreNameTv
        fun bind(storeEvent: StoreEvent) {
            binding.root.setOnClickListener {
                onItemClickListener?.onItemClick(storeEvent)

            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int): StoreEventCardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = StoreEventCardBinding.inflate(inflater, parent, false)
        return StoreEventCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoreEventPagerAdapter.StoreEventCardViewHolder, position: Int) {

        val event = getItem(position % itemCount)
        with(holder){
            bind(event)

            eventStoreName.text = event.shopName

            if(event.thumbnailImages?.isEmpty() == true){
                eventStoreImage.setImageResource(R.drawable.event_default)
            }
            else{
                Glide.with(eventStoreImage)
                    .load(event.thumbnailImages?.get(0))
                    .override(100, 100)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
                    .into(eventStoreImage)
            }
        }
    }

    inline fun setOnItemClickListener(crossinline onItemClick: (storeEvent: StoreEvent) -> Unit) {
        onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(storeEvent: StoreEvent) {
                onItemClick(storeEvent)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(storeEvent: StoreEvent)
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<StoreEvent>() {
            override fun areItemsTheSame(oldItem: StoreEvent, newItem: StoreEvent): Boolean {
                return oldItem.shopId == newItem.shopId
            }

            override fun areContentsTheSame(oldItem: StoreEvent, newItem: StoreEvent): Boolean {
                return oldItem == newItem
            }
        }
    }
}