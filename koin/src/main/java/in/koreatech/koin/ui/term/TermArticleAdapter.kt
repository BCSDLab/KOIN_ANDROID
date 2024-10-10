package `in`.koreatech.koin.ui.term

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import `in`.koreatech.koin.databinding.ItemTermArticleBinding

class TermArticleAdapter(
    private val onClickArticle: (Int) -> Unit
) : ListAdapter<String, TermArticleAdapter.TermArticleViewHolder>(diffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TermArticleViewHolder {
        return TermArticleViewHolder(ItemTermArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false), onClickArticle)
    }

    override fun onBindViewHolder(holder: TermArticleViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    inner class TermArticleViewHolder(
        private val binding: ItemTermArticleBinding,
        private val onClickArticle: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(title: String, position: Int) {
            binding.root.setOnClickListener { onClickArticle(position) }
            binding.tvArticleTitle.text = title
        }
    }


    private companion object {
        val diffCallback = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(
                oldItem: String,
                newItem: String
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: String,
                newItem: String
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}