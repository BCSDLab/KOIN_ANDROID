package `in`.koreatech.koin.ui.bus.adpater.timetable.viewholder

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import `in`.koreatech.koin.databinding.BusTimetableCityFooterBinding
import `in`.koreatech.koin.databinding.BusTimetableExpressFooterBinding
import `in`.koreatech.koin.databinding.BusTimetableShuttleFooterBinding

abstract class BusTimetableFooterViewHolder(protected val binding: ViewDataBinding)
    : RecyclerView.ViewHolder(binding.root) {
        fun setUpdatedAt(date: String?) {
            when(binding) {
                is BusTimetableShuttleFooterBinding -> {
                    binding.textViewUpdatedAt.text = date
                }
                is BusTimetableExpressFooterBinding -> {
                    binding.textViewUpdatedAt.text = date
                }
                is BusTimetableCityFooterBinding -> {
                    binding.textViewUpdatedAt.text = date
                }
            }
        }
    }