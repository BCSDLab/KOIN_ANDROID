package in.koreatech.koin.ui.timetable.presenter;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


import android.text.format.DateFormat;

import in.koreatech.koin.core.contract.BasePresenter;
import in.koreatech.koin.data.sharedpreference.TimeTableSharedPreferencesHelper;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Lecture;
import in.koreatech.koin.data.network.entity.TimeTable;
import in.koreatech.koin.data.network.entity.TimeTable.TimeTableItem;
import in.koreatech.koin.data.network.entity.Version;
import in.koreatech.koin.data.network.interactor.AppVersionInteractor;
import in.koreatech.koin.data.network.interactor.AppVersionRestInteractor;
import in.koreatech.koin.data.network.interactor.LectureInteractor;
import in.koreatech.koin.data.network.interactor.LectureRestInteractor;
import in.koreatech.koin.data.network.interactor.TimeTableInteractor;
import in.koreatech.koin.data.network.interactor.TimeTableRestInteractor;
import in.koreatech.koin.util.SaveManager;


public class TimetablePresenter implements BasePresenter {
    public static final String TAG = TimetablePresenter.class.getName();
    public static final String TIMETABLE_SERVICE_CODE = "timetable";

    private AppVersionInteractor appVersionInteractor;
    private TimeTableInteractor mTimeTableInteractor;
    private LectureInteractor mLectureInteractor;
    private TimetableContract.View mTimeTableView;
    private int deleteId;

    public TimetablePresenter(TimetableContract.View mTimeTableView) {
        this.mTimeTableView = mTimeTableView;
        this.appVersionInteractor = new AppVersionRestInteractor();
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

    public void addTimeTableItem(TimeTableItem timeTableItem, String semster) {
        mTimeTableView.showLoading();
        JsonObject timetableJson = SaveManager.saveTimeTableItemAsJson(timeTableItem, semster);
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
        appVersionInteractor.readAppVersion(TIMETABLE_SERVICE_CODE, readTableVersionApiCallback);
    }


}
