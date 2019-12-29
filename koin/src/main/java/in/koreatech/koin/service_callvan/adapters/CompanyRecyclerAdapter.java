package in.koreatech.koin.service_callvan.adapters;

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
import in.koreatech.koin.core.networks.entity.Company;

/**
 * Created by hyerim on 2018. 6. 17....
 */
public class CompanyRecyclerAdapter extends RecyclerView.Adapter<CompanyRecyclerAdapter.ViewHolder> {
    private final String TAG = CompanyRecyclerAdapter.class.getSimpleName();

    private LayoutInflater mLayoutInflater; //inflate 사용위한 inflater
    private ArrayList<Company> mCompanyArrayList; //콜밴 업체 정보를 저장할 List
    private Context mContext;

    public CompanyRecyclerAdapter(Context context, ArrayList<Company> companyArrayList) {
        mLayoutInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mCompanyArrayList = companyArrayList;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.company_name_textview)
        TextView mTextViewCallvanName;  //콜밴 업체의 이름을 표시할 TextView
        @BindView(R.id.company_number_textview)
        TextView mTextViewCallvanNumber;  //콜밴 업체의 번호를 표시할 TextView

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
        Company company = mCompanyArrayList.get(position);
        holder.mTextViewCallvanName.setText(company.name); //콜밴 이름
        holder.mTextViewCallvanNumber.setText(company.phone); //콜밴 번호

    }

    @Override
    public int getItemCount() {
        return mCompanyArrayList.size();
    }

}