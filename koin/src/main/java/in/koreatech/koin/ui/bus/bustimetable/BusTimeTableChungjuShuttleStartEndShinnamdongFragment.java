package in.koreatech.koin.ui.bus.bustimetable;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Unbinder;
import in.koreatech.koin.R;
import in.koreatech.koin.ui.bus.BusMainFragment;


/**
 * @author yunjae na
 * @since 2018.09.16
 */
public class BusTimeTableChungjuShuttleStartEndShinnamdongFragment extends BusMainFragment {
    private final String TAG = "BusTimeTableChungjuShuttleStartEndShinnamdongFragment";

    /* View Component */
    private View view;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.bus_timetable_chungju_shuttle_start_end_shinnamdong, container, false);
        init();
        return this.view;
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