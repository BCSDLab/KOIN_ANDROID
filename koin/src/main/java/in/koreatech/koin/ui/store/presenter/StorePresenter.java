package in.koreatech.koin.ui.store.presenter;

import in.koreatech.koin.R;
import in.koreatech.koin.data.StoreRepository;
import in.koreatech.koin.util.schedulers.SchedulerProvider;
import io.reactivex.disposables.CompositeDisposable;

public class StorePresenter {

    private final StoreContract.View storeView;
    private final StoreRepository storeRepository;
    private final CompositeDisposable compositeDisposable;
    private final SchedulerProvider schedulerProvider;

    public StorePresenter(StoreContract.View storeView, StoreRepository storeRepository, SchedulerProvider schedulerProvider) {
        this.storeView = storeView;
        this.schedulerProvider = schedulerProvider;
        this.storeRepository = storeRepository;
        compositeDisposable = new CompositeDisposable();
    }

    public void getStoreListWithCategory(String... category) {
        compositeDisposable.add(
                storeRepository.getStoreListByCategory(category)
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribe(storeView::onStoreListDataReceived,
                                throwable -> storeView.showMessage(R.string.error_network))
        );

    }

    public void getStoreList() {
        compositeDisposable.add(
                storeRepository.getStoreList()
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .doOnSubscribe(disposable -> storeView.showLoading())
                        .doFinally(storeView::hideLoading)
                        .subscribe(storeView::onStoreListDataReceived,
                                throwable -> storeView.showMessage(R.string.error_network))
        );
    }

    public void getStoreListWithStoreName(String storeName){
        compositeDisposable.add(
                storeRepository.getStoreListByName(storeName)
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribe(storeView::onStoreListDataReceived,
                                throwable -> storeView.showMessage(R.string.error_network))
        );
    }

    public void unSubscribe() {
        compositeDisposable.dispose();
    }

    public void refresh() {
        storeRepository.refresh();
    }
}