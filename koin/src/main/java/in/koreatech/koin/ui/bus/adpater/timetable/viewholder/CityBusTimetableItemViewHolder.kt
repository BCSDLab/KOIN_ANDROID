package `in`.koreatech.koin.ui.bus.adpater.timetable.viewholder

import `in`.koreatech.koin.databinding.BusTimetableCityItemBinding
import `in`.koreatech.koin.ui.bus.state.CityBusTimetableUiItem

class CityBusTimetableItemViewHolder(private val binding: BusTimetableCityItemBinding) :
    TableItemViewHolder<CityBusTimetableUiItem>(binding) {
    override fun bind(item: CityBusTimetableUiItem) {
        with(binding) {
            textViewBusLocation.text = item.am
            textViewBusRideTime.text = item.pm
        }
    }
}