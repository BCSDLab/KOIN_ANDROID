package `in`.koreatech.koin.ui.main.adapter

import `in`.koreatech.koin.R
import `in`.koreatech.koin.ui.main.state.DiningTypeUiState
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class DiningTypeAdapter :
    ListAdapter<DiningTypeUiState, DiningTypeAdapter.DiningTypeTextViewHolder>(
        diffCallback
    ) {

    var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiningTypeTextViewHolder {
        return DiningTypeTextViewHolder(parent)
    }

    override fun onBindViewHolder(holder: DiningTypeTextViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener { onItemClickListener?.onItemClick(position) }
    }

    inner class DiningTypeTextViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.main_dining_type_text_view, parent, false)
    ) {
        private val textView = itemView.findViewById<TextView>(R.id.text_view)

        fun bind(diningTypeUiState: DiningTypeUiState) {
            textView.text = diningTypeUiState.name
            textView.isSelected = diningTypeUiState.isSelected
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    inline fun setOnItemClickListener(crossinline onItemClick: (Int) -> Unit) {
        onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                onItemClick(position)
            }
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<DiningTypeUiState>() {
            override fun areItemsTheSame(
                oldItem: DiningTypeUiState,
                newItem: DiningTypeUiState
            ): Boolean = oldItem.name == newItem.name

            override fun areContentsTheSame(
                oldItem: DiningTypeUiState,
                newItem: DiningTypeUiState
            ): Boolean = oldItem == newItem
        }
    }
}