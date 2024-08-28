package `in`.koreatech.koin.ui.store.adapter.review

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import `in`.koreatech.koin.databinding.ItemStoreDetailReviewMenuBinding

class StoreDetailReviewMenuRecyclerAdapter ():
    ListAdapter<String, StoreDetailReviewMenuRecyclerAdapter.StoreDetailReviewViewHolder>(
        diffCallback
    ){

    inner class StoreDetailReviewViewHolder(val binding: ItemStoreDetailReviewMenuBinding) : RecyclerView.ViewHolder(binding.root){
        val menuName = binding.storeReviewMenuTextview
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):StoreDetailReviewMenuRecyclerAdapter.StoreDetailReviewViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemStoreDetailReviewMenuBinding.inflate(inflater, parent, false)
        return StoreDetailReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder:StoreDetailReviewMenuRecyclerAdapter.StoreDetailReviewViewHolder, position: Int) {
        with(holder){
           menuName.text = getItem(position)
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