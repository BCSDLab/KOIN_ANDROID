package `in`.koreatech.koin.core.view.notificaiton

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import `in`.koreatech.koin.core.R
import `in`.koreatech.koin.core.databinding.NotificationHeaderBinding

class NotificationHeader @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attributeSet, defStyleAttr) {
    private lateinit var binding: NotificationHeaderBinding
    var onSwitchClickListener: OnSwitchClickListener? = null

    var isChecked: Boolean? = null
        set(value) {
            binding.btnSwitch.isChecked = value == true
            field = value
        }

    init {
        initView()
        context.theme.obtainStyledAttributes(
            attributeSet, R.styleable.Notification, 0, 0
        ).apply {
            binding.tvTitle.text = getString(R.styleable.Notification_text)
            binding.tvDescription.text = getString(R.styleable.Notification_description)
            recycle()
        }

    }

    private fun initView() {
        binding = NotificationHeaderBinding.bind(
            inflate(context, R.layout.notification_header, this)
        ).apply {
            btnSwitch.setOnClickListener { onSwitchClickListener?.onSwitch(btnSwitch.isChecked) }
        }
    }

    interface OnSwitchClickListener {
        fun onSwitch(isChecked: Boolean)
    }


    inline fun setOnSwitchClickListener(crossinline onSwitch: (Boolean) -> Unit) {
        this.onSwitchClickListener = object : OnSwitchClickListener {
            override fun onSwitch(isChecked: Boolean) {
                onSwitch(isChecked)
            }
        }
    }
}
