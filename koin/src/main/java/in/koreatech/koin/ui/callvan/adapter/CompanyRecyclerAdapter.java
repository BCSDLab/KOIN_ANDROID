package in.koreatech.koin.ui.callvan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.koreatech.koin.R;
import in.koreatech.koin.data.network.entity.Company;

public class CompanyRecyclerAdapter extends RecyclerView.Adapter<CompanyRecyclerAdapter.ViewHolder> {
    private final String TAG = "CompanyRecyclerAdapter";

    private LayoutInflater layoutInflater; //inflate 사용위한 inflater
    private ArrayList<Company> companyArrayList; //콜밴 업체 정보를 저장할 List
    private Context context;

    public CompanyRecyclerAdapter(Context context, ArrayList<Company> companyArrayList) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.companyArrayList = companyArrayList;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.company_name_textview)
        TextView textViewCallvanName;  //콜밴 업체의 이름을 표시할 TextView
        @BindView(R.id.company_number_textview)
        TextView textViewCallvanNumber;  //콜밴 업체의 번호를 표시할 TextView

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.callvan_company_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        Company company = companyArrayList.get(position);
        holder.textViewCallvanName.setText(company.name); //콜밴 이름
        holder.textViewCallvanNumber.setText(company.phone); //콜밴 번호

    }

    @Override
    public int getItemCount() {
        return companyArrayList.size();
    }

}