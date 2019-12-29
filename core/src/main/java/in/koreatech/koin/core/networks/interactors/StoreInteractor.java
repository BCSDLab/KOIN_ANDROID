package in.koreatech.koin.core.networks.interactors;

import in.koreatech.koin.core.networks.ApiCallback; /**
 * Created by hyerim on 2018. 8. 12....
 */
public interface StoreInteractor {
    void readStoreList(ApiCallback apiCallback);
    void readStore(int id, ApiCallback apiCallback);


}
