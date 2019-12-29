package in.koreatech.koin.service_bus.ui.bustimetable;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.koreatech.koin.R;


public class BusTimeTableVacationChungjuShuttleYongamEveningFragment extends Fragment {
   private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bus_time_table_vacation_chungju_shuttle_yongam_evening, container, false);
        return view;
    }


}
