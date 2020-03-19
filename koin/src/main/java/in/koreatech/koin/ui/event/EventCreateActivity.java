package in.koreatech.koin.ui.event;

import in.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.R;

public class EventCreateActivity extends KoinNavigationDrawerActivity implements AdvertisingContract.View {
    Context context;
    private ArrayList<Advertising> adArrayList;
    private AdvertisingPresenter adPresenter;
    private GridLayoutManager adGridLayoutManager;
    private AdvertisingRecyclerAdapter adRecyclerAdapter;
    private GenerateProgressTask generateProgressTask;

    @BindView(R.id.advertising_recyclerview)
    RecyclerView adRecyclerView;
    @BindView(R.id.activity_advertising_processing_checkbox2)
    CheckBox adProcessingCheckBox1;
    @BindView(R.id.activity_advertising_processing_checkbox)
    CheckBox adProcessingCheckBox2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advertising_list_activity);
        ButterKnife.bind(this);

        init();
    }

    void init() {
        context = this;
        adArrayList = new ArrayList<>();
        adGridLayoutManager = new GridLayoutManager(this, 2);
        adRecyclerView.setLayoutManager(adGridLayoutManager);
        adRecyclerAdapter = new AdvertisingRecyclerAdapter(adArrayList, this);
        adRecyclerView.setAdapter(adRecyclerAdapter);
        adRecyclerView.addOnItemTouchListener(recyclerClickListener);
        setPresenter(new AdvertisingPresenter(this, new AdvertisingRestInteractor()));
    }

    @OnClick(R.id.activity_advertising_processing_checkbox2)
    public void onClick1Checked() {
        checkBoxDataDisplay();
    }

    @OnClick(R.id.activity_advertising_processing_checkbox)
    public void onClick2Checked() {
        checkBoxDataDisplay();
    }

    @OnClick(R.id.koin_base_app_bar_dark)
    public void onClickedBaseAppbar(View v) {
        int id = v.getId();
        if (id == KoinBaseAppbarDark.getLeftButtonId()) {
            onBackPressed();
        } else if (id == KoinBaseAppbarDark.getRightButtonId()) {
            // TODO: 점주계정 확인 -> my/shops
            Intent intent = new Intent(context, AdvertisingCreateActivity.class);
            if (getAuthority() == AuthorizeConstant.ANONYMOUS) {
                showLoginRequestDialog();
                return;
            }
            if (getUser().userNickName != null)
                startActivity(intent);
            else {
                ToastUtil.makeLongToast(this, "닉네임이 필요합니다.");
                intent = new Intent(this, UserInfoEditedActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adPresenter.getAdList();
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
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
    }

    @Override
    public void setPresenter(AdvertisingPresenter presenter) {
        this.adPresenter = presenter;
    }

    @Override
    public void onAdvertisingDataReceived(ArrayList<Advertising> adList) {
        adArrayList.clear();
        adArrayList.addAll(adList);
        adRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGrantCheckReceived(AdDetail adDetail) {
        Intent intent = new Intent(this, AdvertisingDetailActivity.class);
        intent.putExtra("ID", adDetail.id);
        intent.putExtra("GRANT_EDIT", adDetail.grantEdit);
        startActivity(intent);
    }

    /**
     * 체크박스에 맞는 데이터로 재설정해주기
     */
    public void checkBoxDataDisplay() {
        // 프레젠터로 넘겨서 연산하고 가져와서 화면 갱신해!!
        ArrayList<Advertising> applyAdDataList = adPresenter.displayProcessingEvent(adProcessingCheckBox1.isChecked(), adProcessingCheckBox2.isChecked());
        adRecyclerAdapter.setAdArrayList(applyAdDataList);
        adRecyclerAdapter.notifyDataSetChanged();
    }


    @Override
    public void showMessage(String message) {
        ToastUtil.makeShortToast(this, message);
    }

    @Override
    public void showLoading() {
        if (generateProgressTask == null) {
            generateProgressTask = new GenerateProgressTask(this, "로딩 중");
            generateProgressTask.execute();
        }
    }

    @Override
    public void hideLoading() {
        if (generateProgressTask != null) {
            generateProgressTask.cancel(true);
            generateProgressTask = null;
        }
    }

    public final RecyclerClickListener recyclerClickListener = new RecyclerClickListener(null, null, new RecyclerViewClickListener() {
        @Override
        public void onClick(View view, int position) {
            adPresenter.getAdGrantCheck(adArrayList.get(position).id);
        }

        @Override
        public void onLongClick(View view, int position) {

        }
    });
}