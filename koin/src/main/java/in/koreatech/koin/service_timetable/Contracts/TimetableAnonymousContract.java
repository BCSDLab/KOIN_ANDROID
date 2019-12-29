package in.koreatech.koin.service_timetable.Contracts;

import java.util.ArrayList;

import in.koreatech.koin.core.bases.BaseView;
import in.koreatech.koin.core.networks.entity.Lecture;
import in.koreatech.koin.core.networks.entity.TimeTable;
import in.koreatech.koin.service_timetable.presenters.TimetableAnonymousPresenter;
import in.koreatech.koin.service_timetable.presenters.TimetablePresenter;

public interface TimetableAnonymousContract {
    interface View extends BaseView<TimetableAnonymousPresenter> {
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

        void showUpdateAlertDialog(String message);

        void updateSemesterCode(String semester);

        void updateWidget();
    }

}
