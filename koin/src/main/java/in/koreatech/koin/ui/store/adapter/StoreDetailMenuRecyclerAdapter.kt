package `in`.koreatech.koin.ui.store.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import `in`.koreatech.koin.R
import `in`.koreatech.koin.databinding.StoreDetailMenuListItemBinding
import `in`.koreatech.koin.domain.model.store.ShopMenus

class StoreDetailMenuRecyclerAdapter :
    ListAdapter<ShopMenus, StoreDetailMenuRecyclerAdapter.ViewHolder>(diffCallback) {

    class ViewHolder(private val binding: StoreDetailMenuListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(shopMenu: ShopMenus) {
            binding.storeDetailMenuNameTextview.text = shopMenu.name
            when {
                shopMenu.isSingle && shopMenu.singlePrice != null -> {
                    binding.storeDetailMenuPriceTextview.text = binding.root.context.getString(
                        R.string.store_delivery_price,
                        shopMenu.singlePrice
                    )
                }

                shopMenu.optionPrices?.isNotEmpty() == true -> {
                    val menus = shopMenu.optionPrices?.fold("") { acc, menu ->
                        acc + "${getOptionPriceText(menu)}\n"
                    }?.trim()

                    binding.storeDetailMenuPriceTextview.text = menus
                }
            }
        }

        private fun getOptionPriceText(menu: ShopMenus.ShopMenuOptions) = when {
            menu.price != null -> {
                val priceText = binding.root.context.getString(
                    R.string.store_delivery_price,
                    menu.price
                )
                "${menu.option} - $priceText"
            }

            else -> menu.option
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
        private val diffCallback = object : DiffUtil.ItemCallback<ShopMenus>() {
            override fun areItemsTheSame(oldItem: ShopMenus, newItem: ShopMenus): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ShopMenus, newItem: ShopMenus): Boolean {
                return oldItem == newItem
            }
        }
    }
}