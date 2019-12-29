package in.koreatech.koin.service_timetable.Contracts;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import in.koreatech.koin.core.bases.BaseView;
import in.koreatech.koin.core.networks.entity.Lecture;
import in.koreatech.koin.core.networks.entity.TimeTable;
import in.koreatech.koin.service_timetable.presenters.TimetablePresenter;

public interface TimetableContract {
    interface View extends BaseView<TimetablePresenter> {
        void showLoading();

        void hideLoading();

        void showLecture(ArrayList<Lecture> lecture);

        void showFailMessage(String message);

        void showSuccessCreateTimeTable();

        void showFailCreateTimeTable();

        void showSuccessAddTimeTableItem(TimeTable timeTable);

        void showFailAddTimeTableItem();

        void showSuccessEditTimeTable();

        void showFailEditTimeTable();

        void showDeleteSuccessTimeTableItem(int id);

        void showFailDeleteTimeTableItem();

        void showDeleteSuccessTimeTableAllItem();

        void showFailDeleteTImeTableAllItem();

        void showSavedTimeTable(TimeTable timeTable);

        void showFailSavedTimeTable();

        void updateWidget();

        //동민 추가
        void showUpdateAlertDialog(String message);

        void updateSemesterCode(String semester);
    }

}
