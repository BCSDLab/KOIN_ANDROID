package in.koreatech.koin.ui.timetable;

/**
 * Created by hyerim on 2018. 8. 4....
 */

import android.content.Context;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import in.koreatech.koin.core.R;
import in.koreatech.koin.core.helper.RecyclerClickListener;
import in.koreatech.koin.core.helper.RecyclerViewClickListener;
import in.koreatech.koin.core.util.FormValidatorUtil;
import in.koreatech.koin.ui.timetable.adapter.BottomSheetItemRecyclerAdapter;

public class CustomBottomSheetDialog extends BottomSheetDialog {

    private final static String TAG = CustomBottomSheetDialog.class.getSimpleName();
    private Context mContext;
    private View mVIew;

    private static CustomBottomSheetDialog instance;
    private BottomSheetBehavior mBottomSheetBehavior;

    private BottomSheetItemRecyclerAdapter mBottomSheetItemRecyclerAdapter;
    private ArrayList<String> mArrayListItems;

    private BottomSheetListener mBottomSheetListener; // the callback

    private String mSelectedItem;

    private TextView mTextViewTitle;
    private RecyclerView mRecyclerViewItems;


    public static CustomBottomSheetDialog getInstance(@NonNull Context context) {
        return instance == null ? new CustomBottomSheetDialog(context) : instance;
    }

    public CustomBottomSheetDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
        mVIew = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        setContentView(mVIew);

        init();
    }

    private void init() {
        mTextViewTitle = findViewById(R.id.bottom_sheet_title);
        mRecyclerViewItems = findViewById(R.id.bottom_sheet_recyclerview);
        mBottomSheetBehavior = BottomSheetBehavior.from((View) mVIew.getParent());
//        BottomSheetBehavior.BottomSheetCallback bottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//
//            }
//
//            @Override
//            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//
//            }
//        };

        mRecyclerViewItems.setHasFixedSize(true);
        mRecyclerViewItems.setLayoutManager(new LinearLayoutManager(mContext));

        mArrayListItems = new ArrayList<>();
        mBottomSheetItemRecyclerAdapter = new BottomSheetItemRecyclerAdapter(mContext, mArrayListItems);
        mRecyclerViewItems.setAdapter(mBottomSheetItemRecyclerAdapter);
        mRecyclerViewItems.addOnItemTouchListener(recyclerItemtouchListener);
    }

    public void makeBottomSheetDialog(Context context, String title, ArrayList<String> arrayList, final TextView textview) {
        CustomBottomSheetDialog bottomSheetDialog = CustomBottomSheetDialog.getInstance(context);
        bottomSheetDialog.setTitle(title);
        bottomSheetDialog.setItems(arrayList);
        bottomSheetDialog.setBottomSheetListener(new CustomBottomSheetDialog.BottomSheetListener() {
            public void finish(String result) {
                if (!FormValidatorUtil.validateStringIsEmpty(result)) {
                    textview.setText(result);
                }
            }
        });
        bottomSheetDialog.show();
    }

    public void setTitle(String title) {
        mTextViewTitle.setText(title);
    }

    public void setItems(ArrayList<String> arrayList) {
        mArrayListItems.clear();
        mArrayListItems.addAll(arrayList);

        mBottomSheetItemRecyclerAdapter.notifyDataSetChanged();
    }

    private RecyclerClickListener recyclerItemtouchListener = new RecyclerClickListener(mContext, mRecyclerViewItems, new RecyclerViewClickListener() {
        @Override
        public void onClick(View view, final int position) {
            mSelectedItem = mArrayListItems.get(position);
            dismiss();
        }

        @Override
        public void onLongClick(View view, int position) {
        }
    });

    @Override
    public void dismiss() {
        mBottomSheetListener.finish(mSelectedItem);
        super.dismiss();
    }

    public void setBottomSheetListener(BottomSheetListener bottomSheetListener) {
        mBottomSheetListener = bottomSheetListener;
    }

    public interface BottomSheetListener {
        void finish(String result);
    }
}

