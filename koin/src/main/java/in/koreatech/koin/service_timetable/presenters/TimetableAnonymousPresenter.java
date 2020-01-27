package in.koreatech.koin.service_timetable.presenters;

import android.text.format.DateFormat;
import android.util.Log;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

import in.koreatech.koin.core.bases.BasePresenter;
import in.koreatech.koin.core.helpers.TimeTableSharedPreferencesHelper;
import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.entity.Lecture;
import in.koreatech.koin.core.networks.entity.Semester;
import in.koreatech.koin.core.networks.entity.TimeTable;
import in.koreatech.koin.core.networks.entity.TimeTable.TimeTableItem;
import in.koreatech.koin.core.networks.entity.Version;
import in.koreatech.koin.core.networks.interactors.AppVersionInteractor;
import in.koreatech.koin.core.networks.interactors.AppVersionRestInteractor;
import in.koreatech.koin.core.networks.interactors.LectureInteractor;
import in.koreatech.koin.core.networks.interactors.LectureRestInteractor;
import in.koreatech.koin.core.networks.interactors.TimeTableInteractor;
import in.koreatech.koin.core.networks.interactors.TimeTableRestInteractor;
import in.koreatech.koin.core.util.timetable.SaveManager;
import in.koreatech.koin.service_timetable.Contracts.TimetableAnonymousContract;
import in.koreatech.koin.service_timetable.Contracts.TimetableContract;


public class TimetableAnonymousPresenter implements BasePresenter {
    public static final String TAG = "TimetableAnonymous";
    public static final String TIMETABLE_SERVICE_CODE = "timetable";

    private AppVersionInteractor mAppVersionInteractor;
    private TimeTableInteractor mTimeTableInteractor;
    private LectureInteractor mLectureInteractor;
    private TimetableAnonymousContract.View mTimeTableView;
    private int deleteId;

    public TimetableAnonymousPresenter(TimetableAnonymousContract.View mTimeTableView) {
        this.mTimeTableView = mTimeTableView;
        this.mAppVersionInteractor = new AppVersionRestInteractor();
        this.mTimeTableInteractor = new TimeTableRestInteractor();
        this.mLectureInteractor = new LectureRestInteractor();
        //
    }

