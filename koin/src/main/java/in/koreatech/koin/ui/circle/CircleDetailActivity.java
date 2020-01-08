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
import in.koreatech.koin.core.progressdialog.CustomProgressDialog;
import in.koreatech.koin.core.appbar.AppbarBase;
import in.koreatech.koin.data.network.entity.Circle;
import in.koreatech.koin.data.network.interactor.CircleRestInteractor;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.ui.circle.presenter.CIrcleDetailContract;
import in.koreatech.koin.ui.circle.presenter.CircleDetailPresenter;

/**
 * Created by hyerim on 2018. 8. 12....
 */
public class CircleDetailActivity extends KoinNavigationDrawerActivity implements CIrcleDetailContract.View {
    private final String TAG = CircleDetailActivity.class.getSimpleName();

    private Context mContext;
    private static CustomProgressDialog mGenerateProgress;
    private CircleDetailPresenter mCirlceDetailPresenter;
    private Circle mCircle;
    private int mCircleId;
    private ArrayList<Circle.CircleUrl> mUrl;

    /* URL */
    private String mFaceBookUrl;
    private String mNaverUrl;
    private String mCyworldUrl;


    /* View Component */
    @BindView(R.id.koin_base_app_bar_dark)
    AppbarBase appbarBase;
    @BindView(R.id.circle_detail_logo_border_imageview)
    ImageView mCircleDetailLogoBoderImageview;
    @BindView(R.id.circle_detail_logo_background_imageview)
    ImageView mCircleDetailLogoBackgroundImageview;
    @BindView(R.id.circle_detail_logo_imageview)
    ImageView mCircleDetailLogoImageview;
    @BindView(R.id.circle_detail_name_textview)
    TextView mCircleDetailNameTextview;
    @BindView(R.id.circle_detail_line_description_textview)
    TextView mCircleDetailLineDescriptionTextview;
    @BindView(R.id.circle_detail_main_logo_imageview)
    ImageView mCircleDetailMainLogoImageview;
    @BindView(R.id.circle_detail_location_textview)
    TextView mCircleDetailLocationTextview;
    @BindView(R.id.circle_detail_major_business_textview)
    TextView mCircleDetailMajorBusinessTextview;
    @BindView(R.id.circle_detail_professor_textview)
    TextView mCircleDetailProfessorTextview;
    @BindView(R.id.circle_detail_introduce_url_textview)
    TextView mCircleDetailIntroduceUrlTextview;
    @BindView(R.id.circle_detail_description_textview)
    TextView mCircleDetailDescriptionTextview;
    @BindView(R.id.circle_detail_facebook_button)
    Button mCircleDetailFacebookButton;
    @BindView(R.id.circle_detail_naver_button)
    Button mCircleDetailNaverButton;
    @BindView(R.id.circle_detail_cyworld_button)
    Button mCircleDetailCyworldButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circle_activity_detail);
        mCircleId = getIntent().getIntExtra("CIRCLE_ID", -1);
        ButterKnife.bind(this);
        mContext = this;
        init();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mCircleId == -1) {
            ToastUtil.getInstance().makeShort("동아리 리스트를 받아오지 못했습니다.");
            finish();
        }
        if (mCirlceDetailPresenter != null)
            mCirlceDetailPresenter.getCirlceInfo(mCircleId);
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
        mCircleDetailLogoImageview.setTransitionName(CircleActivity.SHARE_VIEW_NAME_LOGO);
        mCircleDetailNameTextview.setTransitionName(CircleActivity.SHARE_VIEW_NAME);
        mCircleDetailLineDescriptionTextview.setTransitionName(CircleActivity.SHARE_VIEW_DETAIL);
        setPresenter(new CircleDetailPresenter(this, new CircleRestInteractor()));
    }

    @Override
    public void onCircleDataReceived(Circle circle) {
        mCircle = circle;
        Glide.with(mContext).load(new ColorDrawable(getResources().getColor(R.color.gray1))).apply(RequestOptions.circleCropTransform()).into(mCircleDetailLogoBoderImageview);
        Glide.with(mContext).load(new ColorDrawable(getResources().getColor(R.color.white))).apply(RequestOptions.circleCropTransform()).into(mCircleDetailLogoBackgroundImageview);
        if (circle.logoUrl == null)
            Glide.with(mContext).load(new ColorDrawable(getResources().getColor(R.color.gray1))).apply(RequestOptions.circleCropTransform()).into(mCircleDetailLogoImageview);
        else {
            Glide.with(mContext).load(circle.logoUrl).apply(RequestOptions.circleCropTransform()).into(mCircleDetailLogoImageview);
            Glide.with(mContext).load(circle.logoUrl).into(mCircleDetailMainLogoImageview);
        }

        mCircleDetailNameTextview.setText((circle.name == null) ? "정보없음" : circle.name);
        mCircleDetailMajorBusinessTextview.setText((circle.majorBusiness == null) ? "정보없음" : circle.majorBusiness);
        mCircleDetailLocationTextview.setText((circle.location == null) ? "정보없음" : circle.location);
        mCircleDetailLineDescriptionTextview.setText((circle.lineDescription == null) ? "정보없음" : circle.lineDescription);
        mCircleDetailProfessorTextview.setText((circle.professor == null) ? "정보없음" : circle.professor);
        mCircleDetailIntroduceUrlTextview.setText((circle.introduceUrl == null) ? "정보없음" : circle.introduceUrl);
        mCircleDetailDescriptionTextview.setText((circle.description == null) ? "정보없음" : circle.description);
        if (circle.linkUrls != null && !circle.linkUrls.isEmpty()) {
            for (Circle.CircleUrl urls : circle.linkUrls) {
                if (urls.type.equals("facebook") && !urls.link.isEmpty()) {
                    mCircleDetailFacebookButton.setVisibility(View.VISIBLE);
                    mFaceBookUrl = urls.link;
                }
                if (urls.type.equals("cyworld") && !urls.link.isEmpty()) {
                    mCircleDetailCyworldButton.setVisibility(View.VISIBLE);
                    mCyworldUrl = urls.link;
                }
                if (urls.type.equals("naver") && !urls.link.isEmpty()) {
                    mCircleDetailNaverButton.setVisibility(View.VISIBLE);
                    mNaverUrl = urls.link;
                }
            }
        }
    }

    @OnClick(R.id.koin_base_app_bar_dark)
    public void onClickedBaseAppbar(View v) {
        int id = v.getId();
        if (id == AppbarBase.getLeftButtonId())
            onBackPressed();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick(R.id.circle_detail_facebook_button)
    public void onClickedFacebookButton() {
        Uri uri = Uri.parse(mFaceBookUrl);
        try {
            ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo("com.facebook.katana", 0);
            if (applicationInfo.enabled) {
                uri = Uri.parse("fb://facewebmodal/f?href=" + mFaceBookUrl);
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    @OnClick(R.id.circle_detail_naver_button)
    public void onClickedNaverButton() {
        Uri uri = Uri.parse(mNaverUrl);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    @OnClick(R.id.circle_detail_cyworld_button)
    public void onClickedCyworldButton() {
        Uri uri = Uri.parse(mCyworldUrl);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }


    @Override
    public void setPresenter(CircleDetailPresenter presenter) {
        this.mCirlceDetailPresenter = presenter;
    }


    @Override
    public void showLoading() {
        mGenerateProgress = new CustomProgressDialog(mContext, "로딩 중");
        mGenerateProgress.execute();
    }


    @Override
    public void hideLoading() {
        mGenerateProgress.cancel(true);
    }

    @Override
    public void showMessage(String message) {
        ToastUtil.getInstance().makeShort(message);
    }


    @Override
    public void updateUserInterface() {


    }
}