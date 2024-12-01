package `in`.koreatech.koin.ui.store.adapter.search


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import `in`.koreatech.koin.R
import `in`.koreatech.koin.databinding.ItemStoreSearchBinding
import `in`.koreatech.koin.domain.model.store.ShopSearchRelated

class SearchRelatedRecyclerAdapter(val onItemClick: (Int) -> Unit) :
    ListAdapter<ShopSearchRelated, ViewHolder>(diffCallback) {

    inner class ViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(shopSearchRelated: ShopSearchRelated, onItemClick: (Int) -> Unit) {
            with(binding as ItemStoreSearchBinding) {
                if (shopSearchRelated.shopId == null) {
                    Glide.with(itemIcon)
                        .load(R.drawable.ic_search_related_menu)
                        .into(itemIcon)
                    itemView.setOnClickListener {
                        onItemClick(currentList[bindingAdapterPosition].shopIds[0])
                    }
                    itemArrow.visibility = RecyclerView.GONE
                } else {
                    Glide.with(itemIcon)
                        .load(R.drawable.ic_store_search)
                        .into(itemIcon)
                    itemView.setOnClickListener {
                        onItemClick(currentList[bindingAdapterPosition].shopId ?: 0)
                    }
                    itemArrow.visibility = RecyclerView.VISIBLE
                }
                searchTv.text = shopSearchRelated.keyword
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemStoreSearchBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        (holder as ViewHolder).bind(getItem(position), onItemClick)
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<ShopSearchRelated>() {
            override fun areItemsTheSame(
                oldItem: ShopSearchRelated,
                newItem: ShopSearchRelated
            ): Boolean {
                return oldItem.shopId == newItem.shopId
            }

            override fun areContentsTheSame(
                oldItem: ShopSearchRelated,
                newItem: ShopSearchRelated
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
