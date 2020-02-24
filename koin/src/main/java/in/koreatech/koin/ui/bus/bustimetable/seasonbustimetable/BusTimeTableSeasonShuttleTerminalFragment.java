package in.koreatech.koin.ui.bus.bustimetable.seasonbustimetable;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.koreatech.koin.R;

public class BusTimeTableSeasonShuttleTerminalFragment extends Fragment {

    private FragmentManager fragmentManager;
    private  View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bus_time_table_season_shuttle_terminal, container, false);
        return view;
    }
}