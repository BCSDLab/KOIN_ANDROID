package in.koreatech.koin.core.progressdialog;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

public interface IProgressDialog {
    void showProgressDialog(@Nullable String message);

    void showProgressDialog(@StringRes int resId);

    void hideProgressDialog();
}
