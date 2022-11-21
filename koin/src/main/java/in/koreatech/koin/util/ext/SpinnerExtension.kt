package `in`.koreatech.koin.util.ext

import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Spinner

inline fun <T : Adapter> AdapterView<T>.setOnItemSelectedListener(
    crossinline nothingSelected: (AdapterView<*>) -> Unit = {},
    crossinline itemSelected: (
        parent: AdapterView<*>,
        view: View,
        position: Int,
        id: Long) -> Unit
) {
    this.onItemSelectedListener = object : OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            itemSelected(parent, view, position, id)
        }

        override fun onNothingSelected(parent: AdapterView<*>) {
            nothingSelected(parent)
        }
    }
}