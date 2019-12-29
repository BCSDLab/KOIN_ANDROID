package in.koreatech.koin.core.networks;

/**
 * Created by hyerim on 2018. 5. 31....
 */
public interface ApiCallback {
    void onSuccess(Object object);
    void onFailure(Throwable throwable);

}
