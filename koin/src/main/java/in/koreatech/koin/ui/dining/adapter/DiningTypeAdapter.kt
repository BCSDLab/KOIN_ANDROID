package `in`.koreatech.koin.ui.dining.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import `in`.koreatech.koin.databinding.ItemDiningTypeBinding
import `in`.koreatech.koin.domain.model.dining.Dining

class DiningTypeAdapter : ListAdapter<Dining, RecyclerView.ViewHolder>(diffCallback) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as DiningViewHolder).bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DiningViewHolder(ItemDiningTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    class DiningViewHolder(private val binding: ItemDiningTypeBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(dining: Dining) {
            with(binding) {
                textViewDiningCorner.text = dining.place
                textViewKcal.text = dining.kcal + "kcal"
                textViewCashPrice.text = dining.priceCash + "원"
                textViewCardPrice.text = dining.priceCard + "원"
                textViewDiningMenuItems.text = dining.menu.joinToString("\n")

                if(dining.imageUrl.isNotEmpty()) {
                    Glide.with(root.context)
                        .load(dining.imageUrl)
                        .into(imageViewDining)
                }

                if(dining.isSoldOut) {
                    groupSoldOut.visibility = android.view.View.VISIBLE
                } else {
                    groupSoldOut.visibility = android.view.View.INVISIBLE
                }
            }
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Dining>() {
            override fun areItemsTheSame(
                oldItem: Dining,
                newItem: Dining
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Dining,
                newItem: Dining
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}