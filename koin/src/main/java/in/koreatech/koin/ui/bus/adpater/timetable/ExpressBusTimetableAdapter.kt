package `in`.koreatech.koin.ui.bus.adpater.timetable

import `in`.koreatech.koin.databinding.BusTimetableExpressHeaderBinding
import `in`.koreatech.koin.databinding.BusTimetableExpressItemBinding
import `in`.koreatech.koin.ui.bus.adpater.timetable.viewholder.BusTimetableHeaderViewHolder
import `in`.koreatech.koin.ui.bus.adpater.timetable.viewholder.BusTimetableItemViewHolder
import `in`.koreatech.koin.ui.bus.adpater.timetable.viewholder.ExpressBusTimetableHeaderViewHolder
import `in`.koreatech.koin.ui.bus.adpater.timetable.viewholder.ExpressBusTimetableItemViewHolder
import `in`.koreatech.koin.ui.bus.state.ExpressBusTimetableUiItem
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil.ItemCallback

class ExpressBusTimetableAdapter : BusTimetableAdapter<ExpressBusTimetableUiItem>(itemCallback) {

    override fun onCreateHeaderViewHolder(parent: ViewGroup): BusTimetableHeaderViewHolder {
        return ExpressBusTimetableHeaderViewHolder(
            BusTimetableExpressHeaderBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onCreateItemViewHolder(parent: ViewGroup): BusTimetableItemViewHolder<ExpressBusTimetableUiItem> {
        return ExpressBusTimetableItemViewHolder(
            BusTimetableExpressItemBinding.inflate(LayoutInflater.from(parent.context)).apply {
                root.layoutParams = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                )
            }
        )
    }

    companion object {
        private val itemCallback = object : ItemCallback<ExpressBusTimetableUiItem>() {
            override fun areItemsTheSame(
                oldItem: ExpressBusTimetableUiItem,
                newItem: ExpressBusTimetableUiItem
            ): Boolean {
                return oldItem.arrivalTime == newItem.arrivalTime
            }

            override fun areContentsTheSame(
                oldItem: ExpressBusTimetableUiItem,
                newItem: ExpressBusTimetableUiItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}