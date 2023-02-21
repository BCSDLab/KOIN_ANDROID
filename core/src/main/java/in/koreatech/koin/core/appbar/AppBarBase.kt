package `in`.koreatech.koin.core.appbar

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.TextView
import androidx.annotation.AttrRes
import com.google.android.material.appbar.AppBarLayout
import `in`.koreatech.koin.core.R

class AppBarBase @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int? = null) : AppBarLayout(context, attrs) {
    private val view = inflate(context, R.layout.base_appbar_dark, null)
    private val background = view.findViewById<AppBarLayout>(R.id.base_appbar_dark)
    private val leftButton = view.findViewById<TextView>(R.id.base_appbar_dark_left_button)
    private val rightButton = view.findViewById<TextView>(R.id.base_appbar_dark_right_button)
    private val title = view.findViewById<TextView>(R.id.base_appbar_dark_title)

    private val textFont = Typeface.createFromAsset(context.assets, "fonts/notosans_medium.ttf")

    init {
        addView(view)
        title.typeface = textFont
        leftButton.typeface = textFont
        rightButton.typeface = textFont

        if(attrs != null) {
            getAttribute(attrs, context, defStyle)
        }
    }

    override fun setOnClickListener(onClickListener: OnClickListener?) {
        background.setOnClickListener(onClickListener)
        leftButton.setOnClickListener(onClickListener)
        rightButton.setOnClickListener(onClickListener)
        title.setOnClickListener(onClickListener)
    }

    private fun getAttribute(attrs: AttributeSet?, context: Context) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AppBarBase)
        setTypeArray(typedArray)
    }

    private fun getAttribute(attrs: AttributeSet, context: Context, @AttrRes defStyle: Int?) {
        val typedArray = if(defStyle == null) {
            context.obtainStyledAttributes(attrs, R.styleable.AppBarBase)
        } else {
            context.obtainStyledAttributes(attrs, R.styleable.AppBarBase, defStyle, 0)
        }

        setTypeArray(typedArray)
    }

    private fun setTypeArray(typedArray: TypedArray) {
        val backgroundColor = typedArray.getColor(
            R.styleable.AppBarBase_backgroundColor,
            resources.getColor(R.color.colorPrimary)
        )
        val titleTextColor = typedArray.getColor(
            R.styleable.AppBarBase_titleTextColor,
            resources.getColor(R.color.white)
        )
        val titleText = typedArray.getString(R.styleable.AppBarBase_titleText)
        val titleVisibility = typedArray.getInt(R.styleable.AppBarBase_titleVisibility, VISIBLE)
        val leftButtonBackground =
            typedArray.getDrawable(R.styleable.AppBarBase_leftButtonBackground)
        val leftButtonColor = typedArray.getColor(
            R.styleable.AppBarBase_leftButtonTextColor,
            resources.getColor(R.color.white)
        )
        val leftButtonString = typedArray.getString(R.styleable.AppBarBase_leftButtonText)
        val leftButtonVisibility =
            typedArray.getInt(R.styleable.AppBarBase_leftButtonVisibility, VISIBLE)
        val rightButtonColor = typedArray.getColor(
            R.styleable.AppBarBase_rightButtonTextColor,
            resources.getColor(R.color.white)
        )
        val rightButtonBackground =
            typedArray.getDrawable(R.styleable.AppBarBase_rightButtonBackground)
        val rightButtonString = typedArray.getString(R.styleable.AppBarBase_rightButtonText)
        val rightButtonVisibility =
            typedArray.getInt(R.styleable.AppBarBase_rightButtonVisibility, VISIBLE)
        val leftButtonHeight =
            typedArray.getDimensionPixelSize(R.styleable.AppBarBase_leftButtonHeight, -1)
        val rightButtonHeight =
            typedArray.getDimensionPixelSize(R.styleable.AppBarBase_leftButtonHeight, -1)
        val leftButtonWidth =
            typedArray.getDimensionPixelSize(R.styleable.AppBarBase_leftButtonHeight, -1)
        val rightButtonWidth =
            typedArray.getDimensionPixelSize(R.styleable.AppBarBase_leftButtonHeight, -1)

        /* background */background.setBackgroundColor(backgroundColor)
        /* title */title.setTextColor(titleTextColor)
        title.text = titleText
        title.visibility = titleVisibility
        /* leftButton */leftButton.setTextColor(leftButtonColor)
        leftButton.background = leftButtonBackground
        leftButton.text = leftButtonString
        leftButton.visibility = leftButtonVisibility
        if (leftButtonHeight != -1 || leftButtonWidth != -1) {
            leftButton.height = leftButtonHeight
            leftButton.width = leftButtonWidth
        }
        /* rightButton */rightButton.setTextColor(rightButtonColor)
        rightButton.background = rightButtonBackground
        rightButton.text = rightButtonString
        rightButton.visibility = rightButtonVisibility
        if (leftButtonHeight != -1 || leftButtonWidth != -1) {
            rightButton.height = rightButtonHeight
            rightButton.width = rightButtonWidth
        }
        typedArray.recycle()
    }

    override fun setBackgroundColor(backgroundColor: Int) {
        background.setBackgroundColor(backgroundColor)
    }

    fun setTitleTextColor(titleTextColor: Int) {
        title.setTextColor(titleTextColor)
    }

    fun setTitleText(titleText: String?) {
        title.text = titleText
    }

    fun setTitleVisibility(titleVisibility: Int) {
        title.visibility = titleVisibility
    }

    fun setLeftButtonTextColor(leftButtonColor: Int) {
        rightButton.setTextColor(leftButtonColor)
    }

    fun setLeftButtonDrawalble(leftButtonBackground: Drawable?) {
        leftButton.background = leftButtonBackground
    }

    fun setLeftButtonText(leftButtonString: String?) {
        leftButton.text = leftButtonString
    }

    fun setLeftButtonVisibility(leftButtonVisibility: Int) {
        leftButton.visibility = leftButtonVisibility
    }

    fun setRightButtonTextColor(rightButtonColor: Int) {
        rightButton.setTextColor(rightButtonColor)
    }

    fun setRightButtonDrawalble(rightButtonBackground: Drawable?) {
        rightButton.background = rightButtonBackground
    }

    fun setRightButtonText(rightButtonString: String?) {
        rightButton.text = rightButtonString
    }

    fun setRightButtonVisibility(rightButtonVisibility: Int) {
        rightButton.visibility = rightButtonVisibility
    }

    companion object {
        @JvmStatic
        val titleId: Int = R.id.base_appbar_dark_title
        @JvmStatic
        val leftButtonId: Int = R.id.base_appbar_dark_left_button
        @JvmStatic
        val rightButtonId: Int = R.id.base_appbar_dark_right_button
    }
}