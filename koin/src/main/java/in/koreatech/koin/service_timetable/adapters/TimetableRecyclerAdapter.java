package in.koreatech.koin.service_timetable.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.koreatech.koin.R;
import in.koreatech.koin.core.helpers.RecyclerViewClickListener;
import in.koreatech.koin.core.networks.entity.Lecture;

import static in.koreatech.koin.core.util.timetable.SeparateTime.getSpertateTimeToString;

public class TimetableRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private Context context;
    private ArrayList<Lecture> lectureArrayList;
    private RecyclerViewClickListener recyclerViewClickListener;


    public void setRecyclerViewClickListener(RecyclerViewClickListener recyclerViewClickListener) {
        this.recyclerViewClickListener = recyclerViewClickListener;
    }

    public TimetableRecyclerAdapter(Context context, ArrayList<Lecture> lectureArrayList) {
        this.context = context;
        this.lectureArrayList = lectureArrayList;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timetable_recyclerview_item, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timetable_recyclerview_item_load, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ItemViewHolder) {
            populateItemRows((ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }

    }

    @Override
    public int getItemCount() {
        return this.lectureArrayList == null ? 0 : this.lectureArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return this.lectureArrayList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout mTmetableRecyclerviewItemRelativelayout;
        TextView mLectureTitle;
        TextView mLectureInformationOne;
        TextView mLectureInfromationTwo;
        ImageButton mAddLectureButton;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mTmetableRecyclerviewItemRelativelayout = itemView.findViewById(R.id.timetable_recyclerview_item_relativelayout);
            mLectureTitle = itemView.findViewById(R.id.lecture_title);
            mLectureInformationOne = itemView.findViewById(R.id.lecture_information1);
            mLectureInfromationTwo = itemView.findViewById(R.id.lecture_infromation2);
            mAddLectureButton = itemView.findViewById(R.id.add_lecture_button);
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {

    }

    private void populateItemRows(ItemViewHolder holder, int position) {

        holder.setIsRecyclable(false);
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilderTwo = new StringBuilder();
        Lecture lecture = this.lectureArrayList.get(position);
        if (lecture.isItemClicked)
            holder.mTmetableRecyclerviewItemRelativelayout.setBackgroundColor(this.context.getResources().getColor(R.color.white6));
        else
            holder.mTmetableRecyclerviewItemRelativelayout.setBackgroundColor(this.context.getResources().getColor(R.color.white));

        if (lecture.isAddButtonClicked)
            holder.mAddLectureButton.setBackground(this.context.getDrawable(R.drawable.ic_delete_lecture_item_button));
        else
            holder.mAddLectureButton.setBackground(this.context.getDrawable(R.drawable.ic_add_lecture_item_button));
        holder.mLectureTitle.setText(lecture.name);

        stringBuilder.append(getSpertateTimeToString(lecture.classTime)).append("/ 정원 ").append(lecture.regularNumber);
        holder.mLectureInformationOne.setText(stringBuilder.toString());

        stringBuilderTwo.append(lecture.department).append("/").append(lecture.grades).append("학점").append("/").append(lecture.code).append("/").append(lecture.professor);
        if (lecture.isElearning.equals("Y"))
            stringBuilderTwo.append("/").append("e러닝");
        if (lecture.isEnglish.equals("Y"))
            stringBuilderTwo.append("/").append("영강");

        holder.mLectureInfromationTwo.setText(stringBuilderTwo.toString());

        if (recyclerViewClickListener != null) {
            holder.mAddLectureButton.setOnClickListener(v -> recyclerViewClickListener.onClick(v, position));
            holder.mTmetableRecyclerviewItemRelativelayout.setOnClickListener(v -> recyclerViewClickListener.onClick(v, position));
        }
    }


}

