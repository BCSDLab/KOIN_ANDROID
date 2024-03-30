package `in`.koreatech.koin.ui.bus.adpater.timetable

import `in`.koreatech.koin.R
import `in`.koreatech.koin.ui.bus.adpater.timetable.viewholder.BusTimetableHeaderViewHolder
import `in`.koreatech.koin.ui.bus.adpater.timetable.viewholder.BusTimetableItemViewHolder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import `in`.koreatech.koin.ui.bus.adpater.timetable.viewholder.BusTimetableFooterViewHolder

abstract class BusTimetableAdapter<T>(itemCallback: ItemCallback<T>) :
    ListAdapter<T, ViewHolder>(itemCallback) {

    private var updatedAt: String? = null

    override fun getItemViewType(position: Int) = when (position) {
        0 -> HEADER
        itemCount - 1 -> FOOTER
        else -> ITEM
    }

    final override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return when (viewType) {
            HEADER -> onCreateHeaderViewHolder(parent)
            ITEM -> onCreateItemViewHolder(parent)
            FOOTER -> onCreateFooterViewHolder(parent)
            else -> throw IllegalArgumentException("Wrong viewtype")
        }
    }

    final override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ITEM -> (holder as BusTimetableItemViewHolder<T>).bind(getItem(position))
            FOOTER -> (holder as BusTimetableFooterViewHolder).setUpdatedAt(updatedAt)
        }
    }

    abstract fun onCreateHeaderViewHolder(parent: ViewGroup): BusTimetableHeaderViewHolder
    abstract fun onCreateItemViewHolder(parent: ViewGroup): BusTimetableItemViewHolder<T>
    abstract fun onCreateFooterViewHolder(parent: ViewGroup): BusTimetableFooterViewHolder

    fun setUpdatedAt(date: String?) {
        updatedAt = date
    }

    final override fun submitList(list: List<T>?) {
        super.submitList(
            if (!list.isNullOrEmpty()) {
                mutableListOf<T>().apply {
                    add(list[0])
                    addAll(list)
                    add(list[0])
                }
            } else null
        )
    }

    final override fun submitList(list: List<T>?, commitCallback: Runnable?) {
        super.submitList(
            if (!list.isNullOrEmpty()) {
                mutableListOf<T>().apply {
                    add(list[0])
                    addAll(list)
                    add(list[0])
                }
            } else null, commitCallback)
    }

    companion object {
        private const val ITEM = 1000
        private const val HEADER = 2000
        private const val FOOTER = 3000
    }
}