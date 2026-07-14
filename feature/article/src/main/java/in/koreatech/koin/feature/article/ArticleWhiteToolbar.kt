package `in`.koreatech.koin.feature.article

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup.MarginLayoutParams
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import `in`.koreatech.koin.core.R as CoreR
import `in`.koreatech.koin.core.appbar.ToolbarMenu
import `in`.koreatech.koin.feature.article.databinding.ArticleToolbarBinding

class ArticleWhiteToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : Toolbar(context, attrs, defStyleAttr) {
    private lateinit var binding: ArticleToolbarBinding

    init {
        binding = ArticleToolbarBinding.inflate(LayoutInflater.from(context), this, true)
        binding.toolbarArticle.navigationIcon = AppCompatResources.getDrawable(context, CoreR.drawable.ic_arrow_left)
        ViewCompat.setOnApplyWindowInsetsListener(binding.toolbarArticle) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updateLayoutParams<MarginLayoutParams> {
                leftMargin = systemBars.left
                topMargin = systemBars.top
                rightMargin = systemBars.right
            }
            WindowInsetsCompat.CONSUMED
        }
    }

    override fun setTitle(title: CharSequence?) {
        binding.textArticleToolbarTitle.text = title ?: context.getString(CoreR.string.app_name)
    }

    fun setOnNavigationIconClickListener(listener: OnClickListener) {
        binding.toolbarArticle.setNavigationOnClickListener(listener)
    }

    fun setMenus(toolbarMenu: ToolbarMenu) {
        binding.toolbarArticle.apply {
            menu.clear()
            toolbarMenu.menuRes?.let {
                inflateMenu(it)
                setOnMenuItemClickListener { item ->
                    toolbarMenu.onClick(item.itemId)
                    true
                }
            }
        }
    }
}
