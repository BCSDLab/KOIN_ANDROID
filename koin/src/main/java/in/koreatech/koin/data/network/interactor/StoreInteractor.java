package in.koreatech.koin.data.network.interactor;

import in.koreatech.koin.core.network.ApiCallback; /**
 * Created by hyerim on 2018. 8. 12....
 */
public interface StoreInteractor {
    void readStoreList(ApiCallback apiCallback);
    void readStore(int id, ApiCallback apiCallback);


}
