package in.koreatech.koin.ui.koinfragment;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;

import in.koreatech.koin.core.fragment.BaseFragment;

public class KoinBaseFragment extends BaseFragment {
    public static final String TAG = "KoinBaseFragment";
    private boolean isFinishingBackgroundEnabled;
    private OnBackPressedCallback callback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        isFinishingBackgroundEnabled = true;
        callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                addFinishingBackground();

                if (isEnabled()) {
                    setEnabled(false);
                    requireActivity().onBackPressed();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    public void addFinishingBackground() {
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundColor(getResources().getColor(in.koreatech.koin.core.R.color.black_alpha25));
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        if (getView() != null && getView().getClass().getName().equals(FrameLayout.class.getName())) {
            ((FrameLayout) getView()).addView(imageView);
            imageView.bringToFront();
        }

    }

    protected boolean isFinishingBackgroundEnabled() {
        return isFinishingBackgroundEnabled;
    }

    protected void setFinishingBackgroundEnabled(boolean enable) {
        isFinishingBackgroundEnabled = enable;
        callback.setEnabled(isFinishingBackgroundEnabled);
    }

    public void onBackPressed() {
        requireActivity().onBackPressed();
    }
}
