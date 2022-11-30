package `in`.koreatech.koin.ui.bus.adpater

import `in`.koreatech.koin.databinding.ItemCityBusRemainTimeBinding
import `in`.koreatech.koin.databinding.ItemExpressBusRemainTimeBinding
import `in`.koreatech.koin.databinding.ItemShuttleBusRemainTimeBinding
import `in`.koreatech.koin.domain.model.bus.BusType
import `in`.koreatech.koin.ui.bus.state.BusRemainTimeUiState
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class BusRemainTimeAdapter :
    ListAdapter<BusRemainTimeUiState, RecyclerView.ViewHolder>(diffCallback) {
    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).type) {
            is BusType.Shuttle -> SHUTTLE
            is BusType.Express -> EXPRESS
            is BusType.City -> CITY
            else -> -1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            SHUTTLE -> ShuttleViewHolder(
                ItemShuttleBusRemainTimeBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    )
                )
            )
            EXPRESS -> ExpressViewHolder(
                ItemExpressBusRemainTimeBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    )
                )
            )
            CITY -> CityViewHolder(ItemCityBusRemainTimeBinding.inflate(LayoutInflater.from(parent.context)))
            else -> throw IllegalArgumentException("Wrong view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ShuttleViewHolder -> holder.bind(getItem(position))
            is ExpressViewHolder -> holder.bind(getItem(position))
            is CityViewHolder -> holder.bind(getItem(position))
        }
    }

    private inner class ShuttleViewHolder(
        private val binding: ItemShuttleBusRemainTimeBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        fun bind(busRemainTimeUiState: BusRemainTimeUiState) {
            if (busRemainTimeUiState.type != BusType.Shuttle) {
                throw IllegalArgumentException("Not shuttle bus ui state")
            }

            with(binding) {
                textViewBusDeparture.text = busRemainTimeUiState.departure
                textViewBusArrival.text = busRemainTimeUiState.arrival

                textViewNowArrivalTime.text = busRemainTimeUiState.nowBusRemainTime
                textViewNowDepartureTime.text = busRemainTimeUiState.nowBusDepartureTime
                textViewNextArrivalTime.text = busRemainTimeUiState.nextBusRemainTime
                textViewNextDepartureTime.text = busRemainTimeUiState.nextBusDepartureTime
            }
        }
    }

    private inner class ExpressViewHolder(
        private val binding: ItemExpressBusRemainTimeBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        fun bind(busRemainTimeUiState: BusRemainTimeUiState) {
            if (busRemainTimeUiState.type != BusType.Express) {
                throw IllegalArgumentException("Not express bus ui state")
            }

            with(binding) {
                textViewBusDeparture.text = busRemainTimeUiState.departure
                textViewBusArrival.text = busRemainTimeUiState.arrival

                textViewNowArrivalTime.text = busRemainTimeUiState.nowBusRemainTime
                textViewNowDepartureTime.text = busRemainTimeUiState.nowBusDepartureTime
                textViewNextArrivalTime.text = busRemainTimeUiState.nextBusRemainTime
                textViewNextDepartureTime.text = busRemainTimeUiState.nextBusDepartureTime

                textViewBusType.isVisible = false
            }
        }
    }

    private inner class CityViewHolder(
        private val binding: ItemCityBusRemainTimeBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        fun bind(busRemainTimeUiState: BusRemainTimeUiState) {
            if (busRemainTimeUiState.type != BusType.City) {
                throw IllegalArgumentException("Not city bus ui state")
            }

            with(binding) {
                textViewBusDeparture.text = busRemainTimeUiState.departure
                textViewBusArrival.text = busRemainTimeUiState.arrival

                textViewNowArrivalTime.text = busRemainTimeUiState.nowBusRemainTime
                textViewNowDepartureTime.text = busRemainTimeUiState.nowBusDepartureTime
                textViewNextArrivalTime.text = busRemainTimeUiState.nextBusRemainTime
                textViewNextDepartureTime.text = busRemainTimeUiState.nextBusDepartureTime

                if (busRemainTimeUiState.busNumber == null) {
                    textViewBusNumber.isVisible = false
                } else {
                    textViewBusNumber.isVisible = true
                    textViewBusNumber.text = busRemainTimeUiState.busNumber
                }
            }
        }
    }

    companion object {
        private const val SHUTTLE = 0
        private const val EXPRESS = 1
        private const val CITY = 2

        private val diffCallback = object : DiffUtil.ItemCallback<BusRemainTimeUiState>() {
            override fun areItemsTheSame(
                oldItem: BusRemainTimeUiState,
                newItem: BusRemainTimeUiState
            ): Boolean {
                return oldItem.departure == newItem.departure && oldItem.arrival == newItem.arrival
            }

            override fun areContentsTheSame(
                oldItem: BusRemainTimeUiState,
                newItem: BusRemainTimeUiState
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}