package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.dining.Dining

class FakeDiningRepository : DiningRepository {
    private var fakeDinings: List<Dining> = emptyList()
    private var shouldThrow: Boolean = false
    var lastRequestedDate: String? = null
        private set

    fun setFakeDinings(dinings: List<Dining>) {
        fakeDinings = dinings
    }

    fun setShouldThrow(shouldThrow: Boolean) {
        this.shouldThrow = shouldThrow
    }

    override suspend fun getDining(date: String): List<Dining> {
        lastRequestedDate = date
        if (shouldThrow) throw RuntimeException("Network error")
        return fakeDinings
    }
}
