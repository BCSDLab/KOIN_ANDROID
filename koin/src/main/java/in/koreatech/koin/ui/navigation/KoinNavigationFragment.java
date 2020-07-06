package in.koreatech.koin.ui.navigation;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

import in.koreatech.koin.R;
import in.koreatech.koin.core.activity.WebViewActivity;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.ui.login.LoginActivity;
import in.koreatech.koin.util.AuthorizeManager;
import in.koreatech.koin.util.NavigationManger;

import static in.koreatech.koin.constant.URLConstant.COMMUNITY.ID_ANONYMOUS;
import static in.koreatech.koin.constant.URLConstant.COMMUNITY.ID_FREE;
import static in.koreatech.koin.constant.URLConstant.COMMUNITY.ID_RECRUIT;

public class KoinNavigationFragment extends BaseNavigationFragment implements View.OnClickListener {
    private final int[] NAVIGATION_BUTTON_ID = {
            R.id.navi_item_myinfo, R.id.navi_item_store, R.id.navi_item_dining,
            R.id.navi_item_bus, R.id.navi_item_circle, R.id.navi_item_timetable,
            R.id.navi_item_land, R.id.navi_item_anonymous_board, R.id.navi_item_recruit_board,
            R.id.navi_item_free_board, R.id.navi_item_lostfound, R.id.navi_item_usedmarket,
            R.id.navi_item_kakao_talk, R.id.navi_item_developer
    };

    private final int[] NAVIGATION_TEXT_VIEW_ID = {
            R.id.navi_item_myinfo_textview, R.id.navi_item_store_textview, R.id.navi_item_store_textview,
            R.id.navi_item_bus_textview, R.id.navi_item_circle_textview, R.id.navi_item_timetable_textview,
            R.id.navi_item_land_textview, R.id.navi_item_anonymous_board_textview, R.id.navi_item_recruit_board_textview,
            R.id.navi_item_free_board_textview, R.id.navi_item_lostfound_textview, R.id.navi_item_usedmarket_textview,
            R.id.navi_item_kakao_talk_textview
    };

    private String currentService;
    private TextView nickNameTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);
        currentService = requireArguments().getString("CURRENT_SERVICE");
        init(view);
        return view;
    }

    private void init(View view) {
        for (int id : NAVIGATION_BUTTON_ID) {
            view.findViewById(id).setOnClickListener(this);
        }
        nickNameTextView = view.findViewById(R.id.navi_name_textview);
        view.findViewById(R.id.navi_close_imageview).setOnClickListener(this::closeNavigation);
        nickNameTextView.setText(getName());
        setCurrentServiceTextView(view);
    }

    private void closeNavigation(View view) {
        closeNavigationDrawer();
    }

    private void setCurrentServiceTextView(View view) {
        for (int id : NAVIGATION_TEXT_VIEW_ID) {
            TextView serviceTextView = view.findViewById(id);
            if (serviceTextView.getTag() != null && serviceTextView.getTag().equals(getCurrentService())) {
                serviceTextView.setTextColor(getResources().getColor(R.color.colorAccent));
            }
        }
    }

    private String getName() {
        if (AuthorizeManager.getUser(getContext()) != null && AuthorizeManager.getUser(getContext()).getUserName() != null) {
            return AuthorizeManager.getUser(getContext()).getUserName();
        } else {
            return getResources().getString(R.string.anonymous);
        }
    }

    @Override
    protected void closeNavigationDrawer() {
        if (NavigationManger.isDrawerOpen(getActivity()))
            NavigationManger.getNavigationController(getActivity()).popBackStack();
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
        NavigationManger.getNavigationController(getActivity()).navigate(R.id.navi_item_dining_action, null, NavigationManger.getNavigationDrawerServiceSelectAnimation());
    }

    @Override
    protected void goToBusFragment() {
        NavigationManger.getNavigationController(getActivity()).navigate(R.id.navi_item_bus_action, null, NavigationManger.getNavigationDrawerServiceSelectAnimation());
    }

    @Override
    protected void goToFreeBoardFragment() {
        Bundle bundle = new Bundle();
        bundle.putInt("BOARD_UID", ID_FREE);
        NavigationManger.getNavigationController(getActivity()).navigate(R.id.navi_free_board_action, bundle, NavigationManger.getNavigationDrawerServiceSelectAnimation());
    }

    @Override
    protected void goToRecruitBoardFragment() {
        Bundle bundle = new Bundle();
        bundle.putInt("BOARD_UID", ID_RECRUIT);
        NavigationManger.getNavigationController(getActivity()).navigate(R.id.navi_recruit_board_action, bundle, NavigationManger.getNavigationDrawerServiceSelectAnimation());
    }

    @Override
    protected void goToAnonymousBoardFragment() {
        Bundle bundle = new Bundle();
        bundle.putInt("BOARD_UID", ID_ANONYMOUS);
        NavigationManger.getNavigationController(getActivity()).navigate(R.id.navi_anonymous_board_action, bundle, NavigationManger.getNavigationDrawerServiceSelectAnimation());
    }

    @Override
    protected void goToLandFragment() {

    }

    @Override
    protected void goToMarketFragment() {

    }

    @Override
    protected void goToCircleFragment() {
        NavigationManger.getNavigationController(getActivity()).navigate(R.id.navi_item_circle_action, null, NavigationManger.getNavigationDrawerServiceSelectAnimation());
    }

    @Override
    protected void goToLostFoundFragment() {
    }

    @Override
    protected void goToTimeTableFragment() {
        NavigationManger.getNavigationController(getActivity()).navigate(R.id.navi_item_timetable_anonymous_action, null, NavigationManger.getNavigationDrawerServiceSelectAnimation());
    }

    @Override
    protected void goToUserInfoFragment(int service) {
        Bundle bundle = new Bundle();
        bundle.putInt("CONDITION", service);
        NavigationManger.getNavigationController(getActivity()).navigate(R.id.navi_userinfo_action, bundle, NavigationManger.getNavigationDrawerServiceSelectAnimation());
    }

    @Override
    protected void onClickNavigationkakaoTalk() {
        boolean isKaKaoTalkInstalled = false;

        try {   //카카오톡 APP 설치 유무
            getContext().getPackageManager().getPackageInfo("com.kakao.talk", PackageManager.GET_ACTIVITIES);
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

    @Override
    protected void onClickNavigationDeveloper() {
        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        intent.putExtra("title", "BCSD 홈페이지");
        intent.putExtra("url", "https://bcsdlab.com/");
        startActivity(intent);
    }

    @Override
    protected void onClickNavigationLogout() {
        UserInfoSharedPreferencesHelper.getInstance().clear();
        getActivity().finishAffinity();
        startActivity(new Intent(getContext(), LoginActivity.class));
        ToastUtil.getInstance().makeShort("로그아웃 되었습니다.");
        getActivity().overridePendingTransition(R.anim.fade, R.anim.hold);
    }

    @Override
    protected void onClickNavigationLogin() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.putExtra("FIRST_LOGIN", false);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.fade, R.anim.hold);
    }

    @Override
    protected void showLoginRequestDialog() {
        AuthorizeManager.showLoginRequestDialog(getActivity());
    }

    @Override
    protected void showNickNameRequestDialog() {
        AuthorizeManager.showNickNameRequestDialog(getActivity());
    }

    @Override
    protected Context setContext() {
        return getContext();
    }

}
