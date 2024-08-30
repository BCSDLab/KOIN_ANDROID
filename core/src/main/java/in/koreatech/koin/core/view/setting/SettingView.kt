package `in`.koreatech.koin.core.view.setting

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import `in`.koreatech.koin.core.R
import `in`.koreatech.koin.core.databinding.SettingViewBinding

/**
 *
 */
class SettingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private lateinit var binding: SettingViewBinding

    init {
        initAttrs(attrs)
        initView(context)
    }

    private fun initAttrs(attrs: AttributeSet?) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.SettingView,
            0,
            0
        ).apply {
            try {

            } finally {
              recycle()
            }
        }
    }

    private fun initView(context: Context) {
        val inflater = LayoutInflater.from(context)
        binding = SettingViewBinding.inflate(inflater, this, false)

        with(binding) {

        }
    }
}