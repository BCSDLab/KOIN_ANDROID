package in.koreatech.koin.ui;

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
import in.koreatech.koin.core.helpers.VersionDialogClickListner;
import in.koreatech.koin.core.networks.entity.Version;

import static androidx.core.content.ContextCompat.startActivity;
import static com.google.common.reflect.Reflection.getPackageName;

public class VersionUpdateDialog extends Dialog {

    @BindView(R.id.version_update_dialog_later_textview)
    TextView mVersionUpdateDialogLaterTextview;
    @BindView(R.id.version_update_dialog_update_textview)
    TextView mVersionUpdateDialogUpdateTextview;
    @BindView(R.id.version_update_dialog_current_version_textview)
    TextView mVersionUpdateCurrentVersionTextview;
    @BindView(R.id.version_update_dialog_update_version_textview)
    TextView mVersionUpdateUpdateVersionTextview;
    @BindView(R.id.version_update_dialog_info_textview)
    TextView mVersionUpdateDialogInfoTextview;
    Context mContext;
    VersionDialogClickListner mVersionDialogClickListner;


    public VersionUpdateDialog(@NonNull Context context, int type, String currentVersion, String updateVersion, VersionDialogClickListner versionDialogClickListner) {
        super(context);
        this.mContext = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.version_update_dialog);
        this.mVersionDialogClickListner = versionDialogClickListner;
        ButterKnife.bind(this);
        if (type == Version.PRIORITY_HIGH) {
            mVersionUpdateDialogLaterTextview.setVisibility(View.GONE);
            mVersionUpdateDialogInfoTextview.setText("필수 업데이트가 있습니다.\n업데이트 하시겠습니까?");
        }
        else if(type == Version.PRIORITY_MIDDLE)
        {
            mVersionUpdateDialogInfoTextview.setText("최신 업데이트가 있습니다.\n업데이트 하시겠습니까?");
        }
        mVersionUpdateCurrentVersionTextview.setText("현재버전 : " + currentVersion);
        mVersionUpdateUpdateVersionTextview.setText("업데이트 버전 : " + updateVersion);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    @OnClick(R.id.version_update_dialog_later_textview)
    public void onlaterTextviewClick() {
        mVersionDialogClickListner.onLaterClick();
        dismiss();
    }

    @OnClick(R.id.version_update_dialog_update_textview)
    public void onUpdateTextviewClick() {
        final String appPackageName = getContext().getPackageName();
        try {
            Intent appStoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName));
            appStoreIntent.setPackage("com.android.vending");
            startActivity(mContext, appStoreIntent, null);
        } catch (android.content.ActivityNotFoundException exception) {
            startActivity(mContext, new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)), null);
        }
        mVersionDialogClickListner.onUpdateClick();
        dismiss();
    }

}
