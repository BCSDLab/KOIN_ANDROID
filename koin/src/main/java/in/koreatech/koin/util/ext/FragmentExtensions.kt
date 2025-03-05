package `in`.koreatech.koin.util.ext

import android.os.Build
import android.view.WindowInsets
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment

inline val Fragment.windowHeight: Int
    get() {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val metrics = requireActivity().windowManager.currentWindowMetrics
            val insets = metrics.windowInsets.getInsets(WindowInsets.Type.systemBars())
            metrics.bounds.height() - insets.bottom - insets.top
        } else {
            val view = requireActivity().window.decorView
            val insets =
                WindowInsetsCompat.toWindowInsetsCompat(
                    view.rootWindowInsets,
                    view,
                ).getInsets(WindowInsetsCompat.Type.systemBars())
            resources.displayMetrics.heightPixels - insets.bottom - insets.top
        }
    }

inline val Fragment.windowWidth: Int
    get() {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val metrics = requireActivity().windowManager.currentWindowMetrics
            val insets = metrics.windowInsets.getInsets(WindowInsets.Type.systemBars())
            metrics.bounds.width() - insets.left - insets.right
        } else {
            val view = requireActivity().window.decorView
            val insets =
                WindowInsetsCompat.toWindowInsetsCompat(
                    view.rootWindowInsets,
                    view,
                ).getInsets(WindowInsetsCompat.Type.systemBars())
            resources.displayMetrics.widthPixels - insets.left - insets.right
        }
    }
