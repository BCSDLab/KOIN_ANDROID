package in.koreatech.koin.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import in.koreatech.koin.R;
import in.koreatech.koin.core.activity.ActivityBase;
import in.koreatech.koin.core.recyclerview.RecyclerViewClickListener;
import in.koreatech.koin.core.utils.StatusBarUtil;
import in.koreatech.koin.core.viewpager.ScaleViewPager;
import in.koreatech.koin.data.network.interactor.CityBusRestInteractor;
import in.koreatech.koin.data.network.interactor.TermRestInteractor;
import in.koreatech.koin.ui.bus.TimerRenewListener;
import in.koreatech.koin.ui.main.enums.DiningKinds;
import in.koreatech.koin.ui.main.presenter.MainActivityContact;
import in.koreatech.koin.ui.main.presenter.MainActivityPresenter;
import in.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity;
import in.koreatech.koin.ui.store.StoreActivity;
import in.koreatech.koin.util.BusTimerUtil;
import in.koreatech.koin.util.FirebasePerformanceUtil;
import in.koreatech.koin.util.TimeUtil;
import in.koreatech.koin.util.TimerListener;

public class MainActivity extends ActivityBase implements MainActivityContact.View, TimerRenewListener, SwipeRefreshLayout.OnRefreshListener, BusPagerAdapter.OnSwitchClickListener {
    private static String TAG = MainActivity.class.getSimpleName();
    public static final int REFRESH_TIME = 60; // 1분 갱신

    private MainActivityContact.Presenter presenter = null;
    private Unbinder unbinder;
    
    private int term;

    private BusTimerUtil citySoonBusTimerUtil;
    private BusTimerUtil daesungBusSoonBusTimerUtil;
    private BusTimerUtil shuttleBusSoonBusTimerUtil;
    private BusTimerUtil refreshBusTimerUtil;

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
    @BindViews({R.id.text_view_card_dining_korean, R.id.text_view_card_dining_onedish, R.id.text_view_card_dining_western, R.id.text_view_card_dining_special})
    List<TextView> textViewDiningKinds;

    @BindViews({R.id.text_view_card_dining_menu_0,
            R.id.text_view_card_dining_menu_1,
            R.id.text_view_card_dining_menu_2,
            R.id.text_view_card_dining_menu_3,
            R.id.text_view_card_dining_menu_4,
            R.id.text_view_card_dining_menu_5,
            R.id.text_view_card_dining_menu_6,
            R.id.text_view_card_dining_menu_7,
            R.id.text_view_card_dining_menu_8,
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

        citySoonBusTimerUtil.setTimerListener(value -> busPagerAdapter.updateCityBusTimeText(citySoonBusTimerUtil.getStrTime()));
        daesungBusSoonBusTimerUtil.setTimerListener(value ->
                busPagerAdapter.updateDaesungBusTimeText(daesungBusSoonBusTimerUtil.getStrTime()));
        shuttleBusSoonBusTimerUtil.setTimerListener(value -> busPagerAdapter.updateShuttleBusTimeText(shuttleBusSoonBusTimerUtil.getStrTime()));

        initBusPager();
        initStoreRecyclerView();
        initDining();
    }

    private void initBusPager() {
        busPagerAdapter = new BusPagerAdapter();
        busPagerAdapter.setOnSwitchClickListener(this);
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
        selectDiningKind(textViewDiningKinds.get(DiningKinds.KOREAN.getPosition()));
    }

    public void setPresenter() {
        this.presenter = new MainActivityPresenter(this, new CityBusRestInteractor(), new TermRestInteractor());
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



    @OnClick({R.id.text_view_card_dining_korean, R.id.text_view_card_dining_onedish, R.id.text_view_card_dining_western, R.id.text_view_card_dining_special})
    void selectDiningKind(View view) {
        for (TextView textView : textViewDiningKinds) textView.setSelected(false);
        view.setSelected(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(citySoonBusTimerUtil != null) citySoonBusTimerUtil.stopTimer();
        if(daesungBusSoonBusTimerUtil != null) daesungBusSoonBusTimerUtil.stopTimer();
        if(shuttleBusSoonBusTimerUtil != null) shuttleBusSoonBusTimerUtil.stopTimer();
        if(refreshBusTimerUtil != null) refreshBusTimerUtil.stopTimer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if(citySoonBusTimerUtil != null) citySoonBusTimerUtil.stopTimer();
        if(daesungBusSoonBusTimerUtil != null) daesungBusSoonBusTimerUtil.stopTimer();
        if(shuttleBusSoonBusTimerUtil != null) shuttleBusSoonBusTimerUtil.stopTimer();
        if(refreshBusTimerUtil != null) refreshBusTimerUtil.stopTimer();
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
            //citybusSoonDepartureTimeTextview.setVisibility(View.VISIBLE);
            //citybusSoonDepartureTimeTextview.setText("(" + TimeUtil.getAddTimeSecond(current) + ")분 출발");
            this.citySoonBusTimerUtil.setEndTime(current);
            busPagerAdapter.updateCityBusTimeText(citySoonBusTimerUtil.getStrTime());
            this.citySoonBusTimerUtil.startTimer();

        } else {
            //citybusSoonArrivalTimeTextview.setText(R.string.bus_no_information);
            //citybusSoonDepartureTimeTextview.setVisibility(View.INVISIBLE);
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
        }
    }

    @Override
    public void onSwitchClick() {
        onRefresh();
    }
}