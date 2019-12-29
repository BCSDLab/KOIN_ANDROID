package in.koreatech.koin.core.networks.interactors;

import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.entity.CallvanRoom;

/**
 * Created by hyerim on 2018. 6. 17....
 */
public interface CallvanInteractor {
    //Company
    void readCompanyList(final ApiCallback apiCallback);

    void readCompany(int uid, final ApiCallback apiCallback);

    //Room
    void readRoomList(final ApiCallback apiCallback);

    void createRoom(CallvanRoom room, final ApiCallback apiCallback);

    void readRoom(int roomUid, final ApiCallback apiCallback);

    void updateRoom(CallvanRoom room, final ApiCallback apiCallback);

    void deleteRoom(int roomUid, final ApiCallback apiCallback);

    //Room participant
    void createParticipant(int roomUid, final ApiCallback apiCallback);

    void deleteParticipant(int roomUid, final ApiCallback apiCallback);
}
