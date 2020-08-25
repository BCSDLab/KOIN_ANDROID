package in.koreatech.koin.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.koreatech.koin.R;
import in.koreatech.koin.util.BusTimerUtil;

public class BusPagerAdapter extends PagerAdapter {
    enum BusType {
        SHUTTLE, DAESUNG, CITYBUS
    }

    private List<View> itemViewsShuttle = new ArrayList<>();
    private List<View> itemViewsDaesung = new ArrayList<>();
    private List<View> itemViewsCityBus = new ArrayList<>();

    private String shuttleInfo = "";
    private String daesungInfo = "";
    private int citybusInfo = 0;

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.main_card_bus, container, false);
        view.setTag(BusType.values()[position % 3]);

        LinearLayout busTypeLayout = view.findViewById(R.id.bus_type_layout);
        TextView textViewBusType = view.findViewById(R.id.text_view_bus_type);
        TextView textView = view.findViewById(R.id.text_view_bus_info);

        Object tag = view.getTag();

        if (BusType.SHUTTLE.equals(tag)) {
            busTypeLayout.setBackgroundColor(ContextCompat.getColor(container.getContext(), R.color.colorAccent));
            textViewBusType.setText("학교셔틀");
            textView.setVisibility(shuttleInfo.isEmpty() ? View.INVISIBLE : View.VISIBLE);
            textView.setText(shuttleInfo + "분 출발");
            itemViewsShuttle.add(view);
        } else if (BusType.DAESUNG.equals(tag)) {
            busTypeLayout.setBackgroundColor(ContextCompat.getColor(container.getContext(), R.color.blue5));
            textViewBusType.setText("대성고속");
            textView.setVisibility(daesungInfo.isEmpty() ? View.INVISIBLE : View.VISIBLE);
            textView.setText(daesungInfo + "분 출발");
            itemViewsDaesung.add(view);
        } else if (BusType.CITYBUS.equals(tag)) {
            busTypeLayout.setBackgroundColor(ContextCompat.getColor(container.getContext(), R.color.green3));
            textViewBusType.setText("시내버스");
            textView.setVisibility(citybusInfo == 0 ? View.INVISIBLE : View.VISIBLE);
            textView.setText(citybusInfo + "번 버스");
            itemViewsCityBus.add(view);
        }

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        container.removeView(view);
        Object tag = view.getTag();

        if (BusType.SHUTTLE.equals(tag)) itemViewsShuttle.remove(view);
        else if (BusType.DAESUNG.equals(tag)) itemViewsDaesung.remove(view);
        else if (BusType.CITYBUS.equals(tag)) itemViewsCityBus.remove(view);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View) object);
    }

    public void updateShuttleBusTimeText(String value) {
        for(View view : itemViewsShuttle) ((TextView) view.findViewById(R.id.text_view_remaining_time)).setText(value);
    }

    public void updateCityBusTimeText(String value) {
        for(View view : itemViewsCityBus) ((TextView) view.findViewById(R.id.text_view_remaining_time)).setText(value);
    }

    public void updateDaesungBusTimeText(String value) {
            for(View view : itemViewsDaesung) ((TextView) view.findViewById(R.id.text_view_remaining_time)).setText(value);
    }

    public void updateShuttleBusDepartInfoText(String value) {
        shuttleInfo = value;
        for(View view : itemViewsShuttle) {            TextView textView = view.findViewById(R.id.text_view_bus_info);
            textView.setVisibility(value.isEmpty() ? View.INVISIBLE : View.VISIBLE);
            textView.setText(value + "분 출발");
        }
    }

    public void updateCityBusDepartInfoText(int value) {
        citybusInfo = value;
        for(View view : itemViewsCityBus) {
            TextView textView = view.findViewById(R.id.text_view_bus_info);
            textView.setVisibility(value == 0 ? View.INVISIBLE : View.VISIBLE);
            textView.setText(value + "번 버스");
        }
    }

    public void updateDaesungBusDepartInfoText(String value) {
        daesungInfo = value;
        for(View view : itemViewsDaesung) {
            TextView textView = view.findViewById(R.id.text_view_bus_info);
            textView.setVisibility(value.isEmpty() ? View.INVISIBLE : View.VISIBLE);
            textView.setText(value + "분 출발");
        }
    }




}
