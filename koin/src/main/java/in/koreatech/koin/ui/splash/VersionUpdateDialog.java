package in.koreatech.koin.ui.splash;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;

import androidx.annotation.NonNull;

import android.view.View;
import android.view.Window;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.R;
import in.koreatech.koin.core.helper.VersionDialogClickListener;
import in.koreatech.koin.data.network.entity.Version;

import static androidx.core.content.ContextCompat.startActivity;
import static com.google.common.reflect.Reflection.getPackageName;

public class VersionUpdateDialog extends Dialog {

    @BindView(R.id.version_update_dialog_later_textview)
    TextView versionUpdateDialogLaterTextview;
    @BindView(R.id.version_update_dialog_update_textview)
    TextView versionUpdateDialogUpdateTextview;
    @BindView(R.id.version_update_dialog_current_version_textview)
    TextView versionUpdateCurrentVersionTextview;
    @BindView(R.id.version_update_dialog_update_version_textview)
    TextView versionUpdateUpdateVersionTextview;
    @BindView(R.id.version_update_dialog_info_textview)
    TextView versionUpdateDialogInfoTextview;
    Context context;
    VersionDialogClickListener versionDialogClickListener;


    public VersionUpdateDialog(@NonNull Context context, int type, String currentVersion, String updateVersion, VersionDialogClickListener versionDialogClickListener) {
        super(context);
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.version_update_dialog);
        this.versionDialogClickListener = versionDialogClickListener;
        ButterKnife.bind(this);
        if (type == Version.PRIORITY_HIGH) {
            this.versionUpdateDialogLaterTextview.setVisibility(View.GONE);
            versionUpdateDialogInfoTextview.setText("필수 업데이트가 있습니다.\n업데이트 하시겠습니까?");
        } else if (type == Version.PRIORITY_MIDDLE) {
            versionUpdateDialogInfoTextview.setText("최신 업데이트가 있습니다.\n업데이트 하시겠습니까?");
        }
        versionUpdateCurrentVersionTextview.setText("현재버전 : " + currentVersion);
        versionUpdateUpdateVersionTextview.setText("업데이트 버전 : " + updateVersion);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    @OnClick(R.id.version_update_dialog_later_textview)
    public void onlaterTextviewClick() {
        versionDialogClickListener.onLaterClick();
        dismiss();
    }

    @OnClick(R.id.version_update_dialog_update_textview)
    public void onUpdateTextviewClick() {
        final String appPackageName = getContext().getPackageName();
        try {
            Intent appStoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName));
            appStoreIntent.setPackage("com.android.vending");
            startActivity(context, appStoreIntent, null);
        } catch (android.content.ActivityNotFoundException exception) {
            startActivity(context, new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)), null);
        }
        versionDialogClickListener.onUpdateClick();
        dismiss();
    }

}
