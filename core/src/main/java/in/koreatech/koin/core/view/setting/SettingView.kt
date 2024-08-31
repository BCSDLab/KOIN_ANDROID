package `in`.koreatech.koin.core.view.setting

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
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

    var paddingVertical = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics).toInt()
    var paddingHorizontal = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24f, resources.displayMetrics).toInt()

    @StyleRes
    private var headTextAppearanceRes = R.style.TextAppearance_Koin_Regular_16
    var headText = "설정 항목 텍스트"

    private var labelType = LABEL_TYPE_NONE

    @StyleRes
    private var labelTextAppearanceRes = R.style.TextAppearance_Koin_Regular_16
    var labelText = "라벨 텍스트"

    @DrawableRes
    private var labelImageRes = R.drawable.ic_arrow_right

    private var isEnableDivider = true
    @ColorRes
    private var dividerColorRes = R.color.neutral_100

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
                paddingVertical = getDimensionPixelSize(R.styleable.SettingView_paddingVertical, paddingVertical)
                paddingHorizontal = getDimensionPixelSize(R.styleable.SettingView_paddingHorizontal, paddingHorizontal)

                headText = getString(R.styleable.SettingView_headText) ?: headText
                headTextAppearanceRes = getResourceId(R.styleable.SettingView_headTextAppearance, -1)

                labelType = getInt(R.styleable.SettingView_labelType, LABEL_TYPE_NONE)
                labelText = getString(R.styleable.SettingView_labelText) ?: labelText
                labelTextAppearanceRes = getResourceId(R.styleable.SettingView_labelTextAppearance, -1)
                labelImageRes = getResourceId(R.styleable.SettingView_labelImage, labelImageRes)

                isEnableDivider = getBoolean(R.styleable.SettingView_enableDivider, isEnableDivider)
                dividerColorRes = getResourceId(R.styleable.SettingView_dividerColor, dividerColorRes)

            } finally {
                recycle()
            }
        }
    }

    private fun initView(context: Context) {
        binding = SettingViewBinding.bind(inflate(context, R.layout.setting_view, this))

        with(binding) {
            tvHead.setPadding(paddingHorizontal, paddingVertical, 0, paddingVertical)
            tvLabel.setPadding(0, 0, paddingHorizontal, 0)
            ivLabel.setPadding(0, 0, paddingHorizontal, 0)

            with(tvHead) {
                text = headText
                setTextAppearance(headTextAppearanceRes)
            }

            when (labelType) {
                LABEL_TYPE_TEXT -> {
                    ivLabel.visibility = GONE
                    tvLabel.visibility = VISIBLE

                    tvLabel.text = labelText
                    tvLabel.setTextAppearance(labelTextAppearanceRes)
                }

                LABEL_TYPE_IMAGE -> {
                    ivLabel.visibility = VISIBLE
                    tvLabel.visibility = GONE

                    ivLabel.setImageResource(labelImageRes)
                }

                else -> {
                    ivLabel.visibility = GONE
                    tvLabel.visibility = GONE
                }
            }

            with(viewDivider) {
                visibility = if (isEnableDivider) VISIBLE else GONE
                setBackgroundResource(dividerColorRes)
            }
        }
    }

    private companion object {
        const val LABEL_TYPE_NONE = -1
        const val LABEL_TYPE_TEXT = 0
        const val LABEL_TYPE_IMAGE = 1

    }
}