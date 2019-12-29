package in.koreatech.koin.core.networks.interactors;

import in.koreatech.koin.core.networks.ApiCallback;

public interface LectureInteractor {
    void readLecture(String semester, final ApiCallback apiCallback);
}
