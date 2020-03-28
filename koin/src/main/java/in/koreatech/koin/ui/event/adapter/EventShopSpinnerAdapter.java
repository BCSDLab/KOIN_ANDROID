package in.koreatech.koin.ui.event.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import in.koreatech.koin.R;

public class EventShopSpinnerAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> myShopList;
    private LayoutInflater inflater;

    public EventShopSpinnerAdapter(Context context, ArrayList<String> myShopList) {
        this.context = context;
        this.myShopList = myShopList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if(myShopList != null)
            return myShopList.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return myShopList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if(view == null) {
            view = inflater.inflate(R.layout.event_create_spinner_custom, viewGroup, false);
            Log.d("ShopSpinner", String.valueOf(view));
        }

        if(myShopList != null) {
            String shopName = myShopList.get(position);
            ((TextView)view.findViewById(R.id.event_create_spinner_textview)).setText(shopName);
        }
        return view;
    }

    @Override
    public View getDropDownView(int position, View view, ViewGroup viewGroup) {
        if(view == null) {
            view = inflater.inflate(R.layout.event_create_spinner_item, viewGroup, false);
        }

        if(myShopList != null) {
            if(position != -1) {
                view.findViewById(R.id.event_create_spinner_item_store_imageview).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.event_create_spinner_item_arrow_imageview).setVisibility(View.INVISIBLE);
            }
            String shopName = myShopList.get(position);
            ((TextView)view.findViewById(R.id.event_create_spinner_item_textview)).setText(shopName);
        }

        return view;
    }
}
