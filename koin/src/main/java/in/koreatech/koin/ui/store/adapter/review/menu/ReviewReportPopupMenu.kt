package `in`.koreatech.koin.ui.store.adapter.review.menu

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.PopupWindow
import androidx.core.content.ContextCompat
import `in`.koreatech.koin.R
import `in`.koreatech.koin.databinding.ItemReviewPopupBinding
import `in`.koreatech.koin.databinding.MenuReviewPopupBinding

class ReviewReportPopupMenu(
    context: Context,
    private val onReport: () -> Unit,
) : PopupWindow() {
    private val binding = MenuReviewPopupBinding.inflate(LayoutInflater.from(context), null, false)
    private val menuList = listOf(
        MENU_REPORT to R.drawable.icon_report
    )

    init {
        contentView = binding.root
        width = 600
        height = 120 * menuList.size
        isOutsideTouchable = true
        isFocusable = true

        menuList.forEach { (menuTitle, iconRes) ->
            val itemBinding =
                ItemReviewPopupBinding.inflate(LayoutInflater.from(context), null, false)

            itemBinding.tvMenuTitle.text = menuTitle
            itemBinding.ivMenuIcon.setImageResource(iconRes)
            itemBinding.root.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        itemBinding.root.setBackgroundColor(ContextCompat.getColor(context, R.color.gray16))
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        itemBinding.root.setBackgroundColor(ContextCompat.getColor(context, R.color.white))

                        if (event.action == MotionEvent.ACTION_UP) {
                            when (menuTitle) {
                                MENU_REPORT -> onReport()
                            }
                            dismiss()
                        }
                    }
                }

                true
            }
            binding.menuContainer.addView(itemBinding.root)
        }
    }

    fun show(anchor: View) {
        if (!isShowing) {
            showAsDropDown(anchor)
        }
    }

    companion object {
        const val MENU_REPORT = "신고하기"
    }
}