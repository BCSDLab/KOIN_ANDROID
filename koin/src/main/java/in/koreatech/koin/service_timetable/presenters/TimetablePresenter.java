package in.koreatech.koin.service_timetable.presenters;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


import android.text.format.DateFormat;

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
import in.koreatech.koin.service_timetable.Contracts.TimetableContract;


public class TimetablePresenter implements BasePresenter {
    public static final String TAG = TimetablePresenter.class.getName();
    public static final String TIMETABLE_SERVICE_CODE = "timetable";

    private AppVersionInteractor mAppVersionInteractor;
    private TimeTableInteractor mTimeTableInteractor;
    private LectureInteractor mLectureInteractor;
    private TimetableContract.View mTimeTableView;
    private int deleteId;

    public TimetablePresenter(TimetableContract.View mTimeTableView) {
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

    final ApiCallback addTimetableApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            if (!(object instanceof TimeTable))
                return;
            TimeTable timeTable = (TimeTable) object;
            mTimeTableView.showSuccessAddTimeTableItem(timeTable);
            mTimeTableView.updateWidget();
            mTimeTableView.hideLoading();

        }

        @Override
        public void onFailure(Throwable throwable) {
            mTimeTableView.showFailAddTimeTableItem();
            mTimeTableView.hideLoading();
        }
    };

    final ApiCallback getSavedTimetableApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            if (!(object instanceof TimeTable))
                return;
            TimeTable timeTable = (TimeTable) object;
            mTimeTableView.showSavedTimeTable(timeTable);
            mTimeTableView.updateWidget();
            mTimeTableView.hideLoading();

        }

        @Override
        public void onFailure(Throwable throwable) {
            mTimeTableView.showFailSavedTimeTable();
            mTimeTableView.hideLoading();
        }
    };

    final ApiCallback deleteTimetableItemApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            mTimeTableView.showDeleteSuccessTimeTableItem(deleteId);
            mTimeTableView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            mTimeTableView.showFailSavedTimeTable();
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
            } else
                return;

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
        JsonObject timetableJson = SaveManager.saveTimeTableItemAsJson(timeTableItem, semester);
        mTimeTableInteractor.createTimeTable(addTimetableApiCallback, timetableJson);
    }

    public void getSavedTimeTableItem(String semester) {
        mTimeTableView.showLoading();
        mTimeTableInteractor.readTimeTable(getSavedTimetableApiCallback, semester);
    }

    public void deleteItem(int id) {
        mTimeTableView.showLoading();
        deleteId = id;
        mTimeTableInteractor.deleteTimeTable(deleteTimetableItemApiCallback, id);
    }

    public void getTimetTableVersion() {
        mTimeTableView.showLoading();
        mAppVersionInteractor.readAppVersion(TIMETABLE_SERVICE_CODE, readTableVersionApiCallback);
    }

    public void readSemesters() {
        mTimeTableView.showLoading();
        mTimeTableInteractor.readSemesters(readSemestersApiCallback);
    }


}
