package in.koreatech.koin.core.contract;


public interface BaseView<T extends BasePresenter> {

    void setPresenter(T presenter);

}
