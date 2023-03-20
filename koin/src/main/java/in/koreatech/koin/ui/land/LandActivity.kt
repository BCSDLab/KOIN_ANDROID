package `in`.koreatech.koin.ui.land

import `in`.koreatech.koin.R
import `in`.koreatech.koin.constant.LAND
import `in`.koreatech.koin.core.appbar.AppBarBase
import `in`.koreatech.koin.databinding.LandActivityMainBinding
import `in`.koreatech.koin.domain.model.land.Land
import `in`.koreatech.koin.domain.util.ext.nameSearchFilter
import `in`.koreatech.koin.ui.land.adapter.LandRecyclerViewAdapter
import `in`.koreatech.koin.ui.land.viewmodel.LandViewModel
import `in`.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity
import `in`.koreatech.koin.ui.navigation.state.MenuState
import `in`.koreatech.koin.util.ext.dpToIntPx
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapOptions
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage

class LandActivity : KoinNavigationDrawerActivity(), OnMapReadyCallback {
    override val menuState = MenuState.Land
    lateinit var binding: LandActivityMainBinding
    private val landViewModel by viewModels<LandViewModel>()
    private val landAdapter = LandRecyclerViewAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.land_activity_main)
        initView()
        landViewModel.landData.observe(this) {
            if (it.isEmpty()) Toast.makeText(this, R.string.land_data_load_fail, Toast.LENGTH_SHORT)
                .show()
            else {
                landAdapter.setLandList(it)
                makeNaverMapMarker(it)
            }
        }
    }

    override fun onMapReady(naverMap: NaverMap) {
        landViewModel.naverMap = naverMap
        landViewModel.getLandList()
    }

    private fun initView() {
        with(binding) {
            with(landRecyclerlayout) {
                adapter = landAdapter
                layoutManager = GridLayoutManager(this@LandActivity, 1)
            }
            stickyScrollView.post {
                landRecyclerlayout.minimumHeight =
                    stickyScrollView.height - searchViewContainer.height
            }
            searchViewContainer.setOnClickListener {
                runOnUiThread {
                    stickyScrollView.scrollTo(0, it.top)
                }
            }
            searchEditText.setOnClickListener {
                searchViewContainer.performClick()
            }
            searchEditText.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    searchViewContainer.performClick()
                }
            }
            searchEditText.addTextChangedListener {
                landViewModel.landData.value?.nameSearchFilter(it.toString())?.let { landList ->
                    landAdapter.setLandList(landList)
                    makeNaverMapMarker(landList)
                }
            }

            koinBaseAppBarDark.setOnClickListener {
                when (it.id) {
                    AppBarBase.getLeftButtonId() -> callDrawerItem(R.id.navi_item_home)
                    AppBarBase.getRightButtonId() -> toggleNavigationDrawer()
                }
            }
        }
        naverMapSetting()
    }

    private fun naverMapSetting() {
        val options = NaverMapOptions()
            .camera(
                CameraPosition(
                    LatLng(LAND.INITIAL_LATITUDE, LAND.INITIAL_LONGITUDE),
                    LAND.INITIAL_ZOOM
                )
            )
        var mapFragment =
            supportFragmentManager.findFragmentById(R.id.activity_land_navermap) as NaverMapFragment?
        if (mapFragment == null) {
            mapFragment = NaverMapFragment().newInstance(options)
        }
        supportFragmentManager.beginTransaction().add(R.id.activity_land_navermap, mapFragment!!)
            .commit()
        mapFragment.getMapAsync(this)
    }

    private fun makeNaverMapMarker(landList: List<Land>) {
        landViewModel.markerList.forEach { it.map = null }
        landViewModel.markerList.clear()
        landList.forEach { land ->
            landViewModel.markerList.add(
                Marker(
                    LatLng(land.latitude, land.longitude),
                    OverlayImage.fromResource(R.drawable.ic_marker_normal)
                )
            )
        }
        landViewModel.markerList.forEachIndexed { index, marker ->
            with(marker) {
                map = landViewModel.naverMap
                width = LAND.MARKER_WIDTH.dpToIntPx()
                height = LAND.MARKER_HEIGHT.dpToIntPx()
                tag = index.toString()
                setOnClickListener {
                    selectMarker(landViewModel.markerList, index)
                    landAdapter.setSelectedPosition(index)
                    true
                }
            }
        }
    }

    private fun selectMarker(markers: List<Marker>, selectIndex: Int) {
        markers.forEachIndexed { index, marker ->
            if (index == selectIndex) marker.icon =
                OverlayImage.fromResource(R.drawable.ic_marker_selected)
            else marker.icon = OverlayImage.fromResource(R.drawable.ic_marker_normal)
        }
    }
}
