package `in`.koreatech.koin.ui.bus.adpater.search

import `in`.koreatech.koin.R
import `in`.koreatech.koin.databinding.BusTimetableSearchResultItemBinding
import `in`.koreatech.koin.domain.model.bus.BusType
import `in`.koreatech.koin.ui.bus.state.BusSearchResultItem
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class BusSearchResultAdapter : ListAdapter<BusSearchResultItem, BusSearchResultAdapter.ViewHolder>(
    itemCallback
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            BusTimetableSearchResultItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).apply {
                root.layoutParams = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                )
            }
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: BusTimetableSearchResultItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(busSearchResultItem: BusSearchResultItem) {
            with(binding.root.context) {
                when (busSearchResultItem.busType) {
                    BusType.City -> {
                        binding.textViewSearchResultBusType.apply {
                            text = getString(R.string.bus_name_city)
                            setTextColor(ContextCompat.getColor(this@with, R.color.green3))
                        }
                    }
                    BusType.Commuting -> {
                        binding.textViewSearchResultBusType.apply {
                            text = getString(R.string.bus_name_commuting)
                            setTextColor(ContextCompat.getColor(this@with, R.color.colorAccent))
                        }
                    }
                    BusType.Express -> {
                        binding.textViewSearchResultBusType.apply {
                            text = getString(R.string.bus_name_express)
                            setTextColor(ContextCompat.getColor(this@with, R.color.gray))
                        }
                    }
                    BusType.Shuttle -> {
                        binding.textViewSearchResultBusType.apply {
                            text = getString(R.string.bus_name_shuttle)
                            setTextColor(ContextCompat.getColor(this@with, R.color.colorAccent))
                        }
                    }
                }

                binding.textViewSearchResultBusTime.text = busSearchResultItem.time
            }
        }
    }

    companion object {
        private val itemCallback = object : DiffUtil.ItemCallback<BusSearchResultItem>() {
            override fun areItemsTheSame(
                oldItem: BusSearchResultItem,
                newItem: BusSearchResultItem
            ): Boolean {
                return oldItem.busType == newItem.busType
            }

            override fun areContentsTheSame(
                oldItem: BusSearchResultItem,
                newItem: BusSearchResultItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}