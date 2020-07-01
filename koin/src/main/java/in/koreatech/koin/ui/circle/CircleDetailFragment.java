package in.koreatech.koin.ui.circle;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.R;
import in.koreatech.koin.core.appbar.AppBarBase;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.data.network.entity.Circle;
import in.koreatech.koin.data.network.interactor.CircleRestInteractor;
import in.koreatech.koin.ui.circle.presenter.CircleDetailContract;
import in.koreatech.koin.ui.circle.presenter.CircleDetailPresenter;
import in.koreatech.koin.ui.koinfragment.KoinBaseFragment;
import in.koreatech.koin.ui.main.MainActivity;

public class CircleDetailFragment extends KoinBaseFragment implements CircleDetailContract.View {
    private final String TAG = "CircleDetailFragment";
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
    private Context context;
    private CircleDetailPresenter cirlceDetailPresenter;
    private Circle circle;
    private int circleId;
    private ArrayList<Circle.CircleUrl> url;
    /* URL */
    private String faceBookUrl;
    private String naverUrl;
    private String cyworldUrl;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.circle_fragment_detail, container, false);
        this.circleId = getArguments().getInt("CIRCLE_ID",-1);
        ButterKnife.bind(this,view);
        init();

        return view;
    }


    private void init() {
        context = getContext();
        setPresenter(new CircleDetailPresenter(this, new CircleRestInteractor()));
        cirlceDetailPresenter.getCirlceInfo(this.circleId);
    }

    @Override
    public void onCircleDataReceived(Circle circle) {
        this.circle = circle;
        Glide.with(context).load(new ColorDrawable(getResources().getColor(R.color.gray4))).apply(RequestOptions.circleCropTransform()).into(this.circleDetailLogoBoderImageview);
        Glide.with(context).load(new ColorDrawable(getResources().getColor(R.color.white))).apply(RequestOptions.circleCropTransform()).into(this.circleDetailLogoBackgroundImageview);
        if (circle.getLogoUrl() == null)
            Glide.with(context).load(new ColorDrawable(getResources().getColor(R.color.gray4))).apply(RequestOptions.circleCropTransform()).into(this.circleDetailLogoImageview);
        else {
            Glide.with(context).load(circle.getLogoUrl()).apply(RequestOptions.circleCropTransform()).into(this.circleDetailLogoImageview);
            Glide.with(context).load(circle.getLogoUrl()).into(this.circleDetailMainLogoImageview);
        }

        this.circleDetailNameTextview.setText((circle.getName() == null) ? "정보없음" : circle.getName());
        this.circleDetailMajorBusinessTextview.setText((circle.getMajorBusiness() == null) ? "정보없음" : circle.getMajorBusiness());
        this.circleDetailLocationTextview.setText((circle.getLocation() == null) ? "정보없음" : circle.getLocation());
        this.circleDetailLineDescriptionTextview.setText((circle.getLineDescription() == null) ? "정보없음" : circle.getLineDescription());
        this.circleDetailProfessorTextview.setText((circle.getProfessor() == null) ? "정보없음" : circle.getProfessor());
        this.circleDetailIntroduceUrlTextview.setText((circle.getIntroduceUrl() == null) ? "정보없음" : circle.getIntroduceUrl());
        this.circleDetailDescriptionTextview.setText((circle.getDescription() == null) ? "정보없음" : circle.getDescription());
        if (circle.getLinkUrls() != null && !circle.getLinkUrls().isEmpty()) {
            for (Circle.CircleUrl urls : circle.getLinkUrls()) {
                if (urls.getType().equals("facebook") && !urls.getLink().isEmpty()) {
                    this.circleDetailFacebookButton.setVisibility(View.VISIBLE);
                    this.faceBookUrl = urls.getLink();
                }
                if (urls.getType().equals("cyworld") && !urls.getLink().isEmpty()) {
                    this.circleDetailCyworldButton.setVisibility(View.VISIBLE);
                    this.cyworldUrl = urls.getLink();
                }
                if (urls.getType().equals("naver") && !urls.getLink().isEmpty()) {
                    this.circleDetailNaverButton.setVisibility(View.VISIBLE);
                    this.naverUrl = urls.getLink();
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

    @OnClick(R.id.circle_detail_facebook_button)
    public void onClickedFacebookButton() {
        Uri uri = Uri.parse(this.faceBookUrl);
        try {
            ApplicationInfo applicationInfo = getContext().getPackageManager().getApplicationInfo("com.facebook.katana", 0);
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
        ((MainActivity) getActivity()).showProgressDialog(R.string.loading);
    }


    @Override
    public void hideLoading() {
        ((MainActivity) getActivity()).hideProgressDialog();
    }

    @Override
    public void showMessage(String message) {
        ToastUtil.getInstance().makeShort(message);
    }


    @Override
    public void updateUserInterface() {


    }
}