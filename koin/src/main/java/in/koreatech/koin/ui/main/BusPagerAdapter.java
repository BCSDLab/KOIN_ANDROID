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

public class BusPagerAdapter extends PagerAdapter {

    interface OnSwitchClickListener {
        void onSwitchClick();
    }

    interface OnCardClickListener {
        void onCardClick(BusKind busKind);
    }

    enum BusKind {
        SHUTTLE, DAESUNG, CITYBUS
    }

    private OnSwitchClickListener onSwitchClickListener;
    private OnCardClickListener onCardClickListener;

    private int departureState = 0; // 0 : 한기대 1 : 야우리 2 : 천안역
    private int arrivalState = 1; // 0 : 한기대 1 : 야우리 2 : 천안역
    private String busStates[] = {"한기대", "야우리"};

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
        view.setTag(BusKind.values()[position % 3]);

        LinearLayout busTypeLayout = view.findViewById(R.id.bus_type_layout);
        TextView textViewBusType = view.findViewById(R.id.text_view_bus_type);
        TextView textView = view.findViewById(R.id.text_view_bus_info);

        Object tag = view.getTag();

        if (BusKind.SHUTTLE.equals(tag)) {
            busTypeLayout.setBackgroundColor(ContextCompat.getColor(container.getContext(), R.color.colorAccent));
            textViewBusType.setText("학교셔틀");
            textView.setVisibility(shuttleInfo.isEmpty() ? View.GONE : View.VISIBLE);
            textView.setText(shuttleInfo + "분 출발");
            itemViewsShuttle.add(view);
        } else if (BusKind.DAESUNG.equals(tag)) {
            busTypeLayout.setBackgroundColor(ContextCompat.getColor(container.getContext(), R.color.blue5));
            textViewBusType.setText("대성고속");
            textView.setVisibility(daesungInfo.isEmpty() ? View.GONE : View.VISIBLE);
            textView.setText(daesungInfo + "분 출발");
            itemViewsDaesung.add(view);
        } else if (BusKind.CITYBUS.equals(tag)) {
            busTypeLayout.setBackgroundColor(ContextCompat.getColor(container.getContext(), R.color.green3));
            textViewBusType.setText("시내버스");
            textView.setVisibility(citybusInfo == 0 ? View.GONE : View.VISIBLE);
            textView.setText(citybusInfo + "번 버스");
            itemViewsCityBus.add(view);
        }

        ((TextView) view.findViewById(R.id.text_view_departures)).setText(busStates[departureState]);
        ((TextView) view.findViewById(R.id.text_view_arrival)).setText(busStates[arrivalState]);

        view.findViewById(R.id.image_button_switch).setOnClickListener(v -> {
            departureState = Math.abs(departureState - 1);
            arrivalState = Math.abs(arrivalState - 1);
            List<View> merged = new ArrayList<>(itemViewsCityBus);
            merged.addAll(itemViewsDaesung);
            merged.addAll(itemViewsShuttle);

            for (View itemView : merged) {
                ((TextView) itemView.findViewById(R.id.text_view_departures)).setText(busStates[departureState]);
                ((TextView) itemView.findViewById(R.id.text_view_arrival)).setText(busStates[arrivalState]);
            }

            if (onSwitchClickListener != null) onSwitchClickListener.onSwitchClick();
        });

        view.findViewById(R.id.container).setOnClickListener(v -> {
            if (onCardClickListener != null) onCardClickListener.onCardClick((BusKind) tag);
        });

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        container.removeView(view);
        Object tag = view.getTag();

        if (BusKind.SHUTTLE.equals(tag)) itemViewsShuttle.remove(view);
        else if (BusKind.DAESUNG.equals(tag)) itemViewsDaesung.remove(view);
        else if (BusKind.CITYBUS.equals(tag)) itemViewsCityBus.remove(view);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View) object);
    }

    public void updateShuttleBusTimeText(String value) {
        for (View view : itemViewsShuttle)
            ((TextView) view.findViewById(R.id.text_view_remaining_time)).setText(value);
    }

    public void updateCityBusTimeText(String value) {
        for (View view : itemViewsCityBus)
            ((TextView) view.findViewById(R.id.text_view_remaining_time)).setText(value);
    }

    public void updateDaesungBusTimeText(String value) {
        for (View view : itemViewsDaesung)
            ((TextView) view.findViewById(R.id.text_view_remaining_time)).setText(value);
    }

    public void updateShuttleBusDepartInfoText(String value) {
        shuttleInfo = value;
        for (View view : itemViewsShuttle) {
            TextView textView = view.findViewById(R.id.text_view_bus_info);
            textView.setVisibility(value.isEmpty() ? View.GONE : View.VISIBLE);
            textView.setText(value + "분 출발");
        }
    }

    public void updateCityBusDepartInfoText(int value) {
        citybusInfo = value;
        for (View view : itemViewsCityBus) {
            TextView textView = view.findViewById(R.id.text_view_bus_info);
            textView.setVisibility(value == 0 ? View.GONE : View.VISIBLE);
            textView.setText(value + "번 버스");
        }
    }

    public void updateDaesungBusDepartInfoText(String value) {
        daesungInfo = value;
        for (View view : itemViewsDaesung) {
            TextView textView = view.findViewById(R.id.text_view_bus_info);
            textView.setVisibility(value.isEmpty() ? View.GONE : View.VISIBLE);
            textView.setText(value + "분 출발");
        }
    }

    public int getDepartureState() {
        return departureState;
    }

    public int getArrivalState() {
        return arrivalState;
    }

    public void setOnSwitchClickListener(OnSwitchClickListener onSwitchClickListener) {
        this.onSwitchClickListener = onSwitchClickListener;
    }

    public void setOnCardClickListener(OnCardClickListener onCardClickListener) {
        this.onCardClickListener = onCardClickListener;
    }
}
