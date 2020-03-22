package in.koreatech.koin.data.network.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;


/**
 * @since 2019.09.20
 * 버스 관련 model
 */
public abstract class Bus {


    private static final String[] expressFromKoreatechToTerminal = {
            /* TODO -> 코로나 바이러스 끝난 후 교체 필요
             "08:00",
             "09:35",
             "10:35",
             "11:35",
             "12:25",
             "13:15",
             "14:05",
             "15:05",
             "16:05",
             "16:55",
             "18:05",
             "18:55",
             "19:35",
             "20:00",
             "21:05",
             "21:55"
             */

            "8:35",
            "10:35",
            "12:35",
            "14:35",
            "16:35",
            "18:35",
            "20:35"
    };

    private static final String[] expressFromTerminalToKoreatech = {
            /* TODO -> 코로나 바이러스 끝난 후 교체 필요
            "07:00",
            "07:30",
            "09:00",
            "10:00",
            "11:00",
            "12:00",
            "13:00",
            "14:00",
            "14:30",
            "15:00",
            "16:00",
            "17:00",
            "17:50",
            "19:30",
            "20:30",
            "21:00"
             */

            "8:05",
            "10:00",
            "12:00",
            "14:00",
            "16:00",
            "18:00",
            "20:30"
    };

    /**
     * @param timetable  남은 시간 값을 받고 싶은 시간표를 인자로 받는다.
     * @param startIndex 검색을 시작하고 싶은 index를 인자로 받는다.
     * @return 현 시간에서 가장 가까운 남은 버스 시간 값을 반환해준다.
     * @throws ParseException
     */
    private static long getRemainTimeToLong(String[] timetable, int startIndex) throws ParseException {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Seoul");
        Calendar currentTime = Calendar.getInstance(timeZone); // 오늘 일자의 Calendar 객체

        Calendar currentBusTime = Calendar.getInstance(timeZone); // 현재 버스 시간의 Calendar 객체

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm"); // 시간표 포맷
        simpleDateFormat.setTimeZone(timeZone);

        if (timetable.length == 0) { // 오늘의 운행 시간표가 없다면 ?
            return -1;
        }

        for (int i = startIndex; i < timetable.length; i++) {
            currentBusTime.setTime(simpleDateFormat.parse(timetable[i]));
            currentBusTime.set(currentTime.get(Calendar.YEAR), currentTime.get(Calendar.MONTH), currentTime.get(Calendar.DAY_OF_MONTH));

            // 오늘 일자로 시간 설정
            long timeDifference = currentBusTime.getTimeInMillis() - currentTime.getTimeInMillis();
            if (timeDifference > 0) { // 오늘 남은 버스가 존재한다면 ?
                return timeDifference / 1000;
            }
        }
        return -1;
    }


    /**
     * @param timetable  index 값을 받고 싶은 시간표를 인자로 받는다.
     * @param startIndex 검색을 시작하고 싶은 index를 인자로 받는다.
     * @return 현 시간에서 가장 가까운 남은 버스 시간 값의 index를 반환해준다.
     * @throws ParseException
     */
    private static int getBusTimeIndex(String[] timetable, int startIndex) throws ParseException {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Seoul");
        Calendar currentTime = Calendar.getInstance(timeZone); // 오늘 일자의 Calendar 객체

        Calendar currentBusTime = Calendar.getInstance(timeZone); // 현재 버스 시간의 Calendar 객체

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm"); // 시간표 포맷
        simpleDateFormat.setTimeZone(timeZone);

        if (timetable.length == 0) { // 오늘의 운행 시간표가 없다면 ?
            return -1;
        }

        for (int i = startIndex; i < timetable.length; i++) {
            currentBusTime.setTime(simpleDateFormat.parse(timetable[i]));
            currentBusTime.set(currentTime.get(Calendar.YEAR), currentTime.get(Calendar.MONTH), currentTime.get(Calendar.DAY_OF_MONTH));
            // 오늘 일자로 시간 설정
            long timeDifference = currentBusTime.getTimeInMillis() - currentTime.getTimeInMillis();

            if (timeDifference > 0) { // 오늘 남은 버스가 존재한다면 ?
                return i;
            }
        }
        return -1;
    }

