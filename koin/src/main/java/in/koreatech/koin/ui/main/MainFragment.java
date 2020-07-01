package in.koreatech.koin.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.R;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.ui.koinfragment.KoinBaseFragment;
import in.koreatech.koin.util.NavigationManger;

public class MainFragment extends KoinBaseFragment {
    public static final int TIMETABLE_REQUEST_CODE = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.activity_main_store_constraint_layout, R.id.activity_main_bus_constraint_layout,
            R.id.activity_main_dining_constraint_layout, R.id.activity_main_free_board_constraint_layout,
            R.id.activity_main_circle_constraint_layout, R.id.activity_main_anonymous_board_constraint_layout,
            R.id.activity_main_recruit_board_constraint_layout,
            R.id.activity_main_used_market_constraint_layout, R.id.activity_main_timetable_constraint_layout})
    public void onClickedLayout(View v) {
        switch (v.getId()) {
            case R.id.activity_main_store_constraint_layout:
                break;
            case R.id.activity_main_timetable_constraint_layout:
                break;
            case R.id.activity_main_bus_constraint_layout:
                break;
            case R.id.activity_main_dining_constraint_layout:
                break;
            case R.id.activity_main_free_board_constraint_layout:
                break;
            case R.id.activity_main_anonymous_board_constraint_layout:
                break;
            case R.id.activity_main_recruit_board_constraint_layout:
                break;
            case R.id.activity_main_used_market_constraint_layout:
                break;
            case R.id.activity_main_circle_constraint_layout:
                NavigationManger.getNavigationController(getActivity()).navigate(R.id.navi_item_circle_action, null, NavigationManger.getNavigationAnimation());
                break;
            default:
                ToastUtil.getInstance().makeShort("서비스예정입니다");
                break;
        }
    }
}
