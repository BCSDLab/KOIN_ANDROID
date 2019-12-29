package in.koreatech.koin.service_callvan.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.KoinNavigationDrawerActivity;
import in.koreatech.koin.R;
import in.koreatech.koin.core.bases.KoinBaseAppbarDark;
import in.koreatech.koin.core.util.FirebasePerformanceUtil;
import in.koreatech.koin.service_callvan.adapters.CallvanViewPagerAdapter;

/**
 * Created by hyerim on 2018. 6. 17....
 */
public class CallvanActivity extends KoinNavigationDrawerActivity {
    private final String TAG = CallvanActivity.class.getSimpleName();

    private Context mContext;
    private FirebasePerformanceUtil mFirebasePerformanceUtil;

    /* View Component */
    @BindView(R.id.koin_base_appbar)
    KoinBaseAppbarDark mKoinBaseAppbar;
    @BindView(R.id.callvan_main_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.callvan_main_tabs)
    TabLayout mTabLayout;

    private InputMethodManager mInputMethodManager;
    private CallvanViewPagerAdapter mViewPagerAdapter;
    private int mSelectedPage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.callvan_activity_main);
        ButterKnife.bind(this);
        mContext = this;
        init();
    }

    @Override
    public void onStart() {
        super.onStart();
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
        mFirebasePerformanceUtil.stop();
        super.onStop();
    }

    private void init() {
        mSelectedPage = 0;
        mViewPagerAdapter = new CallvanViewPagerAdapter(getSupportFragmentManager(), 2);
        mFirebasePerformanceUtil = new FirebasePerformanceUtil("Market_used");
        mFirebasePerformanceUtil.start();
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        //hide keyboard
        mInputMethodManager = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
    }

//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            Fragment fragment = null;
//
//            switch (item.getItemId()) {
//                case R.id.action_callvan:
//                    fragment = new RoomFragment();
//                    break;
//                case R.id.action_company:
//                    fragment = new CompanyFragment();
//                    break;
//            }
//            return loadFragment(fragment);
//        }
//    };

//    private boolean loadFragment(Fragment fragment) {
//        //switching fragment
//        if (fragment != null) {
//            getSupportFragmentManager()
//                    .beginTransaction()
////                    .replace(R.id.callvan_frame_container, fragment)
//                    .commit();
//            return true;
//        }
//        return false;
//    }

//    public void refreshRoomChatFragment() {
//        loadFragment(new RoomChatFragment());
//    }

//    public void refreshRoomFragment() {
//        loadFragment(new RoomFragment());
//    }

    @OnClick(R.id.koin_base_appbar)
    public void koinBaseAppbarClick(View view)
    {
        int id = view.getId();
        if (id == KoinBaseAppbarDark.getLeftButtonId())
            onBackPressed();
        else if (id == KoinBaseAppbarDark.getRightButtonId()) {
            goToCreateCallvanRoomActivity();
        }
    }

    public void goToCreateCallvanRoomActivity() {
        Intent intent = new Intent(this, CreateRoomActivity.class);
        startActivity(intent);


//        if (getAuthority() == AuthorizeConstant.ANONYMOUS) {
//            showLoginRequestDialog();
//            return;
//        }
//        if (getUser().userNickName != null)
//            startActivity(intent);
//        else {
//            ToastUtil.makeLongToast(this, "닉네임이 필요합니다.");
//            intent = new Intent(this, UserInfoEditedActivity.class);
//            startActivity(intent);
//        }

    }
}
