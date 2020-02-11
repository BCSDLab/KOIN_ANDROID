package in.koreatech.koin.ui.bus;

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
    TextView busTimeTableSearchResultShuttleBusTextview;
    @BindView(R.id.bus_timetable_search_result_daesung_bus_textview)
    TextView busTimeTableSearchResultDaesungBusTextview;
    @BindView(R.id.bus_timetable_search_result_close_button)
    Button busTimeTableSearchResultCloseButton;


    public BusTimeTableSearchResultDialog(@NonNull Context context, String shuttleBusTime, String daesungBusTime) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.bus_timetable_search_result_dialog);
        ButterKnife.bind(this);
        this.busTimeTableSearchResultShuttleBusTextview.setText(shuttleBusTime);
        this.busTimeTableSearchResultDaesungBusTextview.setText(daesungBusTime);
    }

    @OnClick(R.id.bus_timetable_search_result_close_button)
    public void oncloseButtonClick() {
        dismiss();
    }

}
