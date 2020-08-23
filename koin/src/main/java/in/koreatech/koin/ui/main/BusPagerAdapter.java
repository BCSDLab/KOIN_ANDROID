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
import java.util.List;

import in.koreatech.koin.R;
import in.koreatech.koin.util.BusTimerUtil;

public class BusPagerAdapter extends PagerAdapter {
    enum BusType {
        SHUTTLE, DAESUNG, CITYBUS
    }

    List<View> itemViewsShuttle = new ArrayList<>();
    List<View> itemViewsDaesung = new ArrayList<>();
    List<View> itemViewsCityBus = new ArrayList<>();

    private BusTimerUtil citySoonBusTimerUtil;
    private BusTimerUtil daesungBusSoonBusTimerUtil;
    private BusTimerUtil shuttleBusSoonBusTimerUtil;

    public BusPagerAdapter() {
        citySoonBusTimerUtil = new BusTimerUtil(10);
        daesungBusSoonBusTimerUtil = new BusTimerUtil(11);
        shuttleBusSoonBusTimerUtil = new BusTimerUtil(12);
    }

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

        Object tag = view.getTag();
        if (BusType.SHUTTLE.equals(tag)) {
            busTypeLayout.setBackgroundColor(ContextCompat.getColor(container.getContext(), R.color.colorAccent));
            textViewBusType.setText("학교셔틀");
            itemViewsShuttle.add(view);
        } else if (BusType.DAESUNG.equals(tag)) {
            busTypeLayout.setBackgroundColor(ContextCompat.getColor(container.getContext(), R.color.blue5));
            textViewBusType.setText("대성고속");
            itemViewsDaesung.add(view);
        } else if (BusType.CITYBUS.equals(tag)) {
            busTypeLayout.setBackgroundColor(ContextCompat.getColor(container.getContext(), R.color.green3));
            textViewBusType.setText("시내버스");
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

    public void updateShuttleBusTime(int current) {
        if (current >= 0) {
            shuttleBusSoonBusTimerUtil.setEndTime(current);
            for(View view : itemViewsShuttle) ((TextView) view.findViewById(R.id.text_view_remaining_time)).setText(
                    shuttleBusSoonBusTimerUtil.getStrTime());
            shuttleBusSoonBusTimerUtil.startTimer();
        } else {
            shuttleBusSoonBusTimerUtil.stopTimer();
            for(View view : itemViewsShuttle) ((TextView) view.findViewById(R.id.text_view_remaining_time)).setText(R.string.bus_no_information);
        }
    }

    public void updateCityBusTime(int current) {
        if (current > 0) {
            this.citySoonBusTimerUtil.setEndTime(current);
            for(View view : itemViewsCityBus) ((TextView) view.findViewById(R.id.text_view_remaining_time)).setText(
                    citySoonBusTimerUtil.getStrTime());
            this.citySoonBusTimerUtil.startTimer();

        } else {
            for(View view : itemViewsCityBus) ((TextView) view.findViewById(R.id.text_view_remaining_time)).setText(
                R.string.bus_no_information);
            this.citySoonBusTimerUtil.stopTimer();
        }
    }

    public void updateDaesungBusTime(int current) {
        if (current > 0) {
            daesungBusSoonBusTimerUtil.setEndTime(current);
            for(View view : itemViewsDaesung) ((TextView) view.findViewById(R.id.text_view_remaining_time)).setText(
                    daesungBusSoonBusTimerUtil.getStrTime());
            daesungBusSoonBusTimerUtil.startTimer();
        } else {
            daesungBusSoonBusTimerUtil.stopTimer();
            for(View view : itemViewsDaesung) ((TextView) view.findViewById(R.id.text_view_remaining_time)).setText(
                    R.string.bus_no_information);
        }
    }

    public void updateShuttleBusDepartInfo(String current) {
        for(View view : itemViewsShuttle) {
            TextView textView = view.findViewById(R.id.text_view_bus_info);
            textView.setVisibility(current.isEmpty() ? View.INVISIBLE : View.VISIBLE);
            textView.setText("(" + current + ")분 출발");
        }
    }

    public void updateCityBusDepartInfo(int current) {
        for(View view : itemViewsCityBus) {
            TextView textView = view.findViewById(R.id.text_view_bus_info);
            if (current == 0) {
                textView.setVisibility(View.INVISIBLE);
            } else {
                textView.setVisibility(View.VISIBLE);
                textView.setText(Integer.toString(current) + "번 버스");
            }
        }
    }

    public void updateDaesungBusDepartInfo(String current) {
        for(View view : itemViewsDaesung) {
            TextView textView = view.findViewById(R.id.text_view_bus_info);
            textView.setVisibility(current.isEmpty() ? View.INVISIBLE : View.VISIBLE);
            textView.setText("(" + current + ")분 출발");
        }
    }

}
