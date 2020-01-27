package in.koreatech.koin.service_timetable.ui;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import in.koreatech.koin.KoinNavigationDrawerActivity;
import in.koreatech.koin.R;
import in.koreatech.koin.core.asynctasks.GenerateProgressTask;
import in.koreatech.koin.core.bases.KoinBaseAppbarDark;
import in.koreatech.koin.core.helpers.RecyclerViewClickListener;
import in.koreatech.koin.core.helpers.TimeTableSharedPreferencesHelper;
import in.koreatech.koin.core.helpers.UserLockBottomSheetBehavior;
import in.koreatech.koin.core.networks.entity.Lecture;
import in.koreatech.koin.core.networks.entity.Semester;
import in.koreatech.koin.core.networks.entity.TimeTable;
import in.koreatech.koin.core.networks.entity.TimeTable.TimeTableItem;
import in.koreatech.koin.core.util.LogUtil;
import in.koreatech.koin.core.util.ToastUtil;
import in.koreatech.koin.core.util.timetable.DepartmentCode;
import in.koreatech.koin.core.util.timetable.TimeDuplicateCheckUtil;
import in.koreatech.koin.core.util.timetable.TimetableView;
import in.koreatech.koin.service_timetable.contracts.MajorDialogListener;
import in.koreatech.koin.service_timetable.contracts.TimetableContract;
import in.koreatech.koin.service_timetable.adapters.TimetableRecyclerAdapter;
import in.koreatech.koin.service_timetable.adapters.TimetableSemesterRecyclerAdapter;
import in.koreatech.koin.service_timetable.presenters.TimetablePresenter;
import in.koreatech.koin.service_timetable.widget.TimetableWidget;
import in.koreatech.koin.service_timetable.widget.helper.FileUtil;
import in.koreatech.koin.service_timetable.widget.helper.ScreenshotUtil;

import static in.koreatech.koin.core.util.timetable.LectureFilterUtil.getClikckedUtil;
import static in.koreatech.koin.core.util.timetable.LectureFilterUtil.getFilterUtil;
import static in.koreatech.koin.core.util.timetable.SeparateTime.getSpertateTimeToString;
import static in.koreatech.koin.core.util.timetable.TimeDuplicateCheckUtil.duplicateScheduleTostring;


public class TimetableActivity extends KoinNavigationDrawerActivity implements TimetableContract.View, TimetableSelectMajorDialog.OnCLickedDialogItemListener, RecyclerViewClickListener {
    public static final String TAG = "TimetableActivity";
    private GenerateProgressTask generateProgressTask;
    public static final int MY_REQUEST_CODE = 1;
    public static final int MAX_ITEM_LOAD = 40;
    public static final int LOAD_TIME_MS = 500;
    public static final float BOTTOM_NAVIGATION_HEIGHT_PX = Resources.getSystem().getDisplayMetrics().density * 56;

    public static int select = -1;          //TimetableSelectMajorDialog에서 선택했던것을 기억하는 변수
    @BindView(R.id.timetable_timetableview)
    TimetableView timetableView;
    @BindView(R.id.timetable_add_schedule_bottom_sheet)
    LinearLayout bottomsheet;
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

    private Context context;
    private UserLockBottomSheetBehavior bottomSheetBehavior;
    private UserLockBottomSheetBehavior bottomSheetDetailBehavior;
    private UserLockBottomSheetBehavior semesterSheetBehavior;
    private boolean isBottomSheetOpen;
    private boolean isBottomDetailSheetOpen;
    private boolean isSemesterSelectSheetOpen;
    private TimetablePresenter timetablePresenter;
    private int categoryNumber;

    private RecyclerView.LayoutManager layoutManager; // RecyclerView LayoutManager
    private ArrayList<TimeTableItem> timeTableArrayList;
    private ArrayList<TimeTableItem> timeTableEditArrayList;
    private ArrayList<Lecture> totalLectureArrayList;
    private ArrayList<Lecture> selectedLectureArrayList;
    private ArrayList<Lecture> selectedLectureSeperateArrayList;
    private ArrayList<Semester> semesters;
    private int selectedLectureSeperateClickIndex = -1;

    private RecyclerView.ViewHolder viewHolder;
    private TimetableRecyclerAdapter timetableRecyclerAdapter;
    private TimetableSemesterRecyclerAdapter timetableSemesterRecyclerAdapter;
    private TimetableSelectMajorDialog timetableSelectMajorDialog;
    private boolean isLoading;
    private DepartmentCode selectedDepartmentCode;
    private int blockId;

