package `in`.koreatech.koin.ui.dining.adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import `in`.koreatech.koin.R
import `in`.koreatech.koin.databinding.ItemDiningBinding
import `in`.koreatech.koin.domain.model.dining.Dining

class DiningAdapter : ListAdapter<Dining, RecyclerView.ViewHolder>(diffCallback) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as DiningViewHolder).bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DiningViewHolder(ItemDiningBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    class DiningViewHolder(private val binding: ItemDiningBinding) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(dining: Dining) {
            with(binding) {
                val context = root.context
                textViewDiningCorner.text = dining.place
                textViewKcal.text =
                    context.getString(R.string.dining_kcal, dining.kcal)
                textViewCashPrice.text =
                    context.getString(R.string.price, dining.priceCash)
                textViewCardPrice.text =
                    context.getString(R.string.price, dining.priceCard)
                textViewDiningMenuItems.text = dining.menu.joinToString("\n")

                if(dining.imageUrl.isNotEmpty()) {
                    Glide.with(context)
                        .load(dining.imageUrl)
                        .into(imageViewDining)

                    val dialog = Dialog(context).apply {
                        setContentView(R.layout.dialog_dining_image)
                        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    }
                    val closeButton = dialog.findViewById<ImageView>(R.id.image_view_close)
                    closeButton.setOnClickListener {
                        dialog.dismiss()
                    }
                    cardViewDining.setOnClickListener {
                        val imageView = dialog.findViewById<ImageView>(R.id.image_view_dining)
                        Glide.with(context)
                            .load(dining.imageUrl)
                            .into(imageView)
                        dialog.show()
                    }
                }

                if(dining.isSoldOut) {
                    groupSoldOut.visibility = View.VISIBLE
                    textViewDiningSoldOut.visibility = View.VISIBLE
                } else {
                    groupSoldOut.visibility = View.INVISIBLE
                    textViewDiningSoldOut.visibility = View.INVISIBLE
                }

                if(dining.isChanged) {
                    textViewDiningChanged.visibility = View.VISIBLE
                } else {
                    textViewDiningChanged.visibility = View.INVISIBLE
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