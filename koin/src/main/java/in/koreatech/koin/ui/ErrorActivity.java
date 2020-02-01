package in.koreatech.koin.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.BuildConfig;
import in.koreatech.koin.KoinNavigationDrawerActivity;
import in.koreatech.koin.R;
import in.koreatech.koin.core.bases.KoinBaseAppbarDark;

import static in.koreatech.koin.helper.ExceptionHandlerHelper.EXTRA_ERROR_TEXT;

public class ErrorActivity extends KoinNavigationDrawerActivity {

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
            errorTitleMessage.setTextColor(getResources().getColor(R.color.warm_grey));
            errorInfoMessage.setVisibility(View.GONE);
            errorGuideMessage.setVisibility(View.GONE);
        } else {                                  //production일 경우 오류 안내 메시지를 화면에 보여준다

            errorGuideMessage.setText(R.string.error_guide);
            errorInfoMessage.setText(R.string.error_info);
        }
    }

    @OnClick(R.id.error_home_button)
    public void onClickErrorHomeButton() {
        Intent goToHomeIntent = new Intent(this, MainActivity.class);
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
        Intent goToHomeIntent = new Intent(this, MainActivity.class);
        startActivity(goToHomeIntent);
    }

}
