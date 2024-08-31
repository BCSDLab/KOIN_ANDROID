package `in`.koreatech.koin.ui.article.adapter

import android.text.TextUtils
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

        fun bind(articleHeader: ArticleHeaderState) {
            binding.apply {
                articleHeader.boardName?.let {
                    textViewArticleBoardName.text = root.context.getString(it)
                }
                textViewArticleTitle.text = articleHeader.title.trim()
                textViewArticleAuthor.text = articleHeader.author
                try {
                    textViewArticleDate.text = TextUtils.concat(
                        DateFormatUtil.getSimpleMonthAndDay(articleHeader.registeredAt),
                        " ",
                        DateFormatUtil.getDayOfWeek(TimeUtil.stringToDateYYYYMMDD(articleHeader.registeredAt))
                    )
                } catch(e: Exception) { }
                textViewArticleViewCount.text = articleHeader.viewCount.toString()
                root.setOnClickListener { onClick(articleHeader) }
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
