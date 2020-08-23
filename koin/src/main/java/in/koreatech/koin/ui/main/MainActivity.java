package in.koreatech.koin.ui.main;

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
import in.koreatech.koin.core.utils.StatusBarUtil;
import in.koreatech.koin.core.viewpager.ScaleViewPager;
import in.koreatech.koin.data.network.interactor.CityBusRestInteractor;
import in.koreatech.koin.data.network.interactor.TermRestInteractor;
import in.koreatech.koin.ui.main.enums.DiningKinds;
import in.koreatech.koin.ui.main.presenter.MainActivityContact;
import in.koreatech.koin.ui.main.presenter.MainActivityPresenter;
import in.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity;
import in.koreatech.koin.util.FirebasePerformanceUtil;

public class MainActivity extends ActivityBase implements MainActivityContact.View {
    private static String TAG = MainActivity.class.getSimpleName();
    private MainActivityContact.Presenter presenter = null;
    private Unbinder unbinder;

    private int departureState = 0; // 0 : 한기대 1 : 야우리 2 : 천안역
    private int arrivalState = 1; // 0 : 한기대 1 : 야우리 2 : 천안역

    private FirebasePerformanceUtil firebasePerformanceUtil;


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

    private void init() {
        unbinder = ButterKnife.bind(this);
        setPresenter();
        StatusBarUtil.applyTopPaddingStatusBarHeight(toolbarLayout, getResources());

        this.firebasePerformanceUtil = new FirebasePerformanceUtil("Bus_Activity");
        this.firebasePerformanceUtil.start();

        initBusPager();
        initStoreRecyclerView();
        initDining();
    }

    private void initBusPager() {
        busPagerAdapter = new BusPagerAdapter();
        busCardViewPager.setAdapter(busPagerAdapter);
        busCardViewPager.setCurrentItem(Integer.MAX_VALUE / 2);
        busCardViewPager.setOffscreenPageLimit(5);
    }

    private void initStoreRecyclerView() {
        recyclerViewStoreCategory.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerViewStoreCategory.setAdapter(new StoreCategoryRecyclerAdapter());
    }

    private void initDining() {
        selectDiningKind(textViewDiningKinds.get(DiningKinds.KOREAN.getPosition()));
    }

    public void setPresenter() {
        this.presenter = new MainActivityPresenter(this, new CityBusRestInteractor(), new TermRestInteractor());
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
        if (this.presenter != null) {
            showLoading();
            this.presenter.getCityBus(this.departureState, this.arrivalState);
            this.presenter.getDaesungBus(this.departureState, this.arrivalState);
            this.presenter.getTermInfo();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

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
        busPagerAdapter.updateShuttleBusTime(current);
    }

    @Override
    public void updateCityBusTime(int current) {
        busPagerAdapter.updateCityBusTime(current);
    }

    @Override
    public void updateDaesungBusTime(int current) {
        busPagerAdapter.updateDaesungBusTime(current);
    }

    @Override
    public void updateShuttleBusDepartInfo(String current) {
        busPagerAdapter.updateShuttleBusDepartInfo(current);
    }

    @Override
    public void updateCityBusDepartInfo(int current) {
        busPagerAdapter.updateCityBusDepartInfo(current);
    }

    @Override
    public void updateDaesungBusDepartInfo(String current) {
        busPagerAdapter.updateDaesungBusDepartInfo(current);
    }

    @Override
    public void updateFailDaesungBusDepartInfo() {

    }

    @Override
    public void updateFailShuttleBusDepartInfo() {

    }

    @Override
    public void updateFailCityBusDepartInfo() {

    }

    @Override
    public void updateShuttleBusInfo(int term) {

    }

    @Override
    public void setPresenter(MainActivityContact.Presenter presenter) {
        this.presenter = presenter;
    }
}