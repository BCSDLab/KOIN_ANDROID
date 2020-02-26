package in.koreatech.koin.service_advertise.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.KoinEditorActivity;
import in.koreatech.koin.KoinNavigationDrawerActivity;
import in.koreatech.koin.R;
import in.koreatech.koin.core.asynctasks.GenerateProgressTask;
import in.koreatech.koin.core.bases.KoinBaseAppbarDark;
import in.koreatech.koin.core.networks.entity.AdDetail;
import in.koreatech.koin.core.networks.entity.Comment;
import in.koreatech.koin.core.networks.interactors.AdDetailInterator;
import in.koreatech.koin.core.networks.interactors.AdDetailRestInterator;
import in.koreatech.koin.core.util.ToastUtil;
import in.koreatech.koin.service_advertise.adapters.AdvertisingCommentAdapter;
import in.koreatech.koin.service_advertise.contracts.AdDetailContract;
import in.koreatech.koin.service_advertise.presenters.AdDetailPresenter;
import in.koreatech.koin.service_lostfound.adapter.LostFoundCommentRecyclerviewAdapter;

/**
 * created by hansol on 2020.1.3...
 * edited by seongyun on 2020. 02. 26...
 */
public class AdvertisingDetailActivity extends KoinEditorActivity implements AdDetailContract.View {

    private AdDetailPresenter adDetailPresenter;
    private Context context;
    private RecyclerView.LayoutManager layoutManager;
    private AdvertisingCommentAdapter commentRecyclerAdapter;
    private GenerateProgressTask adDetailGenerateProgress;
    private RequestOptions glideOptions;
    private AdDetail adDetailInfo;

    @BindView(R.id.advertising_detail_title_textview)
    TextView titleTextview;
    @BindView(R.id.advertising_detail_period_textview)
    TextView periodTextview;
    @BindView(R.id.advertising_detail_view_count_publisher_textview)
    TextView viewPublisherTextview;
    @BindView(R.id.advertising_detail_publish_date_textview)
    TextView publishDateTextview;
    @BindView(R.id.advertising_detail_event_imageview)
    ImageView eventImage;
    @BindView(R.id.advertising_detail_reply_count_textview)
    TextView replyCountTextview;
    @BindView(R.id.advertising_detail_view_count_textview)
    TextView viewCountTextview;
    @BindView(R.id.advertising_recyclerview)
    RecyclerView detailRecyclerview;
    @BindView(R.id.advertising_detail_page_edit_button)
    Button editButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.advertising_detail_page_activity);
        super.onCreate(savedInstanceState);

        adDetailInfo = new AdDetail();
        adDetailInfo.id = getIntent().getIntExtra("ID", -1);
        adDetailInfo.grantEdit = getIntent().getBooleanExtra("GRANT_EDIT", false);

        if (adDetailInfo.id == -1) {
            ToastUtil.makeShortToast(context, "이벤트 정보를 불러오지 못했습니다.");
            finish();
        }

        init();
    }

    @Override
    protected int getRichEditorId() {
        return R.id.advertising_detail_contents;
    }

    @Override
    protected boolean isEditable() {
        return false;
    }

    @Override
    protected void successImageProcessing(File imageFile, String uuid) {

    }

    private void init() {
        context = this;
        ButterKnife.bind(this);
        setPresenter(new AdDetailPresenter(this, new AdDetailRestInterator()));
        layoutManager = new LinearLayoutManager(this);
        if (adDetailPresenter != null) {
            adDetailPresenter.getAdDetailInfo(adDetailInfo.id);
        }
    }

    public void initView() {
        commentRecyclerAdapter = new AdvertisingCommentAdapter(context, adDetailInfo.comments);
        detailRecyclerview.setHasFixedSize(true);
        detailRecyclerview.setLayoutManager(layoutManager);
        detailRecyclerview.setAdapter(commentRecyclerAdapter);
        titleTextview.setText(adDetailInfo.eventTitle);
        periodTextview.setText(adDetailInfo.startDate + " ~ " + adDetailInfo.endDate);
        viewPublisherTextview.setText("조회 " + adDetailInfo.getHit() + " · " + adDetailInfo.getNickname());
        renderEditor(renderHtmltoString(adDetailInfo.content));
        replyCountTextview.setText(adDetailInfo.comentCount + "");
        viewCountTextview.setText(adDetailInfo.hit + "");

        if(adDetailInfo.grantEdit)
            editButton.setVisibility(View.VISIBLE);

        glideOptions = new RequestOptions()
                .fitCenter()
                .override(650, 870)
                .error(R.drawable.img_noimage)
                .placeholder(R.color.white);

        Glide.with(context)
                .load(adDetailInfo.thumbnail)
                .apply(glideOptions)
                .into(eventImage);
    }

    @Override
    protected void onStart() {
        super.onStart();


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
        adDetailInfo.title = adDetail.title;
        adDetailInfo.eventTitle = adDetail.eventTitle;
        adDetailInfo.startDate = adDetail.startDate;
        adDetailInfo.endDate = adDetail.endDate;
        adDetailInfo.content = adDetail.content;
        adDetailInfo.comentCount = adDetail.comentCount;
        adDetailInfo.hit = adDetail.hit;
        adDetailInfo.comments = adDetail.comments; //댓글 ArrayList
        adDetailInfo.thumbnail = adDetail.thumbnail;

        initView();
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

    // [목록으로] 버튼 클릭 메소드
    @OnClick(R.id.advertising_detail_back_list_button)
    public void goBackListClicked(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
