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
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DiningDateAdapter(
    private val onClick: (Date) -> Unit
) : ListAdapter<Date, RecyclerView.ViewHolder>(diffCallback) {

    private var selectedPosition = 0

    fun setSelectedPosition(position: Int) {
        selectedPosition = position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DiningDateViewHolder(
            ItemDiningDateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as DiningDateViewHolder).bind(getItem(position), position)
    }

    inner class DiningDateViewHolder(val binding: ItemDiningDateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(date: Date, position: Int) {

            with(binding) {
                val calendar = Calendar.getInstance()
                calendar.time = date
                textViewDayOfTheWeek.text =
                    calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.KOREAN)
                        ?.toString() ?: ""
                textViewDay.text = date.date.toString()

                if (position < selectedPosition) {
                    textViewDay.setTextColor(Color.parseColor("#8E8E8E"))
                    textViewDayOfTheWeek.setTextColor(Color.parseColor("#8E8E8E"))
                } else {
                    textViewDay.setTextColor(Color.BLACK)
                    textViewDayOfTheWeek.setTextColor(Color.parseColor("#4B4B4B"))
                }

                backgroundSelectedDate.visibility = View.INVISIBLE
                backgroundSelectedDateToday.visibility = View.INVISIBLE
                if (position == selectedPosition) {
                    if (DateUtils.isToday(date.time)) {
                        textViewDay.setTextColor(Color.WHITE)
                        backgroundSelectedDateToday.visibility = View.VISIBLE
                    } else {
                        textViewDay.setTextColor(
                            ContextCompat.getColor(
                                root.context,
                                R.color.colorPrimary
                            )
                        )
                        backgroundSelectedDate.visibility = View.VISIBLE
                    }
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
                    selectedPosition = position
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
