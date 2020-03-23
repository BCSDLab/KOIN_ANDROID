package in.koreatech.koin.ui.event;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.data.network.entity.Event;
import in.koreatech.koin.data.network.interactor.EventRestInteractor;
import in.koreatech.koin.ui.event.adapter.EventRecyclerAdapter;
import in.koreatech.koin.ui.event.presenter.EventContract;
import in.koreatech.koin.ui.event.presenter.EventPresenter;
import in.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity;
import in.koreatech.koin.ui.userinfo.UserInfoEditedActivity;

// TODO: 진행중인 이벤트와 종료된 이벤트를 서버에서 불러오는 방식으로 변경
// TODO: 작성 권한 없을 경우 팅기는 버그 해결
// TODO: GridLayout 의 아이템간 간격 조절
public class EventActivity extends KoinNavigationDrawerActivity implements EventContract.View {
    Context context;
    private ArrayList<Event> eventArrayList;
    private EventPresenter eventPresenter;
    private GridLayoutManager eventGridLayoutManager;
    private EventRecyclerAdapter eventRecyclerAdapter;

    @BindView(R.id.event_recyclerview)
    RecyclerView eventRecyclerView;
    @BindView(R.id.activity_event_processing_checkbox2)
    CheckBox eventProcessingCheckBox1;
    @BindView(R.id.activity_event_processing_checkbox)
    CheckBox eventProcessingCheckBox2;
    @BindView(R.id.koin_base_app_bar_dark)
    AppBarBase koinBaseAppbarDark;

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
        setPresenter(new EventPresenter(this, new EventRestInteractor()));
    }

    @OnClick(R.id.activity_event_processing_checkbox2)
    public void onClick1Checked() {
        checkBoxDataDisplay();
    }

    @OnClick(R.id.activity_event_processing_checkbox)
    public void onClick2Checked() {
        checkBoxDataDisplay();
    }

    @OnClick(R.id.koin_base_app_bar_dark)
    public void onClickedBaseAppbar(View v) {
        int id = v.getId();
        if (id == AppBarBase.getLeftButtonId()) {
            onBackPressed();
        } else if (id == AppBarBase.getRightButtonId()) {
            // TODO: 점주계정 확인 -> my/shops
            Intent intent = new Intent(context, EventCreateActivity.class);
            if (getAuthority() == AuthorizeConstant.ANONYMOUS) {
                showLoginRequestDialog();
                return;
            }
            if (getUser().getUserNickName() != null)
                startActivity(intent);
            else {
                ToastUtil.getInstance().makeLong("닉네임이 필요합니다.");
                intent = new Intent(this, UserInfoEditedActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        eventPresenter.getEventList();
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
        eventArrayList.clear();
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

    /**
     * 체크박스에 맞는 데이터로 재설정해주기
     */
    public void checkBoxDataDisplay() {
        // 프레젠터로 넘겨서 연산하고 가져와서 화면 갱신해!!
        ArrayList<Event> applyAdDataList = eventPresenter.displayProcessingEvent(eventProcessingCheckBox1.isChecked(), eventProcessingCheckBox2.isChecked());
        eventRecyclerAdapter.setAdArrayList(applyAdDataList);
        eventRecyclerAdapter.notifyDataSetChanged();
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

    public final RecyclerClickListener recyclerClickListener = new RecyclerClickListener(null, null, new RecyclerViewClickListener() {
        @Override
        public void onClick(View view, int position) {
            eventPresenter.getEventGrantCheck(eventArrayList.get(position).getId());
        }

        @Override
        public void onLongClick(View view, int position) {

        }
    });
}