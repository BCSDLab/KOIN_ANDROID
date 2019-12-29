package in.koreatech.koin.service_bus.ui.bustimetable;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Unbinder;
import in.koreatech.koin.R;
import in.koreatech.koin.service_bus.ui.BusMainFragment;


/**
 * @author yunjae na
 * @since 2018.09.16
 */
public class BusTimeTableChungjuShuttleStartEndGymFragment extends BusMainFragment {
    private final String TAG = BusTimeTableChungjuShuttleStartEndGymFragment.class.getSimpleName();


    private Unbinder mUnbinder;
    private FragmentManager mFragmentManger;

    /* View Component */
    private View mView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.bus_timetable_chungju_shuttle_start_end_gym, container, false);
        init();
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public void init() {

    }


}