# Begin: Gson reflection targets

# In TimetableRepositoryImpl, we are using gson to deserialize those models
-keep class in.koreatech.koin.domain.model.timetable.response.** { *; }
# End: Gson reflection targets

# Begin: Retrofit API interfaces
-keep class in.koreatech.koin.data.api.** { *; }
# End: Retrofit API interfaces