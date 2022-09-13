package in.koreatech.koin.ui.timetable;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import in.koreatech.koin.R;
import in.koreatech.koin.constant.AuthorizeConstant;
import in.koreatech.koin.core.appbar.AppBarBase;
import in.koreatech.koin.core.recyclerview.RecyclerViewClickListener;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.data.network.entity.Lecture;
import in.koreatech.koin.data.network.entity.Semester;
import in.koreatech.koin.data.network.entity.TimeTable;
import in.koreatech.koin.data.sharedpreference.TimeTableSharedPreferencesHelper;
import in.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity;
import in.koreatech.koin.ui.navigation.state.MenuState;
import in.koreatech.koin.ui.timetable.adapter.TimetableRecyclerAdapter;
import in.koreatech.koin.ui.timetable.adapter.TimetableSemesterRecyclerAdapter;
import in.koreatech.koin.ui.timetable.presenter.MajorDialogListener;
import in.koreatech.koin.ui.timetable.presenter.TimetableAnonymousContract;
import in.koreatech.koin.ui.timetable.presenter.TimetableAnonymousPresenter;
import in.koreatech.koin.util.DepartmentCode;
import in.koreatech.koin.util.FileUtil;
import in.koreatech.koin.util.LogUtil;
import in.koreatech.koin.util.ScreenshotUtil;
import in.koreatech.koin.util.TimeDuplicateCheckUtil;

import static in.koreatech.koin.util.LectureFilterUtil.getClikckedUtil;
import static in.koreatech.koin.util.LectureFilterUtil.getFilterUtil;
import static in.koreatech.koin.util.SeparateTime.getSpertateTimeToString;


public class TimetableAnonymousActivity extends KoinNavigationDrawerActivity implements TimetableAnonymousContract.View, TimetableSelectAnonymousMajorDialog.OnCLickedDialogItemListener, RecyclerViewClickListener {
    public static final String TAG = "TimetableAnonymous";
    public static final int MY_REQUEST_CODE = 1;
    public static final int MAX_ITEM_LOAD = 40;
    public static final int LOAD_TIME_MS = 500;
    public static final float BOTTOM_NAVIGATION_HEIGHT_PX = Resources.getSystem().getDisplayMetrics().density * 0;

    public static int select = -1;          //TimetableSelectAnonymousMajorDialog에서 선택했던것을 기억하는 변수
    @BindView(R.id.timetable_timetableview)
    TimetableView timetableView;
    @BindView(R.id.timetable_add_schedule_bottom_sheet)
    LinearLayout bottomSheet;
    @BindView(R.id.timetable_container_relativelayout)
    RelativeLayout timeTableContainerRelativeLayout;
    @BindView(R.id.timetable_search_recyclerview)
    RecyclerView timetableSearchRecyclerview;
    @BindView(R.id.timetable_semester_recyclerview)
    RecyclerView timetableSemesterRecyclerview;
    @BindView(R.id.timetable_add_schedule_bottom_sheet_center_textview)
    TextView timetableAddTopTextView;
    @BindView(R.id.timetable_detail_schedule_bottom_sheet)
    LinearLayout detailBottomsheet;
    @BindView(R.id.timetable_select_semester_bottom_sheet)
    LinearLayout semesterSelectBottomsheet;
    @BindView(R.id.timetable_detail_schedule_bottom_sheet_center_textview)
    TextView timetableDetailTopTextView;
    @BindView(R.id.timetable_select_semester_bottom_sheet_center_textview)
    TextView timetableSemesterSelectTopTextView;
    @BindView(R.id.timetable_detail_schedule_bottom_sheet_class_title_textview)
    TextView timetableDetailClassTitleTextview;
    @BindView(R.id.timetable_detail_schedule_bottom_sheet_class_detail_textview)
    TextView timetableDetailinformationTextview;
    @BindView(R.id.timetable_detail_schedule_bottom_sheet_left_textview)
    TextView timetableDetailLeftTextview;
    @BindView(R.id.timetable_detail_schedule_bottom_sheet_right_textview)
    TextView timetableDetailRightTextview;
    @BindView(R.id.timetable_add_schedule_search_edittext)
    TextView timetableAddScheduleSearchEdittext;
    @BindView(R.id.timetable_scrollview)
    NestedScrollView timeTableScrollview;
    @BindView(R.id.table_header)
    TableLayout timeTableHeader;
    @BindView(R.id.timetable_select_semester_textview)
    TextView selectSemesterTextview;
    @BindView(R.id.timetable_add_floating_button)
    FloatingActionButton timetableAddFloatingButton;

