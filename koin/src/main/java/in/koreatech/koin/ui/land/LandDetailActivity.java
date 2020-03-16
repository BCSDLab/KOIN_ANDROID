package in.koreatech.koin.ui.land;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity;
import in.koreatech.koin.R;
import in.koreatech.koin.core.appbar.AppBarBase;
import in.koreatech.koin.data.network.entity.Land;
import in.koreatech.koin.data.network.interactor.LandRestInteractor;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.ui.land.presenter.LandDetailContract;
import in.koreatech.koin.ui.land.presenter.LandDetailPresenter;
import in.koreatech.koin.ui.land.adapter.LandDetailPagerAdapter;

/**
 * 복덕방 상세페이지 Activity
 */
public class LandDetailActivity extends KoinNavigationDrawerActivity implements LandDetailContract.View, OnMapReadyCallback {
    private final String TAG = "LandDetailActivity";
    private int landId;
    private Double landLatitude;
    private Double landLongitude;
    private LandDetailPresenter landDetailPresenter;
    private NaverMap naverMap;
    private ViewPager.OnPageChangeListener viewPagerOnPageChangeListener;

    @BindView(R.id.koin_base_app_bar_dark)
    AppBarBase appbarBase;

    @BindView(R.id.land_detail_name_textview) // 원룸명
            TextView landDetailNameTextView;
    @BindView(R.id.land_detail_charter_fee) // 전세
            TextView landDetailCharterfeeTextView;
    @BindView(R.id.land_detail_room_type) // 방 종류
            TextView landDetailRoomTypeTextView;
    @BindView(R.id.land_detail_monthly_fee) // 월세
            TextView landDetailMonthlyfeeTextView;
    @BindView(R.id.land_detail_deposit) // 보증금
            TextView landDetailDepositTextView;
    @BindView(R.id.land_detail_floor) // 층수
            TextView landDetailFloorTextView;
    @BindView(R.id.land_detail_management_fee) // 관리비
            TextView landDetailManagementfeeTextView;
    @BindView(R.id.land_detail_room_size) // 방 크기
            TextView landDetailRoomSizeTextView;
    @BindView(R.id.land_detail_phone) // 문의 전화번호
            TextView landDetailPhoneTextView;

    @BindView(R.id.land_detail_noimage_textview) // 사진이 없을 경우 안내메시지
            TextView landDetailNoImageTextView;
    @BindView(R.id.land_detail_image_viewpager) // 집 사진 뷰페이저
            ViewPager viewPager;
    @BindView(R.id.land_detail_ic_left_imageview) // 왼쪽 화살표
            ImageView landDetailIcLeftImageview;
    @BindView(R.id.land_detail_ic_right_imageview) // 오른쪽 화살표
            ImageView landDetailIcRightImageview;
    private LandDetailPagerAdapter landDetailPagerAdapter;

