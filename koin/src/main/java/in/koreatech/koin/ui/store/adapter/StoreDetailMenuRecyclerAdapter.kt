package `in`.koreatech.koin.ui.store.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import `in`.koreatech.koin.R
import `in`.koreatech.koin.databinding.StoreDetailMenuListItemBinding
import `in`.koreatech.koin.domain.model.store.StoreMenu
import `in`.koreatech.koin.domain.model.store.StoreMenuPrice

class StoreDetailMenuRecyclerAdapter :
    ListAdapter<StoreMenu, StoreDetailMenuRecyclerAdapter.ViewHolder>(diffCallback) {

    class ViewHolder(private val binding: StoreDetailMenuListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(storeMenu: StoreMenu) {
            binding.storeDetailMenuNameTextview.text = storeMenu.name
            binding.storeDetailMenuPriceTextview.text = generatePriceText(storeMenu.priceType)
        }

        private fun generatePriceText(storeMenuPrices: List<StoreMenuPrice>): String {
            return storeMenuPrices.joinToString("\n") {
                binding.root.context.getString(
                    R.string.store_menu_price_info,
                    it.sizeName,
                    it.price
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            StoreDetailMenuListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<StoreMenu>() {
            override fun areItemsTheSame(oldItem: StoreMenu, newItem: StoreMenu): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: StoreMenu, newItem: StoreMenu): Boolean {
                return oldItem == newItem
            }
        }
    }
}