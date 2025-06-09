package `in`.koreatech.koin.feature.timetable

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.domain.model.timetable.response.TimetableLecture
import org.junit.Rule
import org.junit.Test

val FOLDABLE_DEVICE = DeviceConfig(
    screenWidth = 1800,
    screenHeight = 2200,
    xdpi = 480,
    ydpi = 480,
    fontScale = 1.0f,
    softButtons = false
)

class TimetableTestPIXEL6PRO {
    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_6_PRO.copy(softButtons = false)
    )

    @Test
    fun snapshot_timetable_FOLDABLE() {
        paparazzi.snapshot(name = "store_detail_FOLDABLE") {
            KoinTheme {
                TimetableLecture(
                    id = 1,
                    lectureId = 1,
                    code = "HRD011",
                    classTitle = "직업능력개발훈련평가",
                    professor = "우성민",
                    grades = "2",
                    lectureClass = "01",
                    regularNumber = "40",
                    department = "HRD학과",
                    target = "전기3",
                    classInfos = emptyList(),
                    designScore = "0"
                )
            }
        }
    }
}

class TimetableTestPIXEL4 {
    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_4.copy(softButtons = false)
    )

    @Test
    fun snapshot_timetable_FOLDABLE() {
        paparazzi.snapshot(name = "store_detail_FOLDABLE") {
            KoinTheme {
                TimetableLecture(
                    id = 1,
                    lectureId = 1,
                    code = "HRD011",
                    classTitle = "직업능력개발훈련평가",
                    professor = "우성민",
                    grades = "2",
                    lectureClass = "01",
                    regularNumber = "40",
                    department = "HRD학과",
                    target = "전기3",
                    classInfos = emptyList(),
                    designScore = "0"
                )
            }
        }
    }
}

class TimetableTestFoldable {
    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = FOLDABLE_DEVICE.copy(softButtons = false)
    )

    @Test
    fun snapshot_timetable_FOLDABLE() {
        paparazzi.snapshot(name = "store_detail_FOLDABLE") {
            KoinTheme {
                TimetableLecture(
                    id = 1,
                    lectureId = 1,
                    code = "HRD011",
                    classTitle = "직업능력개발훈련평가",
                    professor = "우성민",
                    grades = "2",
                    lectureClass = "01",
                    regularNumber = "40",
                    department = "HRD학과",
                    target = "전기3",
                    classInfos = emptyList(),
                    designScore = "0"
                )
            }
        }
    }
}
