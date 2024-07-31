package `in`.koreatech.koin.core.view.notificaiton

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import `in`.koreatech.koin.core.R
import `in`.koreatech.koin.core.databinding.NotificationRowBinding

class NotificationRow @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attributeSet, defStyleAttr) {
    private lateinit var binding: NotificationRowBinding
    var onSwitchClickListener: OnSwitchClickListener? = null

    var isChecked: Boolean? = null
        set(value) {
            binding.btnSwitch.isChecked = value == true
            field = value
        }

    var fakeChecked: Boolean? = null
        set(value) {
            if (value == true) {
                binding.btnSwitchFake.isVisible = true
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.btnSwitch.isChecked = true
                    onSwitchClickListener?.onSwitch(true)
                    binding.btnSwitchFake.isVisible = false
                }, 300)
            } else {
                binding.btnSwitchFake.isVisible = false
            }
            field = value
        }

    init {
        initView()
        context.theme.obtainStyledAttributes(
            attributeSet, R.styleable.Notification, 0, 0
        ).apply {
            binding.tvTitle.text = getString(R.styleable.Notification_text)
            recycle()
        }

    }

    private fun initView() {
        binding = NotificationRowBinding.bind(
            inflate(context, R.layout.notification_row, this)
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
