package `in`.koreatech.koin.ui.bus.adpater.timetable

import `in`.koreatech.koin.databinding.BusTimetableExpressHeaderBinding
import `in`.koreatech.koin.databinding.BusTimetableExpressItemBinding
import `in`.koreatech.koin.ui.bus.adpater.timetable.viewholder.TableHeaderViewHolder
import `in`.koreatech.koin.ui.bus.adpater.timetable.viewholder.TableItemViewHolder
import `in`.koreatech.koin.ui.bus.adpater.timetable.viewholder.ExpressBusTimetableHeaderViewHolder
import `in`.koreatech.koin.ui.bus.adpater.timetable.viewholder.ExpressBusTimetableItemViewHolder
import `in`.koreatech.koin.ui.bus.state.ExpressBusTimetableUiItem
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import `in`.koreatech.koin.databinding.BusTimetableExpressFooterBinding
import `in`.koreatech.koin.ui.bus.adpater.timetable.viewholder.TableFooterViewHolder
import `in`.koreatech.koin.ui.bus.adpater.timetable.viewholder.ExpressBusTimetableFooterViewHolder

class ExpressBusTimetableAdapter : TableAdapter<ExpressBusTimetableUiItem>(itemCallback) {

    override fun onCreateHeaderViewHolder(parent: ViewGroup): TableHeaderViewHolder {
        return ExpressBusTimetableHeaderViewHolder(
            BusTimetableExpressHeaderBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onCreateItemViewHolder(parent: ViewGroup): TableItemViewHolder<ExpressBusTimetableUiItem> {
        return ExpressBusTimetableItemViewHolder(
            BusTimetableExpressItemBinding.inflate(LayoutInflater.from(parent.context)).apply {
                root.layoutParams = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                )
            }
        )
    }

    override fun onCreateFooterViewHolder(parent: ViewGroup): TableFooterViewHolder {
        return ExpressBusTimetableFooterViewHolder(
            BusTimetableExpressFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    companion object {
        private val itemCallback = object : ItemCallback<ExpressBusTimetableUiItem>() {
            override fun areItemsTheSame(
                oldItem: ExpressBusTimetableUiItem,
                newItem: ExpressBusTimetableUiItem
            ): Boolean {
                return oldItem.arrivalTime == newItem.arrivalTime
            }

            override fun areContentsTheSame(
                oldItem: ExpressBusTimetableUiItem,
                newItem: ExpressBusTimetableUiItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}