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
import android.widget.ScrollView;
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
import in.koreatech.koin.core.constants.AuthorizeConstant;
import in.koreatech.koin.core.helpers.DefaultSharedPreferencesHelper;
import in.koreatech.koin.core.helpers.RecyclerViewClickListener;
import in.koreatech.koin.core.helpers.TimeTableSharedPreferencesHelper;
import in.koreatech.koin.core.helpers.UserLockBottomSheetBehavior;
import in.koreatech.koin.core.networks.entity.Lecture;
import in.koreatech.koin.core.networks.entity.TimeTable;
import in.koreatech.koin.core.networks.entity.TimeTable.TimeTableItem;
import in.koreatech.koin.core.util.LogUtil;
import in.koreatech.koin.core.util.ToastUtil;
import in.koreatech.koin.core.util.timetable.DepartmentCode;
import in.koreatech.koin.core.util.timetable.TimeDuplicateCheckUtil;
import in.koreatech.koin.core.util.timetable.TimetableView;
import in.koreatech.koin.service_timetable.Contracts.MajorDialogListener;
import in.koreatech.koin.service_timetable.Contracts.TimetableAnonymousContract;
import in.koreatech.koin.service_timetable.adapters.TimetableRecyclerAdapter;
import in.koreatech.koin.service_timetable.presenters.TimetableAnonymousPresenter;
import in.koreatech.koin.service_timetable.widget.TimetableWidget;
import in.koreatech.koin.service_timetable.widget.helper.FileUtil;
import in.koreatech.koin.service_timetable.widget.helper.ScreenshotUtil;

import static in.koreatech.koin.core.util.timetable.LectureFilterUtil.getClikckedUtil;
import static in.koreatech.koin.core.util.timetable.LectureFilterUtil.getFilterUtil;
import static in.koreatech.koin.core.util.timetable.SeparateTime.getSpertateTimeToString;
import static in.koreatech.koin.core.util.timetable.TimeDuplicateCheckUtil.duplicateScheduleTostring;


public class TimetableAnonymousActivity extends KoinNavigationDrawerActivity implements TimetableAnonymousContract.View, TimetableSelectAnonymousMajorDialog.OnCLickedDialogItemListener, RecyclerViewClickListener {
    public static final String TAG = TimetableAnonymousActivity.class.getName();
    public static final int MY_REQUEST_CODE = 1;
    public static final int MAX_ITEM_LOAD = 40;
    public static final int LOAD_TIME_MS = 500;
    public static final float BOTTOM_NAVIGATION_HEIGHT_PX = Resources.getSystem().getDisplayMetrics().density * 56;

    public static int select = -1;          //TimetableSelectAnonymousMajorDialog에서 선택했던것을 기억하는 변수
    private GenerateProgressTask generateProgressTask;
    @BindView(R.id.timetable_timetableview)
    TimetableView mTimetableView;
    @BindView(R.id.timetable_add_schedule_bottom_sheet)
    LinearLayout mBottomsheet;
    @BindView(R.id.timetable_container_relativelayout)
    RelativeLayout mTimeTableContainerRelativeLayout;
    @BindView(R.id.timetable_search_recyclerview)
    RecyclerView mTimetableSearchRecyclerview;
    @BindView(R.id.timetable_add_schedule_bottom_sheet_center_textview)
    TextView mTimetableAddTopTextView;
    @BindView(R.id.timetable_detail_schedule_bottom_sheet)
    LinearLayout mDetailBottomsheet;
    @BindView(R.id.timetable_detail_schedule_bottom_sheet_center_textview)
    TextView mTimetableDetailTopTextView;
    @BindView(R.id.timetable_detail_schedule_bottom_sheet_class_title_textview)
    TextView mTimetableDetailClassTitleTextview;
    @BindView(R.id.timetable_detail_schedule_bottom_sheet_class_detail_textview)
    TextView mTimetableDetailinformationTextview;
    @BindView(R.id.timetable_detail_schedule_bottom_sheet_left_textview)
    TextView mTimetableDetailLeftTextview;
    @BindView(R.id.timetable_detail_schedule_bottom_sheet_right_textview)
    TextView mTimetableDetailRightTextview;
    @BindView(R.id.timetable_add_schedule_search_edittext)
    TextView mTimetableAddScheduleSearchEdittext;
    @BindView(R.id.timetable_scrollview)
    NestedScrollView mTimeTableScrollview;
    @BindView(R.id.table_header)
    TableLayout mTimeTableHeader;

