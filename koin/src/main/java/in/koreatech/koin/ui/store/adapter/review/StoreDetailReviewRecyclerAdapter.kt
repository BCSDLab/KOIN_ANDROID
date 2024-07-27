package `in`.koreatech.koin.ui.store.adapter.review

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.databinding.ItemStoreDetailReviewBinding
import `in`.koreatech.koin.domain.model.store.StoreReviewContent
import `in`.koreatech.koin.ui.store.activity.StoreReviewReportActivity


class StoreDetailReviewRecyclerAdapter ():
    ListAdapter<StoreReviewContent, StoreDetailReviewRecyclerAdapter.StoreDetailReviewViewHolder>(
    diffCallback
){

    var onItemClickListener: OnItemClickListener? = null
    var selectPosition: Int? = null
    var isDoubleClick: Boolean = false
    var storeId: Int? = null

    inner class StoreDetailReviewViewHolder(val binding: ItemStoreDetailReviewBinding) : RecyclerView.ViewHolder(binding.root){
        val isMineIcon = binding.iconMyReview
        val nickName = binding.userIdTextview
        val rating = binding.userRatingBar
        val updateAt = binding.reviewUpdateDataTextview
        val reviewContent = binding.reviewContentTextview
        val reviewImageRecyclerView = binding.reviewImageRecyclerview
        val reviewMenuRecyclerview = binding.reviewMenuRecyclerview
        val iconKebab = binding.iconKebab
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreDetailReviewRecyclerAdapter.StoreDetailReviewViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =ItemStoreDetailReviewBinding.inflate(inflater, parent, false)
        return StoreDetailReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoreDetailReviewRecyclerAdapter.StoreDetailReviewViewHolder, position: Int) {
        val review = getItem(position)

        with(holder){

            if(review.isMine) {
                isMineIcon.isVisible = true

                iconKebab.setOnClickListener {

                    val popup = PopupMenu(iconKebab.context, iconKebab)
                    popup.menuInflater.inflate(R.menu.review_mine_kebab_menu, popup.menu)

                    // 메뉴 아이템 클릭 리스너 설정
                    popup.setOnMenuItemClickListener { item: MenuItem ->
                        when (item.itemId) {
                            R.id.action_modify -> {

                                true
                            }
                            R.id.action_delete -> {

                                true
                            }
                            else -> false
                        }
                    }

                    // 팝업 메뉴 표시
                    popup.show()
                }
            }
            else{
                iconKebab.setOnClickListener {

                    val popup = PopupMenu(iconKebab.context, iconKebab)
                    popup.menuInflater.inflate(R.menu.review_other_kebab_menu, popup.menu)

                    popup.setOnMenuItemClickListener { item: MenuItem ->
                        when (item.itemId) {
                            R.id.action_report -> {
                                val context = iconKebab.context
                                val intent = Intent(context, StoreReviewReportActivity::class.java)
                                intent.putExtra("storeId", storeId)
                                intent.putExtra("reviewId", review.reviewId)
                                context.startActivity(intent)
                                true
                            }
                            else -> false
                        }
                    }
                    popup.show()
                }
            }

            val storeDetailReviewImageRecyclerAdapter = StoreDetailReviewImageRecyclerAdapter()
            storeDetailReviewImageRecyclerAdapter.submitList(review.imageUrls)

            val storeDetailReviewMenuRecyclerAdapter = StoreDetailReviewMenuRecyclerAdapter()
            storeDetailReviewMenuRecyclerAdapter.submitList(review.menuNames)

            nickName.text = review.nickName
            rating.rating = review.rating.toFloat()
            updateAt.text = review.createdAt.replace("-", ".")
            reviewContent.text = review.content

            reviewImageRecyclerView.apply {
                layoutManager =  LinearLayoutManager(reviewImageRecyclerView.context, RecyclerView.HORIZONTAL, false)
                adapter = storeDetailReviewImageRecyclerAdapter
            }

            reviewMenuRecyclerview.apply {
                layoutManager =  LinearLayoutManager(reviewImageRecyclerView.context, RecyclerView.HORIZONTAL, false)
                adapter =storeDetailReviewMenuRecyclerAdapter
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
            override fun areItemsTheSame(oldItem: StoreReviewContent, newItem: StoreReviewContent): Boolean {
                return oldItem.nickName == newItem.nickName
            }

            override fun areContentsTheSame(oldItem: StoreReviewContent, newItem: StoreReviewContent): Boolean {
                return oldItem == newItem
            }
        }
    }
}