package in.koreatech.koin.ui.event;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.R;
import in.koreatech.koin.constant.AuthorizeConstant;
import in.koreatech.koin.core.appbar.AppBarBase;
import in.koreatech.koin.core.recyclerview.RecyclerClickListener;
import in.koreatech.koin.core.recyclerview.RecyclerViewClickListener;
import in.koreatech.koin.core.swiperefreshbottom.SwipeRefreshLayoutBottom;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.data.network.entity.Event;
import in.koreatech.koin.data.network.entity.Store;
import in.koreatech.koin.data.network.interactor.EventRestInteractor;
import in.koreatech.koin.ui.event.adapter.EventRecyclerAdapter;
import in.koreatech.koin.ui.event.presenter.EventContract;
import in.koreatech.koin.ui.event.presenter.EventPresenter;
import in.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity;
import in.koreatech.koin.ui.userinfo.UserInfoEditedActivity;

public class EventActivity extends KoinNavigationDrawerActivity implements EventContract.View, SwipeRefreshLayoutBottom.OnRefreshListener {
    Context context;
    private ArrayList<Event> eventArrayList;
    private EventPresenter eventPresenter;
    private GridLayoutManager eventGridLayoutManager;
    private EventRecyclerAdapter eventRecyclerAdapter;
    private int pageNum;

    @BindView(R.id.event_recyclerview)
    RecyclerView eventRecyclerView;
    @BindView(R.id.event_closed_checkbox)
    CheckBox eventClosedCheckBox;
    @BindView(R.id.event_pending_checkbox)
    CheckBox eventPendingCheckBox;
    @BindView(R.id.koin_base_app_bar_dark)
    AppBarBase koinBaseAppbarDark;
    @BindView(R.id.event_swipe_refresh_bottom)
    SwipeRefreshLayoutBottom eventRefreshBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list_activity);
        ButterKnife.bind(this);

        init();
    }

    void init() {
        context = this;
        koinBaseAppbarDark.setTitleText("홍보게시판");
        eventArrayList = new ArrayList<>();
        eventGridLayoutManager = new GridLayoutManager(this, 2);
        eventRecyclerView.setLayoutManager(eventGridLayoutManager);
        eventRecyclerAdapter = new EventRecyclerAdapter(eventArrayList, this);
        eventRecyclerView.setAdapter(eventRecyclerAdapter);
        eventRecyclerView.addOnItemTouchListener(recyclerClickListener);
        eventRefreshBottom.setOnRefreshListener(this);
        setPresenter(new EventPresenter(this, new EventRestInteractor()));
        pageNum = 1;
    }

    @OnClick({R.id.event_pending_checkbox, R.id.event_closed_checkbox})
    public void onCheckboxClicked() {
        pageNum = 1;
        eventArrayList.clear(); // List 초기화
        getCheckedEventList(pageNum);
    }

    private void getCheckedEventList(int pageNum) {
        if(eventPendingCheckBox.isChecked() && eventClosedCheckBox.isChecked()) { // 전체 홍보글 조회
            eventPresenter.getEventList(pageNum);
        } else if (eventPendingCheckBox.isChecked()) {    // 진행 중인 홍보글 조회
            eventPresenter.getPendingEventList(pageNum);
        } else if (eventClosedCheckBox.isChecked()) {     // 종료된 홍보글 조회
            eventPresenter.getClosedEventList(pageNum);
        } else { // 체크박스 둘 다 체크가 안된 경우 -> 빈 게시물
            eventRecyclerAdapter.notifyDataSetChanged();
            hideRefreshing();
        }
    }

    @OnClick(R.id.koin_base_app_bar_dark)
    public void onClickedBaseAppbar(View v) {
        int id = v.getId();
        if (id == AppBarBase.getLeftButtonId()) {
            onBackPressed();
        } else if (id == AppBarBase.getRightButtonId()) {
            eventPresenter.getMyPendingEvent();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        pageNum = 1; // 첫 페이지로 초기화
        eventPresenter.getEventList(pageNum);

        // 사용자가 보유한 상점이 있는지 확인
        eventPresenter.getMyShops();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
    }

    @Override
    public void setPresenter(EventPresenter presenter) {
        this.eventPresenter = presenter;
    }

    public void onEventListDataReceived(ArrayList<Event> eventList) {
        hideRefreshing();

        if(eventList != null) {
            updateUserInterface(eventList);
        }
    }

    private void updateUserInterface(ArrayList<Event> eventList) {
        if(pageNum==1)
            eventArrayList.clear();

        pageNum++;

        eventArrayList.addAll(eventList);
        eventRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGrantCheckReceived(Event event) {
        Intent intent = new Intent(this, EventDetailActivity.class);
        intent.putExtra("ID", event.getId());
        intent.putExtra("GRANT_EDIT", event.isGrantEdit());
        startActivity(intent);
    }

    @Override
    public void onMyShopListReceived(ArrayList<Event> shopArrayList) {
        if(shopArrayList == null || shopArrayList.isEmpty()) {
            koinBaseAppbarDark.setRightButtonVisibility(View.INVISIBLE);
        } else {
            koinBaseAppbarDark.setRightButtonVisibility(View.VISIBLE);
        }
    }

    /**
     * 현재 진행중인 이벤트가 있을경우 수정하도록 유도하고, 없을 경우엔 새 글을 작성하도록 유도하는 메소드
     * @param event 현재 계정이 진행중인 이벤트
     */
    @Override
    public void onMyPendingEventReceived(Event event) {
        if(eventArrayList == null || eventArrayList.isEmpty()) {
            Intent intent = new Intent(this, EventCreateActivity.class);
            startActivity(intent);
        } else {
            showAlertDialog(event);
        }
    }

    private void showAlertDialog(Event event) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("이미 진행중인 이벤트가 있습니다.\n수정하시겠습니까?");
        builder.setPositiveButton("확인", (dialogInterface, i) -> {
            Intent intent = new Intent(this, EventCreateActivity.class);
            intent.putExtra("ID", event.getId());
            intent.putExtra("IS_EDIT", true);
            intent.putExtra("TITLE", event.getTitle());
            intent.putExtra("EVENT_TITLE", event.getEventTitle());
            intent.putExtra("START_DATE", event.getStartDate());
            intent.putExtra("END_DATE", event.getEndDate());
            intent.putExtra("CONTENT", event.getContent());
            intent.putExtra("SHOP_ID", event.getShopId());

            startActivity(intent);
        });

        builder.setNegativeButton("취소", ((dialogInterface, i) -> dialogInterface.cancel()));

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void showMessage(String message) {
        ToastUtil.getInstance().makeShort(message);
    }

    @Override
    public void showLoading() {
        showProgressDialog(R.string.loading);
    }

    @Override
    public void hideLoading() {
        hideProgressDialog();
    }

    private void hideRefreshing() {
        if(eventRefreshBottom.isRefreshing()) {
            eventRefreshBottom.setRefreshing(false);
        }
    }

    public final RecyclerClickListener recyclerClickListener = new RecyclerClickListener(null, null, new RecyclerViewClickListener() {
        @Override
        public void onClick(View view, int position) {
            eventPresenter.getEventGrantCheck(eventArrayList.get(position).getId());
        }

        @Override
        public void onLongClick(View view, int position) {

        }
    });

    @Override
    public void onRefresh() {
        getCheckedEventList(pageNum);
    }
}