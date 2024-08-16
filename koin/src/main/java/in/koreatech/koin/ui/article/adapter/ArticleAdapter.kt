package `in`.koreatech.koin.ui.article.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import `in`.koreatech.koin.databinding.ItemArticleHeaderBinding
import `in`.koreatech.koin.domain.util.DateFormatUtil
import `in`.koreatech.koin.domain.util.TimeUtil
import `in`.koreatech.koin.ui.article.state.ArticleHeaderState

class ArticleAdapter(
    private val onClick: (ArticleHeaderState) -> Unit
) : ListAdapter<ArticleHeaderState, RecyclerView.ViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticleHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ArticleViewHolder).bind(getItem(position))
    }

    inner class ArticleViewHolder(
        private val binding: ItemArticleHeaderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(article: ArticleHeaderState) {
            binding.apply {
                textViewArticleBoardName.text = root.context.getString(article.boardName)
                textViewArticleTitle.text = article.title.trim()
                textViewArticleAuthor.text = article.author
                textViewArticleDate.text = DateFormatUtil.getSimpleMonthAndDay(article.createdAt) +
                        " " + DateFormatUtil.getDayOfWeek(TimeUtil.stringToDateYYYYMMDD(article.createdAt))
                textViewArticleViewCount.text = article.viewCount.toString()
                root.setOnClickListener { onClick(article) }
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
