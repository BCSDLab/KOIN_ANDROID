package `in`.koreatech.koin.data.di.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import `in`.koreatech.koin.data.repository.BusRepositoryImpl
import `in`.koreatech.koin.data.repository.CallvanRepositoryImpl
import `in`.koreatech.koin.data.repository.DepartmentRepositoryImpl
import `in`.koreatech.koin.data.repository.RecruitmentRepositoryImpl
import `in`.koreatech.koin.data.repository.TeamRecruitmentRepositoryImpl
import `in`.koreatech.koin.data.repository.TimetableRepositoryImpl
import `in`.koreatech.koin.data.repository.WeatherRepositoryImpl
import `in`.koreatech.koin.data.repository.firebase.messaging.FirebaseMessagingRepositoryImpl
import `in`.koreatech.koin.domain.repository.BusRepository
import `in`.koreatech.koin.domain.repository.CallvanRepository
import `in`.koreatech.koin.domain.repository.DepartmentRepository
import `in`.koreatech.koin.domain.repository.RecruitmentRepository
import `in`.koreatech.koin.domain.repository.TeamRecruitmentRepository
import `in`.koreatech.koin.domain.repository.TimetableRepository
import `in`.koreatech.koin.domain.repository.WeatherRepository
import `in`.koreatech.koin.domain.repository.firebase.messaging.FirebaseMessagingRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BindsRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindsFirebaseMessagingRepository(
        firebaseMessagingRepositoryImpl: FirebaseMessagingRepositoryImpl
    ): FirebaseMessagingRepository

    @Binds
    @Singleton
    abstract fun bindsTimetableRepository(timetableRepositoryImpl: TimetableRepositoryImpl): TimetableRepository

    @Binds
    @Singleton
    abstract fun bindsBusV2Repository(busV2RepositoryImpl: BusRepositoryImpl): BusRepository

    @Binds
    @Singleton
    abstract fun bindsCallvanRepository(callvanRepositoryImpl: CallvanRepositoryImpl): CallvanRepository

    @Binds
    @Singleton
    abstract fun bindsWeatherRepository(weatherRepositoryImpl: WeatherRepositoryImpl): WeatherRepository

    @Binds
    @Singleton
    abstract fun bindsDepartmentRepository(departmentRepositoryImpl: DepartmentRepositoryImpl): DepartmentRepository

    @Binds
    @Singleton
    abstract fun bindsRecruitmentRepository(recruitmentRepositoryImpl: RecruitmentRepositoryImpl): RecruitmentRepository
}
