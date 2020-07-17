package in.koreatech.koin.core.progressdialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;

import in.koreatech.koin.core.R;

public class LottieProgressDialog extends Dialog {
    private LottieAnimationView progressLottieAnimationView;
    public LottieProgressDialog(@NonNull Context context) {
        super(context, R.style.LottieProgressTheme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setAttributes(layoutParams);
        //getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_progress);
        progressLottieAnimationView = findViewById(R.id.progress_lotti_animationview);
        progressLottieAnimationView.playAnimation();
        progressLottieAnimationView.setRepeatCount(LottieDrawable.INFINITE);
    }

}
