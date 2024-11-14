package `in`.koreatech.koin.feature.timetable


//class FakeTimetableRepository() : TimetableRepository {
//    override fun getSemesters(): Flow<List<String>> = flow {
//        delay(400)
//        emit(listOf(SampleTimetable.semester))
//    }
//
//    override fun getLectures(semesterDate: String): Flow<List<Lecture>> = flow {
//        delay(600)
//        emit(listOf(SampleTimetable.lecture))
//    }
//
//    override fun getTimetableFrames(semester: String): Flow<List<TimetableFrame>> {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun getTimetableLectures(timetableFrameId: Int): Result<TimetableLectures> {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun getTimetableLectures(semester: String): Result<TimetableLectures> {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun putTimetableLectures(lectures: TimetableLecturesQuery): TimetableLectures {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun putTimetableLectures(
//        key: String,
//        value: TimetableLectures
//    ): Result<TimetableLectures> {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun putTimetableFrame(id: Int, frame: TimetableFrameQuery): TimetableFrame {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun postTimetableLectures(lectures: LecturesQuery): Result<TimetableLectures> {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun postTimetableFrame(frame: TimetableFrameCreateQuery): TimetableFrame {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun deleteTimetableFrame() {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun deleteTimetableLecture(id: Int) {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun deleteAllTimetableFrame() {
//        TODO("Not yet implemented")
//    }
//
//}