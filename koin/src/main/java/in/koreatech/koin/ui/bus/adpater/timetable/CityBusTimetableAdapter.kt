package `in`.koreatech.koin.ui.bus.adpater.timetable

import `in`.koreatech.koin.databinding.BusTimetableCityHeaderBinding
import `in`.koreatech.koin.databinding.BusTimetableCityItemBinding
import `in`.koreatech.koin.ui.bus.adpater.timetable.viewholder.TableHeaderViewHolder
import `in`.koreatech.koin.ui.bus.adpater.timetable.viewholder.TableItemViewHolder
import `in`.koreatech.koin.ui.bus.adpater.timetable.viewholder.CityBusTimetableHeaderViewHolder
import `in`.koreatech.koin.ui.bus.adpater.timetable.viewholder.CityBusTimetableItemViewHolder
import `in`.koreatech.koin.ui.bus.state.CityBusTimetableUiItem
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import `in`.koreatech.koin.databinding.BusTimetableCityFooterBinding
import `in`.koreatech.koin.ui.bus.adpater.timetable.viewholder.TableFooterViewHolder
import `in`.koreatech.koin.ui.bus.adpater.timetable.viewholder.CityBusTimetableFooterViewHolder

class CityBusTimetableAdapter : TableAdapter<CityBusTimetableUiItem>(itemCallback) {

    override fun onCreateHeaderViewHolder(parent: ViewGroup): TableHeaderViewHolder {
        return CityBusTimetableHeaderViewHolder(
            BusTimetableCityHeaderBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onCreateItemViewHolder(parent: ViewGroup): TableItemViewHolder<CityBusTimetableUiItem> {
        return CityBusTimetableItemViewHolder(
            BusTimetableCityItemBinding.inflate(LayoutInflater.from(parent.context)).apply {
                root.layoutParams = LinearLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                )
            }
        )
    }

    override fun onCreateFooterViewHolder(parent: ViewGroup): TableFooterViewHolder {
        return CityBusTimetableFooterViewHolder(
            BusTimetableCityFooterBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    companion object {
        private val itemCallback = object : ItemCallback<CityBusTimetableUiItem>() {
            override fun areItemsTheSame(
                oldItem: CityBusTimetableUiItem,
                newItem: CityBusTimetableUiItem
            ): Boolean {
                return oldItem.am == newItem.am
            }

            override fun areContentsTheSame(
                oldItem: CityBusTimetableUiItem,
                newItem: CityBusTimetableUiItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}