    private String semester = "";

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
        setPresenter(new TimetablePresenter(this));
        this.categoryNumber = -1;
        select = -1;
        totalLectureArrayList = new ArrayList<>();
        selectedLectureArrayList = new ArrayList<>();
        this.timeTableArrayList = new ArrayList<>();
        semesters = new ArrayList<>();
        selectedLectureSeperateArrayList = new ArrayList<>();
        this.bottomSheetBehavior = (UserLockBottomSheetBehavior) BottomSheetBehavior.from(this.bottomsheet);
        this.bottomSheetDetailBehavior = (UserLockBottomSheetBehavior) BottomSheetBehavior.from(this.detailBottomsheet);
        this.semesterSheetBehavior = (UserLockBottomSheetBehavior) BottomSheetBehavior.from(semesterSelectBottomsheet);
        this.bottomsheet.setNestedScrollingEnabled(false);
        semesterSelectBottomsheet.setNestedScrollingEnabled(false);
        closeBottomSearchSheetButton();
        isBottomSheetOpen = false;
        isSemesterSelectSheetOpen = false;
        this.layoutManager = new LinearLayoutManager(this);
        initBottomSheet();
        initDetailBottomSheet();
        initSemesterSelectBottomSheet();
        initSearchRecyclerview();
        initSemesterRecyclerview();
        initScrollListener();
        initSearchEditText();
        this.timetableView.setOnStickerSelectEventListener(this::onClickedSticker);
//        tempInitSticker();

    }

    @Override
    protected void onStart() {
        super.onStart();
        TimeTableSharedPreferencesHelper.getInstance().init(getApplicationContext());
        this.timetablePresenter.getTimetTableVersion();
        this.timetablePresenter.readSemesters();
    }

    @Override
    public void showLoading() {
        if (generateProgressTask == null) {
            generateProgressTask = new GenerateProgressTask(this, "로딩 중");
            generateProgressTask.execute();
        }
    }

    @Override
    public void hideLoading() {
        if (generateProgressTask != null) {
            generateProgressTask.cancel(true);
            generateProgressTask = null;
        }
    }

    public void tempInitSticker() {
        int[] tempTimes = {0, 1, 2, 401, 402};
        int[] tempTImes2 = {2, 3, 4, 6, 123};
        ArrayList<Integer> time1 = new ArrayList<>();
        ArrayList<Integer> time2 = new ArrayList<>();
        for (int i : tempTimes)
            time1.add(i);
        for (int i : tempTImes2)
            time2.add(i);
        this.timetableView.add(new TimeTableItem("알라딘", time1, "노홍촐"));
        this.timetableView.add(new TimeTableItem("성인학습론", time2, "파바사라"));

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
                String path = Environment.getExternalStorageDirectory() + "/Pictures/" + semester + "_" + timeStamp + ".png";
                File myDir = new File(Environment.getExternalStorageDirectory() + "/Pictures/");
                myDir.mkdirs();
                saveImageFile = FileUtil.getInstance().storeBitmap(bitmap, path);
                if (saveImageFile != null) {
                    Intent mediaIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    mediaIntent.setData(Uri.fromFile(saveImageFile));
                    sendBroadcast(mediaIntent);
                    Toast.makeText(this, "저장되었습니다.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "저장에 실패했습니다.", Toast.LENGTH_LONG).show();
                }
            }
        } catch (NullPointerException e) {
            Toast.makeText(this, "저장에 실패하였습니다.", Toast.LENGTH_LONG).show();
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
        hideKeyboard(TimetableActivity.this);
        clearFilterData();
    }

    @OnClick(R.id.timetable_save_timetable_image_linearlayout)
    public void clickedSaveTimetable() {
        Handler handler = new Handler();
        Runnable runnable;
        if (checkStoragePermisson()) {
            runnable = () -> {
                generateProgressTask = new GenerateProgressTask(this.context, "저장 중");
                generateProgressTask.execute();
            };
            handler.post(runnable);
            handler.postDelayed(() -> {
                saveTimeTableViewInBMP(semester);
                generateProgressTask.cancel(true);
                generateProgressTask = null;
            }, 2000);

        } else {
            ToastUtil.makeShortToast(this.context, "권한이 필요합니다.");
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
                    semesters.get(i).isSelected = false;
                }

                semesters.get(position).isSelected = true;
                timetableSemesterRecyclerAdapter.notifyDataSetChanged();
                semester = semesters.get(position).semester;
                timetablePresenter.getLecture(semester);
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
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        isBottomSheetOpen = false;
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        timetableView.setMarginBottom((int) BOTTOM_NAVIGATION_HEIGHT_PX);
                        isBottomSheetOpen = false;
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
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        isBottomDetailSheetOpen = false;
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        timetableView.setMarginBottom((int) BOTTOM_NAVIGATION_HEIGHT_PX);
                        isBottomDetailSheetOpen = false;
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
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        isSemesterSelectSheetOpen = false;
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        timetableView.setMarginBottom((int) BOTTOM_NAVIGATION_HEIGHT_PX);
                        isSemesterSelectSheetOpen = false;
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
        if (id == KoinBaseAppbarDark.getLeftButtonId())
            onBackPressed();
        else if (id == KoinBaseAppbarDark.getRightButtonId()) {
            onAddClassButtonClicked();
        }
    }

    public void onClickedSticker(TimeTableItem timeTableItem) {
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
        hideKeyboard(TimetableActivity.this);
        this.timetableSearchRecyclerview.setVisibility(View.INVISIBLE);
        closeBottomSearchSheetButton();

    }

    @OnClick(R.id.timetable_add_category_imageview)
    public void onClickedBottomSheetCategoryButton() {
        this.timetableSearchRecyclerview.setVisibility(View.VISIBLE);
        this.timetableSelectMajorDialog = new TimetableSelectMajorDialog(this);
        this.timetableSelectMajorDialog.setOnCLickedDialogItemListener(this);
        this.timetableSelectMajorDialog.setMajorDialogListener(new MajorDialogListener() {
            @Override
            public void sendActivity(String major, int number) {
                setResult(major, number);
            }
        });
        this.timetableSelectMajorDialog.show();
    }

    private void setResult(String major, int number) {
        if (number >= 0 && number < 10)
            select = number;
        Log.d("select", Integer.toString(select));
    }


    @OnClick(R.id.timetable_add_schedule_search_imageview)
    public void onClickedSearchButton() {
        hideKeyboard(TimetableActivity.this);
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
        ArrayList<TimeTableItem> duplicateTimeTableItems;
        duplicateTimeTableItems = TimeDuplicateCheckUtil.checkDuplicateSchedule(this.timetableView.getAllTimeTableItems(), selectedLectureSeperateArrayList.get(position));
        if (duplicateTimeTableItems.isEmpty()) {
            selectedLectureSeperateClickIndex = position;
            this.timetablePresenter.addTimeTableItem(new TimeTableItem(selectedLectureSeperateArrayList.get(position)), semester);
        } else {
            LogUtil.logdArrayList(duplicateTimeTableItems);
            showDuplicateDialog(position, duplicateTimeTableItems);
        }
    }

    public void showDuplicateDialog(int position, ArrayList<TimeTableItem> duplicateTimeTableItems) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("과목 중복 / 시간 중복");
        StringBuilder duplicateClassTitile = new StringBuilder();
        duplicateClassTitile.append(duplicateScheduleTostring(duplicateTimeTableItems));
        duplicateClassTitile.append("바꾸시겠습니까?");
        builder.setMessage(duplicateClassTitile.toString());
        builder.setPositiveButton("확인",
                (dialog, which) -> {
                    for (TimeTableItem timeTableItem : duplicateTimeTableItems) {
                        this.timetablePresenter.deleteItem(timeTableItem.getId());
                    }
                    this.timetablePresenter.addTimeTableItem(new TimeTableItem(selectedLectureSeperateArrayList.get(position)), semester);
                });
        builder.setNegativeButton("취소",
                (dialog, which) -> {
                });
        builder.show();
    }

    public void showAskDeleteTimeTableItemDialog(int index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("해당 수업을 삭제하시겠습니까?");
        builder.setPositiveButton("확인",
                (dialog, which) -> {
                    this.timetablePresenter.deleteItem(index);
                });
        builder.setNegativeButton("취소",
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
    public void showFailMessage(String message) {
        ToastUtil.makeLongToast(this, message);
    }

    @Override
    public void showSuccessCreateTimeTable() {

    }

    @Override
    public void showSuccessAddTimeTableItem(TimeTable timeTable) {
        if (selectedLectureSeperateClickIndex != -1) {
            selectedLectureSeperateArrayList.get(selectedLectureSeperateClickIndex).isAddButtonClicked = true;
            this.timetableView.removeAll();
            for (TimeTableItem timeTableItem : timeTable.getTimeTableItems())
                this.timetableView.add(timeTableItem);
            updateSearchRectclerview();
        }
        selectedLectureSeperateClickIndex = -1;
    }

    @Override
    public void showFailAddTimeTableItem() {
        ToastUtil.makeShortToast(this.context, "인터넷 환경을 확인해주세요");
    }

    @Override
    public void showFailCreateTimeTable() {

    }

    @Override
    public void showSuccessEditTimeTable() {

    }

    @Override
    public void showFailEditTimeTable() {
        ToastUtil.makeShortToast(this.context, "인터넷 환경을 확인해주세요");
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
        ToastUtil.makeShortToast(this.context, "인터넷 환경을 확인해주세요");
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
        for (TimeTableItem timeTableItem : timeTable.getTimeTableItems()) {
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
    public void setPresenter(TimetablePresenter presenter) {
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
                this.timetableView.addCheck(new TimeTableItem(selectedLectureArrayList.get(position)));
                this.timetableRecyclerAdapter.notifyDataSetChanged();
                break;
            case R.id.add_lecture_button:
                if (!selectedLectureSeperateArrayList.get(position).isAddButtonClicked)
                    addTimeTableItemSelctedByRecyclerview(position);
                else {
                    int index = this.timetableView.getSameIdWithLecture(selectedLectureSeperateArrayList.get(position));
                    if (index != -1)
                        this.timetablePresenter.deleteItem(index);
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
        builder.setTitle("업데이트");
        builder.setMessage(message);
        builder.setPositiveButton("확인",
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
    public void updateWidget() {
        Handler handler = new Handler();
        handler.postDelayed(this::saveWidgetImage, 2000);

    }

    @Override
    public void getSemester(ArrayList<Semester> semesters) {
        if (semesters == null) return;
        this.semesters.clear();
        this.semesters.addAll(semesters);
        this.semesters.get(0).isSelected = true;
        timetableSemesterRecyclerAdapter.notifyDataSetChanged();
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
}
