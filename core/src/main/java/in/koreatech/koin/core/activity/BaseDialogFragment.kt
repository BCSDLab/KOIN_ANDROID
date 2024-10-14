package `in`.koreatech.koin.core.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding

abstract class BaseDialogFragment<B : ViewBinding>(
    @LayoutRes private val layoutResId: Int,
    private val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> B
) : DialogFragment() {
    private var _binding: B? = null
    protected val binding: B
        get() = requireNotNull(_binding) { "binding object is not initialized" }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = bindingInflater.invoke(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected fun setLayoutSizeRatio(widthPercent: Float? = null, heightPercent: Float? = null) {
        context?.resources?.displayMetrics?.let { metrics ->
            binding.root.layoutParams.apply {
                if (heightPercent == null && widthPercent != null) {
                    width = ((metrics.widthPixels * widthPercent).toInt())
                    return
                }
                if (widthPercent == null && heightPercent != null) {
                    height = ((metrics.heightPixels * heightPercent).toInt())
                    return
                }
                if (widthPercent != null && heightPercent != null) {
                    width = ((metrics.widthPixels * widthPercent).toInt())
                    height = ((metrics.heightPixels * heightPercent).toInt())
                    return
                }
            }
        }
    }
}