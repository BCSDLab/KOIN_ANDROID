package in.koreatech.koin.service_advertise.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.common.data.AbstractDataBuffer;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.KoinNavigationDrawerActivity;
import in.koreatech.koin.R;
import in.koreatech.koin.core.bases.KoinBaseAppbarDark;
import in.koreatech.koin.core.constants.AuthorizeConstant;
import in.koreatech.koin.core.networks.entity.Advertising;
import in.koreatech.koin.core.networks.entity.BokdukRoom;
import in.koreatech.koin.core.networks.interactors.AdvertisingRestInteractor;
import in.koreatech.koin.core.util.ToastUtil;
import in.koreatech.koin.service_advertise.adapters.AdvertisingRecyclerAdapter;
import in.koreatech.koin.service_advertise.contracts.AdvertisingContract;
import in.koreatech.koin.service_advertise.presenters.AdvertisingPresenter;
import in.koreatech.koin.service_land.adapter.LandRecyclerAdapter;
import in.koreatech.koin.ui.UserInfoEditedActivity;

/**
 * Created by hansol on 2020.1.1...
 */
public class AdvertisingActivity extends KoinNavigationDrawerActivity implements AdvertisingContract.View {
    Context context;
    private ArrayList<Advertising> adArrayList;
    private AdvertisingPresenter adPresenter;
    private GridLayoutManager adGridLayoutManager;
    private AdvertisingRecyclerAdapter adRecyclerAdapter;

    @BindView(R.id.advertising_recyclerview)
    RecyclerView adRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertising);
        init();
    }

    void init(){
        ButterKnife.bind(this);
        context = this;
        adArrayList = new ArrayList<>();
        adGridLayoutManager = new GridLayoutManager(this, 2);
        adRecyclerView.setLayoutManager(adGridLayoutManager);
        setPresenter(new AdvertisingPresenter(this, new AdvertisingRestInteractor()));

    }

    @OnClick(R.id.koin_base_app_bar_dark)
    public void onClickedBaseAppbar(View v) {
        int id = v.getId();
        if (id == KoinBaseAppbarDark.getLeftButtonId()) {
            onBackPressed();
        }else if (id == KoinBaseAppbarDark.getRightButtonId()) {
            //점주계정 확인하세요
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
        adRecyclerAdapter = new AdvertisingRecyclerAdapter(adList, this);
        adRecyclerView.setAdapter(adRecyclerAdapter);
    }

    @Override
    public void showMessage(String message) {
        ToastUtil.makeShortToast(this, message);
    }
}


