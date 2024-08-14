package `in`.koreatech.koin.ui.bus.adpater.timetable.viewholder

import `in`.koreatech.koin.databinding.BusTimetableExpressItemBinding
import `in`.koreatech.koin.ui.bus.state.ExpressBusTimetableUiItem

class ExpressBusTimetableItemViewHolder(private val binding: BusTimetableExpressItemBinding) :
    TableItemViewHolder<ExpressBusTimetableUiItem>(binding) {
    override fun bind(item: ExpressBusTimetableUiItem) {
        with(binding) {
            textViewBusArrivalTime.text = item.arrivalTime
            textViewBusDepartureTime.text = item.departureTime
            textViewCharge.text = item.charge
        }
    }
}