package `in`.koreatech.koin.core.activity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

@Deprecated("Use AppCompatActivity.activityDataBinding instead.")
abstract class DataBindingActivity<T : ViewDataBinding> : ActivityBase() {
    @get:LayoutRes
    abstract val layoutId: Int

    lateinit var binding: T
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)
    }
}