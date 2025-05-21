package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.ClubApi
import `in`.koreatech.koin.data.api.auth.ClubAuthApi
import javax.inject.Inject

class ClubRemoteDataSource @Inject constructor(
    private val clubApi: ClubApi,
    private val clubAuthApi: ClubAuthApi
)
