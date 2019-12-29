package in.koreatech.koin.service_timetable.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.R;
import in.koreatech.koin.core.util.timetable.DepartmentCode;
import in.koreatech.koin.service_timetable.Contracts.MajorDialogListener;

import static in.koreatech.koin.service_timetable.ui.TimetableAnonymousActivity.select;

public class TimetableSelectAnonymousMajorDialog extends Dialog implements View.OnClickListener {
    public static final String TAG = TimetableSelectAnonymousMajorDialog.class.getName();
    private ToggleButton[] mToggleButton = new ToggleButton[10];
    private MajorDialogListener mMajorDialogListener;
    @BindView(R.id.textSpace)
    TextView mTextSpace;
    private static final int[] buttonId = {R.id.select_major_computer_engineering, R.id.select_major_electonic_engineering, R.id.select_major_industrial_management, R.id.select_major_design_engineering,
            R.id.select_major_hrd, R.id.select_major_mechanical_engineering, R.id.select_major_chemical_engineering, R.id.select_major_mechatronics_engineering, R.id.select_major_cultural_department, R.id.select_major_mix};
    private OnCLickedDialogItemListener mOnCLickedDialogItemListener;
    private DepartmentCode selectedDepartmentCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        selectedDepartmentCode = DepartmentCode.DEPARTMENT_CODE_0;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams mWindowManager = new WindowManager.LayoutParams();
        mWindowManager.copyFrom(this.getWindow().getAttributes());
        mWindowManager.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        mWindowManager.dimAmount = 0.8f;
        setCancelable(false);
        setContentView(R.layout.timetable_select_major_dialog);
        init();
        rememberRecord();
        ButterKnife.bind(this);
    }

    public void setOnCLickedDialogItemListener(OnCLickedDialogItemListener onCLickedDialogItemListener) {
        mOnCLickedDialogItemListener = onCLickedDialogItemListener;
    }

    public void setMajorDialogListener(MajorDialogListener mMajorDialogListener) {
        this.mMajorDialogListener = mMajorDialogListener;
    }

    public TimetableSelectAnonymousMajorDialog(@NonNull Context context) {
        super(context);
    }


    @OnClick(R.id.select_major_button)
    public void onClickedCompleteButton() {
        int count = 0;
        for (int i = 0; i < mToggleButton.length; i++) {
            if (mToggleButton[i].isChecked())
                count++;
        }
        if (count == 0) {
            selectedDepartmentCode = DepartmentCode.DEPARTMENT_CODE_0;
            select = -1;
        }

        if (mOnCLickedDialogItemListener != null) {
            mOnCLickedDialogItemListener.onClickedItemList(selectedDepartmentCode);
            Log.d(TAG, "selected: " + selectedDepartmentCode.getDepartMentString());
        }
        Log.e("count", Integer.toString(count));
        dismiss();
    }

    void rememberRecord() {
        if (select == -1) {
            OffToggleButton(-1);
        }
        if (select == 0) {
            mToggleButton[0].performClick();
        }
        if (select == 1) {
            mToggleButton[1].performClick();
        }
        if (select == 2) {
            mToggleButton[2].performClick();
        }
        if (select == 3) {
            mToggleButton[3].performClick();
        }
        if (select == 4) {
            mToggleButton[4].performClick();
        }
        if (select == 5) {
            mToggleButton[5].performClick();
        }
        if (select == 6) {
            mToggleButton[6].performClick();
        }
        if (select == 7) {
            mToggleButton[7].performClick();
        }
        if (select == 8) {
            mToggleButton[8].performClick();
        }
        if (select == 9) {
            mToggleButton[9].performClick();
        }
    }

    void OffToggleButton(int index) {
        Log.e("error", "check");
        if (index == -1) {
            for (int i = 0; i < 10; i++) {
                mToggleButton[i].setChecked(false);
                mToggleButton[i].setBackgroundResource(R.drawable.button_rect_white_radius_13dp);
                mToggleButton[i].setTextColor(ContextCompat.getColor(getContext(), R.color.warm_grey_two));
            }
        } else {
            for (int i = 0; i < 10; i++) {
                mToggleButton[i].setChecked(false);
                mToggleButton[i].setBackgroundResource(R.drawable.button_rect_white_radius_13dp);
                mToggleButton[i].setTextColor(ContextCompat.getColor(getContext(), R.color.warm_grey_two));
            }
            mToggleButton[index].setChecked(true);
        }
    }

    public void onClickedItem(DepartmentCode departmentCode) {
        if (this.selectedDepartmentCode == departmentCode)
            selectedDepartmentCode = DepartmentCode.DEPARTMENT_CODE_0;
        else
            selectedDepartmentCode = departmentCode;
    }

    interface OnCLickedDialogItemListener {
        void onClickedItemList(DepartmentCode departmentCode);

    }

    void init() {
        for (int i = 0; i < 10; i++) {
            mToggleButton[i] = (ToggleButton) findViewById(buttonId[i]);
            mToggleButton[i].setOnClickListener(this);

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select_major_computer_engineering:
                if (mToggleButton[0].isChecked()) {
                    Log.e("2", Boolean.toString(mToggleButton[0].isChecked()));
                    OffToggleButton(0);
                    onClickedItem(DepartmentCode.DEPARTMENT_CODE_1);
                    mMajorDialogListener.sendActivity("computer", 0);
                    mToggleButton[0].setBackgroundResource(R.drawable.button_rect_squash_radius);
                    mToggleButton[0].setTextColor(ContextCompat.getColor(getContext(), R.color.white));

                } else {
                    Log.e("3", Boolean.toString(mToggleButton[0].isChecked()));
                    mToggleButton[0].setBackgroundResource(R.drawable.button_rect_white_radius_13dp);
                    mToggleButton[0].setTextColor(ContextCompat.getColor(getContext(), R.color.warm_grey_two));
                }
                break;
            case R.id.select_major_electonic_engineering:
                if (mToggleButton[1].isChecked()) {
                    OffToggleButton(1);
                    onClickedItem(DepartmentCode.DEPARTMENT_CODE_3);
                    mMajorDialogListener.sendActivity("elecronic", 1);
                    mToggleButton[1].setBackgroundResource(R.drawable.button_rect_squash_radius);
                    mToggleButton[1].setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                } else {
                    mToggleButton[1].setBackgroundResource(R.drawable.button_rect_white_radius_13dp);
                    mToggleButton[1].setTextColor(ContextCompat.getColor(getContext(), R.color.warm_grey_two));
                }
                break;
            case R.id.select_major_industrial_management:
                if (mToggleButton[2].isChecked()) {
                    OffToggleButton(2);
                    onClickedItem(DepartmentCode.DEPARTMENT_CODE_5);
                    mMajorDialogListener.sendActivity("industrialManagement", 2);
                    mToggleButton[2].setBackgroundResource(R.drawable.button_rect_squash_radius);
                    mToggleButton[2].setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                } else {
                    mToggleButton[2].setBackgroundResource(R.drawable.button_rect_white_radius_13dp);
                    mToggleButton[2].setTextColor(ContextCompat.getColor(getContext(), R.color.warm_grey_two));
                }
                break;
            case R.id.select_major_design_engineering:
                if (mToggleButton[3].isChecked()) {
                    OffToggleButton(3);
                    onClickedItem(DepartmentCode.DEPARTMENT_CODE_7);
                    mMajorDialogListener.sendActivity("design", 3);
                    mToggleButton[3].setBackgroundResource(R.drawable.button_rect_squash_radius);
                    mToggleButton[3].setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                } else {
                    mToggleButton[3].setBackgroundResource(R.drawable.button_rect_white_radius_13dp);
                    mToggleButton[3].setTextColor(ContextCompat.getColor(getContext(), R.color.warm_grey_two));
                }
                break;
            case R.id.select_major_hrd:
                if (mToggleButton[4].isChecked()) {
                    OffToggleButton(4);
                    onClickedItem(DepartmentCode.DEPARTMENT_CODE_9);
                    mMajorDialogListener.sendActivity("hrd", 4);
                    mToggleButton[4].setBackgroundResource(R.drawable.button_rect_squash_radius);
                    mToggleButton[4].setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                } else {
                    mToggleButton[4].setBackgroundResource(R.drawable.button_rect_white_radius_13dp);
                    mToggleButton[4].setTextColor(ContextCompat.getColor(getContext(), R.color.warm_grey_two));
                }
                break;
            case R.id.select_major_mechanical_engineering:
                if (mToggleButton[5].isChecked()) {
                    OffToggleButton(5);
                    onClickedItem(DepartmentCode.DEPARTMENT_CODE_2);
                    mMajorDialogListener.sendActivity("mecchanical", 5);
                    mToggleButton[5].setBackgroundResource(R.drawable.button_rect_squash_radius);
                    mToggleButton[5].setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                } else {
                    mToggleButton[5].setBackgroundResource(R.drawable.button_rect_white_radius_13dp);
                    mToggleButton[5].setTextColor(ContextCompat.getColor(getContext(), R.color.warm_grey_two));
                }
                break;
            case R.id.select_major_chemical_engineering:
                if (mToggleButton[6].isChecked()) {
                    OffToggleButton(6);
                    onClickedItem(DepartmentCode.DEPARTMENT_CODE_4);
                    mMajorDialogListener.sendActivity("chemical", 6);
                    mToggleButton[6].setBackgroundResource(R.drawable.button_rect_squash_radius);
                    mToggleButton[6].setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                } else {
                    mToggleButton[6].setBackgroundResource(R.drawable.button_rect_white_radius_13dp);
                    mToggleButton[6].setTextColor(ContextCompat.getColor(getContext(), R.color.warm_grey_two));
                }
                break;
            case R.id.select_major_mechatronics_engineering:
                if (mToggleButton[7].isChecked()) {
                    OffToggleButton(7);
                    onClickedItem(DepartmentCode.DEPARTMENT_CODE_6);
                    mMajorDialogListener.sendActivity("mechatronics", 7);
                    mToggleButton[7].setBackgroundResource(R.drawable.button_rect_squash_radius);
                    mToggleButton[7].setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                } else {
                    mToggleButton[7].setBackgroundResource(R.drawable.button_rect_white_radius_13dp);
                    mToggleButton[7].setTextColor(ContextCompat.getColor(getContext(), R.color.warm_grey_two));
                }
                break;
            case R.id.select_major_cultural_department:
                if (mToggleButton[8].isChecked()) {
                    OffToggleButton(8);
                    onClickedItem(DepartmentCode.DEPARTMENT_CODE_8);
                    mMajorDialogListener.sendActivity("cultural", 8);
                    mToggleButton[8].setBackgroundResource(R.drawable.button_rect_squash_radius);
                    mToggleButton[8].setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                } else {
                    mToggleButton[8].setBackgroundResource(R.drawable.button_rect_white_radius_13dp);
                    mToggleButton[8].setTextColor(ContextCompat.getColor(getContext(), R.color.warm_grey_two));
                }
                break;
            case R.id.select_major_mix:
                if (mToggleButton[9].isChecked()) {
                    OffToggleButton(9);
                    onClickedItem(DepartmentCode.DEPARTMENT_CODE_10);
                    mMajorDialogListener.sendActivity("mix", 9);
                    mToggleButton[9].setBackgroundResource(R.drawable.button_rect_squash_radius);
                    mToggleButton[9].setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                } else {
                    mToggleButton[9].setBackgroundResource(R.drawable.button_rect_white_radius_13dp);
                    mToggleButton[9].setTextColor(ContextCompat.getColor(getContext(), R.color.warm_grey_two));
                }
                break;
        }
    }


}

