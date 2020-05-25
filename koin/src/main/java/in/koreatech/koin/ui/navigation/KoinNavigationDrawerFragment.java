package in.koreatech.koin.ui.navigation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import in.koreatech.koin.R;

public class KoinNavigationDrawerFragment extends Fragment {
    private boolean isMenuSelected = false;
    private int selectItemId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);
        view.findViewById(R.id.navi_item_store).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectItemId = v.getId();
                isMenuSelected = true;
                closeNavigationDrawer();
            }
        });
        init();
        return view;
    }

    public void init() {
        selectItemId = -1;
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
                            Intent intent = new Intent();
                            intent.putExtra(BaseNavigationActivity.IS_NAVIGATION_CLICKED, true);
                            intent.putExtra(BaseNavigationActivity.NAVIGATION_ID, selectItemId);
                            getActivity().setResult(Activity.RESULT_OK, intent);
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
