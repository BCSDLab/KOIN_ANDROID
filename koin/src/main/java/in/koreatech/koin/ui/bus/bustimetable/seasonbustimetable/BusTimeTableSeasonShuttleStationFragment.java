package in.koreatech.koin.ui.bus.bustimetable.seasonbustimetable;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.koreatech.koin.R;

public class BusTimeTableSeasonShuttleStationFragment extends Fragment {

    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bus_time_table_season_shuttle_station, container, false);
        return view;
    }


}