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

public class RoomRecyclerAdapter extends RecyclerView.Adapter<RoomRecyclerAdapter.ViewHolder> implements View.OnClickListener {
    private final String TAG = "RoomRecyclerAdapter";

    private OnJoinButtonClickListener onJoinButtonClickListener;
    private LayoutInflater layoutInflater; //inflate 사용위한 inflater
    private ArrayList<CallvanRoom> roomsArrayList; //CallvanRoom의 List

    public interface OnJoinButtonClickListener {
        void onClickJoinButton(CallvanRoom room);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rooms_starting_textView)
        TextView textViewStartingPlace; // 출발장소
        @BindView(R.id.rooms_ending_textView)
        TextView textViewEndingPlace; // 도착장소
        @BindView(R.id.rooms_people_count_textView)
        TextView textViewPeopleCount; // 현재인원/최대인원
        @BindView(R.id.rooms_date_textView)
        TextView textViewDate; // 날짜
        @BindView(R.id.rooms_time_textView)
        TextView textViewTime; // 시간
        @BindView(R.id.rooms_imagebutton_join)
        ImageButton imageButtonJoin; //참가 버튼

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public RoomRecyclerAdapter(Context context, ArrayList<CallvanRoom> roomsModelArrayList) {
        layoutInflater = LayoutInflater.from(context);
        this.roomsArrayList = roomsModelArrayList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.callvan_room_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CallvanRoom room = roomsArrayList.get(position);
        holder.textViewStartingPlace.setText(room.startingPlace); //출발장소
        holder.textViewEndingPlace.setText(room.endingPlace); //도착장소
        holder.textViewPeopleCount.setText(room.currentPeople + "명 / " + room.maximumPeople + "명"); // 현재인원/최대인원

        String[] startingDateTime = room.startingDate.split(" ");

//        holder.textViewDate.setText(startingDateTime[0]);//날짜
//        holder.textViewTime.setText(startingDateTime[1]);//시간

        holder.imageButtonJoin.setTag(position);//아이템 위치를 태그로 닮
        holder.imageButtonJoin.setOnClickListener(this);// 입장 버튼 이벤트 리스너
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rooms_imagebutton_join:
                onJoinButtonClickListener.onClickJoinButton(roomsArrayList.get((int) v.getTag()));

                break;
            default:
                break;
        }
    }

    public void setCustomOnClickListener(OnJoinButtonClickListener onJoinButtonClickListener) {
        this.onJoinButtonClickListener = onJoinButtonClickListener;
    }

    @Override
    public int getItemCount() {
        return roomsArrayList.size();
    }

}
