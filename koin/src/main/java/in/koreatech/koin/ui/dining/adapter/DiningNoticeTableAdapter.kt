package `in`.koreatech.koin.ui.dining.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import `in`.koreatech.koin.databinding.TableDiningNoticeFooterBinding
import `in`.koreatech.koin.databinding.TableDiningNoticeHeaderBinding
import `in`.koreatech.koin.databinding.TableDiningNoticeItemBinding
import `in`.koreatech.koin.domain.model.coopshop.OpenCloseTimeInfo
import `in`.koreatech.koin.ui.bus.adpater.timetable.TableAdapter
import `in`.koreatech.koin.ui.bus.adpater.timetable.viewholder.TableFooterViewHolder
import `in`.koreatech.koin.ui.bus.adpater.timetable.viewholder.TableHeaderViewHolder
import `in`.koreatech.koin.ui.bus.adpater.timetable.viewholder.TableItemViewHolder

class DiningNoticeTableAdapter : TableAdapter<OpenCloseTimeInfo>(diffUtil) {

    inner class DiningNoticeTableHeaderViewHolder(binding: TableDiningNoticeHeaderBinding): TableHeaderViewHolder(binding)
    inner class DiningNoticeTableItemViewHolder(private val binding: TableDiningNoticeItemBinding): TableItemViewHolder<OpenCloseTimeInfo>(binding) {
        override fun bind(item: OpenCloseTimeInfo) {
            with(binding) {
                tvDayType.text = item.type
                tvOpenTime.text = item.openTime
                tvCloseTime.text = item.closeTime
            }
        }
    }
    inner class DiningNoticeTableFooterViewHolder(binding: TableDiningNoticeFooterBinding): TableFooterViewHolder(binding)

    override fun onCreateHeaderViewHolder(parent: ViewGroup): TableHeaderViewHolder {
        return DiningNoticeTableHeaderViewHolder(
            TableDiningNoticeHeaderBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onCreateItemViewHolder(parent: ViewGroup): TableItemViewHolder<OpenCloseTimeInfo> {
        return DiningNoticeTableItemViewHolder(
            TableDiningNoticeItemBinding.inflate(LayoutInflater.from(parent.context)).apply {
                root.layoutParams = LinearLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                )
            }
        )
    }

    override fun onCreateFooterViewHolder(parent: ViewGroup): TableFooterViewHolder {
        return DiningNoticeTableFooterViewHolder(
            TableDiningNoticeFooterBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<OpenCloseTimeInfo>() {
            override fun areItemsTheSame(
                oldItem: OpenCloseTimeInfo,
                newItem: OpenCloseTimeInfo
            ): Boolean {
                return oldItem.openTime == newItem.openTime
            }

            override fun areContentsTheSame(
                oldItem: OpenCloseTimeInfo,
                newItem: OpenCloseTimeInfo
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}