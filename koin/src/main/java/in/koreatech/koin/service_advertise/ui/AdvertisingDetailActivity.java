package in.koreatech.koin.service_advertise.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.KoinNavigationDrawerActivity;
import in.koreatech.koin.R;
import in.koreatech.koin.core.asynctasks.GenerateProgressTask;
import in.koreatech.koin.core.bases.KoinBaseAppbarDark;
import in.koreatech.koin.core.networks.entity.AdDetail;
import in.koreatech.koin.core.networks.interactors.AdDetailInterator;
import in.koreatech.koin.core.networks.interactors.AdDetailRestInterator;
import in.koreatech.koin.core.util.ToastUtil;
import in.koreatech.koin.service_advertise.contracts.AdDetailContract;
import in.koreatech.koin.service_advertise.presenters.AdDetailPresenter;

/**
 * created by hansol on 2020.1.3...
 */
public class AdvertisingDetailActivity extends KoinNavigationDrawerActivity implements AdDetailContract.View {

    AdDetailPresenter adDetailPresenter;
    Context context;
    private GenerateProgressTask adDetailGenerateProgress;

    @BindView(R.id.advertising_detail_title_textview)
    TextView titleTextview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advertising_detail_page_activity);

        init();
    }

    private void init() {
        context = this;
        ButterKnife.bind(this);
        setPresenter(new AdDetailPresenter(this, new AdDetailRestInterator()));

    }

    @Override
    protected void onStart() {
        super.onStart();

        int id = getIntent().getIntExtra("ID", -1);
        if (id == -1) {
            ToastUtil.makeShortToast(context, "이벤트 정보를 불러오지 못했습니다.");
        } else {
            if (adDetailPresenter != null) {
                adDetailPresenter.getAdDetailInfo(1);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
    }

    @Override
    public void showLoading() {
        adDetailGenerateProgress = new GenerateProgressTask(this, "로딩 중...");
        adDetailGenerateProgress.execute();
    }

    @Override
    public void hideLoading() {
        adDetailGenerateProgress.cancel(true);
    }

    @Override
    public void onAdDetailDataReceived(AdDetail adDetail) {

    }

    @Override
    public void setPresenter(AdDetailPresenter presenter) {
        this.adDetailPresenter = presenter;
    }

    @Override
    public void showMessage(String msg) {
        ToastUtil.makeShortToast(context, msg);
    }

    @OnClick(R.id.koin_base_app_bar_dark)
    public void koinBaseAppbarClick(View view) {
        int id = view.getId();
        if (id == KoinBaseAppbarDark.getLeftButtonId()) {
            onBackPressed();
        }
    }
}