    private Context context;
    private UserLockBottomSheetBehavior bottomSheetBehavior;
    private UserLockBottomSheetBehavior bottomSheetDetailBehavior;
    private UserLockBottomSheetBehavior semesterSheetBehavior;
    private boolean isBottomSheetOpen;
    private boolean isBottomDetailSheetOpen;
    private boolean isSemesterSelectSheetOpen;
    private TimetableAnonymousPresenter timetablePresenter;
    private int categoryNumber;

    private RecyclerView.LayoutManager layoutManager; // RecyclerView LayoutManager
    private ArrayList<TimeTable.TimeTableItem> mTimeTableArrayList;
    private ArrayList<TimeTable.TimeTableItem> mTimeTableEditArrayList;
    private ArrayList<Lecture> totalLectureArrayList;
    private ArrayList<Lecture> selectedLectureArrayList;
    private ArrayList<Lecture> selectedLectureSeperateArrayList;
    private ArrayList<Semester> semesters;
    private int selectedLectureSeperateClickIndex = -1;

    private RecyclerView.ViewHolder viewHolder;
    private TimetableRecyclerAdapter timetableRecyclerAdapter;
    private TimetableSemesterRecyclerAdapter timetableSemesterRecyclerAdapter;
    private TimetableSelectAnonymousMajorDialog timetableSelectAnonymousMajorDialog;
    private boolean isLoading;
    private DepartmentCode selectedDepartmentCode;
    private int blockId;

