package in.koreatech.koin.ui.dining;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.google.firebase.perf.metrics.AddTrace;

import in.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity;
import in.koreatech.koin.R;
import in.koreatech.koin.core.progressdialog.CustomProgressDialog;
import in.koreatech.koin.core.appbar.AppbarBase;
import in.koreatech.koin.data.network.entity.Dining;
import in.koreatech.koin.data.network.interactor.DiningRestInteractor;
import in.koreatech.koin.util.TimeUtil;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.ui.dining.adapter.DiningRecyclerAdapter;
import in.koreatech.koin.ui.dining.presenter.DiningContract;
import in.koreatech.koin.ui.dining.presenter.DiningPresenter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by hyerim on 2018. 6. 21....
 * Edited by yunjae on 2018. 8. 24 .... feature changing date
 * Edited by seongyun on 2019.09.21 .... 시간대에 맞게 조.중.석식 출력, 한-일-양-특-능-수 순으로 식단 출력
 */
public class DiningActivity extends KoinNavigationDrawerActivity implements DiningContract.View, SwipeRefreshLayout.OnRefreshListener {
    private final String TAG = DiningActivity.class.getSimpleName();
    private final static String[] TYPE = {"BREAKFAST", "LUNCH", "DINNER"};
    private final static String[] ENDTIME = {"09:00", "13:30", "18:30"};   // 아침, 점심, 저녁 식당 운영 종료시간 및 초기화 시간
    private final int LIMITDATE = 7; // 식단 조회 제한 날짜
    private final int SWIPE_THRESHOLD = 100;
    private final int SWIPE_VELOCITY_THRESHOLD = 100;

    private Context mContext;
    private DiningRecyclerAdapter mDiningRecyclerAdapter;
    private GestureDetector mGestureDetector;
    private CustomProgressDialog customProgressDialog;

    private String today;
    private DiningPresenter mDiningPresenter;
    private RecyclerView.LayoutManager mLayoutManager; // RecyclerView LayoutManager
    private Map<String, ArrayList<Dining>> mDiningArrayList;
    private int typeIndex; //0 아침 / 1 점심 / 2 저녁
    private int mChangeDate;


    /* View Component */

    @BindView(R.id.dining_breakfast_button)
    TextView mBreakfastButton;
    @BindView(R.id.dining_lunch_button)
    TextView mLunchButton;
    @BindView(R.id.dining_dinner_button)
    TextView mDinnerButton;


    @BindView(R.id.dining_swiperefreshlayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.dining_recyclerview)
    RecyclerView mDiningListRecyclerView;
    @BindView(R.id.dining_date_textView)
    TextView mDiningDateTextView;

    @BindView(R.id.empty_dining_list_frameLayout)
    FrameLayout mEmptyBoardListFrameLayout;

