package `in`.koreatech.koin.ui.dining.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import `in`.koreatech.koin.R
import `in`.koreatech.koin.databinding.ItemTableWithTitleBinding
import `in`.koreatech.koin.domain.model.coopshop.OpenCloseInfo

class DiningNoticeAdapter :
    ListAdapter<OpenCloseInfo, DiningNoticeAdapter.DiningNoticeViewHolder>(diffUtil) {

    inner class DiningNoticeViewHolder(private val binding: ItemTableWithTitleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OpenCloseInfo) {
            val context = itemView.context
            val tableAdapter = DiningNoticeTableAdapter()
            with(binding) {
                tvTitle.text =
                    context.getString(R.string.dining_notice_subtitle_time_format, item.dayOfWeek.value)
                table.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = tableAdapter
                    itemAnimator = null
                }
            }
            tableAdapter.submitList(item.opensByDayType)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiningNoticeViewHolder {
        return DiningNoticeViewHolder(
            ItemTableWithTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: DiningNoticeViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<OpenCloseInfo>() {
            override fun areItemsTheSame(oldItem: OpenCloseInfo, newItem: OpenCloseInfo): Boolean {
                return oldItem.dayOfWeek == newItem.dayOfWeek
            }

            override fun areContentsTheSame(
                oldItem: OpenCloseInfo,
                newItem: OpenCloseInfo
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}