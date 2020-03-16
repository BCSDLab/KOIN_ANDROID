package in.koreatech.koin.data.network.interactor;

import in.koreatech.koin.core.network.ApiCallback;

public interface LectureInteractor {
    void readLecture(String semester, final ApiCallback apiCallback);
}
