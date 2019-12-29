package in.koreatech.koin.service_bus.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.R;

public class BusTimeTableSearchResultDialog extends Dialog {

    @BindView(R.id.bus_timetable_search_result_shuttle_bus_textview)
    TextView mBusTimeTableSearchResultShuttleBusTextview;
    @BindView(R.id.bus_timetable_search_result_daesung_bus_textview)
    TextView mBusTimeTableSearchResultDaesungBusTextview;
    @BindView(R.id.bus_timetable_search_result_close_button)
    Button mBusTimeTableSearchResultCloseButton;


    public BusTimeTableSearchResultDialog(@NonNull Context context , String shuttleBusTime , String daesungBusTime) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.bus_timetable_search_result_dialog);
        ButterKnife.bind(this);
        mBusTimeTableSearchResultShuttleBusTextview.setText(shuttleBusTime);
        mBusTimeTableSearchResultDaesungBusTextview.setText(daesungBusTime);
    }

    @OnClick(R.id.bus_timetable_search_result_close_button)
    public void oncloseButtonClick()
    {
        dismiss();
    }

}
