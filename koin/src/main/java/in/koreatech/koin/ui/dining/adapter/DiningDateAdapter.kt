package `in`.koreatech.koin.ui.dining.adapter

import android.graphics.Color
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import `in`.koreatech.koin.R
import `in`.koreatech.koin.databinding.ItemDiningDateBinding
import `in`.koreatech.koin.domain.util.DateFormatUtil
import java.util.Date

class DiningDateAdapter(
    private val onClick: (Date) -> Unit
) : ListAdapter<Date, RecyclerView.ViewHolder>(diffCallback) {

    private var selectedPosition = 0

    fun selectPosition(position: Int) {
        selectedPosition = position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val holder = DiningDateViewHolder(
            ItemDiningDateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

        val displayMetrics = parent.context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val itemWidth = screenWidth / 7

        val layoutParams = holder.itemView.layoutParams
        layoutParams.width = itemWidth
        holder.itemView.layoutParams = layoutParams

        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as DiningDateViewHolder).bind(getItem(position), position)
    }

    inner class DiningDateViewHolder(val binding: ItemDiningDateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(date: Date, position: Int) {
            with(binding) {
                val context = root.context
                textViewDayOfTheWeek.text = DateFormatUtil.getDayOfWeek(date)
                textViewDay.text = date.date.toString()

                groupTodayIndicator.visibility = View.INVISIBLE
                if (position < itemCount / 2) {     // 오늘 이전
                    textViewDay.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.gray9
                        )
                    )
                    textViewDayOfTheWeek.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.gray9
                        )
                    )
                } else if (position > itemCount / 2) {  // 오늘 이후
                    textViewDay.setTextColor(Color.BLACK)
                    textViewDayOfTheWeek.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.gray14
                        )
                    )
                } else {    // 오늘
                    textViewDay.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.colorPrimary
                        )
                    )
                    textViewDayOfTheWeek.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.gray9
                        )
                    )
                    groupTodayIndicator.visibility = View.VISIBLE
                }

                backgroundSelectedDate.visibility = View.INVISIBLE
                if (position == selectedPosition) {
                    backgroundSelectedDate.visibility = View.VISIBLE
                    textViewDay.setTextColor(Color.WHITE)
                }

                root.setOnClickListener {
                    onClick(date)
                    if (selectedPosition < position)
                        notifyItemRangeChanged(
                            selectedPosition,
                            position - selectedPosition + 1
                        )
                    else
                        notifyItemRangeChanged(position, selectedPosition - position + 1)
                    selectPosition(position)
                }
            }
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Date>() {
            override fun areItemsTheSame(
                oldItem: Date,
                newItem: Date
            ): Boolean {
                return oldItem.time == newItem.time
            }

            override fun areContentsTheSame(
                oldItem: Date,
                newItem: Date
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
