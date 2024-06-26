package `in`.koreatech.koin.ui.dining.adapter

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.constant.AnalyticsConstant
import `in`.koreatech.koin.core.dialog.ImageZoomableDialog
import `in`.koreatech.koin.databinding.ItemDiningBinding
import `in`.koreatech.koin.domain.constant.BREAKFAST
import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.domain.util.DiningUtil

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

                setDiningCard(context, dining)
                setDiningDataText(context, dining)
                setEmptyDataVisibility(dining)

                if(dining.changedAt.isNotEmpty()) {
                    textViewDiningChanged.visibility = View.VISIBLE
                } else {
                    textViewDiningChanged.visibility = View.INVISIBLE
                }

                if(dining.soldoutAt.isNotEmpty()) {
                    groupSoldOut.visibility = View.VISIBLE
                    textViewDiningSoldOut.visibility = View.VISIBLE
                    textViewDiningChanged.visibility = View.INVISIBLE
                } else {
                    groupSoldOut.visibility = View.INVISIBLE
                    textViewDiningSoldOut.visibility = View.INVISIBLE
                }
            }
        }

        private fun setDiningCard(context: Context, dining: Dining) {
            with (dining) {
                // 능수관, 2캠퍼스일 때 이미지 카드 노출 X
                if (place == context.getString(R.string.dining_nungsu) || place == context.getString(R.string.dining_2campus)) {
                    binding.cardViewDining.visibility = View.GONE
                }
                // 아침 이미지 분기처리
                else if (type == BREAKFAST) {
                    if (imageUrl.isNotEmpty()) showDiningImage(context, dining)
                    else binding.cardViewDining.visibility = View.GONE
                }
                // 점심, 저녁
                else {
                    if (imageUrl.isNotEmpty()) showDiningImage(context, dining)
                    else showEmptyDiningImage(context, dining)
                }
            }
        }

        private fun showDiningImage(context: Context, dining: Dining){
            binding.cardViewDining.visibility = View.VISIBLE
            binding.cardViewDining.strokeWidth = 0
            binding.textViewNoPhoto.visibility = View.INVISIBLE
            binding.imageViewNoPhoto.visibility = View.INVISIBLE
            binding.imageViewDining.visibility = View.VISIBLE

            Glide.with(context)
                .load(dining.imageUrl)
                .into(binding.imageViewDining)
            
            // 이미지 클릭시 dialog 형태로 노출
            val dialog = ImageZoomableDialog(context, dining.imageUrl)
            dialog.initialScale = 0.75f
            binding.cardViewDining.setOnClickListener {
                dialog.show()
                EventLogger.logClickEvent(
                    AnalyticsConstant.Domain.CAMPUS,
                    AnalyticsConstant.Label.MENU_IMAGE,
                    DiningUtil.getKoreanName(dining.type) + "_" + dining.place
                )
            }
        }

        private fun showEmptyDiningImage(context: Context, dining: Dining) {
            with (binding) {
                cardViewDining.visibility = View.VISIBLE
                cardViewDining.strokeWidth =
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, context.resources.displayMetrics).toInt()
                textViewNoPhoto.visibility = View.VISIBLE
                imageViewNoPhoto.visibility = View.VISIBLE
                imageViewDining.visibility = View.INVISIBLE
                cardViewDining.setOnClickListener {
                    EventLogger.logClickEvent(
                        AnalyticsConstant.Domain.CAMPUS,
                        AnalyticsConstant.Label.MENU_IMAGE,
                        DiningUtil.getKoreanName(dining.type) + "_" + dining.place
                    )
                }
            }
        }

        private fun setEmptyDataVisibility(dining: Dining) {
            with(binding) {
                textViewKcal.visibility = View.VISIBLE
                dividerDot.visibility = View.VISIBLE
                textViewCashPrice.visibility = View.VISIBLE
                dividerSlash.visibility = View.VISIBLE
                textViewCardPrice.visibility = View.VISIBLE
                
                if(dining.kcal.isEmpty() || dining.kcal == "0") {
                    textViewKcal.visibility = View.GONE
                    dividerDot.visibility = View.GONE
                }
                if(dining.priceCash.isEmpty() || dining.priceCash == "0") {
                    textViewCashPrice.visibility = View.GONE
                    dividerSlash.visibility = View.GONE
                }
                if(dining.priceCard.isEmpty() || dining.priceCard == "0") {
                    textViewCardPrice.visibility = View.GONE
                    dividerSlash.visibility = View.GONE
                }
                if((dining.priceCard.isEmpty() || dining.priceCard == "0") && (dining.priceCash.isEmpty() || dining.priceCash == "0")) {
                    dividerDot.visibility = View.GONE
                }
            }
        }
        private fun setDiningDataText(context: Context, dining: Dining) {
            with(binding) {
                textViewDiningCorner.text = dining.place
                textViewKcal.text =
                    context.getString(R.string.dining_kcal, dining.kcal)
                textViewCashPrice.text =
                    context.getString(R.string.price, dining.priceCash)
                textViewCardPrice.text =
                    context.getString(R.string.price, dining.priceCard)
                textViewDiningMenuItems.text = dining.menu.joinToString("\n")
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