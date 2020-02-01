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
import in.koreatech.koin.service_timetable.contracts.MajorDialogListener;

import static in.koreatech.koin.service_timetable.ui.TimetableAnonymousActivity.select;

public class TimetableSelectAnonymousMajorDialog extends Dialog implements View.OnClickListener {
    public static final String TAG = "TimetableSelectMajor";
    private ToggleButton[] toggleButton = new ToggleButton[10];
    private MajorDialogListener majorDialogListener;
    @BindView(R.id.textSpace)
    TextView textSpace;
    private static final int[] buttonId = {R.id.select_major_computer_engineering, R.id.select_major_electonic_engineering, R.id.select_major_industrial_management, R.id.select_major_design_engineering,
            R.id.select_major_hrd, R.id.select_major_mechanical_engineering, R.id.select_major_chemical_engineering, R.id.select_major_mechatronics_engineering, R.id.select_major_cultural_department, R.id.select_major_mix};
    private OnCLickedDialogItemListener onCLickedDialogItemListener;
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
        this.onCLickedDialogItemListener = onCLickedDialogItemListener;
    }

    public void setMajorDialogListener(MajorDialogListener majorDialogListener) {
        this.majorDialogListener = majorDialogListener;
    }

    public TimetableSelectAnonymousMajorDialog(@NonNull Context context) {
        super(context);
    }


    @OnClick(R.id.select_major_button)
    public void onClickedCompleteButton() {
        int count = 0;
        for (int i = 0; i < this.toggleButton.length; i++) {
            if (this.toggleButton[i].isChecked())
                count++;
        }
        if (count == 0) {
            selectedDepartmentCode = DepartmentCode.DEPARTMENT_CODE_0;
            select = -1;
        }

        if (this.onCLickedDialogItemListener != null) {
            this.onCLickedDialogItemListener.onClickedItemList(selectedDepartmentCode);
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
            this.toggleButton[0].performClick();
        }
        if (select == 1) {
            this.toggleButton[1].performClick();
        }
        if (select == 2) {
            this.toggleButton[2].performClick();
        }
        if (select == 3) {
            this.toggleButton[3].performClick();
        }
        if (select == 4) {
            this.toggleButton[4].performClick();
        }
        if (select == 5) {
            this.toggleButton[5].performClick();
        }
        if (select == 6) {
            this.toggleButton[6].performClick();
        }
        if (select == 7) {
            this.toggleButton[7].performClick();
        }
        if (select == 8) {
            this.toggleButton[8].performClick();
        }
        if (select == 9) {
            this.toggleButton[9].performClick();
        }
    }

    void OffToggleButton(int index) {
        Log.e("error", "check");
        if (index == -1) {
            for (int i = 0; i < 10; i++) {
                this.toggleButton[i].setChecked(false);
                this.toggleButton[i].setBackgroundResource(R.drawable.button_rect_white_radius_13dp);
                this.toggleButton[i].setTextColor(ContextCompat.getColor(getContext(), R.color.warm_grey_two));
            }
        } else {
            for (int i = 0; i < 10; i++) {
                this.toggleButton[i].setChecked(false);
                this.toggleButton[i].setBackgroundResource(R.drawable.button_rect_white_radius_13dp);
                this.toggleButton[i].setTextColor(ContextCompat.getColor(getContext(), R.color.warm_grey_two));
            }
            this.toggleButton[index].setChecked(true);
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
            this.toggleButton[i] = (ToggleButton) findViewById(buttonId[i]);
            this.toggleButton[i].setOnClickListener(this);

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select_major_computer_engineering:
                if (this.toggleButton[0].isChecked()) {
                    Log.e("2", Boolean.toString(this.toggleButton[0].isChecked()));
                    OffToggleButton(0);
                    onClickedItem(DepartmentCode.DEPARTMENT_CODE_1);
                    this.majorDialogListener.sendActivity("computer", 0);
                    this.toggleButton[0].setBackgroundResource(R.drawable.button_rect_squash_radius);
                    this.toggleButton[0].setTextColor(ContextCompat.getColor(getContext(), R.color.white));

                } else {
                    Log.e("3", Boolean.toString(this.toggleButton[0].isChecked()));
                    this.toggleButton[0].setBackgroundResource(R.drawable.button_rect_white_radius_13dp);
                    this.toggleButton[0].setTextColor(ContextCompat.getColor(getContext(), R.color.warm_grey_two));
                }
                break;
            case R.id.select_major_electonic_engineering:
                if (this.toggleButton[1].isChecked()) {
                    OffToggleButton(1);
                    onClickedItem(DepartmentCode.DEPARTMENT_CODE_3);
                    this.majorDialogListener.sendActivity("elecronic", 1);
                    this.toggleButton[1].setBackgroundResource(R.drawable.button_rect_squash_radius);
                    this.toggleButton[1].setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                } else {
                    this.toggleButton[1].setBackgroundResource(R.drawable.button_rect_white_radius_13dp);
                    this.toggleButton[1].setTextColor(ContextCompat.getColor(getContext(), R.color.warm_grey_two));
                }
                break;
            case R.id.select_major_industrial_management:
                if (this.toggleButton[2].isChecked()) {
                    OffToggleButton(2);
                    onClickedItem(DepartmentCode.DEPARTMENT_CODE_5);
                    this.majorDialogListener.sendActivity("industrialManagement", 2);
                    this.toggleButton[2].setBackgroundResource(R.drawable.button_rect_squash_radius);
                    this.toggleButton[2].setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                } else {
                    this.toggleButton[2].setBackgroundResource(R.drawable.button_rect_white_radius_13dp);
                    this.toggleButton[2].setTextColor(ContextCompat.getColor(getContext(), R.color.warm_grey_two));
                }
                break;
            case R.id.select_major_design_engineering:
                if (this.toggleButton[3].isChecked()) {
                    OffToggleButton(3);
                    onClickedItem(DepartmentCode.DEPARTMENT_CODE_7);
                    this.majorDialogListener.sendActivity("design", 3);
                    this.toggleButton[3].setBackgroundResource(R.drawable.button_rect_squash_radius);
                    this.toggleButton[3].setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                } else {
                    this.toggleButton[3].setBackgroundResource(R.drawable.button_rect_white_radius_13dp);
                    this.toggleButton[3].setTextColor(ContextCompat.getColor(getContext(), R.color.warm_grey_two));
                }
                break;
            case R.id.select_major_hrd:
                if (this.toggleButton[4].isChecked()) {
                    OffToggleButton(4);
                    onClickedItem(DepartmentCode.DEPARTMENT_CODE_9);
                    this.majorDialogListener.sendActivity("hrd", 4);
                    this.toggleButton[4].setBackgroundResource(R.drawable.button_rect_squash_radius);
                    this.toggleButton[4].setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                } else {
                    this.toggleButton[4].setBackgroundResource(R.drawable.button_rect_white_radius_13dp);
                    this.toggleButton[4].setTextColor(ContextCompat.getColor(getContext(), R.color.warm_grey_two));
                }
                break;
            case R.id.select_major_mechanical_engineering:
                if (this.toggleButton[5].isChecked()) {
                    OffToggleButton(5);
                    onClickedItem(DepartmentCode.DEPARTMENT_CODE_2);
                    this.majorDialogListener.sendActivity("mecchanical", 5);
                    this.toggleButton[5].setBackgroundResource(R.drawable.button_rect_squash_radius);
                    this.toggleButton[5].setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                } else {
                    this.toggleButton[5].setBackgroundResource(R.drawable.button_rect_white_radius_13dp);
                    this.toggleButton[5].setTextColor(ContextCompat.getColor(getContext(), R.color.warm_grey_two));
                }
                break;
            case R.id.select_major_chemical_engineering:
                if (this.toggleButton[6].isChecked()) {
                    OffToggleButton(6);
                    onClickedItem(DepartmentCode.DEPARTMENT_CODE_4);
                    this.majorDialogListener.sendActivity("chemical", 6);
                    this.toggleButton[6].setBackgroundResource(R.drawable.button_rect_squash_radius);
                    this.toggleButton[6].setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                } else {
                    this.toggleButton[6].setBackgroundResource(R.drawable.button_rect_white_radius_13dp);
                    this.toggleButton[6].setTextColor(ContextCompat.getColor(getContext(), R.color.warm_grey_two));
                }
                break;
            case R.id.select_major_mechatronics_engineering:
                if (this.toggleButton[7].isChecked()) {
                    OffToggleButton(7);
                    onClickedItem(DepartmentCode.DEPARTMENT_CODE_6);
                    this.majorDialogListener.sendActivity("mechatronics", 7);
                    this.toggleButton[7].setBackgroundResource(R.drawable.button_rect_squash_radius);
                    this.toggleButton[7].setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                } else {
                    this.toggleButton[7].setBackgroundResource(R.drawable.button_rect_white_radius_13dp);
                    this.toggleButton[7].setTextColor(ContextCompat.getColor(getContext(), R.color.warm_grey_two));
                }
                break;
            case R.id.select_major_cultural_department:
                if (this.toggleButton[8].isChecked()) {
                    OffToggleButton(8);
                    onClickedItem(DepartmentCode.DEPARTMENT_CODE_8);
                    this.majorDialogListener.sendActivity("cultural", 8);
                    this.toggleButton[8].setBackgroundResource(R.drawable.button_rect_squash_radius);
                    this.toggleButton[8].setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                } else {
                    this.toggleButton[8].setBackgroundResource(R.drawable.button_rect_white_radius_13dp);
                    this.toggleButton[8].setTextColor(ContextCompat.getColor(getContext(), R.color.warm_grey_two));
                }
                break;
            case R.id.select_major_mix:
                if (this.toggleButton[9].isChecked()) {
                    OffToggleButton(9);
                    onClickedItem(DepartmentCode.DEPARTMENT_CODE_10);
                    this.majorDialogListener.sendActivity("mix", 9);
                    this.toggleButton[9].setBackgroundResource(R.drawable.button_rect_squash_radius);
                    this.toggleButton[9].setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                } else {
                    this.toggleButton[9].setBackgroundResource(R.drawable.button_rect_white_radius_13dp);
                    this.toggleButton[9].setTextColor(ContextCompat.getColor(getContext(), R.color.warm_grey_two));
                }
                break;
        }
    }


}

