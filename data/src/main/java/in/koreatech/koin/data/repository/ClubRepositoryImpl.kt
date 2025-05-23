package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.source.remote.ClubRemoteDataSource
import `in`.koreatech.koin.domain.repository.ClubRepository
import javax.inject.Inject

class ClubRepositoryImpl @Inject constructor(
    private val clubRemoteDataSource: ClubRemoteDataSource
) : ClubRepository
