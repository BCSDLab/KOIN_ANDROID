package in.koreatech.koin.ui.timetable.adapter;

import android.animation.Animator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;

import in.koreatech.koin.R;
import in.koreatech.koin.core.recyclerview.RecyclerViewClickListener;
import in.koreatech.koin.data.network.entity.Lecture;
import in.koreatech.koin.util.SeparateTime;


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

        RelativeLayout timetableRecyclerviewItemRelativelayout;
        TextView lectureTitle;
        TextView lectureInformationOne;
        TextView lectureInfromationTwo;
        LottieAnimationView addLectureButton;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            timetableRecyclerviewItemRelativelayout = itemView.findViewById(R.id.timetable_recyclerview_item_relativelayout);
            lectureTitle = itemView.findViewById(R.id.lecture_title);
            lectureInformationOne = itemView.findViewById(R.id.lecture_information1);
            lectureInfromationTwo = itemView.findViewById(R.id.lecture_infromation2);
            addLectureButton = itemView.findViewById(R.id.add_lecture_button);
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
            holder.timetableRecyclerviewItemRelativelayout.setBackgroundColor(this.context.getResources().getColor(R.color.white4));
        else
            holder.timetableRecyclerviewItemRelativelayout.setBackgroundColor(this.context.getResources().getColor(R.color.white));

        if (lecture.isAddButtonClicked)
            holder.addLectureButton.setAnimation("delete.json");
        else
            holder.addLectureButton.setAnimation("add.json");

        holder.lectureTitle.setText(lecture.getName());

        stringBuilder.append(SeparateTime.getSpertateTimeToString(lecture.getClassTime())).append("/ 정원 ").append(lecture.getRegularNumber());
        holder.lectureInformationOne.setText(stringBuilder.toString());

        stringBuilderTwo.append(lecture.getDepartment()).append("/").append(lecture.getGrades()).append("학점").append("/").append(lecture.getCode()).append("/").append(lecture.getProfessor());
        if (lecture.getIsElearning().equals("Y"))
            stringBuilderTwo.append("/").append("e러닝");
        if (lecture.getIsEnglish().equals("Y"))
            stringBuilderTwo.append("/").append("영강");

        holder.lectureInfromationTwo.setText(stringBuilderTwo.toString());

        if (recyclerViewClickListener != null) {
            holder.addLectureButton.setOnClickListener(v -> {
                if (lecture.isAddButtonClicked) {
                    holder.addLectureButton.playAnimation();
                } else {
                    holder.addLectureButton.playAnimation();
                }
            });
            holder.timetableRecyclerviewItemRelativelayout.setOnClickListener(v -> recyclerViewClickListener.onClick(v, position));
        }
        holder.addLectureButton.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                recyclerViewClickListener.onClick(holder.addLectureButton, position);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


}

