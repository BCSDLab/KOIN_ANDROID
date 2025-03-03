package `in`.koreatech.koin.data.source.local

import `in`.koreatech.koin.data.source.datastore.BusDataStore
import javax.inject.Inject

class BusLocalDataSource
    @Inject
    constructor(
        private val busDataStore: BusDataStore,
    ) {
        suspend fun getLastShownNoticeId(): Int {
            return busDataStore.getLastShownNoticeId()
        }

        suspend fun saveLastShownNoticeId(id: Int) {
            busDataStore.saveLastShownNoticeId(id)
        }
    }
