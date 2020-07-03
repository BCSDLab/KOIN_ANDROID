package in.koreatech.koin.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;

import in.koreatech.koin.constant.AuthorizeConstant;
import in.koreatech.koin.data.network.entity.User;
import in.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.ui.login.LoginActivity;
import in.koreatech.koin.ui.userinfo.UserInfoActivity;

public class AuthorizeManager {
    public static AuthorizeConstant getAuthorize(Context context) {
        AuthorizeConstant authorizeConstant;
        try {
            authorizeConstant = UserInfoSharedPreferencesHelper.getInstance().checkAuthorize();
        } catch (NullPointerException e) {
            UserInfoSharedPreferencesHelper.getInstance().init(context.getApplicationContext());
            authorizeConstant = UserInfoSharedPreferencesHelper.getInstance().checkAuthorize();
        }
        return authorizeConstant;
    }

    public static User getUser(Context context) {
        User user;
        try {
            user = UserInfoSharedPreferencesHelper.getInstance().loadUser();
        } catch (NullPointerException e) {
            UserInfoSharedPreferencesHelper.getInstance().init(context.getApplicationContext());
            user = UserInfoSharedPreferencesHelper.getInstance().loadUser();
        }
        return user;
    }

    public static void showLoginRequestDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("회원 전용 서비스")
                .setMessage("로그인이 필요한 서비스입니다.\n로그인 하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("확인", (dialog, whichButton) -> {
                    Intent intent = new Intent(activity, LoginActivity.class);
                    intent.putExtra("FIRST_LOGIN", false);
                    activity.startActivity(intent);
                    activity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
                })
                .setNegativeButton("취소", (dialog, whichButton) -> {
                    dialog.cancel();
                });
        AlertDialog dialog = builder.create();    // 알림창 객체 생성
        dialog.show();    // 알림창 띄우기
    }


    public static void showNickNameRequestDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("닉네임이 필요합니다.")
                .setMessage("닉네임이 필요한 서비스입니다.\n닉네임을 추가 하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("확인", (dialog, whichButton) -> {
                    activity.startActivity(new Intent(activity, UserInfoActivity.class));
                    activity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
                })
                .setNegativeButton("취소", (dialog, whichButton) -> dialog.cancel());
        AlertDialog dialog = builder.create();    // 알림창 객체 생성
        dialog.show();    // 알림창 띄우기
    }

}
