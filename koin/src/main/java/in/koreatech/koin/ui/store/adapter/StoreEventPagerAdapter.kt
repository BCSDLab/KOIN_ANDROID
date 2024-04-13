package `in`.koreatech.koin.ui.store.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import `in`.koreatech.koin.R
import `in`.koreatech.koin.databinding.StoreEventCardBinding
import androidx.recyclerview.widget.ListAdapter
import `in`.koreatech.koin.domain.model.bus.timer.BusArrivalInfo
import `in`.koreatech.koin.domain.model.store.StoreEvent

class StoreEventPagerAdapter(): ListAdapter<StoreEvent,StoreEventPagerAdapter.StoreEventCardViewHolder>(
    diffCallback
){
    private val storeEvents = mutableListOf<StoreEvent>()
    inner class StoreEventCardViewHolder(
        val binding: StoreEventCardBinding
    ): RecyclerView.ViewHolder(binding.root){
        val container = binding.storeEventContainer
        val eventStoreImage = binding.eventImageView
        val eventStoreName = binding.evnetStoreNameTv
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int): StoreEventCardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = StoreEventCardBinding.inflate(inflater, parent, false)
        return StoreEventCardViewHolder(binding)
    }

    override fun getItemCount(): Int = storeEvents.size

    override fun onBindViewHolder(holder: StoreEventPagerAdapter.StoreEventCardViewHolder, position: Int) {
        Log.d("로그 이벤트2", storeEvents.toString())
        val event = storeEvents[position]
        with(holder){
            if(storeEvents.size > 0){
                eventStoreName.text = storeEvents[position % storeEvents.size].shop_name
            }

            when(position % 4){
                0 -> container.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.event_card_color_1))
                1 -> container.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.event_card_color_2))
                2 -> container.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.event_card_color_3))
                3 -> container.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.event_card_color_4))
            }
            Log.d("로그 이벤트3", event.toString())
        }
    }

    fun setStoreEvents(items: List<StoreEvent>?) {
        if (items != null) {
            storeEvents.clear()
            storeEvents.addAll(items)
        }
        Log.d("로그 이벤트", storeEvents.toString())
        notifyDataSetChanged()
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<StoreEvent>() {
            override fun areItemsTheSame(oldItem: StoreEvent, newItem: StoreEvent): Boolean {
                return oldItem.shop_id == newItem.shop_id
            }

            override fun areContentsTheSame(oldItem: StoreEvent, newItem: StoreEvent): Boolean {
                return oldItem == newItem
            }
        }
    }
}