    @BindView(R.id.dining_next_date_button)
    Button mNextDateButton;
    @BindView(R.id.dining_before_date_button)
    Button mBeforeDateButton;
    @BindView(R.id.koin_base_app_bar_dark)
    AppbarBase mAppbarBase;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dining_activity_main);
        ButterKnife.bind(this);
        mContext = this;
        init();

        //TODO : 영업 시간 표시
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG, today);
        mDiningPresenter.getDiningList(TimeUtil.getChangeDateFormatYYMMDD(mChangeDate));
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
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mChangeDate = 0; // 현재날짜와 바뀐 날짜 비교
        mAppbarBase.setTitleText("식단");

        mLayoutManager = new LinearLayoutManager(this);
        mDiningArrayList = new HashMap<String, ArrayList<Dining>>();
        typeIndex = 0;

        mDiningRecyclerAdapter = new DiningRecyclerAdapter(mContext, new ArrayList<Dining>());

        mDiningListRecyclerView.setHasFixedSize(true);
        mDiningListRecyclerView.setLayoutManager(mLayoutManager);
        mDiningListRecyclerView.setAdapter(mDiningRecyclerAdapter);
        mDiningListRecyclerView.setNestedScrollingEnabled(false);
        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return handleGestureEvent(e1, e2, velocityX, velocityY);
            }
        });


        setPresenter(new DiningPresenter(this, new DiningRestInteractor()));
        mDiningPresenter.getDiningList(today);


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
        this.mDiningPresenter = presenter;
    }


    @Override
    public void showLoading() {
        if (customProgressDialog == null) {
            customProgressDialog = new CustomProgressDialog(this, "로딩 중");
            customProgressDialog.execute();
        }
    }

    @Override
    public void hideLoading() {
        if (customProgressDialog != null) {
            customProgressDialog.cancel(true);
            customProgressDialog = null;
        }
    }

    @Override
    public void showMessage(String message) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        ToastUtil.getInstance().makeShort(message);
    }

    @AddTrace(name = "DiningActivity_onDiningListDataReceived")
    @Override
    public void showNetworkError() {
        mDiningArrayList.clear();
        mDiningRecyclerAdapter.notifyDataSetChanged();
        mEmptyBoardListFrameLayout.setVisibility(View.VISIBLE);
        ToastUtil.getInstance().makeShort(R.string.error_network);
    }

    @Override
    public void onDiningListDataReceived(ArrayList<Dining> diningArrayList) {
        mDiningArrayList.clear();

        for (Dining dining : arrangeDinings(diningArrayList)) {
            if (dining == null) {
                continue;
            }

            String typeTemp = dining.type;
            if (mDiningArrayList.containsKey(typeTemp)) {
                mDiningArrayList.get(typeTemp).add(dining);
            } else {
                ArrayList<Dining> dinings = new ArrayList<Dining>();
                dinings.add(dining);
                mDiningArrayList.put(dining.type, dinings);
            }
        }

        if (!diningArrayList.isEmpty()) {
            mEmptyBoardListFrameLayout.setVisibility(View.GONE);
        } else {
            mEmptyBoardListFrameLayout.setVisibility(View.VISIBLE);
        }
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        updateUserInterface();
    }

    /**
     * 서버로 받아온 식단에 우선순위를 주어 한식-일품식-양식-특식-능수관-수박여 순으로 재배열하는 메소드
     *
     * @param diningArrayList 서버에서 받아온 식단 정보
     * @return 재정렬된 식단 ArrayList
     */
    private ArrayList<Dining> arrangeDinings(ArrayList<Dining> diningArrayList) {
        Map<Integer, Dining> tempDiningMap = new TreeMap<>();

        for (Dining dining : diningArrayList) {
            int priority = 0;

            if (dining.type.equals(TYPE[0])) {
                priority += 0;
            } else if (dining.type.equals(TYPE[1])) {
                priority += 7;
            } else if (dining.type.equals(TYPE[2])) {
                priority += 14;
            }

            switch (dining.place) {
                case "한식":
                    priority += 0;
                    break;
                case "일품식":
                    priority += 1;
                    break;
                case "양식":
                    priority += 2;
                    break;
                case "특식":
                    priority += 3;
                    break;
                case "능수관":
                    priority += 4;
                    break;
                case "수박여":
                    priority += 5;
                    break;
                default: // 위 6가지 외 종류가 나올 경우 능수, 수박여 다음으로 출력
                    priority += 6;
            }
            tempDiningMap.put(priority, dining);
        }

        diningArrayList.clear();

        for( int key : tempDiningMap.keySet()){
            diningArrayList.add(tempDiningMap.get(key));
        }

        return diningArrayList;
    }

    @OnClick(R.id.koin_base_app_bar_dark)
    public void onClickedKoinBaseAppbar(View v) {
        int id = v.getId();
        if (id == AppbarBase.getLeftButtonId())
            callDrawerItem(R.id.navi_item_home);
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
            mDiningDateTextView.setText(today);

            if (mDiningArrayList.get(TYPE[typeIndex]) != null && mDiningArrayList.get(TYPE[typeIndex]).size() != 0) {
                mDiningRecyclerAdapter = new DiningRecyclerAdapter(mContext, mDiningArrayList.get(TYPE[typeIndex]));
                mDiningListRecyclerView.setAdapter(mDiningRecyclerAdapter);
                mEmptyBoardListFrameLayout.setVisibility(View.GONE);
            } else {
                mDiningRecyclerAdapter = new DiningRecyclerAdapter(mContext, new ArrayList<Dining>());
                mDiningListRecyclerView.setAdapter(mDiningRecyclerAdapter);
                mEmptyBoardListFrameLayout.setVisibility(View.VISIBLE);
            }
        } catch (NullPointerException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public void onRefresh() {
        mDiningPresenter.getDiningList(TimeUtil.getChangeDateFormatYYMMDD(mChangeDate));
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @OnClick(R.id.dining_breakfast_button)
    public void onClickBreakfastButton() {
        typeIndex = 0;
        mBreakfastButton.setText(Html.fromHtml(colorText("#f7941e", "아침")), TextView.BufferType.SPANNABLE);
        mLunchButton.setText("점심");
        mDinnerButton.setText("저녁");
        updateUserInterface();
    }

    @OnClick(R.id.dining_lunch_button)
    public void onClickLunchButton() {
        typeIndex = 1;
        mBreakfastButton.setText("아침");
        mLunchButton.setText(Html.fromHtml(colorText("#f7941e", "점심")), TextView.BufferType.SPANNABLE);
        mDinnerButton.setText("저녁");

        updateUserInterface();
    }

    @OnClick(R.id.dining_dinner_button)
    public void onClickDinnerButton() {
        typeIndex = 2;

        mBreakfastButton.setText("아침");
        mLunchButton.setText("점심");
        mDinnerButton.setText(Html.fromHtml(colorText("#f7941e", "저녁")), TextView.BufferType.SPANNABLE);
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
        if (mChangeDate > -LIMITDATE) {
            mChangeDate--;
            today = TimeUtil.getChangeDate(mChangeDate);
            mDiningPresenter.getDiningList(TimeUtil.getChangeDateFormatYYMMDD(mChangeDate));
            updateUserInterface();
        } else {
            showMessage("더 이상 데이터를 불러올 수 없습니다.");
        }
    }

    public void getNextDiningMenu() {
        if (mChangeDate < LIMITDATE) {
            mChangeDate++;
            today = TimeUtil.getChangeDate(mChangeDate);
            mDiningPresenter.getDiningList(TimeUtil.getChangeDateFormatYYMMDD(mChangeDate));
            updateUserInterface();
        } else
            showMessage("더 이상 데이터를 불러올 수 없습니다.");
    }

    @Override
    public void showUserInterface() {
        mDiningArrayList.clear();
        mDiningRecyclerAdapter.notifyDataSetChanged();
        mEmptyBoardListFrameLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        super.dispatchTouchEvent(ev);
        return mGestureDetector.onTouchEvent(ev);
    }
}
