package `in`.koreatech.koin.domain.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import org.junit.Assert.assertEquals
import org.junit.Test

class TimeUtilTest {
    @Test
    fun `여러 스레드에서 동시에 변환해도 예외가 발생하지 않는다`() {
        val threadCount = 8
        val iterationCount = 2_000
        val targetDate = Date.from(LocalDate.of(2026, 10, 27).atStartOfDay(ZoneId.systemDefault()).toInstant())
        val executor = Executors.newFixedThreadPool(threadCount)
        val startLatch = CountDownLatch(1)

        try {
            val futures = (0 until threadCount).map { index ->
                executor.submit {
                    startLatch.await()
                    repeat(iterationCount) {
                        if (index % 2 == 0) {
                            assertEquals("261027", TimeUtil.dateFormatToYYMMDD(TimeUtil.stringToDateYYMMDD("261027")))
                        } else {
                            assertEquals("2026-10-27", TimeUtil.dateFormatToYYYYMMDD(targetDate))
                        }
                    }
                }
            }

            startLatch.countDown()
            futures.forEach { it.get() }
        } finally {
            executor.shutdownNow()
            executor.awaitTermination(5, TimeUnit.SECONDS)
        }
    }

    @Test
    fun `위젯 날짜 포맷과 운영시간 비교를 동시에 호출해도 예외가 발생하지 않는다`() {
        // DiningAppWidget.setDate (Main) -> dateFormatToMMDDEE 와
        // HomeViewModel reduce (Default) -> DiningUtil.getCurrentType -> compareWithCurrentTime 경합 재현
        val threadCount = 8
        val iterationCount = 500
        val date = Date.from(LocalDate.of(2026, 10, 27).atStartOfDay(ZoneId.systemDefault()).toInstant())
        val executor = Executors.newFixedThreadPool(threadCount)
        val startLatch = CountDownLatch(1)

        try {
            val futures = (0 until threadCount).map { index ->
                executor.submit {
                    startLatch.await()
                    repeat(iterationCount) {
                        if (index % 2 == 0) {
                            assertEquals("10/27 (화)", TimeUtil.dateFormatToMMDDEE(date))
                        } else {
                            assertEquals(true, TimeUtil.compareWithCurrentTime("23:59") != 0L)
                        }
                    }
                }
            }

            startLatch.countDown()
            futures.forEach { it.get() }
        } finally {
            executor.shutdownNow()
            executor.awaitTermination(5, TimeUnit.SECONDS)
        }
    }

    @Test
    fun `yyMMdd 문자열을 Date로 변환한다`() {
        val expected = Date.from(LocalDate.of(2026, 10, 27).atStartOfDay(ZoneId.systemDefault()).toInstant())

        assertEquals(expected, TimeUtil.stringToDateYYMMDD("261027"))
    }

    @Test
    fun `yyyy-MM-dd 문자열과 시간이 붙은 문자열을 같은 날짜로 변환한다`() {
        val expected = Date.from(LocalDate.of(2018, 3, 23).atStartOfDay(ZoneId.systemDefault()).toInstant())

        assertEquals(expected, TimeUtil.stringToDateYYYYMMDD("2018-03-23"))
        assertEquals(expected, TimeUtil.stringToDateYYYYMMDD("2018-03-23 20:25:24"))
        assertEquals(expected, TimeUtil.stringToDateYYYYMMDD("2018-03-23T20:25:24"))
    }

    @Test
    fun `yyyy-MM-dd 변환은 실패하면 yyMMdd로 다시 시도한다`() {
        val expected = Date.from(LocalDate.of(2026, 10, 27).atStartOfDay(ZoneId.systemDefault()).toInstant())

        assertEquals(expected, TimeUtil.stringToDateYYYYMMDD("261027"))
    }

    @Test
    fun `날짜 차이는 자정 기준으로 계산한다`() {
        val date1 = Date.from(LocalDateTime.of(2026, 10, 29, 23, 59).atZone(ZoneId.systemDefault()).toInstant())
        val date2 = Date.from(LocalDateTime.of(2026, 10, 27, 0, 1).atZone(ZoneId.systemDefault()).toInstant())

        assertEquals(2, TimeUtil.getDateDifferenceInDays(date1, date2))
    }

    @Test
    fun `식단 날짜 문자열을 포맷한다`() {
        val date = Date.from(LocalDate.of(2026, 10, 27).atStartOfDay(ZoneId.systemDefault()).toInstant())

        assertEquals("2026-10-27", TimeUtil.dateFormatToYYYYMMDD(date))
        assertEquals("261027", TimeUtil.dateFormatToYYMMDD(date))
        assertEquals("10/27 (화)", TimeUtil.dateFormatToMMDDEE(date))
        assertEquals("10월 27일", TimeUtil.formatDateToKorean("2026-10-27"))
    }

    @Test
    fun `요일 문자열로 주말을 판별한다`() {
        assertEquals(true, TimeUtil.isWeekend("2026-10-24"))
        assertEquals(true, TimeUtil.isWeekend("2026-10-25"))
        assertEquals(false, TimeUtil.isWeekend("2026-10-27"))
    }

    @Test
    fun `운영 시간 비교는 미래 시간이면 양수, 과거 시간이면 음수를 반환한다`() {
        assertEquals(true, TimeUtil.compareWithCurrentTime("00:00") < 0)
        assertEquals(true, TimeUtil.compareWithCurrentTime("23:59") != 0L)
        assertEquals(true, TimeUtil.compareWithCurrentTime("24:00") > 0)
    }

    @Test
    fun `오늘과 내일 문자열을 판별한다`() {
        val today = TimeUtil.dateFormatToYYYYMMDD(TimeUtil.getCurrentTime())
        val tomorrow = TimeUtil.dateFormatToYYYYMMDD(TimeUtil.getNextDayDate(TimeUtil.getCurrentTime()))

        assertEquals(true, TimeUtil.isToday(today))
        assertEquals(false, TimeUtil.isToday(tomorrow))
        assertEquals(true, TimeUtil.isTomorrow(tomorrow))
        assertEquals(false, TimeUtil.isTomorrow(today))
    }
}
