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
 * @author yunjae Na
 * @since 2019.09.20
 * 버스 관련 model
 */
public class Bus {
    private static final String[] expressFromKoreatechToTerminal = {
            "08:00",
            "09:35",
            "10:30",
            "11:45",
            "12:35",
            "14:00",
            "15:05",
            "16:00",
            "16:55",
            "18:05",
            "18:55",
            "20:00",
            "21:05",
            "21:55"
    };

    private static final String[] expressFromTerminalToKoreatech = {
            "07:00",
            "07:30",
            "09:00",
            "10:00",
            "10:30",
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
    };

    private static final String[][] shuttleFromKoreatech = {
            { // 월요일
                    "09:10",
                    "11:00",
                    "14:00",
                    "15:00",
                    "16:00",
                    "16:30",
                    "17:00",
                    "19:30",
                    "21:00",
                    "22:40"
            },
            { // 화요일
                    "09:10",
                    "11:00",
                    "14:00",
                    "15:00",
                    "16:00",
                    "16:30",
                    "17:00",
                    "19:30",
                    "21:00",
                    "22:40"
            },
            { // 수요일
                    "09:10",
                    "11:00",
                    "14:00",
                    "15:00",
                    "16:00",
                    "16:30",
                    "17:00",
                    "19:30",
                    "21:00",
                    "22:40"
            },
            { // 목요일
                    "09:10",
                    "11:00",
                    "14:00",
                    "15:00",
                    "16:00",
                    "16:30",
                    "17:00",
                    "19:30",
                    "21:00",
                    "22:40"
            },
            { // 금요일
                    "09:10",
                    "11:00",
                    "14:00",
                    "14:30",
                    "15:00",
                    "16:00",
                    "16:30",
                    "17:00",
                    "19:30",
                    "21:00",
                    "22:40"
            },
            { // 토요일
                    "14:00"
            },
            { // 일요일
                    "17:00"
            }
    };

    private static final String[][] shuttleFromTerminal = {
            { // 월요일
                    "08:00",
                    "10:10",
                    "11:25",
                    "14:25",
                    "16:05",
                    "16:25",
                    "16:55",
                    "17:25",
                    "18:45",
                    "19:55",
                    "22:00"
            },
            { // 화요일
                    "08:00",
                    "10:10",
                    "11:25",
                    "14:25",
                    "16:05",
                    "16:25",
                    "16:55",
                    "17:25",
                    "18:45",
                    "19:55",
                    "22:00"
            },
            { // 수요일
                    "08:00",
                    "10:10",
                    "11:25",
                    "14:25",
                    "16:05",
                    "16:25",
                    "16:55",
                    "17:25",
                    "18:45",
                    "19:55",
                    "22:00"
            },
            { // 목요일
                    "08:00",
                    "10:10",
                    "11:25",
                    "14:25",
                    "16:05",
                    "16:25",
                    "16:55",
                    "17:25",
                    "18:45",
                    "19:55",
                    "22:00"
            },
            { // 금요일
                    "08:00",
                    "10:10",
                    "11:25",
                    "14:25",
                    "16:05",
                    "16:25",
                    "16:55",
                    "17:25",
                    "18:45",
                    "19:55",
                    "22:00"
            },
            { // 토요일
                    "14:25",
                    "18:45"
            },
            { // 일요일
                    "17:30",
                    "21:15",
                    "21:30"
            }
    };

