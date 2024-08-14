package `in`.koreatech.koin.ui.store.adapter.review

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import `in`.koreatech.koin.R
import `in`.koreatech.koin.databinding.ItemStoreDetailReviewBinding
import `in`.koreatech.koin.domain.model.store.StoreReviewContent
import `in`.koreatech.koin.ui.store.activity.StoreReviewReportActivity
import `in`.koreatech.koin.ui.store.adapter.review.menu.ReviewPopupMenu
import `in`.koreatech.koin.ui.store.adapter.review.menu.ReviewReportPopupMenu


class StoreDetailReviewRecyclerAdapter (
    val onModifyItem: (StoreReviewContent) -> Unit,
    val onDeleteItem: (Int) -> Unit,
    val onReportItem: (StoreReviewContent) -> Unit
):
    ListAdapter<StoreReviewContent, StoreDetailReviewRecyclerAdapter.StoreDetailReviewViewHolder>(
        diffCallback
    ) {

    var onItemClickListener: OnItemClickListener? = null
    var selectPosition: Int? = null
    var isDoubleClick: Boolean = false
    var storeId: Int? = null

    inner class StoreDetailReviewViewHolder(val binding: ItemStoreDetailReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val isMineIcon = binding.iconMyReview
        val nickName = binding.userIdTextview
        val rating = binding.userRatingBar
        val updateAt = binding.reviewUpdateDataTextview
        val reviewContent = binding.reviewContentTextview
        val reviewImageRecyclerView = binding.reviewImageRecyclerview
        val reviewMenuRecyclerview = binding.reviewMenuRecyclerview
        val iconKebab = binding.iconKebab
        val container = binding.storeReviewContainer
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StoreDetailReviewRecyclerAdapter.StoreDetailReviewViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemStoreDetailReviewBinding.inflate(inflater, parent, false)
        return StoreDetailReviewViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: StoreDetailReviewRecyclerAdapter.StoreDetailReviewViewHolder,
        position: Int
    ) {
        val review = getItem(position)

        with(holder) {

            if (review.isMine) {
                isMineIcon.isVisible = true

                container.background = ColorDrawable(ContextCompat.getColor(itemView.context, R.color.my_review))

                iconKebab.setOnClickListener {

                    iconKebab.setImageResource(R.drawable.ic_kebab_clicked)

                    val popupMenu = ReviewPopupMenu(holder.itemView.context,
                        onModify = {
                            onModifyItem(review)
                        },
                        onDelete = {
                            onDeleteItem(review.reviewId)
                        }
                    )
                    popupMenu.show(it)

                    popupMenu.setOnDismissListener {
                        iconKebab.setImageResource(R.drawable.ic_kebab)
                    }
                }
            } else {
                iconKebab.setOnClickListener {
                    iconKebab.setImageResource(R.drawable.ic_kebab_clicked)

                    val popupMenu = ReviewReportPopupMenu(holder.itemView.context,
                        onReport = {
                            onReportItem(review)
                        }
                    )
                    popupMenu.show(it)

                    popupMenu.setOnDismissListener {
                        iconKebab.setImageResource(R.drawable.ic_kebab)
                    }
                }
            }

            val storeDetailReviewImageRecyclerAdapter = StoreDetailReviewImageRecyclerAdapter()
            storeDetailReviewImageRecyclerAdapter.submitList(review.imageUrls)

            val storeDetailReviewMenuRecyclerAdapter = StoreDetailReviewMenuRecyclerAdapter()
            storeDetailReviewMenuRecyclerAdapter.submitList(review.menuNames)

            nickName.text = review.nickName
            rating.rating = review.rating.toFloat()
            updateAt.text = if(review.isModified) String.format(ContextCompat.getString(itemView.context, R.string.store_review_is_modify),review.createdAt.replace("-", "."))
                            else review.createdAt.replace("-", ".")
            reviewContent.text = review.content

            reviewImageRecyclerView.apply {
                layoutManager = LinearLayoutManager(
                    reviewImageRecyclerView.context,
                    RecyclerView.HORIZONTAL,
                    false
                )
                adapter = storeDetailReviewImageRecyclerAdapter
            }

            reviewMenuRecyclerview.apply {
                layoutManager = LinearLayoutManager(
                    reviewImageRecyclerView.context,
                    RecyclerView.HORIZONTAL,
                    false
                )
                adapter = storeDetailReviewMenuRecyclerAdapter
            }

        }
    }

    interface OnItemClickListener {
        fun onItemClick(id: Int)
    }

    inline fun setOnItemClickListener(crossinline onItemClick: (Id: Int) -> Unit) {
        onItemClickListener = object : StoreDetailReviewRecyclerAdapter.OnItemClickListener {
            override fun onItemClick(id: Int) {
                onItemClick(id)
            }
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<StoreReviewContent>() {
            override fun areItemsTheSame(
                oldItem: StoreReviewContent,
                newItem: StoreReviewContent
            ): Boolean {
                return oldItem.nickName == newItem.nickName
            }

            override fun areContentsTheSame(
                oldItem: StoreReviewContent,
                newItem: StoreReviewContent
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}