    @BindView(R.id.land_detail_airconditioner_imageview) // 에어컨 이미지
            ImageView landDetailAirconditionerImageview;
    @BindView(R.id.land_detail_airconditioner_textview) // 에어컨 텍스트
            TextView landDetailAirconditionerTextview;
    @BindView(R.id.land_detail_refrigerator_imageview) // 냉장고 이미지
            ImageView landDetailRefrigeratorImageView;
    @BindView(R.id.land_detail_refrigerator_textview) // 냉장고 텍스트
            TextView landDetailRefrigeratorTextview;
    @BindView(R.id.land_detail_closet_imageview) // 옷장 이미지
            ImageView landDetailClosetImageView;
    @BindView(R.id.land_detail_closet_textview) // 옷장 텍스트
            TextView landDetailClosetTextview;
    @BindView(R.id.land_detail_tv_imageview) // TV 이미지
            ImageView landDetailTvImageview;
    @BindView(R.id.land_detail_tv_textview) // TV 텍스트
            TextView landDetailTvTextview;
    @BindView(R.id.land_detail_doorlock_imageview) // 도어락 이미지
            ImageView landDetailDoorlockImageView;
    @BindView(R.id.land_detail_doorlock_textview) // 도어락 텍스트
            TextView landDetailDoorlockTextview;
    @BindView(R.id.land_detail_microwave_imageview) // 전자레인지 이미지
            ImageView landDetailMicrowaveImageView;
    @BindView(R.id.land_detail_microwave_textview) // 전자레인지 텍스트
            TextView landDetailMicrowaveTextview;
    @BindView(R.id.land_detail_gas_range_imageview) // 가스레인지 이미지
            ImageView landDetailGasrangeImageView;
    @BindView(R.id.land_detail_gas_range_textview) // 가스레인지 텍스트
            TextView landDetailGasrangeTextview;
    @BindView(R.id.land_detail_induction_imageview) // 인덕션 이미지
            ImageView landDetailInductionImageView;
    @BindView(R.id.land_detail_induction_textview) // 인덕션 텍스트
            TextView landDetailInductionTextview;
    @BindView(R.id.land_detail_water_purifier_imageview) // 정수기 이미지
            ImageView landDetailWaterpurifierImageView;
    @BindView(R.id.land_detail_water_purifier_textview) // 정수기 텍스트
            TextView landDetailWaterpurifierTextview;
    @BindView(R.id.land_detail_bidet_imageview) // 비데 이미지
            ImageView landDetailBidetImageView;
    @BindView(R.id.land_detail_bidet_textview) // 비데 텍스트
            TextView landDetailBidetTextview;
    @BindView(R.id.land_detail_washer_imageview) // 세탁기 이미지
            ImageView landDetailWasherImageView;
    @BindView(R.id.land_detail_washer_textview) // 세탁기 텍스트
            TextView landDetailWasherTextview;
    @BindView(R.id.land_detail_bed_imageview) // 침대 이미지
            ImageView landDetailBedImageView;
    @BindView(R.id.land_detail_bed_textview) // 침대 텍스트
            TextView landDetailBedTextview;
    @BindView(R.id.land_detail_desk_imageview) // 책상 이미지
            ImageView landDetailDeskImageView;
    @BindView(R.id.land_detail_desk_textview) // 책상 텍스트
            TextView landDetailDeskTextview;
    @BindView(R.id.land_detail_shoe_closet_imageview) // 신발장 이미지
            ImageView landDetailShoeclosetImageView;
    @BindView(R.id.land_detail_shoe_closet_textview) // 신발장 텍스트
            TextView landDetailShoeclosetTextview;
    @BindView(R.id.land_detail_veranda_imageview) // 베란다 이미지
            ImageView landDetailVerandaImageView;
    @BindView(R.id.land_detail_veranda_textview) // 베란다 텍스트
            TextView landDetailVerandaTextview;
    @BindView(R.id.land_detail_elevator_imageview) // 엘레베이터 이미지
            ImageView landDetailElevatorImageView;
    @BindView(R.id.land_detail_elevator_textview) // 엘레베이터 텍스트
            TextView landDetailElevatorTextview;

