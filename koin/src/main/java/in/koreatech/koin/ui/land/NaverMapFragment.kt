package `in`.koreatech.koin.ui.land

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMapOptions

class NaverMapFragment: MapFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        view?.setOnTouchListener { touchView: View, event: MotionEvent? ->
            touchView.parent.requestDisallowInterceptTouchEvent(true)
            mapView!!.onTouchEvent(event)
            view.performClick()
            true
        }
        return view
    }

    fun newInstance(options: NaverMapOptions?): NaverMapFragment? {
        val var1 = NaverMapFragment()
        val var2 = Bundle()
        var2.putParcelable("NaverMapOptions", options)
        var1.arguments = var2
        return var1
    }
}