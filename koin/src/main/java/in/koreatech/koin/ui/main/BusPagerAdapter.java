package in.koreatech.koin.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;

import in.koreatech.koin.R;

public class BusPagerAdapter extends PagerAdapter {
    enum BusType {
        SHUTTLE, DAESUNG, CIRYBUS
    }

    @Override
    public int getCount() {
        return 3;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.main_card_bus, container, false);
        view.setTag(BusType.values()[position]);

        LinearLayout busTypeLayout = view.findViewById(R.id.bus_type_layout);
        TextView textViewBusType = view.findViewById(R.id.text_view_bus_type);

        Object tag = view.getTag();
        if (BusType.SHUTTLE.equals(tag)) {
            busTypeLayout.setBackgroundColor(ContextCompat.getColor(container.getContext(), R.color.colorAccent));
            textViewBusType.setText("학교셔틀");
        } else if (BusType.DAESUNG.equals(tag)) {
            busTypeLayout.setBackgroundColor(ContextCompat.getColor(container.getContext(), R.color.blue5));
            textViewBusType.setText("대성고속");
        } else if (BusType.CIRYBUS.equals(tag)) {
            busTypeLayout.setBackgroundColor(ContextCompat.getColor(container.getContext(), R.color.green3));
            textViewBusType.setText("시내버스");
        }

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View) object);
    }
}
