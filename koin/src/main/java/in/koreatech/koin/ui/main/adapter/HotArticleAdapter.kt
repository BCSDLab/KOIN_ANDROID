package `in`.koreatech.koin.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import `in`.koreatech.koin.databinding.MainCardArticleBinding
import `in`.koreatech.koin.ui.article.state.ArticleHeaderState

class HotArticleAdapter(
    private val onClick: (ArticleHeaderState) -> Unit
) :
    ListAdapter<ArticleHeaderState, HotArticleAdapter.HotArticleViewHolder>(diffCallback) {

    inner class HotArticleViewHolder(
        private val binding: MainCardArticleBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(articleHeader: ArticleHeaderState) {
            binding.textArticleTitle.text = articleHeader.title
            binding.root.setOnClickListener { onClick(articleHeader) }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HotArticleViewHolder {
        val binding = MainCardArticleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HotArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HotArticleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<ArticleHeaderState>() {
            override fun areItemsTheSame(
                oldItem: ArticleHeaderState,
                newItem: ArticleHeaderState
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ArticleHeaderState,
                newItem: ArticleHeaderState
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}