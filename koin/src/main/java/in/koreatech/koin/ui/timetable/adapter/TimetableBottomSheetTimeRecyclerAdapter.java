package in.koreatech.koin.ui.timetable.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.koreatech.koin.R;
import in.koreatech.koin.data.network.entity.TimeTable;

public class TimetableBottomSheetTimeRecyclerAdapter extends RecyclerView.Adapter<TimetableBottomSheetTimeRecyclerAdapter.ViewHolder> {
    private final String TAG = "TimetableBottomSheetTimeRecyclerAdapter";


    private Context context;
    private ArrayList<TimeTable.TimeTableItem> timeTableItemArrayList;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.timetable_custom_time_add_day_spinner)
        Spinner timetableCustomTimeAddDaySpinner;
        @BindView(R.id.timetable_custom_time_add_start_time)
        TextView timetableCustomTimeAddStartTimeTextview;
        @BindView(R.id.timetable_custom_time_add_end_time)
        TextView timetableCustomTimeAddEndTimeTextview;
        @BindView(R.id.timetable_custom_time_edit_imageview)
        ImageView timetableCustomTimeEditImageview;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public TimetableBottomSheetTimeRecyclerAdapter(Context context, ArrayList<TimeTable.TimeTableItem> lectureArrayList) {
        this.context = context;
        this.timeTableItemArrayList = new ArrayList<>();
        this.timeTableItemArrayList.addAll(lectureArrayList);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.timetable_custom_time_add_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        TimeTable.TimeTableItem timeTableItem = this.timeTableItemArrayList.get(position);
        if (position > 0)
            holder.timetableCustomTimeEditImageview.setBackground(this.context.getResources().getDrawable(R.drawable.ic_delete_timetable_time));
//        holder.timetableCustomTimeAddDaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view,
//                                       int position, long id) {
//                schedule.setDay(position);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//
//        holder.mTimetableCustomTimeAddStartTimeTextview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CustomTimePickerDialog dialog = new CustomTimePickerDialog(this.context,listener,schedule.getStartTime().getHour(), schedule.getStartTime().getMinute(), false);
//                dialog.show();
//            }
//
//            private TimePickerDialog.OnTimeSetListener listener = (view, hourOfDay, minute) -> {
//                holder.mTimetableCustomTimeAddStartTimeTextview.setText(hourOfDay + ":" + minute);
//                schedule.getStartTime().setHour(hourOfDay);
//                schedule.getStartTime().setMinute(minute);
//            };
//        });
//
//        holder.mTimetableCustomTimeAddEndTimeTextview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CustomTimePickerDialog dialog = new CustomTimePickerDialog(this.context,listener,schedule.getStartTime().getHour(), schedule.getStartTime().getMinute(), false);
//                dialog.show();
//            }
//
//            private TimePickerDialog.OnTimeSetListener listener = (view, hourOfDay, minute) -> {
//                holder.mTimetableCustomTimeAddEndTimeTextview.setText(hourOfDay + ":" + minute);
//                schedule.getEndTime().setHour(hourOfDay);
//                schedule.getEndTime().setMinute(minute);
//            };
//        });

    }


    @Override
    public int getItemCount() {
        return this.timeTableItemArrayList.size();
    }

}
