package in.koreatech.koin.ui.navigation;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import in.koreatech.koin.R;
import in.koreatech.koin.util.FormValidatorUtil;

public class KoinNavigationDrawerFragment extends Fragment implements View.OnClickListener {
    private boolean isMenuSelected = false;
    private View fragmentView;
    private TextView nameTextView;
    private ImageView bcsdImageView;
    private ImageView closeImageView;
    private int selectItemId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_navigation, container, false);
        setSelectedTextViewColor(NavigationManager.getInstance().getCurrentService());
        nameTextView = fragmentView.findViewById(R.id.navi_name_textview);
        bcsdImageView = fragmentView.findViewById(R.id.navi_item_developer);
        closeImageView = fragmentView.findViewById(R.id.navi_close_imageview);
        closeImageView.setOnClickListener(v -> closeNavigationDrawer());
        bcsdImageView.setOnClickListener(this);
        for (Integer menuLayoutId : NavigationManager.getInstance().getMenuIdArray()) {
            LinearLayout menuLayout = fragmentView.findViewById(menuLayoutId);
            if (menuLayout != null)
                menuLayout.setOnClickListener(this);
        }

        init();
        return fragmentView;
    }

    public void init() {
        selectItemId = -1;
        setLeftNavigationDrawerName();
    }

    private void setLeftNavigationDrawerName() {
        nameTextView.setText(getName());
    }


    private String getName() {
        String name  = NavigationManager.getInstance().getName();
        return (FormValidatorUtil.validateEndPlaceIsEmpty(name)) ? getActivity().getResources().getString(R.string.anonymous) : name;
    }

    @Override
    public void onClick(View view) {
        int clickId = view.getId();
        selectItemId = clickId;
        isMenuSelected = true;
        if (!NavigationManager.getInstance().isServiceSame(clickId)) {
            setSelectedTextViewColor(clickId);
        }

        closeNavigationDrawer();
    }

    public void setSelectedTextViewColor(@IdRes int serviceId) {
        Integer textViewId = NavigationManager.getInstance().getMenuTextView(serviceId);
        if (textViewId != null) {
            disableAllSelectedTextViewColor();
            TextView changeText = fragmentView.findViewById(textViewId);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                changeText.setTextColor(getActivity().getResources().getColor(R.color.colorAccent, getActivity().getTheme()));
            } else {
                changeText.setTextColor(getActivity().getResources().getColor(R.color.colorAccent));
            }
        }
    }

    public void disableAllSelectedTextViewColor(){
        for (Integer menuTextViewId : NavigationManager.getInstance().getMenuTextViewId()) {
            TextView menuTextView = fragmentView.findViewById(menuTextViewId);
            if (menuTextView != null)
               {
                   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                       menuTextView.setTextColor(getActivity().getResources().getColor(R.color.black, getActivity().getTheme()));
                   } else {
                       menuTextView.setTextColor(getActivity().getResources().getColor(R.color.black));
                   }
               }
        }

    }

    public void closeNavigationDrawer() {
        if (getActivity() != null)
            ((KoinNavigationDrawer) getActivity()).closeNavigationDrawer();
    }


    @Nullable
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (!enter && nextAnim == R.anim.slide_finsh_right) {
            Animation anim = AnimationUtils.loadAnimation(getActivity(), nextAnim);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation arg) {
                    if (getActivity() != null) {
                        if (isMenuSelected) {
                            ((KoinNavigationDrawer) getActivity()).goToService(selectItemId);
                        }
                        getActivity().finish();
                        getActivity().overridePendingTransition(0, 0);
                    }
                }
            });
            return anim;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }
}
