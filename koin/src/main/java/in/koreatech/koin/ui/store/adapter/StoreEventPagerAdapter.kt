package `in`.koreatech.koin.ui.store.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import `in`.koreatech.koin.R
import `in`.koreatech.koin.databinding.StoreEventCardBinding
import `in`.koreatech.koin.domain.model.store.Store
import androidx.recyclerview.widget.ListAdapter

class StoreEventPagerAdapter(val eventData: ArrayList<String>): ListAdapter<Store,StoreEventPagerAdapter.StoreEvnetCardViewHolder>(
    diffCallback
){

    val tmpData:ArrayList<String> = ArrayList<String>()

    inner class StoreEvnetCardViewHolder(
        val binding: StoreEventCardBinding
    ): RecyclerView.ViewHolder(binding.root){
        val container = binding.storeEventContainer
        val eventStoreImage = binding.eventImageView
        val eventStoreName = binding.evnetStoreNameTv

        /*fun bind(store: Store){
            if(store.isEvent){
                eventStoreName.text = store.name + "에서"
            }
        }*/
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int): StoreEvnetCardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = StoreEventCardBinding.inflate(inflater, parent, false)
        return StoreEvnetCardViewHolder(binding)
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun onBindViewHolder(holder: StoreEventPagerAdapter.StoreEvnetCardViewHolder, position: Int) {
        with(holder){
            eventStoreName.text = eventData[position % eventData.size] + "에서"

            when(position % 4){
                0 -> container.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.event_card_color_1))
                1 -> container.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.event_card_color_2))
                2 -> container.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.event_card_color_3))
                3 -> container.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.event_card_color_4))
            }
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Store>() {
            override fun areItemsTheSame(oldItem: Store, newItem: Store): Boolean {
                return oldItem.uid == newItem.uid
            }

            override fun areContentsTheSame(oldItem: Store, newItem: Store): Boolean {
                return oldItem == newItem
            }
        }
    }
}