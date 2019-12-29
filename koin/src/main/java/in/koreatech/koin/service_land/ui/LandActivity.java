package in.koreatech.koin.service_land.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.KoinNavigationDrawerActivity;
import in.koreatech.koin.R;
import in.koreatech.koin.core.bases.KoinBaseAppbarDark;
import in.koreatech.koin.core.networks.entity.BokdukRoom;
import in.koreatech.koin.core.networks.interactors.BokdukRestInteractor;
import in.koreatech.koin.core.util.ToastUtil;
import in.koreatech.koin.service_land.adapter.LandRecyclerAdapter;
import in.koreatech.koin.service_land.contracts.LandContract;
import in.koreatech.koin.service_land.presenter.LandPresenter;

import androidx.annotation.NonNull;

/**
 * 복덕방 목록을 보여주는 Activity
 * Created by dongmin on 2019. 09. 02...
 */
public class LandActivity extends KoinNavigationDrawerActivity implements LandContract.View, OnMapReadyCallback {
    private static final String TAG = LandActivity.class.getSimpleName();
    private Context context;
    private LandPresenter landPresenter;
    private ArrayList<BokdukRoom> landArrayList;
    private RecyclerView landRecyclerView;
    private GridLayoutManager landGridLayoutManager;
    private LandRecyclerAdapter landRecyclerAdapter;
    private NaverMap naverMap;
    HashMap<Marker, Integer> markerMap = new HashMap();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.land_activity_main);
        ButterKnife.bind(this);
        context = this;
        init();
    }

    void init() {
        landArrayList = new ArrayList<>();
        landRecyclerView = findViewById(R.id.land_recyclerlayout);
        landGridLayoutManager = new GridLayoutManager(this, 2);
        landRecyclerView.setLayoutManager(landGridLayoutManager);
        landRecyclerView.setNestedScrollingEnabled(false);
        landRecyclerView.setHasFixedSize(false);
        setPresenter(new LandPresenter(this, new BokdukRestInteractor()));
        naverMapSetting();
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
        if (id == KoinBaseAppbarDark.getLeftButtonId()) {
            onBackPressed();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        landPresenter.getLandList();

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
        Marker[] marker = new Marker[landList.size()];
        for (int i = 0; i < landList.size(); i++) {
            marker[i] = new Marker();

            Double latitude = landList.get(i).getLatitude();
            Double langitute = landList.get(i).getLongitude();
            if (latitude == null || langitute == null) {
                continue;
            }
            marker[i].setPosition(new LatLng(latitude, langitute));
            marker[i].setMap(naverMap);
            marker[i].setWidth(44);
            marker[i].setHeight(46);
            marker[i].setTag(Integer.toString(i));
            marker[i].setOnClickListener(overlay -> {
                Marker clickedMarker = (Marker) overlay;
                String index = (String) clickedMarker.getTag();
                if (index != null)
                    goToLandDetailByIndex(Integer.parseInt(index));
                return true;
            });
        }
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
        ToastUtil.makeShortToast(context, message);
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


}
