package in.koreatech.koin.ui.callvan;

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
import in.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity;
import in.koreatech.koin.R;
import in.koreatech.koin.core.appbar.AppBarBase;
import in.koreatech.koin.util.FirebasePerformanceUtil;
import in.koreatech.koin.ui.callvan.adapter.CallvanViewPagerAdapter;

public class CallvanActivity extends KoinNavigationDrawerActivity {
    private final String TAG = "CallvanActivity";

    private Context context;
    private FirebasePerformanceUtil firebasePerformanceUtil;

    /* View Component */
    @BindView(R.id.koin_base_appbar)
    AppBarBase koinBaseAppbar;
    @BindView(R.id.callvan_main_viewpager)
    ViewPager viewPager;
    @BindView(R.id.callvan_main_tabs)
    TabLayout tabLayout;

    private InputMethodManager inputMethodManager;
    private CallvanViewPagerAdapter viewPagerAdapter;
    private int selectedPage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.callvan_activity_main);
        ButterKnife.bind(this);
        context = this;
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
        firebasePerformanceUtil.stop();
        super.onStop();
    }

    private void init() {
        selectedPage = 0;
        viewPagerAdapter = new CallvanViewPagerAdapter(getSupportFragmentManager(), 2);
        firebasePerformanceUtil = new FirebasePerformanceUtil("Market_used");
        firebasePerformanceUtil.start();
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        //hide keyboard
        inputMethodManager = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
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
    public void koinBaseAppbarClick(View view) {
        int id = view.getId();
        if (id == AppBarBase.getLeftButtonId())
            onBackPressed();
        else if (id == AppBarBase.getRightButtonId()) {
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
//            ToastUtil.makeLong(this, "닉네임이 필요합니다.");
//            intent = new Intent(this, UserInfoEditedFragment.class);
//            startActivity(intent);
//        }

    }
}
