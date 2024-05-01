package `in`.koreatech.koin.ui.dining.adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.github.chrisbanes.photoview.PhotoView
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

                setDiningImageVisibility(context, dining)
                setDiningDataText(context, dining)
                setEmptyDataVisibility(dining)

                if(dining.imageUrl.isNotEmpty()) {
                    cardViewDining.strokeWidth = 0
                    textViewNoPhoto.visibility = View.INVISIBLE
                    imageViewNoPhoto.visibility = View.INVISIBLE
                    imageViewDining.visibility = View.VISIBLE
                    Glide.with(context)
                        .load(dining.imageUrl)
                        .into(imageViewDining)

                    val dialog = createZoomableDialog(context)
                    cardViewDining.setOnClickListener {
                        dialog.show()
                        val photoView = dialog.findViewById<PhotoView>(R.id.photo_view_dining)
                        Glide.with(context)
                            .load(dining.imageUrl)
                            .listener(object : RequestListener<Drawable> {
                                override fun onLoadFailed(
                                    e: GlideException?,
                                    model: Any?,
                                    target: Target<Drawable>?,
                                    isFirstResource: Boolean
                                ): Boolean = false

                                override fun onResourceReady(
                                    resource: Drawable?,
                                    model: Any?,
                                    target: Target<Drawable>?,
                                    dataSource: DataSource?,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    photoView.post {
                                        photoView.scale = DIALOG_MIN_SCALE

                                        val closeButton = dialog.findViewById<ImageView>(R.id.close_button_dining)
                                        val lp = closeButton.layoutParams as FrameLayout.LayoutParams
                                        val rectF = photoView.displayRect
                                        lp.setMargins(0, rectF.top.toInt() - closeButton.height - 8, rectF.left.toInt(), 0)
                                        closeButton.layoutParams = lp
                                        closeButton.visibility = View.VISIBLE
                                    }
                                    return false
                                }
                            })
                            .into(photoView)
                    }
                } else {
                    cardViewDining.strokeWidth =
                        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, context.resources.displayMetrics).toInt()
                    textViewNoPhoto.visibility = View.VISIBLE
                    imageViewNoPhoto.visibility = View.VISIBLE
                    imageViewDining.visibility = View.INVISIBLE
                    cardViewDining.setOnClickListener(null)
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

        private fun setDiningImageVisibility(context: Context, dining: Dining) {
            when(dining.place) {
                context.getString(R.string.dining_nungsu),
                context.getString(R.string.dining_2campus) -> binding.cardViewDining.visibility = View.GONE
                else -> binding.cardViewDining.visibility = View.VISIBLE
            }
        }
        private fun setEmptyDataVisibility(dining: Dining) {
            with(binding) {
                textViewKcal.visibility = View.VISIBLE
                dividerDot.visibility = View.VISIBLE
                textViewCashPrice.visibility = View.VISIBLE
                dividerSlash.visibility = View.VISIBLE
                textViewCardPrice.visibility = View.VISIBLE
                
                if(dining.kcal.isEmpty()) {
                    textViewKcal.visibility = View.GONE
                    dividerDot.visibility = View.GONE
                }
                if(dining.priceCash.isEmpty()) {
                    textViewCashPrice.visibility = View.GONE
                    dividerSlash.visibility = View.GONE
                }
                if(dining.priceCard.isEmpty()) {
                    textViewCardPrice.visibility = View.GONE
                    dividerSlash.visibility = View.GONE
                }
                if(dining.priceCard.isEmpty() && dining.priceCash.isEmpty()) {
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
        private fun createZoomableDialog(context: Context) : Dialog = object : Dialog(context) {

            private var isUserImageInteraction = false
            private lateinit var photoView : PhotoView

            override fun onStart() {
                super.onStart()
                window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }

            @SuppressLint("ClickableViewAccessibility")
            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.dialog_dining_image)

                photoView = findViewById(R.id.photo_view_dining)
                photoView.apply {
                    setOnTouchListener { v, event ->
                        attacher.onTouch(v, event)
                        if (event?.action == ACTION_POINTER_DOWN || event?.action == MotionEvent.ACTION_UP)
                            false
                        else true
                    }
                    minimumScale = DIALOG_MIN_SCALE
                }
            }

            override fun onTouchEvent(event: MotionEvent): Boolean {
                if(event.action == ACTION_POINTER_DOWN)
                    isUserImageInteraction = true

                // 이미지 영역 밖 터치 시 dismiss
                if(event.action == MotionEvent.ACTION_UP) {
                    if (!isUserImageInteraction) {
                        val rect = Rect()
                        window!!.decorView.getWindowVisibleDisplayFrame(rect)
                        val statusBarHeight = rect.top

                        if (!photoView.displayRect.contains(event.rawX, event.rawY - statusBarHeight)) {
                            dismiss()
                        }
                    }
                    isUserImageInteraction = false
                }
                return super.onTouchEvent(event)
            }
        }
    }

    companion object {
        private const val DIALOG_MIN_SCALE = 0.75f
        private const val ACTION_POINTER_DOWN = 261
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