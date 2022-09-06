package in.koreatech.koin.ui.land;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.R;
import in.koreatech.koin.core.appbar.AppBarBase;
import in.koreatech.koin.core.stickysrcollview.StickyNestedScrollView;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.data.network.entity.BokdukRoom;
import in.koreatech.koin.data.network.interactor.BokdukRestInteractor;
import in.koreatech.koin.ui.land.adapter.LandRecyclerAdapter;
import in.koreatech.koin.ui.land.presenter.LandContract;
import in.koreatech.koin.ui.land.presenter.LandPresenter;
import in.koreatech.koin.ui.navigation.KoinNavigationDrawerActivityNew;
import in.koreatech.koin.ui.navigation.state.MenuState;

/**
 * 복덕방 목록을 보여주는 Activity
 */
public class LandActivity extends KoinNavigationDrawerActivityNew implements LandContract.View, OnMapReadyCallback {
    private static final String TAG = "LandActivity";
    private Context context;
    private LandPresenter landPresenter;
    private ArrayList<BokdukRoom> landArrayList;
    private RecyclerView landRecyclerView;
    private EditText searchEditText;
    private LinearLayout searchLayout;
    private LinearLayout searchInsideLayout;
    private GridLayoutManager landGridLayoutManager;
    private LandRecyclerAdapter landRecyclerAdapter;
    private NaverMap naverMap;
    private Marker[] marker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.land_activity_main);
        ButterKnife.bind(this);
        context = this;
        init();
        landPresenter.getLandList();
    }

    void init() {
        landArrayList = new ArrayList<>();
        landRecyclerView = findViewById(R.id.land_recyclerlayout);
        searchEditText = findViewById(R.id.search_edit_text);
        landGridLayoutManager = new GridLayoutManager(this, 1);
        landRecyclerView.setLayoutManager(landGridLayoutManager);
        setPresenter(new LandPresenter(this, new BokdukRestInteractor()));
        searchLayout = findViewById(R.id.search_view_container);
        searchInsideLayout = findViewById(R.id.search_view_inside_container);
        scrollToSearchView();
        searchInsideLayout.setOnClickListener(v -> searchLayout.performClick());
        initSearchLayout();
        initLandRecyclerViewPadding();
        naverMapSetting();
    }

    private void initLandRecyclerViewPadding() {
        StickyNestedScrollView scrollView = findViewById(R.id.sticky_scroll_view);
        scrollView.post(() -> {
            int paddingSize = scrollView.getHeight() - searchLayout.getHeight();
            landRecyclerView.setMinimumHeight(paddingSize);
        });
    }


    private void scrollToSearchView() {
        StickyNestedScrollView stickyScrollView = findViewById(R.id.sticky_scroll_view);
        searchLayout.setOnClickListener(v -> runOnUiThread(() -> stickyScrollView.scrollTo(0, searchLayout.getTop())));
    }

    private void initSearchLayout() {
        searchEditText.setOnClickListener(v -> searchLayout.performClick());
        searchEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                searchLayout.performClick();
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                landPresenter.getLandList(s.toString());
            }
        });
    }

    /**
     * 네이버 맵 세부 설정(위도, 경도, 가까운 정도)
     * 네이버맵 이벤트를 독립적으로 처리하기위한 mapFragment(recyclerview와 스크롤이 함께 이뤄지는 버그 해결 용도)
     */
    public void naverMapSetting() {
        NaverMapOptions options = new NaverMapOptions()
                .camera(new CameraPosition(new LatLng(36.763695, 127.281796), 14));


        FragmentManager fm = getSupportFragmentManager();
        NaverMapFragment mapFragment = (NaverMapFragment) fm.findFragmentById(R.id.activity_land_navermap);
        if (mapFragment == null) {
            mapFragment = NaverMapFragment.newInstance(options);
            fm.beginTransaction().add(R.id.activity_land_navermap, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

    }

    @OnClick(R.id.koin_base_app_bar_dark)
    public void onClickedBaseAppbar(View v) {
        int id = v.getId();
        if (id == AppBarBase.getLeftButtonId()) {
            onBackPressed();
        } else if (id == AppBarBase.getRightButtonId()) {
            toggleNavigationDrawer();
        }
    }

    @Override
    public void setPresenter(LandPresenter presenter) {
        this.landPresenter = presenter;
    }

    /**
     * presenter로부터 받은 복덕방 리스트를 액티비티에 저장
     * 복덕방 데이터를 받아와서 naverMap Marker 추가
     *
     * @param landList 복덕방배열
     */
    @Override
    public void onLandListDataReceived(ArrayList<BokdukRoom> landList) {
        landArrayList.clear();
        landArrayList.addAll(landList);
        updateUserInterface();
        makeNavermapMarker(landArrayList);
    }

    /**
     * NaverMap에 Marker를 표시하는 함수
     *
     * @param landList 복덕방의 위도, 경도값을 가진 데이터
     */
    public void makeNavermapMarker(ArrayList<BokdukRoom> landList) {
        removeMarker();
        marker = new Marker[landList.size()];
        for (int i = 0; i < landList.size(); i++) {
            marker[i] = new Marker();

            Double latitude = landList.get(i).getLatitude();
            Double langitute = landList.get(i).getLongitude();
            if (latitude == null || langitute == null) {
                continue;
            }
            marker[i].setIcon(OverlayImage.fromResource(R.drawable.ic_marker_normal));
            marker[i].setPosition(new LatLng(latitude, langitute));
            marker[i].setMap(naverMap);
            marker[i].setWidth(dpToPx(20));
            marker[i].setHeight(dpToPx(26));
            marker[i].setTag(Integer.toString(i));
            marker[i].setOnClickListener(overlay -> {
                Marker clickedMarker = (Marker) overlay;
                String index = (String) clickedMarker.getTag();
                if (index != null) {
                    selectMarker(marker, Integer.parseInt(index));
                    selectList(Integer.parseInt(index));
                }
                return true;
            });
        }
    }

    private void removeMarker() {
        if (marker != null) {
            for (Marker currentMarker : marker) {
                currentMarker.setMap(null);
            }
        }
    }

    private void selectMarker(Marker[] markers, int selectIndex) {
        for (int i = 0; i < markers.length; i++) {
            if (i == selectIndex) {
                markers[i].setIcon(OverlayImage.fromResource(R.drawable.ic_marker_selected));
                continue;
            }
            markers[i].setIcon(OverlayImage.fromResource(R.drawable.ic_marker_normal));
        }
    }

    private void selectList(int index) {
        if (landRecyclerAdapter == null) return;
        landRecyclerAdapter.select(index);
    }

    /**
     * 마커를 클릭했을 때 해당 인덱스에 맞게 디테일뷰로 화면을 이동시키는 함수
     *
     * @param index 마커의 인덱스로 복덕방의 자료를 가져옴
     */
    public void goToLandDetailByIndex(int index) {
        if (landArrayList.size() <= index)
            return;
        BokdukRoom bokdukRoom = landArrayList.get(index);
        Intent intent = new Intent(this, LandDetailActivity.class);
        intent.putExtra("Land_ID", bokdukRoom.getId());
        intent.putExtra("Land_latitude", bokdukRoom.getLatitude());
        intent.putExtra("Land_longitude", bokdukRoom.getLongitude());
        startActivity(intent);
    }

    /**
     * RecyclerView에 복덕방리스트를 보여주는 함수
     */
    @Override
    public void updateUserInterface() {
        landRecyclerAdapter = new LandRecyclerAdapter(landArrayList, context);
        landRecyclerView.setAdapter(landRecyclerAdapter);
    }

    /**
     * 복덕방 리스트를 받아오지 못하였을때 호출하는 함수
     *
     * @param message "원룸 리스트를 받아오지 못했습니다"
     */
    @Override
    public void showMessage(String message) {
        ToastUtil.getInstance().makeShort(message);
    }

    /**
     * 네이버맵 객체를 가져오는 함수
     * OnMapReadyCallback(인터페이스)의 필수 오버라이딩 함수
     *
     * @param naverMap
     */
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {

        this.naverMap = naverMap;
        landPresenter.getLandList();
    }

    private int dpToPx(int dp) {
        DisplayMetrics dm = new DisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, dm);
    }

    @NonNull
    @Override
    protected MenuState getMenuState() {
        return MenuState.Land.INSTANCE;
    }
}
