package in.koreatech.koin.util;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import in.koreatech.koin.R;

public class DialogUtil {
    public static void setDialogAnimation(Window window) {
        if(window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setWindowAnimations(R.style.DialogPopupAnimationStyle);
        }
    }
}