    final ApiCallback lectureApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            ArrayList<Lecture> lecture = (ArrayList<Lecture>) object;
            mTimeTableView.showLecture(lecture);
            mTimeTableView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            mTimeTableView.showFailMessage("리스트를 받아오지 못했습니다.");
            mTimeTableView.hideLoading();
        }
    };


    final ApiCallback readTableVersionApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            String versionCode = TimeTableSharedPreferencesHelper.getInstance().loadTimeTableVersion();
            String serverVersionCode = "";
            if (object == null) return;
            Version version = (Version) object;
            if (version.getVersion() != null) {
                serverVersionCode = version.getVersion();
            } else {
                mTimeTableView.hideLoading();
                return;
            }

            if (versionCode == null || !versionCode.equals(serverVersionCode)) {
                String[] timeStamp = serverVersionCode.split("_");
                StringBuilder timeStringBuilder = new StringBuilder();
                timeStringBuilder.append("강의가 업데이트 되었습니다.\n");
                timeStringBuilder.append(getDate(Long.parseLong(timeStamp[1])));
                mTimeTableView.showUpdateAlertDialog(timeStringBuilder.toString());
            }
            if (version.getVersion() != null) {
                TimeTableSharedPreferencesHelper.getInstance().saveTimeTableVersion(serverVersionCode);
                mTimeTableView.updateSemesterCode(serverVersionCode.split("_")[0]);
            }
            mTimeTableView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            mTimeTableView.hideLoading();
        }
    };

    final ApiCallback readSemestersApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            ArrayList<Semester> semesters = (ArrayList<Semester>) object;
            mTimeTableView.getSemester(semesters);

        }

        @Override
        public void onFailure(Throwable throwable) {
            mTimeTableView.showFailMessage("정보를 불러오지 못했습니다.");
            mTimeTableView.hideLoading();
        }
    };

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.KOREA);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("yyyy-MM-dd HH:mm:ss", cal).toString();
        return date;
    }

    public void getLecture(String semester) {
        mTimeTableView.showLoading();
        mLectureInteractor.readLecture(semester, lectureApiCallback);
    }

    public void addTimeTableItem(TimeTableItem timeTableItem, String semester) {
        mTimeTableView.showLoading();
        TimeTable savedTimeTable = new TimeTable();
        String timeTable = TimeTableSharedPreferencesHelper.getInstance().loadSaveTimeTable(semester);
        if (timeTable != null) {
            try {
                savedTimeTable = SaveManager.loadTimeTable(timeTable);
            } catch (Exception e) {
                mTimeTableView.showFailSavedTimeTable();
            }
        }

        if (savedTimeTable != null && savedTimeTable.getTimeTableItems() != null) {
            int id = TimeTableSharedPreferencesHelper.getInstance().loadSaveTimeTableBlockID();
            if (id == Integer.MAX_VALUE) id = -1;
            timeTableItem.setId(++id);
            TimeTableSharedPreferencesHelper.getInstance().saveTimeTableBlockID(id);
            savedTimeTable.addTimeTableItem(timeTableItem);
            TimeTableSharedPreferencesHelper.getInstance().saveTimeTable(semester, SaveManager.saveTimeTable(savedTimeTable, semester));
        }


        mTimeTableView.showSuccessAddTimeTableItem(savedTimeTable);
        mTimeTableView.updateWidget();
        mTimeTableView.hideLoading();
    }

    public void getSavedTimeTableItem(String semester) {
        mTimeTableView.showLoading();
        String timeTable = TimeTableSharedPreferencesHelper.getInstance().loadSaveTimeTable(semester);
        if (timeTable != null) {
            try {
                TimeTable savedTimeTable = SaveManager.loadTimeTable(timeTable);
                if (savedTimeTable != null) {
                    mTimeTableView.showSavedTimeTable(savedTimeTable);
                }
            } catch (Exception e) {
                Log.e(TAG, "getSavedTimeTableItem: ", e);
                mTimeTableView.showFailSavedTimeTable();
            }
        } else
            mTimeTableView.showFailSavedTimeTable();
        mTimeTableView.updateWidget();
        mTimeTableView.hideLoading();
    }

    public TimeTable getSavedTimeTable(String semester) {
        TimeTable savedTimeTable = new TimeTable();
        String timeTable = TimeTableSharedPreferencesHelper.getInstance().loadSaveTimeTable(semester);
        if (timeTable != null) {
            try {
                savedTimeTable = SaveManager.loadTimeTable(timeTable);
            } catch (Exception e) {
                Log.e(TAG, "getSavedTimeTableItem: ", e);
            }
        }
        return savedTimeTable;
    }

    public void deleteItem(String semester, int id) {
        mTimeTableView.showLoading();
        deleteId = id;
        TimeTable timeTable = getSavedTimeTable(semester);
        TimeTable saveTable = new TimeTable();
        if (timeTable == null) return;
        saveTable.setSemester(timeTable.semester);
        for (TimeTableItem timeTableItem : timeTable.getTimeTableItems()) {
            if (timeTableItem.getId() != id) {
                saveTable.addTimeTableItem(timeTableItem);
            }
        }
        TimeTableSharedPreferencesHelper.getInstance().saveTimeTable(semester, SaveManager.saveTimeTable(saveTable, saveTable.semester));
        mTimeTableView.showDeleteSuccessTimeTableItem(deleteId);
        mTimeTableView.hideLoading();
    }


    public void getTimeTableVersion() {
        mTimeTableView.showLoading();
        mAppVersionInteractor.readAppVersion(TIMETABLE_SERVICE_CODE, readTableVersionApiCallback);
    }

    public void readSemesters() {
        mTimeTableView.showLoading();
        mTimeTableInteractor.readSemesters(readSemestersApiCallback);
    }
}
