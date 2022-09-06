package in.koreatech.koin.ui.dining;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.perf.metrics.AddTrace;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.R;
import in.koreatech.koin.core.appbar.AppBarBase;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.data.network.entity.Dining;
import in.koreatech.koin.data.network.interactor.DiningRestInteractor;
import in.koreatech.koin.ui.dining.adapter.DiningRecyclerAdapter;
import in.koreatech.koin.ui.dining.presenter.DiningContract;
import in.koreatech.koin.ui.dining.presenter.DiningPresenter;
import in.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity;
import in.koreatech.koin.ui.navigation.state.MenuState;
import in.koreatech.koin.util.DiningUtil;
import in.koreatech.koin.util.TimeUtil;

public class DiningActivity extends KoinNavigationDrawerActivity implements DiningContract.View, SwipeRefreshLayout.OnRefreshListener {
    private final static String[] TYPE = {"BREAKFAST", "LUNCH", "DINNER"};
    private final static String[] ENDTIME = {"09:00", "13:30", "18:30"};   // 아침, 점심, 저녁 식당 운영 종료시간 및 초기화 시간
    private final String TAG = "DiningActivity";
    private final int LIMITDATE = 7; // 식단 조회 제한 날짜
    private final int SWIPE_THRESHOLD = 100;
    private final int SWIPE_VELOCITY_THRESHOLD = 100;
    @BindView(R.id.dining_breakfast_button)
    TextView breakfastButton;
    @BindView(R.id.dining_lunch_button)
    TextView lunchButton;
    @BindView(R.id.dining_dinner_button)
    TextView dinnerButton;
    @BindView(R.id.dining_swiperefreshlayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.dining_recyclerview)
    RecyclerView diningListRecyclerView;
    @BindView(R.id.dining_date_textView)
    TextView diningDateTextView;
    @BindView(R.id.empty_dining_list_frameLayout)
    FrameLayout emptyBoardListFrameLayout;
    @BindView(R.id.dining_next_date_button)
    Button nextDateButton;
    @BindView(R.id.dining_before_date_button)
    Button beforeDateButton;