    private static final String[][] shuttleFromStationToKoreatech = {
            { // 월요일
                    "08:05",
                    "10:15",
                    "11:30",
                    "14:30",
                    "16:10",
                    "16:30",
                    "17:00",
                    "17:30",
                    "18:50",
                    "20:00",
                    "22:05"
            },
            { // 화요일
                    "08:05",
                    "10:15",
                    "11:30",
                    "14:30",
                    "16:10",
                    "16:30",
                    "17:00",
                    "17:30",
                    "18:50",
                    "20:00",
                    "22:05"
            },
            { // 수요일
                    "08:05",
                    "10:15",
                    "11:30",
                    "14:30",
                    "16:10",
                    "16:30",
                    "17:00",
                    "17:30",
                    "18:50",
                    "20:00",
                    "22:05"
            },
            { // 목요일
                    "08:05",
                    "10:15",
                    "11:30",
                    "14:30",
                    "16:10",
                    "16:30",
                    "17:00",
                    "17:30",
                    "18:50",
                    "20:00",
                    "22:05"
            },
            { // 금요일
                    "08:05",
                    "10:15",
                    "11:30",
                    "14:30",
                    "16:10",
                    "16:30",
                    "17:00",
                    "17:30",
                    "18:50",
                    "20:00",
                    "22:05"
            },
            { // 토요일
                    "14:30",
                    "18:50"
            },
            { // 일요일
                    "17:35",
                    "21:20",
                    "21:35"
            }
    };

    private static final String[][] shuttleFromStationToTerminal = {
            { // 월요일
                    "09:30",
                    "15:20"
            },
            { // 화요일
                    "09:30",
                    "15:20"
            },
            { // 수요일
                    "09:30",
                    "15:20"
            },
            { // 목요일
                    "09:30",
                    "15:20"
            },
            { // 금요일
                    "09:30",
                    "15:20"
            },
            { // 토요일
            },
            { // 일요일
            }
    };

    private static final Map<String, Object> shuttleTimeTables = new HashMap<String, Object>() {{
        put("koreatech", shuttleFromKoreatech);
        put("terminal", shuttleFromTerminal);
    }};

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
                return timeDifference/1000;
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
     * @param timetable  index 값을 받고 싶은 시간표를 인자로 받는다.
     * @param hour       시간 입력 받는다.
     * @param min        분 입력 받는다.
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
    public static long getRemainShuttleTimeToLong(String departEnglish, String arrivalEnglish, boolean isNow) throws ParseException {
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
    public static String getNearShuttleTimeToString(String departEnglish, String arrivalEnglish, boolean isNow) throws ParseException {
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


    public static String getNearShuttleTimeToString(String departEnglish, String arrivalEnglish, int year, int month, int day, int hour, int min) throws ParseException {
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
    private static String[] getCurrentDayShuttleDayStringArray(String departEnglish, String arrivalEnglish) {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Seoul");
        Calendar currentTime = Calendar.getInstance(timeZone); // 오늘 일자의 Calendar 객체
        String[] shuttleTimeTable;
        int dayType = (currentTime.get(Calendar.DAY_OF_WEEK) + 5) % 7;

        if (!departEnglish.equals("station")) { // 출발지가 천안역이 아니라면 ?
            String[][] timeTable = (String[][]) shuttleTimeTables.get(departEnglish); // 출발지로 시간표 가져옴
            shuttleTimeTable = timeTable[dayType];
        } // 천안역이라면 ?
        else
            shuttleTimeTable = arrivalEnglish.equals("koreatech") ? shuttleFromStationToKoreatech[dayType] : shuttleFromStationToTerminal[dayType];

        return shuttleTimeTable;
    }

    /**
     * @param departEnglish  koreatech, terminal, station 중 1
     * @param arrivalEnglish koreatech, terminal, station 중 1
     * @return 해당하는 셔틀 버스 시간이 저장된 배열을 반환 해준다.
     */
    private static String[] getCurrentDayShuttleDayStringArray(String departEnglish, String arrivalEnglish, int dayType) {
        String[] shuttleTimeTable;
        dayType %= 7;


        if (!departEnglish.equals("station")) { // 출발지가 천안역이 아니라면 ?
            String[][] timeTable = (String[][]) shuttleTimeTables.get(departEnglish); // 출발지로 시간표 가져옴
            shuttleTimeTable = timeTable[dayType];
        } // 천안역이라면 ?
        else
            shuttleTimeTable = arrivalEnglish.equals("koreatech") ? shuttleFromStationToKoreatech[dayType] : shuttleFromStationToTerminal[dayType];

        return shuttleTimeTable;
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


}