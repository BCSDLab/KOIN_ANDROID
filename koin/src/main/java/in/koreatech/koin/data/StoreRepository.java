package in.koreatech.koin.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import in.koreatech.koin.data.network.entity.Store;
import in.koreatech.koin.util.TimeUtil;
import io.reactivex.Observable;
import io.reactivex.Single;

public class StoreRepository implements StoreDataSource {
    @Nullable
    private static StoreRepository INSTANCE = null;
    @NonNull
    private final StoreDataSource storeRemoteDataSource;
    @Nullable
    private List<Store> storeList;
    @VisibleForTesting
    private boolean cacheIsDirty = false;


    private StoreRepository(@NotNull StoreDataSource storeRemoteDataSource) {
        this.storeRemoteDataSource = storeRemoteDataSource;
    }

    public static StoreRepository getInstance(@NonNull StoreDataSource storeRemoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new StoreRepository(storeRemoteDataSource);
        }
        return INSTANCE;
    }


    @Override
    public Single<List<Store>> getStoreList() {
        if (cacheIsDirty || storeList == null) {
            storeList = new ArrayList<>();
            cacheIsDirty = false;
            return storeRemoteDataSource.getStoreList()
                    .doOnSuccess(stores -> storeList.addAll(stores))
                    .toObservable()
                    .flatMap(Observable::fromIterable)
                    .toSortedList(this::sortStore);
        }
        return Single.just(storeList)
                .toObservable()
                .flatMap(Observable::fromIterable)
                .toSortedList(this::sortStore);
    }

    @Override
    public Single<List<Store>> getStoreListByCategory(String... category) {
        if (cacheIsDirty || storeList == null) {
            storeList = new ArrayList<>();
            cacheIsDirty = false;
            return storeRemoteDataSource.getStoreList()
                    .doOnSuccess(stores -> storeList.addAll(stores))
                    .toObservable()
                    .flatMap(Observable::fromIterable)
                    .filter(store -> {
                        for (String item : category) {
                            if (store.getCategory().equals(item))
                                return true;
                        }
                        return false;
                    }).toSortedList(this::sortStore);
        }

        return Single.just(storeList)
                .toObservable()
                .flatMap(Observable::fromIterable)
                .filter(store -> {
                    for (String item : category) {
                        if (store.getCategory().equals(item))
                            return true;
                    }
                    return false;
                }).toSortedList(this::sortStore);
    }

    @Override
    public Single<List<Store>> getStoreListByName(String name) {
        if (cacheIsDirty || storeList == null) {
            storeList = new ArrayList<>();
            cacheIsDirty = false;
            return storeRemoteDataSource.getStoreList()
                    .doOnSuccess(stores -> storeList.addAll(stores))
                    .toObservable()
                    .flatMap(Observable::fromIterable)
                    .filter(store -> store.getName().contains(name))
                    .toSortedList((o1, o2) -> o1.getName().compareTo(o2.getName()));
        }

        return Single.just(storeList)
                .toObservable()
                .flatMap(Observable::fromIterable)
                .filter(store -> store.getName().contains(name))
                .toSortedList(this::sortStore);
    }

    @Override
    public Single<List<Store>> getRandomStoreListByCategory(int count, int currentStoreID, String... category) {
        if (cacheIsDirty || storeList == null) {
            storeList = new ArrayList<>();
            cacheIsDirty = false;
            return storeRemoteDataSource.getStoreList()
                    .doOnSuccess(stores -> storeList.addAll(stores))
                    .toObservable()
                    .flatMap(Observable::fromIterable)
                    .filter(store -> {
                        if (!TimeUtil.isBetweenCurrentTime(store.getOpenTime(), store.getCloseTime())) {
                            return false;
                        }
                        for (String item : category) {
                            if (store.getCategory().equals(item) && store.getUid() != currentStoreID)
                                return true;
                        }
                        return false;
                    })
                    .toList()
                    .map(store -> {
                        Collections.shuffle(store);
                        return store;
                    })
                    .toObservable()
                    .flatMap(Observable::fromIterable)
                    .take(count)
                    .toList();
        }

        return Single.just(storeList)
                .toObservable()
                .flatMap(Observable::fromIterable)
                .filter(store -> {
                    if (!TimeUtil.isBetweenCurrentTime(store.getOpenTime(), store.getCloseTime())) {
                        return false;
                    }
                    for (String item : category) {
                        if (store.getCategory().equals(item) && store.getUid() != currentStoreID)
                            return true;
                    }
                    return false;
                })
                .toList()
                .map(store -> {
                    Collections.shuffle(store);
                    return store;
                })
                .toObservable()
                .flatMap(Observable::fromIterable)
                .take(count)
                .toList();
    }

    @Override
    public Single<Store> getStoreDetail(int storeID) {
        return storeRemoteDataSource.getStoreDetail(storeID);
    }

    @Override
    public void refresh() {
        cacheIsDirty = true;
    }

    private int sortStore(Store o1, Store o2) {
        boolean isFirstStoreOpened = TimeUtil.isBetweenCurrentTime(o1.getOpenTime(), o1.getCloseTime());
        boolean isSecondStoredOpened = TimeUtil.isBetweenCurrentTime(o2.getOpenTime(), o2.getCloseTime());
        if (isFirstStoreOpened && isSecondStoredOpened) {
            return o1.getName().compareTo(o2.getName());
        } else if (!isFirstStoreOpened && isSecondStoredOpened) {
            return 1;
        } else if (isFirstStoreOpened && !isSecondStoredOpened) {
            return -1;
        } else {
            return o1.getName().compareTo(o2.getName());
        }

    }
}