    /**
     * @param timetable index 값을 받고 싶은 시간표를 인자로 받는다.
     * @param hour      시간 입력 받는다.
     * @param min       분 입력 받는다.
     * @return 현 시간에서 가장 가까운 남은 버스 시간 값의 index를 반환해준다.
     * @throws ParseException
     */
    private static int getBusTimeIndex(String[] timetable, int hour, int min) throws ParseException {
        String currentDateString = String.format("%02d:%02d", hour, min);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.KOREA);
        Date searchDate = simpleDateFormat.parse(currentDateString);
        Date busDate;

        if (timetable.length == 0) { // 오늘의 운행 시간표가 없다면 ?
            return -1;
        }
        for (int i = 0; i < timetable.length; i++) {
            busDate = simpleDateFormat.parse(timetable[i]);
            long timeDifference = busDate.getTime() - searchDate.getTime();
            if (timeDifference > 0) { // 오늘 남은 버스가 존재한다면 ?
                return i;
            }
        }
        return -1;
    }

    /**
     * @param departEnglish  koreatech, terminal, station 중 1
     * @param arrivalEnglish koreatech, terminal, station 중 1
     * @param isNow          true일 경우 최근의 값을 받아오고 false일 경우 다음 값을 받아온다.
     * @return 남은 시간은 mills로 Long으로 반환해준다.
     * @throws ParseException 셔틀 버스 남은 시간을 반환 해주는 함수이다.
     */
    public long getRemainShuttleTimeToLong(String departEnglish, String arrivalEnglish, boolean isNow) throws ParseException {
        String[] shuttleTimeTable = getCurrentDayShuttleDayStringArray(departEnglish, arrivalEnglish);
        int resultNowIndex;
        if (shuttleTimeTable == null) return -1;
        resultNowIndex = getBusTimeIndex(shuttleTimeTable, 0);
        if (resultNowIndex == -1 || resultNowIndex >= shuttleTimeTable.length) return -1;
        //지금일 경우에
        if (isNow)
            return getRemainTimeToLong(shuttleTimeTable, 0);
        else if (resultNowIndex + 1 >= shuttleTimeTable.length)
            return -1;
        else
            return getRemainTimeToLong(shuttleTimeTable, resultNowIndex + 1);
    }

    /**
     * @param departEnglish  koreatech, terminal, station 중 1
     * @param arrivalEnglish koreatech, terminal, station 중 1
     * @param isNow          true일 경우 최근의 값을 받아오고 false일 경우 다음 값을 받아온다.
     * @return 남은 시간은 mills로 Long으로 반환해준다.
     * @throws ParseException 대승 버스 남은 시간을 반환 해주는 함수이다.
     */
    public static long getRemainExpressTimeToLong(String departEnglish, String arrivalEnglish, boolean isNow) throws ParseException {
        String[] expressTimeTable = getCurrentDayExpressDayStringArray(departEnglish, arrivalEnglish);
        int resultNowIndex;
        if (expressTimeTable == null) return -1;
        resultNowIndex = getBusTimeIndex(expressTimeTable, 0);
        if (resultNowIndex == -1 || resultNowIndex >= expressTimeTable.length) return -1;

        if (isNow)
            return getRemainTimeToLong(expressTimeTable, 0);
        else if (resultNowIndex + 1 >= expressTimeTable.length)
            return -1;
        else
            return getRemainTimeToLong(expressTimeTable, resultNowIndex + 1);
    }

    /**
     * @param departEnglish  koreatech, terminal, station 중 1
     * @param arrivalEnglish koreatech, terminal, station 중 1
     * @param isNow          true일 경우 최근의 값을 받아오고 false일 경우 다음 값을 받아온다.
     * @return 해당 되는 셔틀 버스 시간을 String으로 반환해준다. 없는 경우 "" 로 반환
     * @throws ParseException
     */
    public String getNearShuttleTimeToString(String departEnglish, String arrivalEnglish, boolean isNow) throws ParseException {
        String[] shuttleTimeTable = getCurrentDayShuttleDayStringArray(departEnglish, arrivalEnglish);
        int resultNowIndex;

        if (shuttleTimeTable == null) return "";
        resultNowIndex = getBusTimeIndex(shuttleTimeTable, 0);
        if (resultNowIndex == -1 || resultNowIndex >= shuttleTimeTable.length) return "";

        if (isNow)
            return shuttleTimeTable[resultNowIndex];
        else if (resultNowIndex + 1 >= shuttleTimeTable.length)
            return "";
        else
            return shuttleTimeTable[resultNowIndex + 1];

    }


    public String getNearShuttleTimeToString(String departEnglish, String arrivalEnglish, int year, int month, int day, int hour, int min) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        Calendar currentTime = Calendar.getInstance();
        String currentDate = String.format("%4d-%2d-%2d", year, month, day);
        Date date = sdf.parse(currentDate);
        currentTime.setTime(date);
        int dayType = (currentTime.get(Calendar.DAY_OF_WEEK) + 5) % 7;
        String[] shuttleTimeTable = getCurrentDayShuttleDayStringArray(departEnglish, arrivalEnglish, dayType);
        int resultNowIndex;

        if (shuttleTimeTable == null) return "";
        resultNowIndex = getBusTimeIndex(shuttleTimeTable, hour, min);
        if (resultNowIndex == -1 || resultNowIndex >= shuttleTimeTable.length) return "";

        return shuttleTimeTable[resultNowIndex];
    }

    /**
     * @param departEnglish  koreatech, terminal, station 중 1
     * @param arrivalEnglish koreatech, terminal, station 중 1
     * @param isNow          true일 경우 최근의 값을 받아오고 false일 경우 다음 값을 받아온다.
     * @return 해당 되는 대성 고속 시간을 String으로 반환해준다. 없는 경우 "" 로 반환
     * @throws ParseException
     */
    public static String getNearExpressTimeToString(String departEnglish, String arrivalEnglish, boolean isNow) throws ParseException {
        String[] expressTimeTable = getCurrentDayExpressDayStringArray(departEnglish, arrivalEnglish);
        int resultNowIndex;

        if (expressTimeTable == null) return "";
        resultNowIndex = getBusTimeIndex(expressTimeTable, 0);
        if (resultNowIndex == -1 || resultNowIndex >= expressTimeTable.length) return "";
        if (isNow)
            return expressTimeTable[resultNowIndex];
        else if (resultNowIndex + 1 >= expressTimeTable.length)
            return "";
        else
            return expressTimeTable[resultNowIndex + 1];

    }

    /**
     * @param departEnglish  koreatech, terminal, station 중 1
     * @param arrivalEnglish koreatech, terminal, station 중 1
     * @param hour           시간을 받아온다.
     * @param min            분을 받아온다.
     * @return 해당 되는 대성 고속 시간을 String으로 반환해준다. 없는 경우 "" 로 반환
     * @throws ParseException
     */
    public static String getNearExpressTimeToString(String departEnglish, String arrivalEnglish, int hour, int min) throws ParseException {
        String[] expressTimeTable = getCurrentDayExpressDayStringArray(departEnglish, arrivalEnglish);
        int resultNowIndex;

        if (expressTimeTable == null) return "";
        resultNowIndex = getBusTimeIndex(expressTimeTable, hour, min);
        if (resultNowIndex == -1 || resultNowIndex >= expressTimeTable.length) return "";

        return expressTimeTable[resultNowIndex];

    }

    /**
     * @param departEnglish  koreatech, terminal, station 중 1
     * @param arrivalEnglish koreatech, terminal, station 중 1
     * @return 해당하는 셔틀 버스 시간이 저장된 배열을 반환 해준다.
     */
    private String[] getCurrentDayShuttleDayStringArray(String departEnglish, String arrivalEnglish) {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Seoul");
        Calendar currentTime = Calendar.getInstance(timeZone); // 오늘 일자의 Calendar 객체
        int dayType = (currentTime.get(Calendar.DAY_OF_WEEK) + 5) % 7;

        if (departEnglish.equalsIgnoreCase("koreatech")) { // 출발지가 학교라면
            return arrivalEnglish.equalsIgnoreCase("station") ? getShuttleFromKoreatechToStation()[dayType] : getShuttleFromKoreatechToTerminal()[dayType];
        } else if (departEnglish.equalsIgnoreCase("station")) { // 천안역이라면?
            return arrivalEnglish.equalsIgnoreCase("koreatech") ? getShuttleFromStationToKoreatech()[dayType] : getShuttleFromStationToTerminal()[dayType];
        } else { // 터미널이라면?
            return arrivalEnglish.equalsIgnoreCase("koreatech") ? getShuttleFromTerminalToKoreatech()[dayType] : getShuttleFromTerminalToStation()[dayType];
        }
    }

    /**
     * @param departEnglish  koreatech, terminal, station 중 1
     * @param arrivalEnglish koreatech, terminal, station 중 1
     * @return 해당하는 셔틀 버스 시간이 저장된 배열을 반환 해준다.
     */
    private String[] getCurrentDayShuttleDayStringArray(String departEnglish, String arrivalEnglish, int dayType) {
        dayType %= 7;
        if (departEnglish.equalsIgnoreCase("koreatech")) { // 출발지가 학교라면
            return arrivalEnglish.equalsIgnoreCase("station") ? getShuttleFromKoreatechToStation()[dayType] : getShuttleFromKoreatechToTerminal()[dayType];
        } else if (departEnglish.equalsIgnoreCase("station")) { // 천안역이라면?
            return arrivalEnglish.equalsIgnoreCase("koreatech") ? getShuttleFromStationToKoreatech()[dayType] : getShuttleFromStationToTerminal()[dayType];
        } else { // 터미널이라면?
            return arrivalEnglish.equalsIgnoreCase("koreatech") ? getShuttleFromTerminalToKoreatech()[dayType] : getShuttleFromTerminalToStation()[dayType];
        }
    }

    /**
     * @param departEnglish  koreatech, terminal, station 중 1
     * @param arrivalEnglish koreatech, terminal, station 중 1
     * @return 해당하는 대성 버스 시간이 저장된 배열을 반환 해준다.
     */
    private static String[] getCurrentDayExpressDayStringArray(String departEnglish, String arrivalEnglish) {
        String[] expressTimeTable;
        if (departEnglish.equals("koreatech") && arrivalEnglish.equals("terminal")) { // 학교에서 야우리
            expressTimeTable = expressFromKoreatechToTerminal;
        } else if (departEnglish.equals("terminal") && arrivalEnglish.equals("koreatech")) { // 야우리에서 학교
            expressTimeTable = expressFromTerminalToKoreatech;
        } else { // 그 외에는 운행하지 않으므로
            return null;
        }

        return expressTimeTable;
    }

    public abstract String[][] getShuttleFromStationToKoreatech();

    public abstract String[][] getShuttleFromStationToTerminal();

    public abstract String[][] getShuttleFromTerminalToKoreatech();

    public abstract String[][] getShuttleFromTerminalToStation();

    public abstract String[][] getShuttleFromKoreatechToStation();

    public abstract String[][] getShuttleFromKoreatechToTerminal();


}