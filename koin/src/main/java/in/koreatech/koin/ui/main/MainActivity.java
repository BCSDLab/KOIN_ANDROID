package in.koreatech.koin.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import butterknife.ButterKnife;
import in.koreatech.koin.R;
import in.koreatech.koin.core.activity.ActivityBase;
import in.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity;


/**
 * 로그인 후 처음 보여지는 화면
 */
public class MainActivity extends ActivityBase {
    public static final int TIMETABLE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
