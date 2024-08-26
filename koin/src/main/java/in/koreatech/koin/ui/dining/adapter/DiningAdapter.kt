package `in`.koreatech.koin.ui.dining.adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.constant.AnalyticsConstant
import `in`.koreatech.koin.core.dialog.AlertModalDialog
import `in`.koreatech.koin.core.dialog.AlertModalDialogData
import `in`.koreatech.koin.core.dialog.ImageZoomableDialog
import `in`.koreatech.koin.databinding.ItemDiningBinding
import `in`.koreatech.koin.domain.constant.BREAKFAST
import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.domain.model.dining.LikeActionType
import `in`.koreatech.koin.domain.model.dining.DiningPlace
import `in`.koreatech.koin.domain.util.DiningUtil
import `in`.koreatech.koin.ui.dining.DiningActivity
import `in`.koreatech.koin.ui.login.LoginActivity
import `in`.koreatech.koin.util.ext.toStringWithComma
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DiningAdapter(
    private val onLikeClickResult: suspend (Dining) -> LikeActionType,
    private val onShareClick: (Dining) -> Unit,
    private val coroutineScope: CoroutineScope
) : ListAdapter<Dining, RecyclerView.ViewHolder>(diffCallback) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as DiningViewHolder).bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DiningViewHolder(ItemDiningBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    inner class DiningViewHolder(private val binding: ItemDiningBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(dining: Dining) {
            with(binding) {
                val context = root.context

                setDiningImageVisibility(context, dining)
                setDiningData(context, dining)
                setEmptyDataVisibility(dining)
                initShareAction(dining)
                initLikeAction(dining)

                if(dining.imageUrl.isNotEmpty()) {
                    lottieImageLoading.visibility = View.VISIBLE
                    cardViewDining.strokeWidth = 0
                    textViewNoPhoto.visibility = View.INVISIBLE
                    imageViewNoPhoto.visibility = View.INVISIBLE
                    imageViewDining.visibility = View.VISIBLE
                    Glide.with(context)
                        .load(dining.imageUrl)
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding.lottieImageLoading.visibility = View.GONE
                                return false
                            }
                        })
                        .into(imageViewDining)

                    val dialog = ImageZoomableDialog(context, dining.imageUrl)
                    dialog.initialScale = 0.75f
                    cardViewDining.setOnClickListener {
                        dialog.show()
                        EventLogger.logClickEvent(
                            AnalyticsConstant.Domain.CAMPUS,
                            AnalyticsConstant.Label.MENU_IMAGE,
                            DiningUtil.getKoreanName(dining.type) + "_" + dining.place
                        )
                    }
                } else {
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
            with(dining) {
                // 능수관, 2캠퍼스일 때 이미지 카드 노출 X
                if (place == DiningPlace.Nungsu.place || place == DiningPlace.Campus2.place) {
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

        private fun initLikeAction(dining: Dining) {
            binding.linearLayoutLike.setOnClickListener {
                coroutineScope.launch {
                    withContext(Dispatchers.Main) {
                        val result = onLikeClickResult(dining)
                        when(result) {
                            LikeActionType.LIKE -> {
                                dining.isLiked = true
                                ++dining.likes
                                setLikeUI(dining)
                            }

                            LikeActionType.UNLIKE -> {
                                dining.isLiked = false
                                --dining.likes
                                setLikeUI(dining)
                            }

                            LikeActionType.LOGIN_REQUIRED -> {
                                val dialog =
                                    AlertModalDialog(binding.root.context, AlertModalDialogData(
                                        R.string.recommend_login_to_like_dining,
                                        R.string.recommend_like_dining,
                                        R.string.action_login,
                                    ),
                                        onPositiveButtonClicked = {
                                            val intent = Intent(
                                                binding.root.context,
                                                LoginActivity::class.java
                                            )
                                            intent.putExtra("activity", DiningActivity::class.java)
                                            binding.root.context.startActivity(intent)
                                        },
                                        onNegativeButtonClicked = {
                                            it.dismiss()
                                        }
                                    )
                                dialog.show()
                            }
                        }
                    }
                }
            }
        }

        private fun initShareAction(dining: Dining) {
            binding.linearLayoutShare.setOnClickListener {
                onShareClick(dining)
            }
        }

        private fun setLikeUI(dining: Dining) {
            with(binding) {
                if (dining.likes > 0) {
                    textViewLike.visibility = View.INVISIBLE
                    textViewLikeCount.visibility = View.VISIBLE
                    textViewLikeCount.text = dining.likes.toStringWithComma()
                } else {
                    textViewLike.visibility = View.VISIBLE
                    textViewLikeCount.visibility = View.INVISIBLE
                }

                if(dining.isLiked) {
                    imageViewLike.setImageResource(R.drawable.ic_like_filled)
                } else {
                    imageViewLike.setImageResource(R.drawable.ic_like)
                }
            }
        }

        private fun setDiningImageVisibility(context: Context, dining: Dining) {
            when(dining.place) {
                context.getString(R.string.dining_nungsu),
                context.getString(R.string.dining_2campus) -> binding.cardViewDining.visibility = View.GONE
                else -> binding.cardViewDining.visibility = View.VISIBLE
            }
        }

        private fun showDiningImage(context: Context, dining: Dining){
            with(binding) {
                cardViewDining.visibility = View.VISIBLE
                cardViewDining.strokeWidth = 0
                textViewNoPhoto.visibility = View.INVISIBLE
                imageViewNoPhoto.visibility = View.INVISIBLE
                imageViewDining.visibility = View.VISIBLE

                Glide.with(context)
                    .load(dining.imageUrl)
                    .into(imageViewDining)

                // 이미지 클릭시 dialog 형태로 노출
                val dialog = ImageZoomableDialog(context, dining.imageUrl)
                dialog.initialScale = 0.75f
                cardViewDining.setOnClickListener {
                    dialog.show()
                    EventLogger.logClickEvent(
                        EventAction.CAMPUS,
                        AnalyticsConstant.Label.MENU_IMAGE,
                        DiningUtil.getKoreanName(dining.type) + "_" + dining.place
                    )
                }
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
                        EventAction.CAMPUS,
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
        private fun setDiningData(context: Context, dining: Dining) {
            with(binding) {
                textViewDiningCorner.text = dining.place
                textViewKcal.text =
                    context.getString(R.string.dining_kcal, dining.kcal)
                textViewCashPrice.text =
                    context.getString(R.string.price, dining.priceCash)
                textViewCardPrice.text =
                    context.getString(R.string.price, dining.priceCard)
                textViewDiningMenuItems1.text = dining.menu.subList(0, dining.menu.size / 2).joinToString("\n")
                textViewDiningMenuItems2.text = dining.menu.subList(dining.menu.size / 2, dining.menu.size).joinToString("\n")
                setLikeUI(dining)
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