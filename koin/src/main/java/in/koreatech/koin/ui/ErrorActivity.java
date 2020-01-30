package in.koreatech.koin.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.BuildConfig;
import in.koreatech.koin.KoinNavigationDrawerActivity;
import in.koreatech.koin.R;

import static in.koreatech.koin.helper.ExceptionHandlerHelper.EXTRA_ERROR_TEXT;

public class ErrorActivity extends KoinNavigationDrawerActivity {

    @BindView(R.id.error_home_button)
    Button homeButton;
    @BindView(R.id.error_guide_message)
    TextView errorGuideMessage;
    @BindView(R.id.error_info_message)
    TextView errorInfoMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        ButterKnife.bind(this);
        init();
    }

    public void init() {
        if(BuildConfig.IS_DEBUG){               //stage일 경우 에러 내용을 화면에 보여준다
            errorInfoMessage.setText(getIntent().getExtras().getString(EXTRA_ERROR_TEXT));
            errorGuideMessage.setText("");
        }
        else {                                  //production일 경우 오류 안내 메시지를 화면에 보여준다
            errorGuideMessage.setText("다음과 같은 상황이 지속되신다면 문의바랍니다.");
            errorInfoMessage.setText("예상치 못한 오류가 발생되었습니다.");
        }
    }

    @OnClick(R.id.error_home_button)
    public void onClickErrorHomeButton() {
        Intent goToHomeIntent = new Intent(this, MainActivity.class);
        startActivity(goToHomeIntent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent goToHomeIntent = new Intent(this, MainActivity.class);
        startActivity(goToHomeIntent);
    }
}
