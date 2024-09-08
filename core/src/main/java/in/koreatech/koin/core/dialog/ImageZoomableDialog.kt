package `in`.koreatech.koin.core.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import `in`.koreatech.koin.core.R
import `in`.koreatech.koin.core.databinding.ImageZoomableDialogBinding

class ImageZoomableDialog(private val context: Context, private val image: String) : Dialog(context) {

    private lateinit var binding: ImageZoomableDialogBinding
    private var isUserImageInteraction = false
    var initialScale = 1f
    var cancelableOnTouchOutside = true

    override fun onStart() {
        super.onStart()
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ImageZoomableDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.dialogCloseButton.setOnClickListener {
            dismiss()
        }

        binding.photoView.apply {
            setOnTouchListener { v, event ->
                attacher.onTouch(v, event)

                if (event?.action == ACTION_POINTER_DOWN || event?.action == MotionEvent.ACTION_UP)
                    false
                else true
            }
            minimumScale = initialScale
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

                if(binding.photoView.displayRect != null)
                    if (!binding.photoView.displayRect.contains(event.rawX, event.rawY - statusBarHeight)) {
                        if (cancelableOnTouchOutside)
                            dismiss()
                    }
            }
            isUserImageInteraction = false
        }
        return super.onTouchEvent(event)
    }

    override fun show() {
        super.show()
        Glide.with(context)
            .load(image)
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
                    binding.photoView.post {
                        binding.photoView.scale = initialScale

                        val closeButton = findViewById<ImageView>(R.id.dialog_close_button)
                        val lp = closeButton?.layoutParams as FrameLayout.LayoutParams
                        val rectF = binding.photoView.displayRect
                        lp.setMargins(0, rectF.top.toInt() - closeButton.height - 8, rectF.left.toInt(), 0)
                        closeButton.layoutParams = lp
                        closeButton.visibility = View.VISIBLE
                    }
                    return false
                }
            })
            .into(binding.photoView)
    }

    companion object {
        private const val ACTION_POINTER_DOWN = 261
    }
}