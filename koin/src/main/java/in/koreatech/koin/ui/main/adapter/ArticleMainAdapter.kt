package `in`.koreatech.koin.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import `in`.koreatech.koin.databinding.MainCardArticleBinding
import `in`.koreatech.koin.databinding.MainCardArticleNotiBinding
import `in`.koreatech.koin.ui.main.state.ArticleMainState

class ArticleMainAdapter(
    private val onNotiClick: () -> Unit,
    private val onArticleClick: (ArticleMainState.Content) -> Unit
) :
    ListAdapter<ArticleMainState, RecyclerView.ViewHolder>(diffCallback) {

    inner class KeywordNotiViewHolder(
        private val binding: MainCardArticleNotiBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(content: ArticleMainState.Noti) {
            binding.textArticleNotiTitle.text = content.title
            binding.textArticleNotiSub.text = content.sub
            binding.cardViewArticleNoti.setOnClickListener { onNotiClick() }
        }
    }

    inner class HotArticleViewHolder(
        private val binding: MainCardArticleBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(content: ArticleMainState.Content) {
            binding.textArticleTitle.text = content.title
            binding.cardViewArticleHeader.setOnClickListener { onArticleClick(content) }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ArticleMainState.Noti -> TYPE_NOTI
            is ArticleMainState.Content -> TYPE_ARTICLE
            else -> throw IllegalArgumentException("Invalid type of data - $position")
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_NOTI -> {
                KeywordNotiViewHolder(
                    MainCardArticleNotiBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            TYPE_ARTICLE -> {
                HotArticleViewHolder(
                    MainCardArticleBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> throw IllegalArgumentException("Invalid type of view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is ArticleMainState.Noti -> (holder as KeywordNotiViewHolder).bind(item)
            is ArticleMainState.Content -> (holder as HotArticleViewHolder).bind(item)
        }
    }

    companion object {
        const val TYPE_NOTI = 0
        const val TYPE_ARTICLE = 1

        private val diffCallback = object : DiffUtil.ItemCallback<ArticleMainState>() {
            override fun areItemsTheSame(
                oldItem: ArticleMainState,
                newItem: ArticleMainState
            ): Boolean {
                return when {
                    oldItem is ArticleMainState.Noti && newItem is ArticleMainState.Noti -> oldItem.title == newItem.title
                    oldItem is ArticleMainState.Content && newItem is ArticleMainState.Content -> oldItem.id == newItem.id
                    else -> false
                }
            }

            override fun areContentsTheSame(
                oldItem: ArticleMainState,
                newItem: ArticleMainState
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}