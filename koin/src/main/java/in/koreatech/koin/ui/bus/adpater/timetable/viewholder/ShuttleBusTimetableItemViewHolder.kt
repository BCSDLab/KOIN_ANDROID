package `in`.koreatech.koin.ui.bus.adpater.timetable.viewholder

import `in`.koreatech.koin.databinding.BusTimetableShuttleItemBinding
import `in`.koreatech.koin.ui.bus.state.ShuttleBusTimetableUiItem

class ShuttleBusTimetableItemViewHolder(private val binding: BusTimetableShuttleItemBinding) :
    TableItemViewHolder<ShuttleBusTimetableUiItem>(binding) {
    override fun bind(item: ShuttleBusTimetableUiItem) {
        with(binding) {
            textViewBusLocation.text = item.node
            textViewBusRideTime.text = item.arrivalTime
        }
    }
}