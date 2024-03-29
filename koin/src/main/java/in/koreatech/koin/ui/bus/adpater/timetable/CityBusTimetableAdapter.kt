package `in`.koreatech.koin.ui.bus.adpater.timetable

import `in`.koreatech.koin.databinding.BusTimetableCityHeaderBinding
import `in`.koreatech.koin.databinding.BusTimetableCityItemBinding
import `in`.koreatech.koin.ui.bus.adpater.timetable.viewholder.BusTimetableHeaderViewHolder
import `in`.koreatech.koin.ui.bus.adpater.timetable.viewholder.BusTimetableItemViewHolder
import `in`.koreatech.koin.ui.bus.adpater.timetable.viewholder.CityBusTimetableHeaderViewHolder
import `in`.koreatech.koin.ui.bus.adpater.timetable.viewholder.CityBusTimetableItemViewHolder
import `in`.koreatech.koin.ui.bus.state.CityBusTimetableUiItem
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil.ItemCallback

class CityBusTimetableAdapter : BusTimetableAdapter<CityBusTimetableUiItem>(itemCallback) {

    override fun onCreateHeaderViewHolder(parent: ViewGroup): BusTimetableHeaderViewHolder {
        return CityBusTimetableHeaderViewHolder(
            BusTimetableCityHeaderBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onCreateItemViewHolder(parent: ViewGroup): BusTimetableItemViewHolder<CityBusTimetableUiItem> {
        return CityBusTimetableItemViewHolder(
            BusTimetableCityItemBinding.inflate(LayoutInflater.from(parent.context)).apply {
                root.layoutParams = LinearLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                )
            }
        )
    }

    companion object {
        private val itemCallback = object : ItemCallback<CityBusTimetableUiItem>() {
            override fun areItemsTheSame(
                oldItem: CityBusTimetableUiItem,
                newItem: CityBusTimetableUiItem
            ): Boolean {
                return oldItem.timeInfo == newItem.timeInfo
            }

            override fun areContentsTheSame(
                oldItem: CityBusTimetableUiItem,
                newItem: CityBusTimetableUiItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}