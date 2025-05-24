package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toClubCategories
import `in`.koreatech.koin.data.source.remote.ClubRemoteDataSource
import `in`.koreatech.koin.domain.model.club.ClubCategories
import `in`.koreatech.koin.domain.repository.ClubRepository
import javax.inject.Inject

class ClubRepositoryImpl @Inject constructor(
    private val clubRemoteDataSource: ClubRemoteDataSource
) : ClubRepository {
    override suspend fun getClubsCategories(): Result<ClubCategories> {
        return runCatching {
            clubRemoteDataSource.getClubsCategories().toClubCategories()
        }
    }
}
