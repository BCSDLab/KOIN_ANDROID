package in.koreatech.koin.ui.store.presenter;

import android.util.Log;

import in.koreatech.koin.R;
import in.koreatech.koin.data.StoreRepository;
import in.koreatech.koin.ui.store.StoreDetailViewModel;
import in.koreatech.koin.util.schedulers.SchedulerProvider;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

public class StoreDetailPresenter {

    private final StoreDetailContract.View storeView;
    private final StoreRepository storeRepository;
    private final CompositeDisposable compositeDisposable;
    private final SchedulerProvider schedulerProvider;

    public StoreDetailPresenter(StoreDetailContract.View storeView, StoreRepository storeRepository, SchedulerProvider schedulerProvider) {
        this.storeView = storeView;
        this.schedulerProvider = schedulerProvider;
        this.storeRepository = storeRepository;
        compositeDisposable = new CompositeDisposable();
    }

    public void getStore(int storeID) {
        storeView.showLoading();
        compositeDisposable.add(
                storeRepository.getStoreDetail(storeID)
                        .doFinally(storeView::hideLoading)
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribe(storeView::onStoreDataReceived,
                                throwable -> storeView.showMessage(R.string.error_network))
        );
    }


    public void start(int storeID, int categoryItemCount, int currentStoreID, String... category) {
        storeView.showLoading();
        compositeDisposable.add(
                Single.zip(
                        storeRepository.getStoreDetail(storeID),
                        storeRepository.getRandomStoreListByCategory(categoryItemCount, currentStoreID, category),
                        StoreDetailViewModel::new
                ).doFinally(storeView::hideLoading)
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribe(storeDetailViewModel -> {
                                    storeView.onStoreDataReceived(storeDetailViewModel.getStore());
                                    storeView.onRandomStoreListReceived(storeDetailViewModel.getRandomStoreList());
                                },
                                throwable -> {
                                    Log.e("StoreDetailPresenter", "error: ", throwable);
                                    storeView.showMessage(R.string.error_network);
                                }));
    }

    public void unSubscribe() {
        compositeDisposable.dispose();
    }
}