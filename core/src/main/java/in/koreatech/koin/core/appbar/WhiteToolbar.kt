package `in`.koreatech.koin.core.appbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.MenuRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import `in`.koreatech.koin.core.R
import `in`.koreatech.koin.core.databinding.WhiteToolbarBinding

data class ToolbarMenu(
    @MenuRes val menuRes: Int?,
    val onClick: (Int) -> Unit
)

class WhiteToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : Toolbar(context, attrs, defStyleAttr) {

    private lateinit var binding: WhiteToolbarBinding

    init {
        initView()
        context.theme.obtainStyledAttributes(
            attrs, R.styleable.WhiteToolbar, 0, 0
        ).apply {
            setTitle(getString(R.styleable.WhiteToolbar_title))
            setNavigationIconEnabled(getBoolean(R.styleable.WhiteToolbar_navigationIconEnabled, true))
        }
    }

    private fun initView() {
        binding = WhiteToolbarBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setTitle(title: String?) {
        binding.toolbarWhite.title = title ?: context.getString(R.string.app_name)
    }

    fun setOnNavigationIconClickListener(listener: OnClickListener) {
        binding.toolbarWhite.setNavigationOnClickListener(listener)
    }

    private fun setNavigationIconEnabled(enabled: Boolean) {
        binding.toolbarWhite.navigationIcon = if (enabled) {
            AppCompatResources.getDrawable(context, R.drawable.ic_arrow_left)
        } else {
            null
        }
    }

    fun setMenus(toolbarMenu: ToolbarMenu) {
        binding.toolbarWhite.apply {
            this.menu.clear()
            toolbarMenu.menuRes?.let {
                this.inflateMenu(it)
                setOnMenuItemClickListener { item ->
                    toolbarMenu.onClick(item.itemId)
                    true
                }
            }
        }
    }
}
