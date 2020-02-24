package in.koreatech.koin.ui.timetable.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.koreatech.koin.R;
import in.koreatech.koin.core.recyclerview.RecyclerViewClickListener;
import in.koreatech.koin.data.network.entity.Semester;


public class TimetableSemesterRecyclerAdapter extends RecyclerView.Adapter<TimetableSemesterRecyclerAdapter.ViewHolder> {

    private ArrayList<Semester> semesters;
    private RecyclerViewClickListener recyclerViewClickListener;
    private Context context;


    public void setRecyclerViewClickListener(RecyclerViewClickListener recyclerViewClickListener) {
        this.recyclerViewClickListener = recyclerViewClickListener;
    }

    public TimetableSemesterRecyclerAdapter(Context context, ArrayList<Semester> semesters) {
        this.context = context;
        this.semesters = semesters;
    }


    @NonNull
    @Override
    public TimetableSemesterRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timetable_select_semester_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimetableSemesterRecyclerAdapter.ViewHolder viewHolder, int position) {
        Semester semesterInfo = semesters.get(position);
        String yearString = semesterInfo.semester.substring(0, 4);
        String semesterString = semesterInfo.semester.substring(4);
        viewHolder.semesterLinearLayout.setOnClickListener(v -> {
            if (recyclerViewClickListener == null) return;
            recyclerViewClickListener.onClick(viewHolder.semesterLinearLayout, position);
        });

        viewHolder.semesterRadioButton.setOnClickListener(v -> {
            if (recyclerViewClickListener == null) return;
            recyclerViewClickListener.onClick(viewHolder.semesterLinearLayout, position);
        });

        if (semesterInfo.isSelected)
            viewHolder.semesterRadioButton.setChecked(true);
        else
            viewHolder.semesterRadioButton.setChecked(false);

        if (semesterInfo.semester != null)
            viewHolder.semesterTextview.setText(yearString + "년 " + semesterString + "학기");
    }

    @Override
    public int getItemCount() {
        return semesters == null ? 0 : semesters.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.timetable_semester_linearLayout)
        LinearLayout semesterLinearLayout;
        @BindView(R.id.timetable_semester_radiobutton)
        RadioButton semesterRadioButton;
        @BindView(R.id.timetable_semester_textview)
        TextView semesterTextview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}

