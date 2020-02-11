package in.koreatech.koin.ui.circle;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity;
import in.koreatech.koin.R;
import in.koreatech.koin.core.appbar.AppBarBase;
import in.koreatech.koin.data.network.entity.Circle;
import in.koreatech.koin.data.network.interactor.CircleRestInteractor;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.ui.circle.presenter.CircleDetailContract;
import in.koreatech.koin.ui.circle.presenter.CircleDetailPresenter;

public class CircleDetailActivity extends KoinNavigationDrawerActivity implements CircleDetailContract.View {
    private final String TAG = "CircleDetailActivity";

    private Context context;
    private CircleDetailPresenter cirlceDetailPresenter;
    private Circle circle;
    private int circleId;
    private ArrayList<Circle.CircleUrl> url;

    /* URL */
    private String faceBookUrl;
    private String naverUrl;
    private String cyworldUrl;


    /* View Component */
    @BindView(R.id.koin_base_app_bar_dark)
    AppBarBase appbarBase;
    @BindView(R.id.circle_detail_logo_border_imageview)
    ImageView circleDetailLogoBoderImageview;
    @BindView(R.id.circle_detail_logo_background_imageview)
    ImageView circleDetailLogoBackgroundImageview;
    @BindView(R.id.circle_detail_logo_imageview)
    ImageView circleDetailLogoImageview;
    @BindView(R.id.circle_detail_name_textview)
    TextView circleDetailNameTextview;
    @BindView(R.id.circle_detail_line_description_textview)
    TextView circleDetailLineDescriptionTextview;
    @BindView(R.id.circle_detail_main_logo_imageview)
    ImageView circleDetailMainLogoImageview;
    @BindView(R.id.circle_detail_location_textview)
    TextView circleDetailLocationTextview;
    @BindView(R.id.circle_detail_major_business_textview)
    TextView circleDetailMajorBusinessTextview;
    @BindView(R.id.circle_detail_professor_textview)
    TextView circleDetailProfessorTextview;
    @BindView(R.id.circle_detail_introduce_url_textview)
    TextView circleDetailIntroduceUrlTextview;
    @BindView(R.id.circle_detail_description_textview)
    TextView circleDetailDescriptionTextview;
    @BindView(R.id.circle_detail_facebook_button)
    Button circleDetailFacebookButton;
    @BindView(R.id.circle_detail_naver_button)
    Button circleDetailNaverButton;
    @BindView(R.id.circle_detail_cyworld_button)
    Button circleDetailCyworldButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circle_activity_detail);
        this.circleId = getIntent().getIntExtra("CIRCLE_ID", -1);
        ButterKnife.bind(this);
        context = this;
        init();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (this.circleId == -1) {
            ToastUtil.getInstance().makeShort("동아리 리스트를 받아오지 못했습니다.");
            finish();
        }
        if (cirlceDetailPresenter != null)
            cirlceDetailPresenter.getCirlceInfo(this.circleId);
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
        appbarBase.setTransitionName(CircleActivity.SHARE_VIEW_NAME_APP_BAR);
        this.circleDetailLogoImageview.setTransitionName(CircleActivity.SHARE_VIEW_NAME_LOGO);
        this.circleDetailNameTextview.setTransitionName(CircleActivity.SHARE_VIEW_NAME);
        this.circleDetailLineDescriptionTextview.setTransitionName(CircleActivity.SHARE_VIEW_DETAIL);
        setPresenter(new CircleDetailPresenter(this, new CircleRestInteractor()));
    }

    @Override
    public void onCircleDataReceived(Circle circle) {
        this.circle = circle;
        Glide.with(context).load(new ColorDrawable(getResources().getColor(R.color.gray4))).apply(RequestOptions.circleCropTransform()).into(this.circleDetailLogoBoderImageview);
        Glide.with(context).load(new ColorDrawable(getResources().getColor(R.color.white))).apply(RequestOptions.circleCropTransform()).into(this.circleDetailLogoBackgroundImageview);
        if (circle.logoUrl == null)
            Glide.with(context).load(new ColorDrawable(getResources().getColor(R.color.gray4))).apply(RequestOptions.circleCropTransform()).into(this.circleDetailLogoImageview);
        else {
            Glide.with(context).load(circle.logoUrl).apply(RequestOptions.circleCropTransform()).into(this.circleDetailLogoImageview);
            Glide.with(context).load(circle.logoUrl).into(this.circleDetailMainLogoImageview);
        }

        this.circleDetailNameTextview.setText((circle.name == null) ? "정보없음" : circle.name);
        this.circleDetailMajorBusinessTextview.setText((circle.majorBusiness == null) ? "정보없음" : circle.majorBusiness);
        this.circleDetailLocationTextview.setText((circle.location == null) ? "정보없음" : circle.location);
        this.circleDetailLineDescriptionTextview.setText((circle.lineDescription == null) ? "정보없음" : circle.lineDescription);
        this.circleDetailProfessorTextview.setText((circle.professor == null) ? "정보없음" : circle.professor);
        this.circleDetailIntroduceUrlTextview.setText((circle.introduceUrl == null) ? "정보없음" : circle.introduceUrl);
        this.circleDetailDescriptionTextview.setText((circle.description == null) ? "정보없음" : circle.description);
        if (circle.linkUrls != null && !circle.linkUrls.isEmpty()) {
            for (Circle.CircleUrl urls : circle.linkUrls) {
                if (urls.type.equals("facebook") && !urls.link.isEmpty()) {
                    this.circleDetailFacebookButton.setVisibility(View.VISIBLE);
                    this.faceBookUrl = urls.link;
                }
                if (urls.type.equals("cyworld") && !urls.link.isEmpty()) {
                    this.circleDetailCyworldButton.setVisibility(View.VISIBLE);
                    this.cyworldUrl = urls.link;
                }
                if (urls.type.equals("naver") && !urls.link.isEmpty()) {
                    this.circleDetailNaverButton.setVisibility(View.VISIBLE);
                    this.naverUrl = urls.link;
                }
            }
        }
    }

    @OnClick(R.id.koin_base_app_bar_dark)
    public void onClickedBaseAppbar(View v) {
        int id = v.getId();
        if (id == AppBarBase.getLeftButtonId())
            onBackPressed();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick(R.id.circle_detail_facebook_button)
    public void onClickedFacebookButton() {
        Uri uri = Uri.parse(this.faceBookUrl);
        try {
            ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo("com.facebook.katana", 0);
            if (applicationInfo.enabled) {
                uri = Uri.parse("fb://facewebmodal/f?href=" + this.faceBookUrl);
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    @OnClick(R.id.circle_detail_naver_button)
    public void onClickedNaverButton() {
        Uri uri = Uri.parse(this.naverUrl);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    @OnClick(R.id.circle_detail_cyworld_button)
    public void onClickedCyworldButton() {
        Uri uri = Uri.parse(this.cyworldUrl);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }


    @Override
    public void setPresenter(CircleDetailPresenter presenter) {
        this.cirlceDetailPresenter = presenter;
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
        ToastUtil.getInstance().makeShort(message);
    }


    @Override
    public void updateUserInterface() {


    }
}