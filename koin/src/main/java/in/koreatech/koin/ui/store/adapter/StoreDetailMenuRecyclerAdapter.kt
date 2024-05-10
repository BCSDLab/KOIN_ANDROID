package `in`.koreatech.koin.ui.store.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import `in`.koreatech.koin.R
import `in`.koreatech.koin.databinding.StoreDetailMenuHeaderBinding
import `in`.koreatech.koin.databinding.StoreDetailMenuListItemBinding
import `in`.koreatech.koin.domain.model.store.ShopMenus

class StoreDetailMenuRecyclerAdapter :
    ListAdapter<ShopMenus, RecyclerView.ViewHolder>(diffCallback) {
    private var category: String? = null
    override fun getItemViewType(position: Int) = when (position) {
        0 -> HEADER
        else -> ITEM
    }

    class HeaderViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setCategory(category: String?) {
            (binding as StoreDetailMenuHeaderBinding).textViewHeaderTitle.text = category
            binding.imageViewHeaderIcon.setBackgroundResource(

                when (category) {
                    "추천 메뉴" -> R.drawable.ic_recommend
                    "메인 메뉴" -> R.drawable.ic_represent
                    "세트 메뉴" -> R.drawable.ic_set
                    "사이드 메뉴" -> R.drawable.ic_side
                    else -> R.drawable.ic_represent
                }
            )
        }

    }

    class ItemViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(shopMenu: ShopMenus) {
            (binding as StoreDetailMenuListItemBinding).storeDetailMenuNameTextview.text =
                shopMenu.name

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
            Glide.with(binding.root.context)
                .load(shopMenu.imageUrls?.getOrNull(0) ?: R.drawable.no_image)
                .into(binding.storeDetailMenuImageview)
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

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            HEADER -> HeaderViewHolder(
                StoreDetailMenuHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            ITEM -> ItemViewHolder(
                StoreDetailMenuListItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            else -> throw IllegalArgumentException("Wrong viewtype")
        }
    }

    fun setCategory(data: String?) {
        category = data
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ITEM -> (holder as ItemViewHolder).bind(getItem(position))
            HEADER -> (holder as HeaderViewHolder).setCategory(category)
        }
    }

    override fun submitList(list: List<ShopMenus>?) {
        super.submitList(
            if (!list.isNullOrEmpty()) {
                mutableListOf<ShopMenus>().apply {
                    add(list[0])
                    addAll(list)
                }
            } else null
        )
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

        private const val ITEM = 1000
        private const val HEADER = 2000

    }
}