    private Context mContext;
    private UserLockBottomSheetBehavior mBottomSheetBehavior;
    private UserLockBottomSheetBehavior mBottomSheetDetailBehavior;
    private boolean isBottomSheetOpen;
    private boolean isBottomDetailSheetOpen;
    private TimetableAnonymousPresenter mTimetablePresenter;
    private int mCategoryNumber;

    private RecyclerView.LayoutManager mLayoutManager; // RecyclerView LayoutManager
    private ArrayList<TimeTableItem> mTimeTableArrayList;
    private ArrayList<TimeTableItem> mTimeTableEditArrayList;
    private ArrayList<Lecture> totalLectureArrayList;
    private ArrayList<Lecture> selectedLectureArrayList;
    private ArrayList<Lecture> selectedLectureSeperateArrayList;
    private int selectedLectureSeperateClickIndex = -1;

    private RecyclerView.ViewHolder mViewHolder;
    private TimetableRecyclerAdapter mTimetableRecyclerAdapter;
    private TimetableSelectAnonymousMajorDialog mTimetableSelectAnonymousMajorDialog;
    private boolean isLoading;
    private DepartmentCode mSelectedDepartmentCode;
    private int mBlockId;

    private String semester = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable_activity_main);
        mContext = this;
        ButterKnife.bind(this);
        init();
    }


    public void init() {
        isLoading = false;
        mSelectedDepartmentCode = DepartmentCode.DEPARTMENT_CODE_0;
        setPresenter(new TimetableAnonymousPresenter(this));
        mCategoryNumber = -1;
        select = -1;
        totalLectureArrayList = new ArrayList<>();
        selectedLectureArrayList = new ArrayList<>();
        mTimeTableArrayList = new ArrayList<>();
        selectedLectureSeperateArrayList = new ArrayList<>();
        mBottomSheetBehavior = (UserLockBottomSheetBehavior) BottomSheetBehavior.from(mBottomsheet);
        mBottomSheetDetailBehavior = (UserLockBottomSheetBehavior) BottomSheetBehavior.from(mDetailBottomsheet);
        mBottomsheet.setNestedScrollingEnabled(false);
        closeBottomSearchSheetButton();
        isBottomSheetOpen = false;
        mLayoutManager = new LinearLayoutManager(this);
        initBottomSheet();
        initDetailBottomSheet();
        initSearchRecyclerview();
        initScrollListener();
        initSearchEditText();
        mTimetableView.setOnStickerSelectEventListener(this::onClickedSticker);
//        tempInitSticker();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!checkIsAnonoymous()) {
            goToTimetableActivty();
        }
        TimeTableSharedPreferencesHelper.getInstance().init(getApplicationContext());
        mTimetablePresenter.getTimeTableVersion();
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
        mTimetableView.add(new TimeTableItem("알라딘", time1, "노홍촐"));
        mTimetableView.add(new TimeTableItem("성인학습론", time2, "파바사라"));

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

    public boolean checkIsAnonoymous() {
        return DefaultSharedPreferencesHelper.getInstance().checkAuthorize() == AuthorizeConstant.ANONYMOUS;
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
//        mTimeTableScrollview.smoothScrollBy(0, 0);
        Bitmap bitmap = ScreenshotUtil.getInstance().takeTimeTableScreenShot(mTimeTableScrollview, mTimeTableHeader);
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
        mTimetableAddScheduleSearchEdittext.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                onClickedSearchButton();
                return true;
            }
            return false;
        });
    }

    public void closeBottomSearchSheetButton() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        hideKeyboard(TimetableAnonymousActivity.this);
        clearFilterData();
    }

    @OnClick(R.id.timetable_save_timetable_image_linearlayout)
    public void clickedSaveTimetable() {
        Handler handler = new Handler();
        Runnable runnable;
        if (checkStoragePermisson()) {
            runnable = () -> {
                generateProgressTask = new GenerateProgressTask(mContext, "저장 중");
                generateProgressTask.execute();
            };
            handler.post(runnable);
            handler.postDelayed(() -> {
                saveTimeTableViewInBMP(semester);
                generateProgressTask.cancel(true);
                generateProgressTask = null;
            }, 2000);

        } else {
            ToastUtil.makeShortToast(mContext, "권한이 필요합니다.");
            askSaveToImagePermission();
        }
    }

    public void initSearchRecyclerview() {
        mTimetableSearchRecyclerview.setLayoutManager(mLayoutManager);
        mTimetableRecyclerAdapter = new TimetableRecyclerAdapter(mContext, selectedLectureSeperateArrayList);
        mTimetableSearchRecyclerview.setHasFixedSize(true);
        mTimetableSearchRecyclerview.setNestedScrollingEnabled(false);
        mTimetableRecyclerAdapter.setRecyclerViewClickListener(this);
        mTimetableSearchRecyclerview.setAdapter(mTimetableRecyclerAdapter);
    }


    public void initBottomSheet() {
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int state) {
                int height = bottomSheet.getHeight();
                switch (state) {
                    case BottomSheetBehavior.STATE_EXPANDED:
                        mTimetableView.setMarginBottom(bottomSheet.getHeight());
                        isBottomSheetOpen = true;
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        isBottomSheetOpen = false;
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        mTimetableView.setMarginBottom((int) BOTTOM_NAVIGATION_HEIGHT_PX);
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
        mBottomSheetDetailBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int state) {
                switch (state) {
                    case BottomSheetBehavior.STATE_EXPANDED:
                        mTimetableView.setMarginBottom(bottomSheet.getHeight());
                        isBottomDetailSheetOpen = true;
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        isBottomDetailSheetOpen = false;
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        mTimetableView.setMarginBottom((int) BOTTOM_NAVIGATION_HEIGHT_PX);
                        isBottomDetailSheetOpen = false;
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
            mTimetableView.setMarginBottom(mTimetableAddTopTextView.getHeight());
            mBottomSheetBehavior.setPeekHeight(mTimetableAddTopTextView.getHeight());
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            isBottomSheetOpen = true;
            mBottomSheetBehavior.setPeekHeight(0);
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }

    }

    @OnClick(R.id.timetable_detail_schedule_bottom_sheet_center_textview)
    public void onClickedDetailBottomSheetCenterTextview() {
        if (isBottomDetailSheetOpen) {
            isBottomDetailSheetOpen = false;
            mTimetableView.setMarginBottom(mTimetableDetailTopTextView.getHeight());
            mBottomSheetDetailBehavior.setPeekHeight(mTimetableDetailTopTextView.getHeight());
            mBottomSheetDetailBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            isBottomDetailSheetOpen = true;
            mBottomSheetDetailBehavior.setPeekHeight(0);
            mBottomSheetDetailBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }

    }


    @Override
    public void onBackPressed() {

        if (isBottomSheetOpen)
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        else if (isBottomDetailSheetOpen)
            mBottomSheetDetailBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
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
            mBottomSheetDetailBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
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
            mTimetableDetailClassTitleTextview.setText(timeTableItem.getClassTitle());
            mTimetableDetailinformationTextview.setText(infoStringBuilder.toString());
            mBlockId = timeTableItem.getId();

        }
    }

    // Bottom sheet 검색 창 불러오는 함수
    public void onAddClassButtonClicked() {
        clearFilterData();
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    // Bottom sheet 검색 확인 함수
    @OnClick(R.id.timetable_add_schedule_bottom_sheet_right_textview)
    public void onClickedBottomSheetRightButton() {
        clearFilterData();
        hideKeyboard(TimetableAnonymousActivity.this);
        mTimetableSearchRecyclerview.setVisibility(View.INVISIBLE);
        closeBottomSearchSheetButton();

    }

    @OnClick(R.id.timetable_add_category_imageview)
    public void onClickedBottomSheetCategoryButton() {
        mTimetableSearchRecyclerview.setVisibility(View.VISIBLE);
        mTimetableSelectAnonymousMajorDialog = new TimetableSelectAnonymousMajorDialog(this);
        mTimetableSelectAnonymousMajorDialog.setOnCLickedDialogItemListener(this);
        mTimetableSelectAnonymousMajorDialog.setMajorDialogListener(new MajorDialogListener() {
            @Override
            public void sendActivity(String major, int number) {
                setResult(major, number);
            }
        });
        mTimetableSelectAnonymousMajorDialog.show();
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
        String keyword = mTimetableAddScheduleSearchEdittext.getText().toString().trim();
        selectedLectureArrayList.addAll(getFilterUtil(mSelectedDepartmentCode, keyword, totalLectureArrayList, mTimetableView.getAllTimeTableItems()));
        Log.d(TAG, "filter item :" + selectedLectureArrayList.size());
        mTimetableSearchRecyclerview.setVisibility(View.VISIBLE);
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
        selectedLectureArrayList.addAll(getFilterUtil(mSelectedDepartmentCode, keyword, totalLectureArrayList, mTimetableView.getAllTimeTableItems()));
        Log.d(TAG, "filter item :" + selectedLectureArrayList.size());
        mTimetableSearchRecyclerview.setVisibility(View.VISIBLE);
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
        if (mTimetableRecyclerAdapter != null)
            mTimetableRecyclerAdapter.notifyDataSetChanged();
    }


    public void addTimeTableItemSelctedByRecyclerview(int position) {
        ArrayList<TimeTableItem> duplicateTimeTableItems;
        duplicateTimeTableItems = TimeDuplicateCheckUtil.checkDuplicateSchedule(mTimetableView.getAllTimeTableItems(), selectedLectureSeperateArrayList.get(position));
        if (duplicateTimeTableItems.isEmpty()) {
            selectedLectureSeperateClickIndex = position;
            mTimetablePresenter.addTimeTableItem(new TimeTableItem(selectedLectureSeperateArrayList.get(position)), semester);
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
                    mTimetablePresenter.addTimeTableItem(new TimeTableItem(selectedLectureSeperateArrayList.get(position)), semester);
                    for (TimeTableItem timeTableItem : duplicateTimeTableItems) {
                        mTimetablePresenter.deleteItem(timeTableItem.getId());
                    }
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
                    mTimetablePresenter.deleteItem(index);
                });
        builder.setNegativeButton("취소",
                (dialog, which) -> {
                });
        builder.show();
    }

    public void removeAllCheckSticker() {
        mTimetableView.removeCheckAll();
        for (Lecture lecture : selectedLectureSeperateArrayList) {
            if (lecture != null)
                lecture.isItemClicked = false;
        }
    }

    @OnClick({R.id.timetable_detail_schedule_bottom_sheet_left_textview, R.id.timetable_detail_schedule_bottom_sheet_right_textview})
    public void onClickedDetailBottomSheet(View view) {
        switch (view.getId()) {
            case R.id.timetable_detail_schedule_bottom_sheet_left_textview:
                showAskDeleteTimeTableItemDialog(mBlockId);
                break;
            case R.id.timetable_detail_schedule_bottom_sheet_right_textview:
                mBottomSheetDetailBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                mTimetableSearchRecyclerview.setVisibility(View.GONE);
                isBottomDetailSheetOpen = false;
                break;
        }
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
            mTimetableView.removeAll();
            for (TimeTableItem timeTableItem : timeTable.getTimeTableItems())
                mTimetableView.add(timeTableItem);
            updateSearchRectclerview();
        }
        selectedLectureSeperateClickIndex = -1;
    }

    @Override
    public void showFailAddTimeTableItem() {
        ToastUtil.makeShortToast(mContext, "인터넷 환경을 확인해주세요");
    }

    @Override
    public void showFailCreateTimeTable() {

    }

    @Override
    public void showSuccessEditTimeTable() {

    }

    @Override
    public void showFailEditTimeTable() {
        ToastUtil.makeShortToast(mContext, "인터넷 환경을 확인해주세요");
    }

    @Override
    public void showDeleteSuccessTimeTableItem(int id) {
        mTimetablePresenter.getSavedTimeTableItem();
        mBottomSheetDetailBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        isBottomDetailSheetOpen = false;
    }

    public void updateSearchRectclerview() {
        getClikckedUtil(selectedLectureSeperateArrayList, mTimetableView.getAllTimeTableItems());
        mTimetableRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void showFailDeleteTimeTableItem() {
        ToastUtil.makeShortToast(mContext, "인터넷 환경을 확인해주세요");
    }

    @Override
    public void showDeleteSuccessTimeTableAllItem() {

    }

    @Override
    public void showFailDeleteTImeTableAllItem() {

    }

    @Override
    public void showSavedTimeTable(TimeTable timeTable) {
        mTimetableView.removeAll();
        for (TimeTableItem timeTableItem : timeTable.getTimeTableItems()) {
            timeTableItem.isSavedAtServer = true;
            mTimetableView.add(timeTableItem);
        }
        updateSearchRectclerview();
    }

    @Override
    public void showFailSavedTimeTable() {

    }

    @Override
    public void setPresenter(TimetableAnonymousPresenter presenter) {
        this.mTimetablePresenter = presenter;
    }

    private void initScrollListener() {
        mTimetableSearchRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        final Runnable r = () -> mTimetableRecyclerAdapter.notifyItemInserted(selectedLectureSeperateArrayList.size() - 1);
        handler.post(r);
        handler.postDelayed(() -> {
            selectedLectureSeperateArrayList.remove(selectedLectureSeperateArrayList.size() - 1);
            int scrollPosition = selectedLectureSeperateArrayList.size();
            mTimetableRecyclerAdapter.notifyItemRemoved(scrollPosition);
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

            mTimetableRecyclerAdapter.notifyDataSetChanged();
            isLoading = false;
        }, LOAD_TIME_MS);


    }

    @Override
    public void onClickedItemList(DepartmentCode departmentCode) {
        new Handler().postDelayed(() -> {
            mSelectedDepartmentCode = departmentCode;
            isLoading = false;
            mTimetableAddScheduleSearchEdittext.setText("");
            onClickedSearchButton();
            mTimetableSearchRecyclerview.scrollToPosition(0);
        }, 200);


    }

    @Override
    public void onClick(View view, int position) {
        mViewHolder = mTimetableSearchRecyclerview.findViewHolderForAdapterPosition(position);
        switch (view.getId()) {
            case R.id.timetable_recyclerview_item_relativelayout:
                removeAllCheckSticker();
                selectedLectureSeperateArrayList.get(position).isItemClicked = true;
                mTimetableView.addCheck(new TimeTableItem(selectedLectureArrayList.get(position)));
                mTimetableRecyclerAdapter.notifyDataSetChanged();
                break;
            case R.id.add_lecture_button:
                if (!selectedLectureSeperateArrayList.get(position).isAddButtonClicked)
                    addTimeTableItemSelctedByRecyclerview(position);
                else {
                    int index = mTimetableView.getSameIdWithLecture(selectedLectureSeperateArrayList.get(position));
                    if (index != -1)
                        mTimetablePresenter.deleteItem(index);
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
        mTimetablePresenter.getLecture(semester);
        mTimetablePresenter.getSavedTimeTableItem();
    }

    @Override
    public void updateWidget() {
        Handler handler = new Handler();
        handler.postDelayed(this::saveWidgetImage, 2000);
    }

    public void saveWidgetImage() {
        Bitmap bitmap;
        mTimetableView.setCheckStickersVisibilty(false);
        bitmap = ScreenshotUtil.getInstance().takeTimeTableScreenShot(mTimeTableScrollview, mTimeTableHeader);
        mTimetableView.setCheckStickersVisibilty(true);
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
