package `in`.koreatech.koin.ui.store.adapter.review

import android.graphics.Outline
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import `in`.koreatech.koin.R
import `in`.koreatech.koin.databinding.ItemImageBinding
import `in`.koreatech.koin.databinding.ItemStoreDetailReviewBinding
import `in`.koreatech.koin.domain.model.store.StoreReviewContent

class StoreDetailReviewImageRecyclerAdapter ():
    ListAdapter<String, StoreDetailReviewImageRecyclerAdapter.StoreDetailReviewViewHolder>(
        diffCallback
    ){

    var onItemClickListener: OnItemClickListener? = null
    var selectPosition: Int? = null
    var isDoubleClick: Boolean = false

    inner class StoreDetailReviewViewHolder(val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root){
        val imageView = binding.imageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):StoreDetailReviewImageRecyclerAdapter.StoreDetailReviewViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemImageBinding.inflate(inflater, parent, false)
        return StoreDetailReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder:StoreDetailReviewImageRecyclerAdapter.StoreDetailReviewViewHolder, position: Int) {
        val imageUri = getItem(position)

        with(holder){


            imageView.outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View, outline: Outline) {
                    outline.setRoundRect(0, 0, view.width, view.height, 20f)
                }
            }
            imageView.clipToOutline = true

            Glide.with(imageView)
                .load(imageUri)
                .override(200, 200)
                .error(R.drawable.image_no_image)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(10)))
                .into(imageView)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(id: Int)
    }

    inline fun setOnItemClickListener(crossinline onItemClick: (Id: Int) -> Unit) {
        onItemClickListener = object : StoreDetailReviewImageRecyclerAdapter.OnItemClickListener {
            override fun onItemClick(id: Int) {
                onItemClick(id)
            }
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem:String): Boolean {
                return oldItem == newItem
            }
        }
    }
}