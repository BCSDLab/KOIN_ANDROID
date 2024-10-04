package `in`.koreatech.koin.ui.term

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import `in`.koreatech.koin.databinding.ItemTermContentBinding
import `in`.koreatech.koin.domain.model.term.TermArticle

class TermContentAdapter: ListAdapter<TermArticle, TermContentAdapter.TermContentViewHolder>(diffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TermContentViewHolder {
        return TermContentViewHolder(ItemTermContentBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: TermContentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TermContentViewHolder(private val binding: ItemTermContentBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(article: TermArticle) {
            with(binding) {
                tvContentTitle.text = article.article
                tvContent.text = article.content.joinToString(separator = "\n")
            }
        }
    }

    private companion object {
        val diffCallback = object : DiffUtil.ItemCallback<TermArticle>() {
            override fun areItemsTheSame(
                oldItem: TermArticle,
                newItem: TermArticle
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: TermArticle,
                newItem: TermArticle
            ): Boolean {
                return oldItem.article == newItem.article
            }
        }
    }
}