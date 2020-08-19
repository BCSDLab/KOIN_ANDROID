package in.koreatech.koin.core.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import in.koreatech.koin.core.progressdialog.CustomProgressDialog;
import in.koreatech.koin.core.progressdialog.IProgressDialog;
import in.koreatech.koin.core.utils.StatusBarUtil;


public class ActivityBase extends AppCompatActivity implements IProgressDialog {
    private CustomProgressDialog customProgressDialog;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        StatusBarUtil.makeStatusBarFillTransparent(getWindow());
        try {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } catch (IllegalStateException ignore) {

        }
    }


    @Override
    public void showProgressDialog(@Nullable String message) {
        if (customProgressDialog == null) {
            customProgressDialog = new CustomProgressDialog(context, message);
            customProgressDialog.execute();
        }
    }

    @Override
    public void showProgressDialog(@StringRes int resId) {
        if (customProgressDialog == null) {
            customProgressDialog = new CustomProgressDialog(context, context.getResources().getString(resId));
            customProgressDialog.execute();
        }
    }

    @Override
    public void hideProgressDialog() {
        if (customProgressDialog != null) {
            customProgressDialog.cancel(false);
            customProgressDialog = null;
        }
    }
}