    /* View Component */
    @BindView(R.id.koin_base_app_bar_dark)
    AppBarBase appbarBase;
    private Context context;
    private DiningRecyclerAdapter diningRecyclerAdapter;
    private GestureDetector gestureDetector;
    private String today;
    private DiningPresenter diningPresenter;
    private RecyclerView.LayoutManager layoutManager; // RecyclerView LayoutManager
    private Map<String, ArrayList<Dining>> diningMap;
    private int typeIndex; //0 아침 / 1 점심 / 2 저녁
    private int changeDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dining_activity_main);
        ButterKnife.bind(this);
        context = this;
        init();

        //TODO : 영업 시간 표시
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG, today);
        diningPresenter.getDiningList(TimeUtil.getChangeDateFormatYYMMDD(changeDate));
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
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void init() {
        today = TimeUtil.getDeviceCreatedDateOnlyString();
        swipeRefreshLayout.setOnRefreshListener(this);
        changeDate = 0; // 현재날짜와 바뀐 날짜 비교
        appbarBase.setTitleText("식단");

        layoutManager = new LinearLayoutManager(this);
        diningMap = new HashMap<String, ArrayList<Dining>>();
        typeIndex = 0;

        diningRecyclerAdapter = new DiningRecyclerAdapter(context, new ArrayList<Dining>());

        diningListRecyclerView.setHasFixedSize(true);
        diningListRecyclerView.setLayoutManager(layoutManager);
        diningListRecyclerView.setAdapter(diningRecyclerAdapter);
        diningListRecyclerView.setNestedScrollingEnabled(false);
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return handleGestureEvent(e1, e2, velocityX, velocityY);
            }
        });


        setPresenter(new DiningPresenter(this, new DiningRestInteractor()));
        diningPresenter.getDiningList(today);


        switch (getCurrentDiningType()) {
            case "BREAKFAST":
                onClickBreakfastButton();
                break;
            case "LUNCH":
                onClickLunchButton();
                break;
            case "DINNER":
                onClickDinnerButton();
                break;
        }
    }

    /**
     * 현재 시간과 아침,점심,저녁의 운영 종료 시각을 비교하여 현재에 맞는 식단 TYPE 을 정해주는 메소드
     *
     * @return TYPE[] 에 저장된 BREAKFAST, LAUNCH, DINNER
     */
    private String getCurrentDiningType() {
        if (TimeUtil.timeCompareWithCurrent(false, ENDTIME[0]) >= 0) {  // 0시부터 9시 전까지 BREAKFAST
            return TYPE[0];
        } else if (TimeUtil.timeCompareWithCurrent(false, ENDTIME[1]) >= 0) { // 9시부터 13시 30분까지 LUNCH
            return TYPE[1];
        } else if (TimeUtil.timeCompareWithCurrent(false, ENDTIME[2]) >= 0) {    // 13시 30분부터 18시 30분까지 DINNER
            return TYPE[2];
        } else {  // 18:30 분부터 23:59분까지 다음날 BREAKFAST
            getNextDiningMenu();
            return TYPE[0];
        }
    }

    private boolean handleGestureEvent(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        boolean result = false;
        try {
            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        onClickBeforeButton();
                    } else {
                        onClickNextButton();
                    }
                    result = true;
                }
            }
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
        return result;
    }

    @Override
    public void setPresenter(DiningPresenter presenter) {
        this.diningPresenter = presenter;
    }


    @Override
    public void showLoading() {
        showProgressDialog(R.string.loading);
    }

    @Override
    public void hideLoading() {
        hideProgressDialog();
    }

    @Override
    public void showMessage(String message) {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }

        ToastUtil.getInstance().makeShort(message);
    }

    @AddTrace(name = "DiningActivity_onDiningListDataReceived")
    @Override
    public void showNetworkError() {
        diningMap.clear();
        diningRecyclerAdapter.notifyDataSetChanged();
        emptyBoardListFrameLayout.setVisibility(View.VISIBLE);
        ToastUtil.getInstance().makeShort(R.string.error_network);
    }

    @Override
    public void onDiningListDataReceived(ArrayList<Dining> diningArrayList) {
        this.diningMap.clear();

        for (Dining dining : DiningUtil.arrangeDinings(diningArrayList)) {
            if (dining == null) {
                continue;
            }

            String typeTemp = dining.getType();
            if (diningMap.containsKey(typeTemp)) {
                diningMap.get(typeTemp).add(dining);
            } else {
                ArrayList<Dining> dinings = new ArrayList<Dining>();
                dinings.add(dining);
                diningMap.put(dining.getType(), dinings);
            }
        }

        if (!diningArrayList.isEmpty()) {
            emptyBoardListFrameLayout.setVisibility(View.GONE);
        } else {
            emptyBoardListFrameLayout.setVisibility(View.VISIBLE);
        }
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        updateUserInterface();
    }

    @OnClick(R.id.koin_base_app_bar_dark)
    public void onClickedKoinBaseAppbar(View v) {
        int id = v.getId();
        if (id == AppBarBase.getLeftButtonId()) {
            callDrawerItem(R.id.navi_item_home);
        } else if (id == AppBarBase.getRightButtonId()) {
            toggleNavigationDrawer();
        }
    }

    @Override
    public void updateUserInterface() {
        StringBuilder sb = new StringBuilder();
        try {
            try { //00월 00일 (화)
                sb.append(TimeUtil.getMMDDE(today));
            } catch (ParseException e) { //00월 00일
                sb.append(today.substring(5, 7)).append("월 ").append(today.substring(8, 9)).append("일");
                e.printStackTrace();
            }
            diningDateTextView.setText(today);

            if (diningMap.get(TYPE[typeIndex]) != null && diningMap.get(TYPE[typeIndex]).size() != 0) {
                diningRecyclerAdapter = new DiningRecyclerAdapter(context, diningMap.get(TYPE[typeIndex]));
                diningListRecyclerView.setAdapter(diningRecyclerAdapter);
                emptyBoardListFrameLayout.setVisibility(View.GONE);
            } else {
                diningRecyclerAdapter = new DiningRecyclerAdapter(context, new ArrayList<Dining>());
                diningListRecyclerView.setAdapter(diningRecyclerAdapter);
                emptyBoardListFrameLayout.setVisibility(View.VISIBLE);
            }
        } catch (NullPointerException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public void onRefresh() {
        diningPresenter.getDiningList(TimeUtil.getChangeDateFormatYYMMDD(changeDate));
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @OnClick(R.id.dining_breakfast_button)
    public void onClickBreakfastButton() {
        typeIndex = 0;
        breakfastButton.setText(Html.fromHtml(colorText("#f7941e", "아침")), TextView.BufferType.SPANNABLE);
        lunchButton.setText("점심");
        dinnerButton.setText("저녁");
        updateUserInterface();
    }

    @OnClick(R.id.dining_lunch_button)
    public void onClickLunchButton() {
        typeIndex = 1;
        breakfastButton.setText("아침");
        lunchButton.setText(Html.fromHtml(colorText("#f7941e", "점심")), TextView.BufferType.SPANNABLE);
        dinnerButton.setText("저녁");

        updateUserInterface();
    }

    @OnClick(R.id.dining_dinner_button)
    public void onClickDinnerButton() {
        typeIndex = 2;

        breakfastButton.setText("아침");
        lunchButton.setText("점심");
        dinnerButton.setText(Html.fromHtml(colorText("#f7941e", "저녁")), TextView.BufferType.SPANNABLE);
        updateUserInterface();
    }

    public String colorText(String color, String text) {
        String styledText = "<u><font color='" + color + "'>" + text + "</font></u>";
        return styledText;
    }

    @OnClick(R.id.dining_before_date_button)
    public void onClickBeforeButton() {
        getBeforeDiningMenu();
    }

    @OnClick(R.id.dining_next_date_button)
    public void onClickNextButton() {
        getNextDiningMenu();
    }

    public void getBeforeDiningMenu() {
        if (changeDate > -LIMITDATE) {
            changeDate--;
            today = TimeUtil.getChangeDate(changeDate);
            diningPresenter.getDiningList(TimeUtil.getChangeDateFormatYYMMDD(changeDate));
            updateUserInterface();
        } else {
            showMessage("더 이상 데이터를 불러올 수 없습니다.");
        }
    }

    public void getNextDiningMenu() {
        if (changeDate < LIMITDATE) {
            changeDate++;
            today = TimeUtil.getChangeDate(changeDate);
            diningPresenter.getDiningList(TimeUtil.getChangeDateFormatYYMMDD(changeDate));
            updateUserInterface();
        } else
            showMessage("더 이상 데이터를 불러올 수 없습니다.");
    }

    @Override
    public void showUserInterface() {
        diningMap.clear();
        diningRecyclerAdapter.notifyDataSetChanged();
        emptyBoardListFrameLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        super.dispatchTouchEvent(ev);
        return true;
    }

    @NonNull
    @Override
    protected MenuState getMenuState() {
        return MenuState.Dining.INSTANCE;
    }
}
