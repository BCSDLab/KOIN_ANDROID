package in.koreatech.koin.ui.error;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.BuildConfig;
import in.koreatech.koin.R;
import in.koreatech.koin.core.activity.ActivityBase;
import in.koreatech.koin.ui.splash.SplashActivity;

import static in.koreatech.koin.util.ExceptionHandlerUtil.EXTRA_ERROR_TEXT;
public class ErrorActivity extends ActivityBase {
    @BindView(R.id.error_home_button)
    Button homeButton;
    @BindView(R.id.error_guide_message)
    TextView errorGuideMessage;
    @BindView(R.id.error_info_message)
    TextView errorInfoMessage;
    @BindView(R.id.error_title_message)
    TextView errorTitleMessage;
    @BindView(R.id.error_linear_layout)
    LinearLayout errorLinearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        ButterKnife.bind(this);
        init();
    }
    public void init() {
        if (BuildConfig.IS_DEBUG) {               //stage일 경우 에러 내용을 화면에 보여준다
            errorTitleMessage.setText(getIntent().getExtras().getString(EXTRA_ERROR_TEXT));
            errorTitleMessage.setTextColor(getResources().getColor(R.color.gray7));
            errorTitleMessage.setGravity(Gravity.START);
            //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            //params.gravity = Gravity.LEFT;

            //errorTitleMessage.setLayoutParams(params);
            errorInfoMessage.setVisibility(View.GONE);
            errorGuideMessage.setVisibility(View.GONE);
        } else {                                  //production일 경우 오류 안내 메시지를 화면에 보여준다
            errorGuideMessage.setText(R.string.error_guide);
            errorInfoMessage.setText(R.string.error_info);
        }
    }
    @OnClick(R.id.error_home_button)
    public void onClickErrorHomeButton() {
        Intent goToHomeIntent = new Intent(this, SplashActivity.class);
        finish();
        startActivity(goToHomeIntent);
    }
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent goToHomeIntent = new Intent(this, SplashActivity.class);
        startActivity(goToHomeIntent);
    }
    @OnClick(R.id.error_kakaotalk_button)
    public void onClickkakaoTalk() {
        boolean isKaKaoTalkInstalled = false;

        try {   //카카오톡 APP 설치 유무
            getPackageManager().getPackageInfo("com.kakao.talk", PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) { //카카오톡 APP이 있을 경우
            e.printStackTrace();
            isKaKaoTalkInstalled = true;
        }

        if (isKaKaoTalkInstalled) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://goto.kakao.com/@bcsdlab")));
        } else {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("kakaoplus://plusfriend/friend/@bcsdlab")));
        }
    }
}