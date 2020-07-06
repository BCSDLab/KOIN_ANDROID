package in.koreatech.koin.ui.land.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.koreatech.koin.R;
import in.koreatech.koin.core.recyclerview.RecyclerViewClickListener;
import in.koreatech.koin.data.network.entity.BokdukRoom;
import in.koreatech.koin.util.NavigationManger;

/**
 * 복덕방 목록을 보여주는 RecyclerView Adapter
 */
public class LandRecyclerAdapter extends RecyclerView.Adapter<LandRecyclerAdapter.ViewHolder> {

    private ArrayList<BokdukRoom> landArrayList; //학교 앞 원룸 List
    private Context context;
    private RecyclerViewClickListener recyclerViewClickListener;

    /**
     * 복덕방 목록을 보여주는 RecyclerView Adapter 생성자
     *
     * @param landArrayList api로 받아온 복덕방배열
     * @param context       현재 화면상태
     */
    public LandRecyclerAdapter(ArrayList landArrayList, Context context) {
        this.landArrayList = new ArrayList<>();
        this.landArrayList.addAll(landArrayList);
        this.context = context;
        Log.e("landArrayList size : ", Integer.toString(landArrayList.size()));

    }

    public void setRecyclerViewClickListener(RecyclerViewClickListener recyclerViewClickListener) {
        this.recyclerViewClickListener = recyclerViewClickListener;
    }

    /**
     * ViewHolder와 Layout 파일을 연결해주는 함수
     *
     * @param parent
     * @param viewType
     * @return
     */

    @NonNull
    @Override
    public LandRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.land_recyclerview_item, parent, false);
        return new LandRecyclerAdapter.ViewHolder(itemView);
    }

    /**
     * ViewHolder에 들어갈 데이터를 연결해주는 함수
     *
     * @param holder   뷰홀더
     * @param position 아이템을 선택했을때 위치
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        holder.onBind(landArrayList.get(position));
        BokdukRoom land = landArrayList.get(position);
        holder.view.setOnClickListener(new View.OnClickListener() {
            /**
             * 아이템을 클릭했을때 복덕방 상세페이지로 이동하는 함수
             * @param v
             */
            @Override
            public void onClick(View v) {
                if (position != RecyclerView.NO_POSITION) {
                    if (recyclerViewClickListener != null)
                        recyclerViewClickListener.onClick(v, position);
                } else {
                    Toast.makeText(context, "복덕방을 찾을수 없습니다", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return landArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View view; //onBindViewHolder 함수에서 아이템의 클릭리스너를 달기위한 view
        @BindView(R.id.land_name_textview)
        TextView textViewLandName;  //원룸의 이름을 표시할 TextView
        @BindView(R.id.month_fee_textview)
        TextView textViewMonthFee; //월세가격
        @BindView(R.id.charter_fee_textview)
        TextView textViewCharterFee; //전세가격

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, itemView);
        }

        /**
         * 복덕방 아이템의 글자들을 설정하는 함수
         *
         * @param land 복덕방
         */
        void onBind(BokdukRoom land) {
            if (land.getName() == null)
                textViewLandName.setText("-");
            else
                textViewLandName.setText(land.getName());
            if (land.getName() == null)
                textViewMonthFee.setText("-");
            else
                textViewMonthFee.setText(land.getMonthlyFee());
            if (land.getCharterFee() == null)
                textViewCharterFee.setText("X");
            else
                textViewCharterFee.setText(land.getCharterFee());
        }

    }


}
