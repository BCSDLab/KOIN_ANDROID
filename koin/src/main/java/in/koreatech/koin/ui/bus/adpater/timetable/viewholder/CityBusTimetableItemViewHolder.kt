package `in`.koreatech.koin.ui.bus.adpater.timetable.viewholder

import `in`.koreatech.koin.databinding.BusTimetableCityItemBinding
import `in`.koreatech.koin.databinding.BusTimetableExpressItemBinding
import `in`.koreatech.koin.databinding.BusTimetableShuttleItemBinding
import `in`.koreatech.koin.ui.bus.state.CityBusTimetableUiItem
import `in`.koreatech.koin.ui.bus.state.ExpressBusTimetableUiItem
import `in`.koreatech.koin.ui.bus.state.ShuttleBusTimetableUiItem

class CityBusTimetableItemViewHolder(private val binding: BusTimetableCityItemBinding) :
    BusTimetableItemViewHolder<CityBusTimetableUiItem>(binding) {
    override fun bind(item: CityBusTimetableUiItem) {
        with(binding) {
            textViewBusLocation.text = item.am
            textViewBusRideTime.text = item.pm
        }
    }
}