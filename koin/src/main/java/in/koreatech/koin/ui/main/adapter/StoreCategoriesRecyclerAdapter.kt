package `in`.koreatech.koin.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import `in`.koreatech.koin.databinding.MainItemStoreBinding
import `in`.koreatech.koin.domain.model.store.StoreCategories

class StoreCategoriesRecyclerAdapter(): ListAdapter<StoreCategories,StoreCategoriesRecyclerAdapter.StoreCategoriesViewHolder>(
diffCallback
){
    var onItemClickListener: OnItemClickListener? = null

    inner class StoreCategoriesViewHolder(val binding: MainItemStoreBinding) : RecyclerView.ViewHolder(binding.root){
        val container = binding.container
        val storeCategoryImage = binding.imageViewStoreCategory
        val storeCategoryName = binding.textViewStoreCategory
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreCategoriesRecyclerAdapter.StoreCategoriesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MainItemStoreBinding.inflate(inflater, parent, false)
        return StoreCategoriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoreCategoriesRecyclerAdapter.StoreCategoriesViewHolder, position: Int) {
        val event = getItem(position)

        with(holder){
            container.setOnClickListener {
                onItemClickListener?.onItemClick(event.id)
            }

            Glide.with(storeCategoryImage)
                .load(event.imageUrl)
                .override(100, 100)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
                .into(storeCategoryImage)

            storeCategoryName.text = event.name
        }
    }

    interface OnItemClickListener {
        fun onItemClick(id: Int)
    }

    inline fun setOnItemClickListener(crossinline onItemClick: (Id: Int) -> Unit) {
        onItemClickListener = object : StoreCategoriesRecyclerAdapter.OnItemClickListener {
            override fun onItemClick(id: Int) {
                onItemClick(id)
            }
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<StoreCategories>() {
            override fun areItemsTheSame(oldItem: StoreCategories, newItem: StoreCategories): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: StoreCategories, newItem: StoreCategories): Boolean {
                return oldItem == newItem
            }
        }
    }
}