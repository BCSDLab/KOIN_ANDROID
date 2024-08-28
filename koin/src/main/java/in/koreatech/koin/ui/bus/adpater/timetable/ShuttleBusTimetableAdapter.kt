package `in`.koreatech.koin.ui.bus.adpater.timetable

import `in`.koreatech.koin.databinding.BusTimetableShuttleHeaderBinding
import `in`.koreatech.koin.databinding.BusTimetableShuttleItemBinding
import `in`.koreatech.koin.ui.bus.adpater.timetable.viewholder.TableHeaderViewHolder
import `in`.koreatech.koin.ui.bus.adpater.timetable.viewholder.TableItemViewHolder
import `in`.koreatech.koin.ui.bus.adpater.timetable.viewholder.ShuttleBusTimetableHeaderViewHolder
import `in`.koreatech.koin.ui.bus.adpater.timetable.viewholder.ShuttleBusTimetableItemViewHolder
import `in`.koreatech.koin.ui.bus.state.ShuttleBusTimetableUiItem
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import `in`.koreatech.koin.databinding.BusTimetableShuttleFooterBinding
import `in`.koreatech.koin.ui.bus.adpater.timetable.viewholder.TableFooterViewHolder
import `in`.koreatech.koin.ui.bus.adpater.timetable.viewholder.ShuttleBusTimetableFooterViewHolder

class ShuttleBusTimetableAdapter : TableAdapter<ShuttleBusTimetableUiItem>(itemCallback) {

    override fun onCreateHeaderViewHolder(parent: ViewGroup): TableHeaderViewHolder {
        return ShuttleBusTimetableHeaderViewHolder(
            BusTimetableShuttleHeaderBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onCreateItemViewHolder(parent: ViewGroup): TableItemViewHolder<ShuttleBusTimetableUiItem> {
        return ShuttleBusTimetableItemViewHolder(
            BusTimetableShuttleItemBinding.inflate(LayoutInflater.from(parent.context)).apply {
                root.layoutParams = LinearLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                )
            }
        )
    }

    override fun onCreateFooterViewHolder(parent: ViewGroup): TableFooterViewHolder {
        return ShuttleBusTimetableFooterViewHolder(
            BusTimetableShuttleFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    companion object {
        private val itemCallback = object : ItemCallback<ShuttleBusTimetableUiItem>() {
            override fun areItemsTheSame(
                oldItem: ShuttleBusTimetableUiItem,
                newItem: ShuttleBusTimetableUiItem
            ): Boolean {
                return oldItem.node == newItem.node
            }

            override fun areContentsTheSame(
                oldItem: ShuttleBusTimetableUiItem,
                newItem: ShuttleBusTimetableUiItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}