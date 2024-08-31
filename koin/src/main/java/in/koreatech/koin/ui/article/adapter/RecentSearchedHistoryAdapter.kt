package `in`.koreatech.koin.ui.article.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import `in`.koreatech.koin.databinding.ItemRecentSearchedHistoryBinding

class RecentSearchedHistoryAdapter(
    private val onKeywordClicked: (String) -> Unit,
    private val onDeleteClicked: (String) -> Unit
) : ListAdapter<String, RecyclerView.ViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentSearchedKeywordViewHolder {
        val binding = ItemRecentSearchedHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecentSearchedKeywordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as RecentSearchedKeywordViewHolder).bind(getItem(position))
    }

    inner class RecentSearchedKeywordViewHolder(
        private val binding: ItemRecentSearchedHistoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(keyword: String) {
            binding.apply {
                textViewKeyword.text = keyword
                textViewKeyword.setOnClickListener { onKeywordClicked(keyword) }
                imageViewDelete.setOnClickListener { onDeleteClicked(keyword) }
            }
            binding.executePendingBindings()
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }
}
