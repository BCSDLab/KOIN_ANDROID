package `in`.koreatech.koin.ui.land

import `in`.koreatech.koin.R
import `in`.koreatech.koin.constant.LAND
import `in`.koreatech.koin.core.appbar.AppBarBase
import `in`.koreatech.koin.databinding.LandActivityDetailBinding
import `in`.koreatech.koin.domain.model.land.LandDetail
import `in`.koreatech.koin.ui.land.adapter.LandDetailViewPagerAdapter
import `in`.koreatech.koin.ui.land.viewmodel.LandDetailViewModel
import `in`.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity
import `in`.koreatech.koin.ui.navigation.state.MenuState
import `in`.koreatech.koin.util.ext.dpToIntPx
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapOptions
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage

class LandDetailActivity : KoinNavigationDrawerActivity(), OnMapReadyCallback {
    override val menuState = MenuState.Land
    lateinit var binding: LandActivityDetailBinding
    private val landDetailViewModel by viewModels<LandDetailViewModel>()
    private val landDetailViewPagerAdapter = LandDetailViewPagerAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.land_activity_detail)
        initView()
        landDetailViewModel.landDetail.observe(this) {
            it?.let {
                if (it.imageUrls.isNotEmpty()) {
                    setRoomImage(it)
                } else {
                    binding.landDetailNoImageTextview.text = getString(R.string.land_detail_no_image)
                }
                binding.landDetailRoomAddressTextview.text = it.address.ifEmpty { getString(R.string.land_detail_no_location_information) }
                setRoomOption(it)
                setRoomInformation(it)
                naverMapSetMarker(it)
                naverMapCameraMove(it)
            } ?: run {
                with(binding) {
                    landDetailRoomAddressText.visibility = View.GONE
                    landDetailRoomAddressTextview.visibility = View.GONE
                    activityLandDetailNavermap.visibility = View.GONE
                }
            }
        }
    }

    private fun initView() {
        naverMapSetting()
        with(binding) {
            koinBaseAppBarDark.setOnClickListener {
                when (it.id) {
                    AppBarBase.getLeftButtonId() -> callDrawerItem(R.id.navi_item_home)
                    AppBarBase.getRightButtonId() -> toggleNavigationDrawer()
                }
            }
            landDetailImageViewpager.adapter = landDetailViewPagerAdapter

            landDetailIcLeftImageview.setOnClickListener {
                landDetailImageViewpager.currentItem = landDetailImageViewpager.currentItem - 1
            }

            landDetailIcRightImageview.setOnClickListener {
                landDetailImageViewpager.currentItem = landDetailImageViewpager.currentItem + 1
            }
        }
    }

    private fun setRoomOption(landDetail: LandDetail) {
        with(binding) {
            if (!landDetail.optAirConditioner) setGray(landDetailAirconditionerImageview, landDetailAirconditionerTextview)
            if (!landDetail.optRefrigerator) setGray(landDetailRefrigeratorImageview, landDetailRefrigeratorTextview)
            if (!landDetail.optCloset) setGray(landDetailClosetImageview, landDetailClosetTextview)
            if (!landDetail.optTv) setGray(landDetailTvImageview, landDetailTvTextview)
            if (!landDetail.optElectronicDoorLock) setGray(landDetailDoorlockImageview, landDetailDoorlockTextview)
            if (!landDetail.optMicrowave) setGray(landDetailMicrowaveImageview, landDetailMicrowaveTextview)
            if (!landDetail.optGasRange) setGray(landDetailGasRangeImageview, landDetailGasRangeTextview)
            if (!landDetail.optInduction) setGray(landDetailInductionImageview, landDetailInductionTextview)
            if (!landDetail.optWaterPurifier) setGray(landDetailWaterPurifierImageview, landDetailWaterPurifierTextview)
            if (!landDetail.optBidet) setGray(landDetailBidetImageview, landDetailBidetTextview)
            if (!landDetail.optWasher) setGray(landDetailWasherImageview, landDetailWasherTextview)
            if (!landDetail.optBed) setGray(landDetailBedImageview, landDetailBedTextview)
            if (!landDetail.optDesk) setGray(landDetailDeskImageview, landDetailDeskTextview)
            if (!landDetail.optShoeCloset) setGray(landDetailShoeClosetImageview, landDetailShoeClosetTextview)
            if (!landDetail.optVeranda) setGray(landDetailVerandaImageview, landDetailVerandaTextview)
            if (!landDetail.optElevator) setGray(landDetailElevatorImageview,landDetailElevatorTextview)
        }
    }

    private fun setRoomInformation(landDetail: LandDetail) {
        with(binding) {
            landDetailNameTextview.text = landDetail.name.ifEmpty { getString(R.string.land_detail_no_name) }
            landDetailCharterFee.text = landDetail.charterFee.ifEmpty { "-" }
            landDetailRoomType.text = landDetail.roomType.ifEmpty { "-" }
            landDetailMonthlyFee.text = landDetail.monthlyFee.ifEmpty { "-" }
            landDetailDeposit.text = landDetail.deposit.ifEmpty { "-" }
            landDetailFloor.text = if (landDetail.floor.isEmpty()) "-" else "${landDetail.floor} ${getString(R.string.land_detail_floor_unit)}"
            landDetailManagementFee.text = landDetail.managementFee.ifEmpty { "-" }
            landDetailRoomSize.text = landDetail.size.ifEmpty { "-" }
            landDetailPhone.text = landDetail.phone.ifEmpty { "-" }
        }
    }

    private fun setRoomImage(landDetail: LandDetail) {
        landDetailViewPagerAdapter.setData(landDetail.imageUrls)
        with(binding) {
            landDetailNoImageTextview.visibility = View.GONE
            if (landDetail.imageUrls.size > 1) landDetailIcRightImageview.visibility = View.VISIBLE
            landDetailImageViewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    with(binding) {
                        if (position == 0) landDetailIcLeftImageview.visibility = View.INVISIBLE
                        else landDetailIcLeftImageview.visibility = View.VISIBLE
                        if (position == landDetail.imageUrls.size - 1) landDetailIcRightImageview.visibility = View.INVISIBLE
                        else landDetailIcRightImageview.visibility = View.VISIBLE
                    }
                }
            })
        }
    }

    private fun setGray(landDetailImageview: ImageView, landDetailTextview: TextView) {
        landDetailImageview.colorFilter = PorterDuffColorFilter(resources.getColor(R.color.gray4, null), PorterDuff.Mode.SRC_ATOP)
        landDetailTextview.setTextColor(resources.getColor(R.color.gray4, null))
    }

    override fun onMapReady(naverMap: NaverMap) {
        landDetailViewModel.naverMap = naverMap
        val landId = intent.getIntExtra(LAND.LAND_EXTRA_NAME, -1)
        if (landId == -1) {
            Toast.makeText(this, R.string.land_detail_unable_id, Toast.LENGTH_SHORT).show()
            finish()
        } else landDetailViewModel.getLandDetail(landId)
    }

    private fun naverMapSetting() {
        val options = NaverMapOptions().camera(
            CameraPosition(
                LatLng(
                    LAND.INITIAL_LATITUDE,
                    LAND.INITIAL_LONGITUDE
                ), LAND.INITIAL_ZOOM
            )
        )
        var mapFragment = supportFragmentManager.findFragmentById(R.id.activity_land_detail_navermap) as NaverMapFragment?
        if (mapFragment == null) {
            mapFragment = NaverMapFragment().newInstance(options)
        }
        supportFragmentManager.beginTransaction().add(R.id.activity_land_detail_navermap, mapFragment!!).commit()
        mapFragment.getMapAsync(this)
    }

    private fun naverMapSetMarker(landDetail: LandDetail) {
        val marker = Marker()
        with(marker) {
            position = LatLng(landDetail.latitude, landDetail.longitude)
            icon = OverlayImage.fromResource(R.drawable.ic_marker_selected)
            width = LAND.MARKER_WIDTH.dpToIntPx()
            height = LAND.MARKER_HEIGHT.dpToIntPx()
            map = landDetailViewModel.naverMap
        }
    }
    private fun naverMapCameraMove(landDetail: LandDetail) {
        landDetailViewModel.naverMap.moveCamera(CameraUpdate.scrollTo(LatLng(landDetail.latitude, landDetail.longitude)))
    }
}