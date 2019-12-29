package in.koreatech.koin.core.bases;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import in.koreatech.koin.core.util.font_change.TypekitContextWrapper;

/**
 * Created by hyerim on 2018. 4. 1..
 */

public class BaseActivity extends AppCompatActivity {
    private final String TAG = BaseActivity.class.getSimpleName();

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
