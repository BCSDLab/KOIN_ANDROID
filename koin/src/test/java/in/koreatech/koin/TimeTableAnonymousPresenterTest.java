package in.koreatech.koin;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Lecture;
import in.koreatech.koin.data.network.entity.Semester;
import in.koreatech.koin.data.network.entity.TimeTable;
import in.koreatech.koin.data.network.entity.Version;
import in.koreatech.koin.data.network.interactor.AppVersionInteractor;
import in.koreatech.koin.data.network.interactor.LectureInteractor;
import in.koreatech.koin.data.network.interactor.TimeTableInteractor;
import in.koreatech.koin.ui.timetable.presenter.TimetableAnonymousContract;
import in.koreatech.koin.ui.timetable.presenter.TimetableAnonymousPresenter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TimeTableAnonymousPresenterTest {
    @Mock
    private AppVersionInteractor appVersionInteractor;
    @Mock
    private TimeTableInteractor timeTableInteractor;
    @Mock
    private LectureInteractor lectureInteractor;
    @Mock
    private TimetableAnonymousContract.View timetableAnonymousView;
    @Mock
    TimeTable timeTable;
    @Mock
    TimeTable.TimeTableItem timeTableItem;
    @Mock
    private Version version;
    private TimetableAnonymousPresenter timetableAnonymousPresenter;
    private ApiCallback apiCallback;
    private ArrayList<Lecture> lectures;
    private ArrayList<Semester> semesters;
    private String semester;

    @Before
    public void setTimeTableAnonymousPresenter() {
        MockitoAnnotations.initMocks(this);
        lectures = new ArrayList<>();
        semesters = new ArrayList<>();
        timetableAnonymousPresenter = new TimetableAnonymousPresenter(timetableAnonymousView, timeTableInteractor, lectureInteractor, appVersionInteractor);
        semester = "2020";
    }

    @Test
    public void createPresenter_setsThePresenterToView() {
        timetableAnonymousPresenter = new TimetableAnonymousPresenter(timetableAnonymousView, timeTableInteractor, lectureInteractor, appVersionInteractor);
        verify(timetableAnonymousView).setPresenter(timetableAnonymousPresenter);
    }

    @Test
    public void errorLoadLectureFromServer_ShowsToastMessage() {
        Exception exception = new Exception();
        doAnswer(invocation -> {
                    apiCallback = invocation.getArgument(1);
                    apiCallback.onFailure(exception);
                    return null;
                }
        ).when(lectureInteractor).readLecture(anyString(), any(ApiCallback.class));
        timetableAnonymousPresenter.getLecture(semester);
        verify(timetableAnonymousView).showLoading();
        verify(timetableAnonymousView).showMessage(R.string.timetable_lecture_load_error_message);
        verify(timetableAnonymousView).hideLoading();
    }

    @Test
    public void loadLectureFromServerLoadIntoView() {
        doAnswer(invocation -> {
                    apiCallback = invocation.getArgument(1);
                    apiCallback.onSuccess(lectures);
                    return null;
                }
        ).when(lectureInteractor).readLecture(anyString(), any(ApiCallback.class));
        timetableAnonymousPresenter.getLecture(semester);
        verify(timetableAnonymousView).showLoading();
        verify(timetableAnonymousView).showLecture(lectures);
        verify(timetableAnonymousView).hideLoading();
    }

    @Test
    public void errorAddTimeTableItemAtLocal_ShowsToastMessage() {
        Exception exception = new Exception();
        doAnswer(invocation -> {
                    apiCallback = invocation.getArgument(2);
                    apiCallback.onFailure(exception);
                    return null;
                }
        ).when(timeTableInteractor).editTimeTableItemAtLocal(any(), anyString(), any(ApiCallback.class));
        timetableAnonymousPresenter.addTimeTableItem(timeTableItem, semester);
        verify(timetableAnonymousView).showLoading();
        verify(timetableAnonymousView).showFailSavedTimeTable();
        verify(timetableAnonymousView).hideLoading();
    }

    @Test
    public void AddTimeTableItemAtLocalLoadIntoView(){
        doAnswer(invocation -> {
                    apiCallback = invocation.getArgument(2);
                    apiCallback.onSuccess(timeTable);
                    return null;
                }
        ).when(timeTableInteractor).editTimeTableItemAtLocal(any(), anyString(), any(ApiCallback.class));
        timetableAnonymousPresenter.addTimeTableItem(timeTableItem, semester);
        verify(timetableAnonymousView).showLoading();
        verify(timetableAnonymousView).showSuccessAddTimeTableItem(timeTable);
        verify(timetableAnonymousView).updateWidget();
        verify(timetableAnonymousView).hideLoading();
    }

    @Test
    public void errorDeleteItemFromLocal_ShowsFailSavedTimeTable(){
        Exception exception = new Exception();
        doAnswer(invocation -> {
                    apiCallback = invocation.getArgument(2);
                    apiCallback.onFailure(exception);
                    return null;
                }
        ).when(timeTableInteractor).deleteTimeTableItemAtLocal(anyString(), anyInt(), any(ApiCallback.class));
        timetableAnonymousPresenter.deleteItem(semester, 0);
        verify(timetableAnonymousView).showLoading();
        verify(timetableAnonymousView).showFailSavedTimeTable();
        verify(timetableAnonymousView).hideLoading();
    }

    @Test
    public void deleteItemFromLocal_ShowsSuccessAddTimeTableItem(){
        doAnswer(invocation -> {
                    apiCallback = invocation.getArgument(2);
                    apiCallback.onSuccess(timeTable);
                    return null;
                }
        ).when(timeTableInteractor).deleteTimeTableItemAtLocal(anyString(), anyInt(), any(ApiCallback.class));
        timetableAnonymousPresenter.deleteItem(semester, 0);
        verify(timetableAnonymousView).showLoading();
        verify(timetableAnonymousView).showSuccessAddTimeTableItem(timeTable);
        verify(timetableAnonymousView).showDeleteSuccessTimeTableItem();
        verify(timetableAnonymousView).updateWidget();
        verify(timetableAnonymousView).hideLoading();
    }

    @Test
    public void errorGetTimeTableVersion_ShowsToastMessage(){
        Exception exception = new Exception();
        doAnswer(invocation -> {
                    apiCallback = invocation.getArgument(1);
                    apiCallback.onFailure(exception);
                    return null;
                }
        ).when(appVersionInteractor).readAppVersion(anyString(), any(ApiCallback.class));
        timetableAnonymousPresenter.getTimeTableVersion();
        verify(timetableAnonymousView).showLoading();
        verify(timetableAnonymousView).showMessage(R.string.timetable_version_load_error_message);
        verify(timetableAnonymousView).hideLoading();
    }

    @Test
    public void getTimeTableVersion_UpdateVersion_ShowsAlertDialog(){
        when(version.getVersion()).thenReturn("20201_1579774355");
        when(version.getLocalVersion()).thenReturn("20201_1579774350");
        doAnswer(invocation -> {
                    apiCallback = invocation.getArgument(1);
                    apiCallback.onSuccess(version);
                    return null;
                }
        ).when(appVersionInteractor).readAppVersion(anyString(), any(ApiCallback.class));
        timetableAnonymousPresenter.getTimeTableVersion();
        verify(timetableAnonymousView).showLoading();
        verify(timetableAnonymousView).showUpdateAlertDialog(getVersionDialogString(version.getVersion()));
        verify(timetableAnonymousView).updateSemesterCode(version.getVersion().split("_")[0]);
        verify(timetableAnonymousView).hideLoading();
    }

    @Test
    public void getTimeTableVersion_SameVersion_UpdateSemesterCode(){
        when(version.getVersion()).thenReturn("20201_1579774350");
        when(version.getLocalVersion()).thenReturn("20201_1579774350");
        doAnswer(invocation -> {
                    apiCallback = invocation.getArgument(1);
                    apiCallback.onSuccess(version);
                    return null;
                }
        ).when(appVersionInteractor).readAppVersion(anyString(), any(ApiCallback.class));
        timetableAnonymousPresenter.getTimeTableVersion();
        verify(timetableAnonymousView).showLoading();
        verify(timetableAnonymousView, never()).showUpdateAlertDialog(getVersionDialogString(version.getVersion()));
        verify(timetableAnonymousView).updateSemesterCode(version.getVersion().split("_")[0]);
        verify(timetableAnonymousView).hideLoading();
    }

    @Test
    public void errorLoadSemestersFromServer_ShowsToastMessage(){
        Exception exception = new Exception();
        doAnswer(invocation -> {
                    apiCallback = invocation.getArgument(0);
                    apiCallback.onFailure(exception);
                    return null;
                }
        ).when(timeTableInteractor).readSemesters(any(ApiCallback.class));
        timetableAnonymousPresenter.readSemesters();
        verify(timetableAnonymousView).showLoading();
        verify(timetableAnonymousView).showMessage(R.string.timetable_semester_load_error_message);
        verify(timetableAnonymousView).hideLoading();
    }

    @Test
    public void loadSemestersFromServerLoadToView(){
        doAnswer(invocation -> {
                    apiCallback = invocation.getArgument(0);
                    apiCallback.onSuccess(semesters);
                    return null;
                }
        ).when(timeTableInteractor).readSemesters(any(ApiCallback.class));
        timetableAnonymousPresenter.readSemesters();
        verify(timetableAnonymousView).showLoading();
        verify(timetableAnonymousView).getSemester(semesters);
        verify(timetableAnonymousView).hideLoading();
    }



    private String getVersionDialogString(String serverVersionCode){
        String[] timeStamp = serverVersionCode.split("_");
        StringBuilder timeStringBuilder = new StringBuilder();
        timeStringBuilder.append("강의가 업데이트 되었습니다.\n");
        timeStringBuilder.append(getDate(Long.parseLong(timeStamp[1])));
        return timeStringBuilder.toString();
    }

    private String getDate(long time) {
        java.text.SimpleDateFormat simple = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date result = new  java.util.Date(time * 1000);
        return simple.format(result);
    }

}
