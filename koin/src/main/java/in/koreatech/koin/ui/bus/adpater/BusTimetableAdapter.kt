package `in`.koreatech.koin.ui.bus.adpater

import `in`.koreatech.koin.R
import `in`.koreatech.koin.databinding.BusTimetableItemBinding
import `in`.koreatech.koin.ui.bus.state.BusTimetableItem
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class BusTimetableAdapter : ListAdapter<BusTimetableItem, RecyclerView.ViewHolder>(
    diffCallback
) {
    override fun getItemViewType(position: Int) = when (position) {
        0 -> HEADER
        itemCount - 1 -> FOOTER
        else -> ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HEADER -> BusTimetableHeaderViewHolder(parent)
            FOOTER -> BusTimetableFooterViewHolder(parent)
            ITEM -> BusTimetableItemViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.bus_timetable_item,
                    parent,
                    false
                )
            )
            else -> throw IllegalStateException("Wrong viewtype")
        }
    }

    override fun submitList(list: MutableList<BusTimetableItem>?) {
        super.submitList(list)
    }

    override fun submitList(list: MutableList<BusTimetableItem>?, commitCallback: Runnable?) {
        super.submitList(list, commitCallback)
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 2
    }

    private class BusTimetableHeaderViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.bus_timetable_header, parent, false)
    )

    private class BusTimetableFooterViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.bus_timetable_footer, parent, false)
    )

    private class BusTimetableItemViewHolder(
        private val binding: BusTimetableItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun setItem(location: String, time: String) {
            binding.textViewBusLocation.text = location
            binding.textViewBusRideTime.text = time
        }
    }

    companion object {
        private const val ITEM = 1000
        private const val HEADER = 1001
        private const val FOOTER = 1002

        private val diffCallback = object : DiffUtil.ItemCallback<BusTimetableItem>() {
            override fun areItemsTheSame(
                oldItem: BusTimetableItem,
                newItem: BusTimetableItem
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: BusTimetableItem,
                newItem: BusTimetableItem
            ): Boolean = oldItem == newItem
        }
    }
}