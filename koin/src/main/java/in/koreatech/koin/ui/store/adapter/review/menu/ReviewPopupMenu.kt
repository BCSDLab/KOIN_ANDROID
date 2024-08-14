package `in`.koreatech.koin.ui.store.adapter.review.menu

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import `in`.koreatech.koin.R
import `in`.koreatech.koin.databinding.ItemReviewPopupBinding
import `in`.koreatech.koin.databinding.MenuReviewPopupBinding

class ReviewPopupMenu(
    context: Context,
    private val onModify: () -> Unit,
    private val onDelete: () -> Unit,
) : PopupWindow() {
    private val binding = MenuReviewPopupBinding.inflate(LayoutInflater.from(context), null, false)
    private val menuList = listOf(
        MENU_MODIFY to R.drawable.ic_modify,
        MENU_DELETE to R.drawable.ic_trash_can
    )

    init {
        contentView = binding.root
        width = 350
        isOutsideTouchable = true
        isFocusable = true

        menuList.forEach { (menuTitle, iconRes) ->
            val itemBinding =
                ItemReviewPopupBinding.inflate(LayoutInflater.from(context), null, false)
            itemBinding.tvMenuTitle.text = menuTitle
            itemBinding.ivMenuIcon.setImageResource(iconRes)
            itemBinding.root.setOnClickListener {
                when (menuTitle) {
                    MENU_MODIFY -> onModify()
                    MENU_DELETE -> onDelete()
                }
                dismiss()
            }
            binding.menuContainer.addView(itemBinding.root)
        }
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        height = contentView.measuredHeight
    }

    fun show(anchor: View) {
        if (!isShowing) {
            showAsDropDown(anchor)
        }
    }

    companion object {
        const val MENU_MODIFY = "수정하기"
        const val MENU_DELETE = "삭제하기"
    }
}
