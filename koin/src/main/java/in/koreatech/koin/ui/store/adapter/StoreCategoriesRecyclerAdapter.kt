package `in`.koreatech.koin.ui.store.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import `in`.koreatech.koin.R
import `in`.koreatech.koin.databinding.MainItemStoreBinding
import `in`.koreatech.koin.domain.model.store.StoreCategories

class StoreCategoriesRecyclerAdapter(): ListAdapter<StoreCategories, StoreCategoriesRecyclerAdapter.StoreCategoriesViewHolder>(
    diffCallback
){

    var onItemClickListener: OnItemClickListener? = null
    var selectPosition: Int? = null
    var isDoubleClick: Boolean = false

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

        val layoutParams = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        if(position > 4){
            layoutParams.width = 450
        }
        else{
            layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
        }

        holder.itemView.layoutParams = layoutParams
        val events = getItem(position)
        with(holder){
            container.setOnClickListener {
                onItemClickListener?.onItemClick(events.id)
                if(selectPosition == position){
                    isDoubleClick = !isDoubleClick
                }
                else{
                    selectPosition = position
                    isDoubleClick = false
                }
                notifyDataSetChanged()
            }

            Glide.with(storeCategoryImage)
                .load(events.imageUrl)
                .override(100, 100)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
                .into(storeCategoryImage)

            storeCategoryName.text = events.name
            storeCategoryName.setTextColor(ContextCompat.getColor(itemView.context, if (selectPosition == position && !isDoubleClick)R.color.colorAccent else R.color.black))
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