    @BindView(R.id.land_detail_room_address_textview) // 원룸 위치 텍스트
            TextView landDetailRoomAddressTextView;
    @BindView(R.id.land_detail_room_address_text)
    TextView landDetailRoomAddressText;
    @BindView(R.id.activity_land_detail_navermap)
    LinearLayout landDetailRoomAddressNavermap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.land_activity_detail);
        ButterKnife.bind(this);

        navermapNullGone();
        init();
    }

    /**
     * 네이버 맵 값을 받아오고 Null값인 경우 setVisibility을 Null로 설정하는 함수
     */
    private void navermapNullGone() {

        landId = getIntent().getIntExtra("Land_ID", -1);
        landLatitude = (Double) getIntent().getSerializableExtra("Land_latitude");
        landLongitude = (Double) getIntent().getSerializableExtra("Land_longitude");

        if (landLatitude == null || landLongitude == null) {
            landDetailRoomAddressTextView.setVisibility(View.GONE);
            landDetailRoomAddressText.setVisibility(View.GONE);
            landDetailRoomAddressNavermap.setVisibility(View.GONE);
        }
    }

    private void init() {
        setPresenter(new LandDetailPresenter(this, new LandRestInteractor()));
        if (landLatitude != null && landLongitude != null)
            naverMapSetting();
    }

    public void naverMapSetting() {
        NaverMapOptions options = new NaverMapOptions()
                .camera(new CameraPosition(new LatLng(landLatitude, landLongitude), 14));


        FragmentManager fm = getSupportFragmentManager();
        NaverMapFragment mapFragment = (NaverMapFragment) fm.findFragmentById(R.id.activity_land_detail_navermap);
        if (mapFragment == null) {
            mapFragment = NaverMapFragment.newInstance(options);
            fm.beginTransaction().add(R.id.activity_land_detail_navermap, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        Marker marker = new Marker();

        if (landLatitude == null || landLongitude == null) {
            return;
        } else
            marker.setPosition(new LatLng(landLatitude, landLongitude));
        marker.setMap(naverMap);
        marker.setWidth(44);
        marker.setHeight(46);
    }

    @OnClick(R.id.koin_base_app_bar_dark)
    public void koinBaseAppbarClick(View view) {
        int id = view.getId();
        if (id == AppBarBase.getLeftButtonId()) {
            onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (landId == -1) {
            ToastUtil.getInstance().makeShort("원룸 정보를 불러오지 못했습니다.");
            finish();
        }
        if (landDetailPresenter != null) {
            landDetailPresenter.getLandDetailInfo(landId);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    /**
     * 로딩중임을 표시하는 메소드
     */
    @Override
    public void showLoading() {
        showProgressDialog(R.string.loading);
    }

    /**
     * 상세정보를 성공적으로 불러왔거나 실패했을 때 로딩 화면을 없애는 메소드
     */
    @Override
    public void hideLoading() {
        hideProgressDialog();

    }

    /**
     * 사용자에게 메시지를 보여주는 메소드
     *
     * @param message 사용자에게 전달할 메시지
     */
    @Override
    public void showMessage(String message) {
        ToastUtil.getInstance().makeShort(message);
    }

    /**
     * 원룸 정보에 맞게 레이아웃을 구성하는 메소드
     * 설정해야하는 view 가 많아서 크게 4개의 메소드로 나눔
     *
     * @param land API를 통해 받아온 데이터(Land 객체)
     */
    @Override
    public void onLandDetailDataReceived(Land land) {
        // 원룸 정보
        setInformationTextView(land);

        // 원룸 이미지
        if (land.getImageUrls() != null && !land.getImageUrls().isEmpty()) {
            setRoomImageView(land);
        } else {
            landDetailNoImageTextView.setText("이미지 준비 중입니다.");
        }

        // 원룸 옵션
        setRoomOption(land);

        // 원룸 위치
        landDetailRoomAddressTextView.setText((land.getAddress() == null) ? "원룸 위치에 대한 정보가 없습니다." : land.getAddress());  // 주소

    }

    /**
     * Land 객체내에서 각 원룸 옵션의 유무를 표시하는 메소드
     *
     * @param land Land 객체
     */
    private void setRoomOption(Land land) {
        PorterDuffColorFilter grayFilter = new PorterDuffColorFilter(getResources().getColor(R.color.gray4), PorterDuff.Mode.SRC_ATOP);
        if (!land.getOptAirConditioner())
            setGray(landDetailAirconditionerImageview, landDetailAirconditionerTextview, grayFilter);
        if (!land.getOptRefrigerator())
            setGray(landDetailRefrigeratorImageView, landDetailRefrigeratorTextview, grayFilter);
        if (!land.getOptCloset())
            setGray(landDetailClosetImageView, landDetailClosetTextview, grayFilter);
        if (!land.getOptTv())
            setGray(landDetailTvImageview, landDetailTvTextview, grayFilter);
        if (!land.getOptElectronicDoorLock())
            setGray(landDetailDoorlockImageView, landDetailDoorlockTextview, grayFilter);
        if (!land.getOptMicrowave())
            setGray(landDetailMicrowaveImageView, landDetailMicrowaveTextview, grayFilter);
        if (!land.getOptGasRange())
            setGray(landDetailGasrangeImageView, landDetailGasrangeTextview, grayFilter);
        if (!land.getOptInduction())
            setGray(landDetailInductionImageView, landDetailInductionTextview, grayFilter);
        if (!land.getOptWaterPurifier())
            setGray(landDetailWaterpurifierImageView, landDetailWaterpurifierTextview, grayFilter);
        if (!land.getOptBidet()) setGray(landDetailBidetImageView, landDetailBidetTextview, grayFilter);
        if (!land.getOptWasher())
            setGray(landDetailWasherImageView, landDetailWasherTextview, grayFilter);
        if (!land.getOptBed()) setGray(landDetailBedImageView, landDetailBedTextview, grayFilter);
        if (!land.getOptDesk()) setGray(landDetailDeskImageView, landDetailDeskTextview, grayFilter);
        if (!land.getOptShoeCloset())
            setGray(landDetailShoeclosetImageView, landDetailShoeclosetTextview, grayFilter);
        if (!land.getOptVeranda())
            setGray(landDetailVerandaImageView, landDetailVerandaTextview, grayFilter);
        if (!land.getOptElevator())
            setGray(landDetailElevatorImageView, landDetailElevatorTextview, grayFilter);
    }

    /**
     * 각 원룸 옵션의 이미지뷰와 텍스트뷰를 colorFilter 를 통해 연한 회색으로 만들어주는 메소드
     *
     * @param landDetailImageview 원룸 옵션의 이미지뷰
     * @param landDetailTextview  원룸 옵션의 텍스트뷰
     * @param grayFilter          연한 회색의 colorFilter
     */
    private void setGray(ImageView landDetailImageview, TextView landDetailTextview, PorterDuffColorFilter grayFilter) {
        landDetailImageview.setColorFilter(grayFilter);
        landDetailTextview.setTextColor(getResources().getColor(R.color.gray4));
    }

    /**
     * Land 객체 내에서 이미지URL을 통해서 원룸의 사진을 설정하는 메소드
     *
     * @param land Land 객체
     */
    private void setRoomImageView(Land land) {
        landDetailNoImageTextView.setVisibility(View.GONE);
        if (land.getImageUrls().size() > 1) landDetailIcRightImageview.setVisibility(View.VISIBLE);

        initViewPagerListener();
        viewPager.addOnPageChangeListener(viewPagerOnPageChangeListener);

        landDetailPagerAdapter = new LandDetailPagerAdapter(this, land.getImageUrls());
        viewPager.setAdapter(landDetailPagerAdapter);
    }

    /**
     * ViewPager.SimpleOnPageChangeListener 의 onPageSelected(int position) 을 구현하여 position 를 통해 화살표 이미지의 Visibility 를 설정하는 메소드
     * ex > 3장짜리 ViewPager가 생성되었을 때 첫번째 장에서는 왼쪽 화살표가 안보이고 마지막 장에서는 오른쪽 화살표가 안보이도록 만든다.
     */
    public void initViewPagerListener() {
        viewPagerOnPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    landDetailIcLeftImageview.setVisibility(View.INVISIBLE);
                    landDetailIcRightImageview.setVisibility(View.VISIBLE);
                } else if (position == (landDetailPagerAdapter.getCount() - 1)) {
                    landDetailIcLeftImageview.setVisibility(View.VISIBLE);
                    landDetailIcRightImageview.setVisibility(View.INVISIBLE);
                } else {
                    landDetailIcLeftImageview.setVisibility(View.VISIBLE);
                    landDetailIcRightImageview.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    /**
     * 원룸의 상세정보들을 입력하는 메소드
     *
     * @param land Land 객체
     */
    private void setInformationTextView(Land land) {
        landDetailNameTextView.setText(((land.getName() == null) || (land.getName().equals(""))) ? "No Name" : land.getName());  // 원름 이름
        landDetailCharterfeeTextView.setText(((land.getCharterFee() == null) || (land.getCharterFee().equals(""))) ? "-" : land.getCharterFee()); // 전세
        landDetailRoomTypeTextView.setText(((land.getRoomType() == null) || (land.getRoomType().equals(""))) ? "-" : land.getRoomType()); // 방 종류
        landDetailMonthlyfeeTextView.setText(((land.getMonthlyFee() == null) || (land.getMonthlyFee().equals(""))) ? "-" : land.getMonthlyFee()); // 월세
        landDetailDepositTextView.setText(((land.getDeposit() == null) || (land.getDeposit().equals(""))) ? "-" : land.getDeposit()); // 보증금
        landDetailFloorTextView.setText((land.getFloor() > 1) ? land.getFloor() + " 층" : "-"); // 층수
        landDetailManagementfeeTextView.setText(((land.getManagementFee() == null) || (land.getManagementFee().equals(""))) ? "-" : land.getManagementFee()); // 관리비
        landDetailRoomSizeTextView.setText(((land.getSize() == null) || (land.getSize().equals(""))) ? "-" : land.getSize()); // 방 크기
        landDetailPhoneTextView.setText(((land.getPhone() == null) || (land.getPhone().equals(""))) ? "-" : land.getPhone()); // 연락처
    }

    @Override
    public void setPresenter(LandDetailPresenter presenter) {
        this.landDetailPresenter = presenter;
    }

    /**
     * 뷰페이저의 양쪽 화살표를 눌렀을 때 방향에 맞게 이미지를 전환시키는 메소드
     *
     * @param view 뷰페이저 안에 위치한 왼쪽,오른쪽 화살표 이미지뷰
     */
    @OnClick({R.id.land_detail_ic_left_imageview, R.id.land_detail_ic_right_imageview})
    public void onClickArrow(View view) {
        Log.d(TAG, Integer.toString(view.getId()));
        switch (view.getId()) {
            case R.id.land_detail_ic_left_imageview:
                viewPager.arrowScroll(ViewPager.FOCUS_LEFT);
                break;
            case R.id.land_detail_ic_right_imageview:
                viewPager.arrowScroll(ViewPager.FOCUS_RIGHT);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}