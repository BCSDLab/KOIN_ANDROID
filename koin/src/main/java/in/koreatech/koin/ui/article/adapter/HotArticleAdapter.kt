package `in`.koreatech.koin.ui.article.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import `in`.koreatech.koin.databinding.ItemHotArticleHeaderBinding
import `in`.koreatech.koin.ui.article.state.ArticleHeaderState

class HotArticleAdapter(
    private val onClick: (ArticleHeaderState) -> Unit
) : ListAdapter<ArticleHeaderState, RecyclerView.ViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotArticleViewHolder {
        val binding = ItemHotArticleHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HotArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as HotArticleViewHolder).bind(getItem(position))
    }

    inner class HotArticleViewHolder(
        private val binding: ItemHotArticleHeaderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(article: ArticleHeaderState) {
            binding.apply {
                textViewArticleBoardName.text = root.context.getString(article.board.koreanName)
                textViewArticleTitle.text = article.title.trim()
                root.setOnClickListener {
                    onClick(article)
                }
            }
            binding.executePendingBindings()
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<ArticleHeaderState>() {
            override fun areItemsTheSame(oldItem: ArticleHeaderState, newItem: ArticleHeaderState): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ArticleHeaderState, newItem: ArticleHeaderState): Boolean {
                return oldItem == newItem
            }
        }
    }
}
