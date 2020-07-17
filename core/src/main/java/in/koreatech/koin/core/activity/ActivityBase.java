package in.koreatech.koin.core.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import in.koreatech.koin.core.progressdialog.CustomProgressDialog;
import in.koreatech.koin.core.progressdialog.IProgressDialog;


public class ActivityBase extends AppCompatActivity implements IProgressDialog {
    private CustomProgressDialog customProgressDialog;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        try {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } catch (IllegalStateException ignore) {

        }
    }


    @Override
    public void showProgressDialog() {
        if (customProgressDialog == null) {
            customProgressDialog = new CustomProgressDialog(context);
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
