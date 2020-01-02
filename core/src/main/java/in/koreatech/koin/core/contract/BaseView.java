package in.koreatech.koin.core.contract;

/**
 * Created by hyerim on 2018. 5. 31....
 */
public interface BaseView<T extends BasePresenter> {

    void setPresenter(T presenter);

}
