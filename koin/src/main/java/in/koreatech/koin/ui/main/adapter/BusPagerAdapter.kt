package `in`.koreatech.koin.ui.main.adapter

import `in`.koreatech.koin.R
import `in`.koreatech.koin.data.util.busNumberFormatted
import `in`.koreatech.koin.data.util.localized
import `in`.koreatech.koin.data.util.toBusArrivalTimeFormatted
import `in`.koreatech.koin.data.util.toBusRemainTimeFormatted
import `in`.koreatech.koin.databinding.MainCardBusBinding
import `in`.koreatech.koin.domain.model.bus.BusType
import `in`.koreatech.koin.domain.model.bus.timer.BusArrivalInfo
import `in`.koreatech.koin.domain.model.bus.timer.busType
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView

class BusPagerAdapter : RecyclerView.Adapter<BusPagerAdapter.MainCardBusViewHolder>() {
    override fun getItemCount(): Int = Int.MAX_VALUE

    private val busItems = mutableListOf<BusArrivalInfo>()

    var onSwitchClickListener: OnSwitchClickListener? = null
    var onCardClickListener: OnCardClickListener? = null
    var onGotoClickListener: OnGotoClickListener? = null

    inner class MainCardBusViewHolder(
        val binding: MainCardBusBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(busArrivalInfo: BusArrivalInfo) {
            with(binding) {
                root.setOnClickListener { onCardClickListener?.onCardClick(busArrivalInfo.busType) }
                imageButtonSwitch.setOnClickListener { onSwitchClickListener?.onSwitchClick(busArrivalInfo) }
                busGotoLayout.setOnClickListener { onGotoClickListener?.onGotoClick(busArrivalInfo.busType) }

                textViewDepartures.text = busArrivalInfo.departure.localized(root.context)
                textViewArrival.text = busArrivalInfo.arrival.localized(root.context)
                textViewBusType.text = busArrivalInfo.localized(root.context)
                textViewRemainingTime.text =
                    busArrivalInfo.nowBusRemainTime.toBusRemainTimeFormatted(root.context)

                when (busArrivalInfo) {
                    is BusArrivalInfo.ShuttleBusArrivalInfo -> {
                        textViewBusGoto.setText(R.string.bus_goto_unibus)
                    }
                    is BusArrivalInfo.ExpressBusArrivalInfo -> {
                        textViewBusGoto.setText(R.string.bus_goto_timetable)
                    }
                    is BusArrivalInfo.CityBusArrivalInfo -> {
                        textViewBusGoto.setText(R.string.bus_goto_timetable)
//                        val info = busArrivalInfo.busNumber?.busNumberFormatted(
//                            root.context
//                        )
//                        if(info == null) {
//                            textViewBusInfo.isVisible = false
//                        } else {
//                            textViewBusInfo.isVisible = true
//                            textViewBusInfo.text = info
//                        }
                    }
                    else -> {
                        textViewBusGoto.setText(R.string.bus_goto_timetable)
//                        val info = busArrivalInfo.nowBusArrivalTime?.toBusArrivalTimeFormatted(root.context)
//                        if(info == null) {
//                            textViewBusInfo.isVisible = false
//                        } else {
//                            textViewBusInfo.isVisible = true
//                            textViewBusInfo.text = info
//                        }
                    }
                }

                busTypeLayout.setBackgroundColor(
                    when (busArrivalInfo) {
                        is BusArrivalInfo.CityBusArrivalInfo ->
                            ContextCompat.getColor(root.context, R.color.green3)
                        is BusArrivalInfo.CommutingBusArrivalInfo, is BusArrivalInfo.ShuttleBusArrivalInfo ->
                            ContextCompat.getColor(root.context, R.color.colorAccent)
                        is BusArrivalInfo.ExpressBusArrivalInfo ->
                            ContextCompat.getColor(root.context, R.color.blue5)
                    }
                )
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainCardBusViewHolder {
        return MainCardBusViewHolder(MainCardBusBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MainCardBusViewHolder, position: Int) {
        if(busItems.size >= 3) {
            holder.bind(busItems[position % 3])
        }
    }

    fun setBusTimerItems(items: List<BusArrivalInfo>) {
        busItems.clear()
        busItems.addAll(items)
        notifyDataSetChanged()
    }

    inline fun setOnSwitchClickListener(crossinline onSwitchClick: (BusArrivalInfo) -> Unit) {
        onSwitchClickListener = object : OnSwitchClickListener {
            override fun onSwitchClick(busArrivalInfo: BusArrivalInfo) {
                onSwitchClick(busArrivalInfo)
            }
        }
    }

    inline fun setOnCardClickListener(crossinline onCardClick: (BusType) -> Unit) {
        onCardClickListener = object : OnCardClickListener {
            override fun onCardClick(busType: BusType) {
                onCardClick(busType)
            }
        }
    }

    inline fun setOnGotoClickListener(crossinline onGotoClick: (BusType) -> Unit) {
        onGotoClickListener = object : OnGotoClickListener {
            override fun onGotoClick(busType: BusType) {
                onGotoClick(busType)
            }
        }
    }

    interface OnSwitchClickListener {
        fun onSwitchClick(busArrivalInfo: BusArrivalInfo)
    }

    interface OnCardClickListener {
        fun onCardClick(busType: BusType)
    }

    interface OnGotoClickListener {
        fun onGotoClick(busType: BusType)
    }
}
