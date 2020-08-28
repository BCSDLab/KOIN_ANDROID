package in.koreatech.koin.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

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
import in.koreatech.koin.core.activity.ActivityBase;
import in.koreatech.koin.core.recyclerview.RecyclerViewClickListener;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.core.utils.StatusBarUtil;
import in.koreatech.koin.core.viewpager.ScaleViewPager;
import in.koreatech.koin.data.network.entity.Dining;
import in.koreatech.koin.data.network.interactor.CityBusRestInteractor;
import in.koreatech.koin.data.network.interactor.DiningRestInteractor;
import in.koreatech.koin.data.network.interactor.TermRestInteractor;
import in.koreatech.koin.ui.bus.BusActivity;
import in.koreatech.koin.ui.bus.TimerRenewListener;
import in.koreatech.koin.ui.dining.DiningActivity;
import in.koreatech.koin.ui.main.presenter.MainActivityContact;
import in.koreatech.koin.ui.main.presenter.MainActivityPresenter;
import in.koreatech.koin.ui.store.StoreActivity;
import in.koreatech.koin.util.BusTimerUtil;
import in.koreatech.koin.util.DiningUtil;
import in.koreatech.koin.util.TimeUtil;

import static in.koreatech.koin.util.DiningUtil.TYPE;

public class MainActivity extends ActivityBase implements
        MainActivityContact.View,
        TimerRenewListener,
        SwipeRefreshLayout.OnRefreshListener,
        BusPagerAdapter.OnSwitchClickListener,
        BusPagerAdapter.OnCardClickListener {
    private static String TAG = MainActivity.class.getSimpleName();
    public static final int REFRESH_TIME = 60; // 1분 갱신
    private final int LIMITDATE = 7; // 식단 조회 제한 날짜

    public final static String[] ENDTIME = {"09:00", "13:30", "18:30"};   // 아침, 점심, 저녁 식당 운영 종료시간 및 초기화 시간
    private final static String[] PLACES = {"한식", "일품식", "양식", "특식", "능수관", "수박여", "2캠퍼스"};
    private String today;
    private int changeDate;
    private String currentDiningType;
    private int currentDiningPlacePosition = 0;

    private MainActivityContact.Presenter presenter = null;
    private Unbinder unbinder;

    private int term;

    private BusTimerUtil citySoonBusTimerUtil;
    private BusTimerUtil daesungBusSoonBusTimerUtil;
    private BusTimerUtil shuttleBusSoonBusTimerUtil;
    private BusTimerUtil refreshBusTimerUtil;

    private Map<String, ArrayList<Dining>> diningMap = new HashMap<>();

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
    @BindViews({R.id.text_view_card_dining_korean, R.id.text_view_card_dining_onedish, R.id.text_view_card_dining_western, R.id.text_view_card_dining_special, R.id.text_view_card_dining_neungsugwan, R.id.text_view_card_dining_subakyeo, R.id.text_view_card_dining_2campus})
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (refreshBusTimerUtil != null) {
            refreshBusTimerUtil.setEndTime(REFRESH_TIME); // 5분마다 갱신
            refreshBusTimerUtil.startTimer();
        }
    }

    private void init() {
        unbinder = ButterKnife.bind(this);
        setPresenter();
        StatusBarUtil.applyTopPaddingStatusBarHeight(toolbarLayout, getResources());

        swipeRefreshLayout.setOnRefreshListener(this);

        citySoonBusTimerUtil = new BusTimerUtil(10);
        daesungBusSoonBusTimerUtil = new BusTimerUtil(11);
        shuttleBusSoonBusTimerUtil = new BusTimerUtil(12);
        refreshBusTimerUtil = new BusTimerUtil(13);

        citySoonBusTimerUtil.setTimerRenewListener(this);
        daesungBusSoonBusTimerUtil.setTimerRenewListener(this);
        shuttleBusSoonBusTimerUtil.setTimerRenewListener(this);
        refreshBusTimerUtil.setTimerRenewListener(this);

        citySoonBusTimerUtil.setTimerListener(value -> busPagerAdapter.updateCityBusTimeText(value));
        daesungBusSoonBusTimerUtil.setTimerListener(value -> busPagerAdapter.updateDaesungBusTimeText(value));
        shuttleBusSoonBusTimerUtil.setTimerListener(value -> busPagerAdapter.updateShuttleBusTimeText(value));

        initBusPager();
        initStoreRecyclerView();
        initDining();
    }

    private void initBusPager() {
        busPagerAdapter = new BusPagerAdapter();
        busPagerAdapter.setOnSwitchClickListener(this);
        busPagerAdapter.setOnCardClickListener(this);
        busCardViewPager.setAdapter(busPagerAdapter);
        busCardViewPager.setCurrentItem(Integer.MAX_VALUE / 2);
        busCardViewPager.setOffscreenPageLimit(5);
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
        viewDiningContainer.setOnClickListener((view) -> {
            Intent intent = new Intent(MainActivity.this, DiningActivity.class);
            startActivity(intent);
        });
    }

    public void setPresenter() {
        this.presenter = new MainActivityPresenter(this, new CityBusRestInteractor(), new TermRestInteractor(), new DiningRestInteractor());
    }

    private void gotoStoreActivity(int position) {
        Intent intent = new Intent(MainActivity.this, StoreActivity.class);
        intent.putExtra("store_category", position);
        startActivity(intent);
    }

    /*@OnClick(R.id.button_category)
    void onClickCategory(View view) {
        toggleNavigationDrawer();
    }*/

    @OnClick({R.id.text_view_card_dining_korean, R.id.text_view_card_dining_onedish, R.id.text_view_card_dining_western, R.id.text_view_card_dining_special, R.id.text_view_card_dining_neungsugwan, R.id.text_view_card_dining_subakyeo, R.id.text_view_card_dining_2campus})
    void selectDiningKind(View view) {
        for (int i = 0; i < textViewDiningPlaces.size(); i++) {
            TextView textView = textViewDiningPlaces.get(i);
            if(textView.getId() == view.getId()) {
                textView.setSelected(true);
                currentDiningPlacePosition = i;
            }
            else textView.setSelected(false);
        }
        updateUserInterface(currentDiningPlacePosition);
    }

    void selectDiningKind(int placePosition) {
        for (TextView textView : textViewDiningPlaces) textView.setSelected(false);
        textViewDiningPlaces.get(placePosition).setSelected(true);
        currentDiningPlacePosition = placePosition;
        updateUserInterface(currentDiningPlacePosition);
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
        if (citySoonBusTimerUtil != null) citySoonBusTimerUtil.stopTimer();
        if (daesungBusSoonBusTimerUtil != null) daesungBusSoonBusTimerUtil.stopTimer();
        if (shuttleBusSoonBusTimerUtil != null) shuttleBusSoonBusTimerUtil.stopTimer();
        if (refreshBusTimerUtil != null) refreshBusTimerUtil.stopTimer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if (citySoonBusTimerUtil != null) citySoonBusTimerUtil.stopTimer();
        if (daesungBusSoonBusTimerUtil != null) daesungBusSoonBusTimerUtil.stopTimer();
        if (shuttleBusSoonBusTimerUtil != null) shuttleBusSoonBusTimerUtil.stopTimer();
        if (refreshBusTimerUtil != null) refreshBusTimerUtil.stopTimer();
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
            shuttleBusSoonBusTimerUtil.setEndTime(current);
            busPagerAdapter.updateShuttleBusTimeText(shuttleBusSoonBusTimerUtil.getStrTime());
            shuttleBusSoonBusTimerUtil.startTimer();
        } else {
            shuttleBusSoonBusTimerUtil.stopTimer();
            busPagerAdapter.updateShuttleBusTimeText(getString(R.string.bus_no_information));
        }

    }

    @Override
    public void updateCityBusTime(int current) {
        if (current > 0) {
            this.citySoonBusTimerUtil.setEndTime(current);
            busPagerAdapter.updateCityBusTimeText(citySoonBusTimerUtil.getStrTime());
            this.citySoonBusTimerUtil.startTimer();
        } else {
            this.citySoonBusTimerUtil.stopTimer();
        }
    }

    @Override
    public void updateCityBusDepartInfo(int current) {
        busPagerAdapter.updateCityBusDepartInfoText(current);
    }

    @Override
    public void updateDaesungBusTime(int current) {
        if (current > 0) {
            daesungBusSoonBusTimerUtil.setEndTime(current);
            busPagerAdapter.updateDaesungBusTimeText(daesungBusSoonBusTimerUtil.getStrTime());
            daesungBusSoonBusTimerUtil.startTimer();
        } else {
            daesungBusSoonBusTimerUtil.stopTimer();
            busPagerAdapter.updateDaesungBusDepartInfoText(getString(R.string.bus_no_information));
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
        daesungBusSoonBusTimerUtil.stopTimer();
        busPagerAdapter.updateDaesungBusTimeText(getString(R.string.bus_no_information));
        busPagerAdapter.updateDaesungBusDepartInfoText("");
    }

    @Override
    public void updateFailShuttleBusDepartInfo() {
        daesungBusSoonBusTimerUtil.stopTimer();
        busPagerAdapter.updateShuttleBusTimeText(getString(R.string.bus_no_information));
        busPagerAdapter.updateShuttleBusDepartInfoText("");
    }

    @Override
    public void updateFailCityBusDepartInfo() {
        daesungBusSoonBusTimerUtil.stopTimer();
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
        } else showEmptyDining();
        return false;
    }


    @Override
    public void onDiningListDataReceived(ArrayList<Dining> diningArrayList) {
        diningMap.clear();
        for(TextView textView : textViewDiningPlaces) textView.setVisibility(View.GONE);

        if (!diningArrayList.isEmpty()) {
            for (Dining dining : DiningUtil.arrangeDinings(diningArrayList)) {
                if (dining == null ) {
                    continue;
                }

                String typeTemp = dining.getType();
                if(!typeTemp.equals(currentDiningType)) continue;

                String placeTemp = dining.getPlace();

                if (diningMap.containsKey(placeTemp)) {
                    diningMap.get(placeTemp).add(dining);
                } else {
                    ArrayList<Dining> dinings = new ArrayList<>();
                    dinings.add(dining);
                    diningMap.put(placeTemp, dinings);
                    for(int i = 0; i < PLACES.length; i++) {
                        if(PLACES[i].equals(placeTemp)) textViewDiningPlaces.get(i).setVisibility(View.VISIBLE);
                    }
                }
            }
            hideEmptyDining();
            selectDiningKind(currentDiningPlacePosition);
        } else {
            showEmptyDining();
        }

        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void updateUserInterface(int placePosition) {
        if(diningMap.containsKey(PLACES[placePosition])) {
            for(TextView textView : textViewDiningMenus) textView.setText("");
            List<String> dinings = diningMap.get(PLACES[placePosition]).get(0).getMenu();
            for(int i = 0; i < Math.min(textViewDiningMenus.size(), dinings.size()); i++) {
                textViewDiningMenus.get(i).setText(dinings.get(i));
            }
        } else {
            if(placePosition < PLACES.length - 1) selectDiningKind(++placePosition);
            else showEmptyDining();
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
    public void refreshTimer(int code) {
        if (code == 10)
            presenter.getCityBus(busPagerAdapter.getDepartureState(), busPagerAdapter.getArrivalState());
        else if (code == 11)
            presenter.getDaesungBus(busPagerAdapter.getDepartureState(), busPagerAdapter.getArrivalState());
        else if (code == 12)
            presenter.getTermInfo();
        else if (code == 13) {
            presenter.getCityBus(busPagerAdapter.getDepartureState(), busPagerAdapter.getArrivalState());
            presenter.getDaesungBus(busPagerAdapter.getDepartureState(), busPagerAdapter.getArrivalState());
            presenter.getTermInfo();
            refreshBusTimerUtil.setEndTime(REFRESH_TIME);
            refreshBusTimerUtil.startTimer();
        }
    }

    @Override
    public void onRefresh() {
        if (this.presenter != null) {
            showLoading();
            this.presenter.getCityBus(busPagerAdapter.getDepartureState(), busPagerAdapter.getArrivalState());
            this.presenter.getDaesungBus(busPagerAdapter.getDepartureState(), busPagerAdapter.getArrivalState());
            this.presenter.getTermInfo();
            if(setCurrentDiningType()) presenter.getDiningList(TimeUtil.getChangeDateFormatYYMMDD(changeDate));
            updateDiningTypeTextView();
        }
    }

    @Override
    public void onSwitchClick() {
        onRefresh();
    }

    @Override
    public void onCardClick(BusPagerAdapter.BusKind busKind) {
        Intent intent = new Intent(MainActivity.this, BusActivity.class);
        intent.putExtra("departure", busPagerAdapter.getDepartureState());
        intent.putExtra("arrival", busPagerAdapter.getArrivalState());
        startActivity(intent);
    }
}