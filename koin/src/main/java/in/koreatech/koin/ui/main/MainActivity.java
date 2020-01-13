package in.koreatech.koin.ui.main;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;

import androidx.annotation.Nullable;


import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity;
import in.koreatech.koin.R;
import in.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper;

import in.koreatech.koin.core.toast.ToastUtil;


/**
 * 로그인 후 처음 보여지는 화면
 * Created by hyerim on 2018. 5. 28....
 */
public class MainActivity extends KoinNavigationDrawerActivity {
    public static final int TIMETABLE_REQUEST_CODE = 1;
    private final int[] HOME_MENU_ID = {R.id.activity_main_store_constraint_layout, R.id.activity_main_bus_constraint_layout,
            R.id.activity_main_dining_constraint_layout, R.id.activity_main_free_board_constraint_layout,
            R.id.activity_main_anonymous_board_constraint_layout, R.id.activity_main_recruit_board_constraint_layout,
//            R.id.activity_main_callvan_constraint_layout,
            R.id.activity_main_circle_constraint_layout,
            R.id.activity_main_used_market_constraint_layout, R.id.activity_main_timetable_constraint_layout};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //checkUserInfoEnough(R.string.navigation_item_anonymous_board);
    }

    @Override
    protected void onStart() {
        super.onStart();
        callDrawerItem(R.id.navi_item_home);

    }

    // 서비스 연결
    @OnClick({R.id.activity_main_store_constraint_layout, R.id.activity_main_bus_constraint_layout,
            R.id.activity_main_dining_constraint_layout, R.id.activity_main_free_board_constraint_layout,
            R.id.activity_main_circle_constraint_layout, R.id.activity_main_anonymous_board_constraint_layout,
            R.id.activity_main_recruit_board_constraint_layout,
//            R.id.activity_main_callvan_constraint_layout,
            R.id.activity_main_used_market_constraint_layout, R.id.activity_main_timetable_constraint_layout})
    public void onClickedLayout(View v) {
        switch (v.getId()) {
            case R.id.activity_main_store_constraint_layout:
                callDrawerItem(R.id.navi_item_store);
                break;
            case R.id.activity_main_timetable_constraint_layout:
                callDrawerItem(R.id.navi_item_timetable);
                break;
            case R.id.activity_main_bus_constraint_layout:
                callDrawerItem(R.id.navi_item_bus);
                break;
            case R.id.activity_main_dining_constraint_layout:
                callDrawerItem(R.id.navi_item_dining);
                break;
            case R.id.activity_main_free_board_constraint_layout:
                callDrawerItem(R.id.navi_item_free_board);
                break;
            case R.id.activity_main_anonymous_board_constraint_layout:
                callDrawerItem(R.id.navi_item_anonymous_board);
                break;
            case R.id.activity_main_recruit_board_constraint_layout:
                callDrawerItem(R.id.navi_item_recruit_board);
                break;
            //콜밴쉐어링 HOLD
//            case R.id.activity_main_callvan_constraint_layout:
//                callDrawerItem(R.id.navi_item_callvansharing);
//                break;
            case R.id.activity_main_used_market_constraint_layout:
                callDrawerItem(R.id.navi_item_usedmarket);
                break;
            case R.id.activity_main_circle_constraint_layout:
                callDrawerItem(R.id.navi_item_cirlce);
                break;
            default:
                ToastUtil.getInstance().makeShort("서비스예정입니다");
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TIMETABLE_REQUEST_CODE)
            callDrawerItem(R.id.navi_item_timetable);
    }
}