    private String semester = "";

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        if (imm != null)
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable_activity_main);
        this.context = this;
        ButterKnife.bind(this);
        init();
    }

    public void init() {
        isLoading = false;
        this.selectedDepartmentCode = DepartmentCode.DEPARTMENT_CODE_0;
        setPresenter(new TimetableAnonymousPresenter(this));
        this.categoryNumber = -1;
        select = -1;
        totalLectureArrayList = new ArrayList<>();
        selectedLectureArrayList = new ArrayList<>();
        mTimeTableArrayList = new ArrayList<>();
        semesters = new ArrayList<>();
        selectedLectureSeperateArrayList = new ArrayList<>();
        this.bottomSheetBehavior = (UserLockBottomSheetBehavior) BottomSheetBehavior.from(this.bottomSheet);
        this.bottomSheetDetailBehavior = (UserLockBottomSheetBehavior) BottomSheetBehavior.from(this.detailBottomsheet);
        this.semesterSheetBehavior = (UserLockBottomSheetBehavior) BottomSheetBehavior.from(semesterSelectBottomsheet);
        this.bottomSheet.setNestedScrollingEnabled(false);
        semesterSelectBottomsheet.setNestedScrollingEnabled(false);
        closeBottomSearchSheetButton();
        isBottomSheetOpen = false;
        isSemesterSelectSheetOpen = false;
        this.layoutManager = new LinearLayoutManager(this);
        initBottomSheet();
        initDetailBottomSheet();
        initSearchRecyclerview();
        initSemesterRecyclerview();
        initSemesterSelectBottomSheet();
        initScrollListener();
        initSearchEditText();
        this.timetableView.setOnStickerSelectEventListener(this::onClickedSticker);
//        tempInitSticker();

    }

    @Override
    protected void onStart() {
        super.onStart();
        TimeTableSharedPreferencesHelper.getInstance().init(getApplicationContext());
        this.timetablePresenter.getTimeTableVersion();
        this.timetablePresenter.readSemesters();
    }

    @Override
    public void showLoading() {
        showProgressDialog(R.string.loading);
    }

    @Override
    public void hideLoading() {
        hideProgressDialog();
    }

    public boolean checkIsAnonoymous() {
        return UserInfoSharedPreferencesHelper.getInstance().checkAuthorize() == AuthorizeConstant.ANONYMOUS;
    }

    public void askSaveToImagePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    || (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_REQUEST_CODE);
        }

    }

    public boolean checkStoragePermisson() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                    || (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED))
                return false;
            else
                return true;
        } else
            return true;
    }

    public void saveTimeTableViewInBMP(String semester) {
        File saveImageFile;
        removeAllCheckSticker();
//        this.timeTableScrollview.smoothScrollBy(0, 0);
        Bitmap bitmap = ScreenshotUtil.getInstance().takeTimeTableScreenShot(this.timeTableScrollview, this.timeTableHeader);
        try {
            if (bitmap != null) {
                String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
                String path = Environment.getExternalStorageDirectory().toString() + "/Pictures/" + semester + "_" + timeStamp + ".png";
                File myDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures/");
                myDir.mkdirs();
                saveImageFile = FileUtil.getInstance().storeBitmap(bitmap, path);
                if (saveImageFile != null) {
                    Intent mediaIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    mediaIntent.setData(Uri.fromFile(saveImageFile));
                    sendBroadcast(mediaIntent);
                    ToastUtil.getInstance().makeShort(R.string.timetable_saved);
                } else {
                    ToastUtil.getInstance().makeShort(R.string.timetable_saved_fail);
                }
            }
        } catch (NullPointerException e) {
            ToastUtil.getInstance().makeShort(R.string.timetable_saved_fail);
        }
    }

    public void initSearchEditText() {
        this.timetableAddScheduleSearchEdittext.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                onClickedSearchButton();
                return true;
            }
            return false;
        });
    }

    public void closeBottomSearchSheetButton() {
        this.bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        hideKeyboard(TimetableAnonymousActivity.this);
        clearFilterData();
    }

    @OnClick(R.id.timetable_add_floating_button)
    public void onClickedTimetableAddFloatingButton(View view) {
        onAddClassButtonClicked();
    }

    @OnClick(R.id.timetable_save_timetable_image_linearlayout)
    public void clickedSaveTimetable() {
        Handler handler = new Handler();
        Runnable runnable;
        if (checkStoragePermisson()) {
            runnable = () -> {
                showProgressDialog(R.string.saving);
            };
            handler.post(runnable);
            handler.postDelayed(() -> {
                saveTimeTableViewInBMP(semester);
                hideProgressDialog();
            }, 2000);

        } else {
            ToastUtil.getInstance().makeShort(R.string.need_permission);
            askSaveToImagePermission();
        }
    }

    public void initSearchRecyclerview() {
        this.timetableSearchRecyclerview.setLayoutManager(this.layoutManager);
        this.timetableRecyclerAdapter = new TimetableRecyclerAdapter(this.context, selectedLectureSeperateArrayList);
        this.timetableSearchRecyclerview.setHasFixedSize(true);
        this.timetableSearchRecyclerview.setNestedScrollingEnabled(false);
        this.timetableRecyclerAdapter.setRecyclerViewClickListener(this);
        this.timetableSearchRecyclerview.setAdapter(this.timetableRecyclerAdapter);
    }

    public void initSemesterRecyclerview() {
        timetableSemesterRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        timetableSemesterRecyclerAdapter = new TimetableSemesterRecyclerAdapter(this.context, semesters);
        timetableSemesterRecyclerview.setHasFixedSize(true);
        timetableSemesterRecyclerview.setNestedScrollingEnabled(false);
        timetableSemesterRecyclerAdapter.setRecyclerViewClickListener(new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                for (int i = 0; i < semesters.size(); i++) {
                    semesters.get(i).setSelected(false);
                }

                semesters.get(position).setSelected(true);
                timetableSemesterRecyclerAdapter.notifyDataSetChanged();
                semester = semesters.get(position).getSemester();
                updateTableBySemester(semester);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        });
        timetableSemesterRecyclerview.setAdapter(timetableSemesterRecyclerAdapter);
    }

    public void initBottomSheet() {
        this.bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int state) {
                int height = bottomSheet.getHeight();
                switch (state) {
                    case BottomSheetBehavior.STATE_EXPANDED:
                        timetableView.setMarginBottom(bottomSheet.getHeight());
                        isBottomSheetOpen = true;
                        setTimetableAddFloatingButtonVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        isBottomSheetOpen = false;
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        timetableView.setMarginBottom((int) BOTTOM_NAVIGATION_HEIGHT_PX);
                        isBottomSheetOpen = false;
                        setTimetableAddFloatingButtonVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float v) {
            }
        });
    }

    public void initDetailBottomSheet() {
        this.bottomSheetDetailBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int state) {
                switch (state) {
                    case BottomSheetBehavior.STATE_EXPANDED:
                        timetableView.setMarginBottom(bottomSheet.getHeight());
                        isBottomDetailSheetOpen = true;
                        setTimetableAddFloatingButtonVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        isBottomDetailSheetOpen = false;
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        timetableView.setMarginBottom((int) BOTTOM_NAVIGATION_HEIGHT_PX);
                        isBottomDetailSheetOpen = false;
                        setTimetableAddFloatingButtonVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float v) {
            }
        });
    }

    public void initSemesterSelectBottomSheet() {
        this.semesterSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int state) {
                switch (state) {
                    case BottomSheetBehavior.STATE_EXPANDED:
                        timetableView.setMarginBottom(bottomSheet.getHeight());
                        isSemesterSelectSheetOpen = true;
                        setTimetableAddFloatingButtonVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        isSemesterSelectSheetOpen = false;
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        timetableView.setMarginBottom((int) BOTTOM_NAVIGATION_HEIGHT_PX);
                        isSemesterSelectSheetOpen = false;
                        setTimetableAddFloatingButtonVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float v) {
            }
        });
    }

    @OnClick(R.id.timetable_add_schedule_bottom_sheet_center_textview)
    public void onClickedBottomSheetCenterTextview() {
        if (isBottomSheetOpen) {
            isBottomSheetOpen = false;
            this.timetableView.setMarginBottom(this.timetableAddTopTextView.getHeight());
            this.bottomSheetBehavior.setPeekHeight(this.timetableAddTopTextView.getHeight());
            this.bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            isBottomSheetOpen = true;
            this.bottomSheetBehavior.setPeekHeight(0);
            this.bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }

    }

    @OnClick(R.id.timetable_detail_schedule_bottom_sheet_center_textview)
    public void onClickedDetailBottomSheetCenterTextview() {
        if (isBottomDetailSheetOpen) {
            isBottomDetailSheetOpen = false;
            this.timetableView.setMarginBottom(this.timetableDetailTopTextView.getHeight());
            this.bottomSheetDetailBehavior.setPeekHeight(this.timetableDetailTopTextView.getHeight());
            this.bottomSheetDetailBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            isBottomDetailSheetOpen = true;
            this.bottomSheetDetailBehavior.setPeekHeight(0);
            this.bottomSheetDetailBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }

    }

    @OnClick(R.id.timetable_select_semester_bottom_sheet_center_textview)
    public void onClickedSemesterSelectTextview() {
        if (isSemesterSelectSheetOpen) {
            isSemesterSelectSheetOpen = false;
            this.timetableView.setMarginBottom(this.timetableSemesterSelectTopTextView.getHeight());
            this.semesterSheetBehavior.setPeekHeight(this.timetableSemesterSelectTopTextView.getHeight());
            this.semesterSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            isSemesterSelectSheetOpen = true;
            this.semesterSheetBehavior.setPeekHeight(0);
            this.semesterSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }

    }

    @Override
    public void onBackPressed() {

        if (isBottomSheetOpen)
            this.bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        else if (isBottomDetailSheetOpen)
            this.bottomSheetDetailBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        else if (isSemesterSelectSheetOpen)
            this.semesterSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        else
            super.onBackPressed();
    }

    @OnClick(R.id.koin_base_appbar)
    public void koinBaseAppbarClick(View view) {
        int id = view.getId();
        if (id == AppBarBase.getLeftButtonId())
            onBackPressed();
        else if (id == AppBarBase.getRightButtonId()) {
            toggleNavigationDrawer();
        }
    }

    public void onClickedSticker(TimeTable.TimeTableItem timeTableItem) {
        if (!isBottomSheetOpen) {
            StringBuilder infoStringBuilder = new StringBuilder();
            isBottomDetailSheetOpen = true;
            this.bottomSheetDetailBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            infoStringBuilder.append(getSpertateTimeToString(timeTableItem.getClassTime())).append("\n");
            if (timeTableItem.getDepartment() != null && !timeTableItem.getDepartment().trim().equals(""))
                infoStringBuilder.append(timeTableItem.getDepartment()).append(" / ");
            else
                infoStringBuilder.append("-").append(" / ");
            if (timeTableItem.getCode() != null && !timeTableItem.getCode().trim().equals(""))
                infoStringBuilder.append(timeTableItem.getCode()).append(" / ");
            else
                infoStringBuilder.append("-").append(" / ");
            if (timeTableItem.getGrades() != null && !timeTableItem.getGrades().trim().equals(""))
                infoStringBuilder.append(timeTableItem.getGrades()).append("학점 / ");
            else
                infoStringBuilder.append("-").append("학점 / ");
            if (timeTableItem.getProfessor() != null && !timeTableItem.getProfessor().trim().equals(""))
                infoStringBuilder.append(timeTableItem.getProfessor()).append(" / ");
            else
                infoStringBuilder.append("-").append(" / ");
            this.timetableDetailClassTitleTextview.setText(timeTableItem.getClassTitle());
            this.timetableDetailinformationTextview.setText(infoStringBuilder.toString());
            this.blockId = timeTableItem.getId();

        }
    }

    // Bottom sheet 검색 창 불러오는 함수
    public void onAddClassButtonClicked() {
        clearFilterData();
        isBottomSheetOpen = true;
        this.bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    // Bottom sheet 학기 검색 창 불러오는 함수
    @OnClick(R.id.timetable_select_semester_linearlayout)
    public void onSelectSemesterButtonClicked() {
        isSemesterSelectSheetOpen = true;
        this.semesterSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    // Bottom sheet 검색 확인 함수
    @OnClick(R.id.timetable_add_schedule_bottom_sheet_right_textview)
    public void onClickedBottomSheetRightButton() {
        clearFilterData();
        hideKeyboard(TimetableAnonymousActivity.this);
        this.timetableSearchRecyclerview.setVisibility(View.INVISIBLE);
        closeBottomSearchSheetButton();

    }

    @OnClick(R.id.timetable_add_category_imageview)
    public void onClickedBottomSheetCategoryButton() {
        this.timetableSearchRecyclerview.setVisibility(View.VISIBLE);
        this.timetableSelectAnonymousMajorDialog = new TimetableSelectAnonymousMajorDialog(this);
        this.timetableSelectAnonymousMajorDialog.setOnCLickedDialogItemListener(this);
        this.timetableSelectAnonymousMajorDialog.setMajorDialogListener(new MajorDialogListener() {
            @Override
            public void sendActivity(String major, int number) {
                setResult(major, number);
            }
        });
        this.timetableSelectAnonymousMajorDialog.show();
    }

    private void setResult(String major, int number) {
        if (number >= 0 && number < 10)
            select = number;
        Log.d("select", Integer.toString(select));
    }

    @OnClick(R.id.timetable_add_schedule_search_imageview)
    public void onClickedSearchButton() {
        hideKeyboard(TimetableAnonymousActivity.this);
        selectedLectureSeperateArrayList.clear();
        selectedLectureArrayList.clear();
        String keyword = this.timetableAddScheduleSearchEdittext.getText().toString().trim();
        selectedLectureArrayList.addAll(getFilterUtil(this.selectedDepartmentCode, keyword, totalLectureArrayList, this.timetableView.getAllTimeTableItems()));
        Log.d(TAG, "filter item :" + selectedLectureArrayList.size());
        this.timetableSearchRecyclerview.setVisibility(View.VISIBLE);
        if (selectedLectureArrayList.size() < MAX_ITEM_LOAD) {
            selectedLectureSeperateArrayList.addAll(selectedLectureArrayList);
        } else {
            for (int i = 0; i < MAX_ITEM_LOAD; i++)
                selectedLectureSeperateArrayList.add(selectedLectureArrayList.get(i));
        }
        updateSearchRectclerview();
    }

    @OnTextChanged(R.id.timetable_add_schedule_search_edittext)
    public void searchItemEditext(CharSequence text) {
        selectedLectureSeperateArrayList.clear();
        selectedLectureArrayList.clear();
        String keyword = text.toString().trim();
        selectedLectureArrayList.addAll(getFilterUtil(this.selectedDepartmentCode, keyword, totalLectureArrayList, this.timetableView.getAllTimeTableItems()));
        Log.d(TAG, "filter item :" + selectedLectureArrayList.size());
        this.timetableSearchRecyclerview.setVisibility(View.VISIBLE);
        if (selectedLectureArrayList.size() < MAX_ITEM_LOAD) {
            selectedLectureSeperateArrayList.addAll(selectedLectureArrayList);
        } else {
            for (int i = 0; i < MAX_ITEM_LOAD; i++)
                selectedLectureSeperateArrayList.add(selectedLectureArrayList.get(i));
        }
        updateSearchRectclerview();
    }

    public void clearFilterData() {
        removeAllCheckSticker();
        if (selectedLectureSeperateArrayList != null)
            selectedLectureSeperateArrayList.clear();
        if (selectedLectureArrayList != null)
            selectedLectureArrayList.clear();
        if (this.timetableRecyclerAdapter != null)
            this.timetableRecyclerAdapter.notifyDataSetChanged();
    }

    public void addTimeTableItemSelctedByRecyclerview(int position) {
        ArrayList<TimeTable.TimeTableItem> duplicateTimeTableItems;
        duplicateTimeTableItems = TimeDuplicateCheckUtil.checkDuplicateSchedule(this.timetableView.getAllTimeTableItems(), selectedLectureSeperateArrayList.get(position));
        if (duplicateTimeTableItems.isEmpty()) {
            selectedLectureSeperateClickIndex = position;
            this.timetablePresenter.addTimeTableItem(new TimeTable.TimeTableItem(selectedLectureSeperateArrayList.get(position)), semester);
        } else {
            LogUtil.logdArrayList(duplicateTimeTableItems);
            showDuplicateDialog(position, duplicateTimeTableItems);
        }
    }

    public void showDuplicateDialog(int position, ArrayList<TimeTable.TimeTableItem> duplicateTimeTableItems) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.timetable_duplicate_warning_message);
        StringBuilder duplicateClassTitile = new StringBuilder();
        duplicateClassTitile.append(TimeDuplicateCheckUtil.duplicateScheduleTostring(duplicateTimeTableItems));
        duplicateClassTitile.append(getResources().getString(R.string.timetable_want_to_change_message));
        builder.setMessage(duplicateClassTitile.toString());
        builder.setPositiveButton(R.string.positive,
                (dialog, which) -> {
                    this.timetablePresenter.addTimeTableItem(new TimeTable.TimeTableItem(selectedLectureSeperateArrayList.get(position)), semester);
                    for (TimeTable.TimeTableItem timeTableItem : duplicateTimeTableItems) {
                        this.timetablePresenter.deleteItem(semester, timeTableItem.getId());
                    }
                });
        builder.setNegativeButton(R.string.neutral,
                (dialog, which) -> {
                });
        builder.show();
    }

    public void showAskDeleteTimeTableItemDialog(int index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.timetable_delete_class_message);
        builder.setPositiveButton(R.string.positive,
                (dialog, which) -> {
                    this.timetablePresenter.deleteItem(semester, index);
                });
        builder.setNegativeButton(R.string.neutral,
                (dialog, which) -> {
                });
        builder.show();
    }

    public void removeAllCheckSticker() {
        this.timetableView.removeCheckAll();
        for (Lecture lecture : selectedLectureSeperateArrayList) {
            if (lecture != null)
                lecture.isItemClicked = false;
        }
    }

    @OnClick({R.id.timetable_detail_schedule_bottom_sheet_left_textview, R.id.timetable_detail_schedule_bottom_sheet_right_textview})
    public void onClickedDetailBottomSheet(View view) {
        switch (view.getId()) {
            case R.id.timetable_detail_schedule_bottom_sheet_left_textview:
                showAskDeleteTimeTableItemDialog(this.blockId);
                break;
            case R.id.timetable_detail_schedule_bottom_sheet_right_textview:
                this.bottomSheetDetailBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                this.timetableSearchRecyclerview.setVisibility(View.GONE);
                isBottomDetailSheetOpen = false;
                break;
        }
    }

    @OnClick(R.id.timetable_elect_semester_bottom_sheet_right_textview)
    public void onClickedSemesterRightButton(View view) {
        this.semesterSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        isSemesterSelectSheetOpen = false;
    }

    @Override
    public void showLecture(ArrayList<Lecture> lecture) {
        if (lecture != null) {
            if (totalLectureArrayList != null) {
                totalLectureArrayList.clear();
                totalLectureArrayList.addAll(lecture);
            }
        }
    }

    @Override
    public void showFailMessage(String message) {
        ToastUtil.getInstance().makeLong(message);
    }

    @Override
    public void showSuccessCreateTimeTable() {

    }

    @Override
    public void showSuccessAddTimeTableItem(TimeTable timeTable) {
        if (selectedLectureSeperateClickIndex != -1) {
            selectedLectureSeperateArrayList.get(selectedLectureSeperateClickIndex).isAddButtonClicked = true;
            this.timetableView.removeAll();
            for (TimeTable.TimeTableItem timeTableItem : timeTable.getTimeTableItems())
                this.timetableView.add(timeTableItem);
            updateSearchRectclerview();
        }
        selectedLectureSeperateClickIndex = -1;
    }

    @Override
    public void showFailAddTimeTableItem() {
        ToastUtil.getInstance().makeShort(R.string.error_network);
    }

    @Override
    public void showFailCreateTimeTable() {

    }

    @Override
    public void showSuccessEditTimeTable() {

    }

    @Override
    public void showFailEditTimeTable() {
        ToastUtil.getInstance().makeShort(R.string.error_network);
    }

    @Override
    public void showDeleteSuccessTimeTableItem(int id) {
        this.timetablePresenter.getSavedTimeTableItem(semester);
        this.bottomSheetDetailBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        isBottomDetailSheetOpen = false;
    }

    public void updateSearchRectclerview() {
        getClikckedUtil(selectedLectureSeperateArrayList, this.timetableView.getAllTimeTableItems());
        this.timetableRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void showFailDeleteTimeTableItem() {
        ToastUtil.getInstance().makeShort(R.string.error_network);
    }

    @Override
    public void showDeleteSuccessTimeTableAllItem() {

    }

    @Override
    public void showFailDeleteTImeTableAllItem() {

    }

    @Override
    public void showSavedTimeTable(TimeTable timeTable) {
        this.timetableView.removeAll();
        for (TimeTable.TimeTableItem timeTableItem : timeTable.getTimeTableItems()) {
            timeTableItem.isSavedAtServer = true;
            this.timetableView.add(timeTableItem);
        }
        updateSearchRectclerview();
    }

    @Override
    public void showFailSavedTimeTable() {
        this.timetableView.removeAll();
    }

    @Override
    public void setPresenter(TimetableAnonymousPresenter presenter) {
        this.timetablePresenter = presenter;
    }

    private void initScrollListener() {
        this.timetableSearchRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy == 0) return;
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == selectedLectureSeperateArrayList.size() - 1) {
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });


    }

    private void loadMore() {
        selectedLectureSeperateArrayList.add(null);
        Handler handler = new Handler();
        final Runnable r = () -> this.timetableRecyclerAdapter.notifyItemInserted(selectedLectureSeperateArrayList.size() - 1);
        handler.post(r);
        handler.postDelayed(() -> {
            selectedLectureSeperateArrayList.remove(selectedLectureSeperateArrayList.size() - 1);
            int scrollPosition = selectedLectureSeperateArrayList.size();
            this.timetableRecyclerAdapter.notifyItemRemoved(scrollPosition);
            int currentSize = scrollPosition;
            int nextLimit = currentSize + MAX_ITEM_LOAD;
            while (currentSize - 1 < nextLimit) {
                if (currentSize - 1 == selectedLectureArrayList.size() - 1) {
                    return;
                }
                selectedLectureSeperateArrayList.add(selectedLectureArrayList.get(currentSize));
                Log.d(this.getClass().getName(), currentSize + "");
                currentSize++;
            }

            this.timetableRecyclerAdapter.notifyDataSetChanged();
            isLoading = false;
        }, LOAD_TIME_MS);


    }

    @Override
    public void onClickedItemList(DepartmentCode departmentCode) {
        new Handler().postDelayed(() -> {
            this.selectedDepartmentCode = departmentCode;
            isLoading = false;
            this.timetableAddScheduleSearchEdittext.setText("");
            onClickedSearchButton();
            this.timetableSearchRecyclerview.scrollToPosition(0);
        }, 200);


    }

    @Override
    public void onClick(View view, int position) {
        this.viewHolder = this.timetableSearchRecyclerview.findViewHolderForAdapterPosition(position);
        switch (view.getId()) {
            case R.id.timetable_recyclerview_item_relativelayout:
                removeAllCheckSticker();
                selectedLectureSeperateArrayList.get(position).isItemClicked = true;
                this.timetableView.addCheck(new TimeTable.TimeTableItem(selectedLectureArrayList.get(position)));
                this.timetableRecyclerAdapter.notifyDataSetChanged();
                break;
            case R.id.add_lecture_button:
                if (!selectedLectureSeperateArrayList.get(position).isAddButtonClicked)
                    addTimeTableItemSelctedByRecyclerview(position);
                else {
                    int index = this.timetableView.getSameIdWithLecture(selectedLectureSeperateArrayList.get(position));
                    if (index != -1)
                        this.timetablePresenter.deleteItem(semester, index);
                }
                break;
            default:
                removeAllCheckSticker();
                break;
        }
    }

    @Override
    public void onLongClick(View view, int position) {

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void showUpdateAlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.timetable_update_message);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.positive,
                (dialog, which) -> {
                });
        builder.show();
    }

    @Override
    public void updateSemesterCode(String semester) {
        this.semester = semester;
        updateTableBySemester(semester);
    }

    public void updateTableBySemester(String semester) {
        String yearString = semester.substring(0, 4);
        String semesterString = semester.substring(4);
        selectSemesterTextview.setText(yearString + "년 " + semesterString + "학기");
        this.timetablePresenter.getLecture(semester);
        this.timetablePresenter.getSavedTimeTableItem(semester);
    }

    @Override
    public void getSemester(ArrayList<Semester> semesters) {
        if (semesters == null) return;
        this.semesters.clear();
        this.semesters.addAll(semesters);
        this.semesters.get(0).setSelected(true);
        timetableSemesterRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateWidget() {
        Handler handler = new Handler();
        handler.postDelayed(this::saveWidgetImage, 2000);
    }

    public void saveWidgetImage() {
        Bitmap bitmap;
        this.timetableView.setCheckStickersVisibilty(false);
        bitmap = ScreenshotUtil.getInstance().takeTimeTableScreenShot(this.timeTableScrollview, this.timeTableHeader);
        this.timetableView.setCheckStickersVisibilty(true);
        if (bitmap == null) return;
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File directory = contextWrapper.getDir("timeTable", Context.MODE_PRIVATE);
        File saveImageFile = new File(directory, "timetable.png");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(saveImageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        } finally {
            try {
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        TimeTableSharedPreferencesHelper.getInstance().saveLastPathTimetableImage(directory.getAbsolutePath());
        Intent intent = new Intent(this, TimetableWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        this.sendBroadcast(intent);
    }

    public void setTimetableAddFloatingButtonVisibility(int visibility) {
        if (visibility == View.GONE)
            timetableAddFloatingButton.hide();
        else
            timetableAddFloatingButton.show();
    }

    @NonNull
    @Override
    protected MenuState getMenuState() {
        return MenuState.Timetable.INSTANCE;
    }
}
