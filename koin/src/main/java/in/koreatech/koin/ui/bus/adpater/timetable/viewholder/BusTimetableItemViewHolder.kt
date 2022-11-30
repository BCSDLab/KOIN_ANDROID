package `in`.koreatech.koin.ui.bus.adpater.timetable.viewholder

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BusTimetableItemViewHolder<T>(binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(item: T)
}