package in.koreatech.koin.ui.navigation;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

import in.koreatech.koin.R;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.util.NavigationManger;

import static in.koreatech.koin.constant.URLConstant.COMMUNITY.ID_ANONYMOUS;
import static in.koreatech.koin.constant.URLConstant.COMMUNITY.ID_FREE;
import static in.koreatech.koin.constant.URLConstant.COMMUNITY.ID_RECRUIT;

public class KoinNavigationFragment extends BaseNavigationFragment implements View.OnClickListener {
    private String currentService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);
        currentService = requireArguments().getString("CURRENT_SERVICE");
        return view;
    }


    @Override
    public void onClick(View view) {
        callDrawerItem(view.getTag().toString());
    }

    @Override
    protected String getCurrentService() {
        return Objects.requireNonNull(currentService);
    }

    @Override
    protected void goToStoreFragment() {
        NavigationManger.getNavigationController(getActivity()).navigate(R.id.navi_item_store_action, null, NavigationManger.getNavigationDrawerServiceSelectAnimation());
    }

    @Override
    protected void goToDiningFragment() {
        NavigationManger.getNavigationController(getActivity()).navigate(R.id.navi_item_store_action, null, NavigationManger.getNavigationDrawerServiceSelectAnimation());
    }

    @Override
    protected void goToBusFragment() {

    }

    @Override
    protected void goToFreeBoardFragment() {
        Bundle bundle = new Bundle();
        bundle.putInt("BOARD_UID", ID_FREE);
        NavigationManger.getNavigationController(getActivity()).navigate(R.id.navi_free_board_action, null, NavigationManger.getNavigationDrawerServiceSelectAnimation());
    }

    @Override
    protected void goToRecruitBoardFragment() {
        Bundle bundle = new Bundle();
        bundle.putInt("BOARD_UID", ID_RECRUIT);
        NavigationManger.getNavigationController(getActivity()).navigate(R.id.navi_recruit_board_action, null, NavigationManger.getNavigationDrawerServiceSelectAnimation());
    }

    @Override
    protected void goToAnonymousBoardFragment() {
        Bundle bundle = new Bundle();
        bundle.putInt("BOARD_UID", ID_ANONYMOUS);
        NavigationManger.getNavigationController(getActivity()).navigate(R.id.navi_anonymous_board_action, null, NavigationManger.getNavigationDrawerServiceSelectAnimation());
    }

    @Override
    protected void goToTimetableFragment() {

    }

    @Override
    protected void goToLandFragment() {

    }

    @Override
    protected void goToAnonymousTimeTableFragment() {

    }

    @Override
    protected void goToMarketFragment() {

    }

    @Override
    protected void goToCircleFragment() {

    }

    @Override
    protected void goToLostFoundFragment() {

    }

    @Override
    protected void goToTimeTableFragment() {

    }

    @Override
    protected void goToUserInfo() {

    }

    @Override
    protected void goToUserInfoFragment(int service) {

    }

    @Override
    protected void onClickNavigationkakaoTalk() {

    }

    @Override
    protected void onClickNavigationDeveloper() {

    }

    @Override
    protected void onClickNavigationLogout() {

    }

    @Override
    protected void onClickNavigationLogin() {

    }

    @Override
    protected void showLoginRequestDialog() {

    }

    @Override
    protected void showNickNameRequestDialog() {

    }

    @Override
    protected Context setContext() {
        return getContext();
    }

}
