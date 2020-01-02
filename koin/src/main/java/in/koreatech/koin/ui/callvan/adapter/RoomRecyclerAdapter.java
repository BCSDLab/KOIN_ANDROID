package in.koreatech.koin.ui.callvan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.koreatech.koin.R;
import in.koreatech.koin.data.network.entity.CallvanRoom;

/**
 * Created by hyerim on 2018. 6. 18....
 */
public class RoomRecyclerAdapter extends RecyclerView.Adapter<RoomRecyclerAdapter.ViewHolder> implements View.OnClickListener {
    private final String TAG = "RoomRecyclerAdapter";

    private OnJoinButtonClickListener mOnJoinButtonClickListener;
    private LayoutInflater mLayoutInflater; //inflate 사용위한 inflater
    private ArrayList<CallvanRoom> mRoomsArrayList; //CallvanRoom의 List

    public interface OnJoinButtonClickListener {
        void onClickJoinButton(CallvanRoom room);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rooms_starting_textView)
        TextView mTextViewStartingPlace; // 출발장소
        @BindView(R.id.rooms_ending_textView)
        TextView mTextViewEndingPlace; // 도착장소
        @BindView(R.id.rooms_people_count_textView)
        TextView mTextViewPeopleCount; // 현재인원/최대인원
        @BindView(R.id.rooms_date_textView)
        TextView mTextViewDate; // 날짜
        @BindView(R.id.rooms_time_textView)
        TextView mTextViewTime; // 시간
        @BindView(R.id.rooms_imagebutton_join)
        ImageButton mImageButtonJoin; //참가 버튼

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public RoomRecyclerAdapter(Context context, ArrayList<CallvanRoom> roomsModelArrayList) {
        mLayoutInflater = LayoutInflater.from(context);
        this.mRoomsArrayList = roomsModelArrayList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.callvan_room_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CallvanRoom room = mRoomsArrayList.get(position);
        holder.mTextViewStartingPlace.setText(room.startingPlace); //출발장소
        holder.mTextViewEndingPlace.setText(room.endingPlace); //도착장소
        holder.mTextViewPeopleCount.setText(room.currentPeople + "명 / " + room.maximumPeople + "명"); // 현재인원/최대인원

        String[] startingDateTime = room.startingDate.split(" ");

//        holder.mTextViewDate.setText(startingDateTime[0]);//날짜
//        holder.mTextViewTime.setText(startingDateTime[1]);//시간

        holder.mImageButtonJoin.setTag(position);//아이템 위치를 태그로 닮
        holder.mImageButtonJoin.setOnClickListener(this);// 입장 버튼 이벤트 리스너
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rooms_imagebutton_join:
                mOnJoinButtonClickListener.onClickJoinButton(mRoomsArrayList.get((int) v.getTag()));

                break;
            default:
                break;
        }
    }

    public void setCustomOnClickListener(OnJoinButtonClickListener onJoinButtonClickListener) {
        this.mOnJoinButtonClickListener = onJoinButtonClickListener;
    }

    @Override
    public int getItemCount() {
        return mRoomsArrayList.size();
    }

}
