package in.koreatech.koin.ui.main;

import static in.koreatech.koin.util.DiningUtil.TYPE;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import in.koreatech.koin.R;
import in.koreatech.koin.core.recyclerview.RecyclerViewClickListener;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.core.viewpager.ScaleViewPager;
import in.koreatech.koin.data.network.entity.Dining;
import in.koreatech.koin.data.network.interactor.CityBusRestInteractor;
import in.koreatech.koin.data.network.interactor.DiningRestInteractor;
import in.koreatech.koin.data.network.interactor.TermRestInteractor;
import in.koreatech.koin.ui.main.presenter.MainActivityContact;
import in.koreatech.koin.ui.main.presenter.MainActivityPresenter;
import in.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity;
import in.koreatech.koin.ui.navigation.state.MenuState;
import in.koreatech.koin.util.TimeUtil;
import in.koreatech.koin.util.timer.CountTimer;
import in.koreatech.koin.util.timer.TimerManager;

public class MainActivity extends KoinNavigationDrawerActivity implements
        MainActivityContact.View,
        SwipeRefreshLayout.OnRefreshListener,
        BusPagerAdapter.OnSwitchClickListener,
        BusPagerAdapter.OnCardClickListener, CountTimer.OnTimerListener {
    public static final int REFRESH_TIME = 60000; // 1분 갱신
    public static final int SECOND = 1000; //1초
    public static final String CITY_SOON_BUS = "CITY_SOON_BUS";
    public static final String DAESUNG_SOON_BUS = "DAESUNG_SOON_BUS";
    public static final String SHUTTLE_SOON_BUS = "SHUTTLE_SOON_BUS";
    public static final String REFRESH = "REFRESH";
    public final static String[] ENDTIME = {"09:00", "13:30", "18:30"};   // 아침, 점심, 저녁 식당 운영 종료시간 및 초기화 시간
    private final static String[] PLACES = {"한식", "일품식", "양식", "특식", "능수관", "수박여", "2캠퍼스"};
    private static String TAG = MainActivity.class.getSimpleName();
    private final int LIMITDATE = 7; // 식단 조회 제한 날짜
    TimerManager timerManager;
    @BindView(R.id.toolbar)
    MaterialCardView toolbar;
    @BindView(R.id.toolbar_layout)
    FrameLayout toolbarLayout;
    @BindView(R.id.main_swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    BusPagerAdapter busPagerAdapter;
    //버스
    @BindView(R.id.main_view_pager)
    ScaleViewPager busCardViewPager;
    //상점
    @BindView(R.id.recycler_view_store_category)
    RecyclerView recyclerViewStoreCategory;
    //학식
    @BindViews({R.id.text_view_card_dining_korean, R.id.text_view_card_dining_onedish, R.id.text_view_card_dining_western, R.id.text_view_card_dining_neungsugwan, R.id.text_view_card_dining_subakyeo, R.id.text_view_card_dining_2campus})
    List<TextView> textViewDiningPlaces;
    @BindView(R.id.view_empty_dining)
    View viewEmptyDining;
    @BindView(R.id.text_view_card_dining_time)
    TextView textViewCardDiningTime;
    @BindView(R.id.dining_container)
    View viewDiningContainer;
    @BindViews({R.id.text_view_card_dining_menu_0,
            R.id.text_view_card_dining_menu_2,
            R.id.text_view_card_dining_menu_4,
            R.id.text_view_card_dining_menu_6,
            R.id.text_view_card_dining_menu_8,
            R.id.text_view_card_dining_menu_1,
            R.id.text_view_card_dining_menu_3,
            R.id.text_view_card_dining_menu_5,
            R.id.text_view_card_dining_menu_7,
            R.id.text_view_card_dining_menu_9})
    List<TextView> textViewDiningMenus;
    private String today;
    private int changeDate;
    private String currentDiningType;
    private int currentDiningPlacePosition = 0;
    private String currentDiningPlace = "";
    private MainActivityContact.Presenter presenter = null;
    private Unbinder unbinder;
    private int term;
    private Map<String, ArrayList<Dining>> diningMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        timerManager.addTimer(REFRESH, REFRESH_TIME, (name, millisUntilFinished) -> {

        });
    }

    private void init() {
        timerManager = new TimerManager();
        unbinder = ButterKnife.bind(this);
        setPresenter();
        swipeRefreshLayout.setOnRefreshListener(this);
        initBusPager();
        initStoreRecyclerView();
        initDining();
    }

    private void initBusPager() {
        busPagerAdapter = new BusPagerAdapter(this);
        busPagerAdapter.setOnSwitchClickListener(this);
        busPagerAdapter.setOnCardClickListener(this);
        busCardViewPager.setAdapter(busPagerAdapter);
        busCardViewPager.setCurrentItem(Integer.MAX_VALUE / 2);
        busCardViewPager.setOffscreenPageLimit(3);
    }

    private void initStoreRecyclerView() {
        recyclerViewStoreCategory.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        StoreCategoryRecyclerAdapter adapter = new StoreCategoryRecyclerAdapter();
        recyclerViewStoreCategory.setAdapter(adapter);
        adapter.setRecyclerViewClickListener(new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                gotoStoreActivity(position);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        });
    }

    private void initDining() {
        today = TimeUtil.getDeviceCreatedDateOnlyString();
        changeDate = 0;
        selectDiningKind(findViewById(R.id.text_view_card_dining_korean));
        viewDiningContainer.setOnClickListener((view) -> {
            callDrawerItem(R.id.navi_item_dining);
        });
    }

    public void setPresenter() {
        this.presenter = new MainActivityPresenter(this, new CityBusRestInteractor(), new TermRestInteractor(), new DiningRestInteractor());
    }

    private void gotoStoreActivity(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("store_category", position);
        callDrawerItem(R.id.navi_item_store, bundle);
    }

    @OnClick(R.id.button_category)
    void onClickCategory(View view) {
        toggleNavigationDrawer();
    }

    @OnClick({R.id.text_view_card_dining_korean, R.id.text_view_card_dining_onedish, R.id.text_view_card_dining_western, R.id.text_view_card_dining_neungsugwan, R.id.text_view_card_dining_subakyeo, R.id.text_view_card_dining_2campus})
    void selectDiningKind(View view) {
        for (int i = 0; i < textViewDiningPlaces.size(); i++) {
            TextView textView = textViewDiningPlaces.get(i);
            if (textView.getId() == view.getId()) {
                textView.setSelected(true);
                currentDiningPlacePosition = i;
                currentDiningPlace = textView.getText().toString();
            } else {
                textView.setSelected(false);
            }
        }
        selectDiningKind(currentDiningPlacePosition);
        updateDiningList();
    }

    void selectDiningKind(int placePosition) {
        for (TextView textView : textViewDiningPlaces) textView.setSelected(false);
        textViewDiningPlaces.get(placePosition).setSelected(true);
        currentDiningPlacePosition = placePosition;
    }

    void updateDiningTypeTextView() {
        if (currentDiningType.equals(TYPE[0])) textViewCardDiningTime.setText("아침");
        else if (currentDiningType.equals(TYPE[1])) textViewCardDiningTime.setText("점심");
        else if (currentDiningType.equals(TYPE[2])) textViewCardDiningTime.setText("저녁");
    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (timerManager != null) timerManager.stopAllTimer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void showLoading() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void updateShuttleBusTime(int current) {
        if (current >= 0) {
            timerManager.addTimer(SHUTTLE_SOON_BUS, current * 1000, busPagerAdapter);
            timerManager.startTimer(SHUTTLE_SOON_BUS);
        } else {
            timerManager.stopTimer(SHUTTLE_SOON_BUS);
            busPagerAdapter.updateShuttleBusTimeText(getString(R.string.bus_no_information));
        }
    }

    @Override
    public void updateCityBusTime(int current) {
        if (current > 0) {
            timerManager.addTimer(CITY_SOON_BUS, current * 1000, busPagerAdapter);
            timerManager.startTimer(CITY_SOON_BUS);
        } else {
            timerManager.stopTimer(CITY_SOON_BUS);
        }
    }

    @Override
    public void updateCityBusDepartInfo(int current) {
        busPagerAdapter.updateCityBusDepartInfoText(current);
    }

    @Override
    public void updateDaesungBusTime(int current) {
        if (current >= 0) {
            timerManager.addTimer(DAESUNG_SOON_BUS, current * 1000, busPagerAdapter);
            timerManager.startTimer(DAESUNG_SOON_BUS);
        } else {
            timerManager.stopTimer(DAESUNG_SOON_BUS);
            busPagerAdapter.updateDaesungBusTimeText(getString(R.string.bus_no_information));
        }
    }

    @Override
    public void updateShuttleBusDepartInfo(String current) {
        busPagerAdapter.updateShuttleBusDepartInfoText(current);
    }

    @Override
    public void updateDaesungBusDepartInfo(String current) {
        busPagerAdapter.updateDaesungBusDepartInfoText(current);
    }

    @Override
    public void updateFailDaesungBusDepartInfo() {
        timerManager.stopTimer(DAESUNG_SOON_BUS);
        busPagerAdapter.updateDaesungBusTimeText(getString(R.string.bus_no_information));
        busPagerAdapter.updateDaesungBusDepartInfoText("");
    }

    @Override
    public void updateFailShuttleBusDepartInfo() {
        timerManager.stopTimer(SHUTTLE_SOON_BUS);
        busPagerAdapter.updateShuttleBusTimeText(getString(R.string.bus_no_information));
        busPagerAdapter.updateShuttleBusDepartInfoText("");
    }

    @Override
    public void updateFailCityBusDepartInfo() {
        timerManager.stopTimer(CITY_SOON_BUS);
        busPagerAdapter.updateCityBusTimeText(getString(R.string.bus_no_information));
        busPagerAdapter.updateCityBusDepartInfoText(0);
    }

    @Override
    public void updateShuttleBusInfo(int term) {
        this.term = term;
        presenter.getShuttleBus(busPagerAdapter.getDepartureState(), busPagerAdapter.getArrivalState(), term);
    }

    @Override
    public void showNetworkError() {
        diningMap.clear();
        showEmptyDining();
        ToastUtil.getInstance().makeShort(R.string.error_network);
    }

    /**
     * 현재 시간과 아침,점심,저녁의 운영 종료 시각을 비교하여 현재에 맞는 식단 TYPE 을 정해주는 메소드
     *
     * @return TYPE[] 에 저장된 BREAKFAST, LAUNCH, DINNER
     */
    private boolean setCurrentDiningType() {
        if (TimeUtil.timeCompareWithCurrent(false, ENDTIME[0]) >= 0) {  // 0시부터 9시 전까지 BREAKFAST
            currentDiningType = TYPE[0];
            return true;
        } else if (TimeUtil.timeCompareWithCurrent(false, ENDTIME[1]) >= 0) { // 9시부터 13시 30분까지 LUNCH
            currentDiningType = TYPE[1];
            return true;
        } else if (TimeUtil.timeCompareWithCurrent(false, ENDTIME[2]) >= 0) {    // 13시 30분부터 18시 30분까지 DINNER
            currentDiningType = TYPE[2];
            return true;
        } else {  // 18:30 분부터 23:59분까지 다음날 BREAKFAST
            currentDiningType = TYPE[0];
            return getNextDiningMenu();
        }
    }

    public boolean getNextDiningMenu() {
        if (changeDate < LIMITDATE) {
            changeDate++;
            today = TimeUtil.getChangeDate(changeDate);
            return true;
        } else {
            showEmptyDining();
        }
        return false;
    }


    @Override
    public void onDiningListDataReceived(ArrayList<Dining> diningArrayList) {
        updateDiningTypeTextView();
        Dining selectedDining = null;
        for (Dining dining : diningArrayList) {
            if (dining.getType().equals(currentDiningType) && dining.getPlace().equals(currentDiningPlace)) {
                selectedDining = dining;
                break;
            }
        }
        if (selectedDining == null) {
            showEmptyDining();
        } else {
            hideEmptyDining();
            for (int i = 0; i < textViewDiningMenus.size(); i++) {
                if (selectedDining.getMenu().size() > i) {
                    this.textViewDiningMenus.get(i).setText(selectedDining.getMenu().get(i));
                } else {
                    this.textViewDiningMenus.get(i).setText("");
                }
            }
        }

        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showEmptyDining() {
        viewEmptyDining.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyDining() {
        viewEmptyDining.setVisibility(View.GONE);
    }

    @Override
    public void setPresenter(MainActivityContact.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onRefresh() {
        if (this.presenter != null) {
            this.presenter.getCityBus(busPagerAdapter.getDepartureState(), busPagerAdapter.getArrivalState());
            this.presenter.getDaesungBus(busPagerAdapter.getDepartureState(), busPagerAdapter.getArrivalState());
            this.presenter.getTermInfo();
            updateDiningList();
        }
    }

    private void refreshBusViews() {
        if (this.presenter != null) {
            this.presenter.getCityBus(busPagerAdapter.getDepartureState(), busPagerAdapter.getArrivalState());
            this.presenter.getDaesungBus(busPagerAdapter.getDepartureState(), busPagerAdapter.getArrivalState());
            this.presenter.getTermInfo();
        }
    }

    private void updateDiningList() {
        if (setCurrentDiningType())
            presenter.getDiningList(TimeUtil.getChangeDateFormatYYMMDD(changeDate));
    }

    @Override
    public void onSwitchClick() {
        onRefresh();
    }

    @Override
    public void onCardClick(BusPagerAdapter.BusKind busKind) {
        Bundle bundle = new Bundle();
        bundle.putInt("departure", busPagerAdapter.getDepartureState());
        bundle.putInt("arrival", busPagerAdapter.getArrivalState());
        callDrawerItem(R.id.navi_item_bus, bundle);
    }

    @Override
    public void onCountEvent(String name, long millisUntilFinished) {
        if (name.equals(REFRESH)) refreshBusViews();
    }

    @NonNull
    @Override
    protected MenuState getMenuState() {
        return MenuState.Main.INSTANCE;
    }
}