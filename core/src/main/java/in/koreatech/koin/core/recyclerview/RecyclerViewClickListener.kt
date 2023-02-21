package `in`.koreatech.koin.core.recyclerview

import android.view.View

interface RecyclerViewClickListener {
    fun onClick(view: View?, position: Int) //click event
    fun onLongClick(view: View?, position: Int) //long click event
}