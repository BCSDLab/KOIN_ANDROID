package in.koreatech.koin.ui.store.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.koreatech.koin.R;
import in.koreatech.koin.data.network.entity.Store;

/**
 * Created by hyerim on 2018. 8. 12....
 */
public class StoreRecyclerAdapter extends RecyclerView.Adapter<StoreRecyclerAdapter.ViewHolder> {
    private final String TAG = StoreRecyclerAdapter.class.getSimpleName();

    private Context context;
    private LayoutInflater layoutInflater; //inflate 사용위한 inflater
    private ArrayList<Store> storeArrayList; //학교 앞 상점 List

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.store_name_textview)
        TextView mTextViewStoreName;  //업체의 이름을 표시할 TextView
        @BindView(R.id.store_delivery_textview)
        TextView mTextViewDelivery; //배달 여부
        @BindView(R.id.store_card_textview)
        TextView mTextViewCard; //카드 결제 여부
        @BindView(R.id.store_account_textview)
        TextView mTextViewAccountTransfer;  //계좌이체 결제 여부

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public StoreRecyclerAdapter(Context context, ArrayList<Store> storeArrayList) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.storeArrayList = new ArrayList<>();
        this.storeArrayList.addAll(storeArrayList);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        Store store = this.storeArrayList.get(position);
        holder.mTextViewStoreName.setText(store.name); //콜밴 이름

        //배달, 카드 결제, 계좌이체 사용 가능 유무를 체크하여 다른 이미지를 띄움
        //TODO:text -> image changed
        if (store.isDeliveryOk) { //카드결제 가능한 경우
            holder.mTextViewDelivery.setTextColor(ContextCompat.getColor(context, R.color.squash));
        } else {
            holder.mTextViewDelivery.setTextColor(ContextCompat.getColor(context, R.color.cloudy_blue));
        }

        if (store.isCardOk) { //카드결제 가능한 경우
            holder.mTextViewCard.setTextColor(ContextCompat.getColor(context, R.color.squash));
        } else {
            holder.mTextViewCard.setTextColor(ContextCompat.getColor(context, R.color.cloudy_blue));
        }

        if (store.isBankOk) {
            holder.mTextViewAccountTransfer.setTextColor(ContextCompat.getColor(context, R.color.squash));
        } else {
            holder.mTextViewAccountTransfer.setTextColor(ContextCompat.getColor(context, R.color.cloudy_blue));
        }
    }

    @Override
    public int getItemCount() {
        return this.storeArrayList.size();
    }

}
