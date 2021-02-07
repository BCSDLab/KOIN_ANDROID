package in.koreatech.koin.util;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtil {
    private final static SimpleDateFormat MMDDE = new SimpleDateFormat("MM월 DD일 (E)", Locale.KOREAN);
    private final static SimpleDateFormat YYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");
    private final static SimpleDateFormat YYMMDD = new SimpleDateFormat("yyMMdd");
    private final static SimpleDateFormat YYYYMMDDHHMM = new SimpleDateFormat("yyyyMMddHHmm");
    private final static SimpleDateFormat HHMM = new SimpleDateFormat("HH:mm");
    private static Calendar cal;

    private static SimpleDateFormat getTimestampStringFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public static String getDeviceCreatedDateTimeString(int offsetDay, int offsetHour, int offsetMin) {
        cal = Calendar.getInstance();
        SimpleDateFormat formatter = getTimestampStringFormat();
        formatter.setTimeZone(TimeZone.getDefault());

        cal.add(Calendar.MINUTE, offsetMin); // adds one min
        cal.add(Calendar.HOUR_OF_DAY, offsetHour); // adds one hour
        cal.add(Calendar.DAY_OF_MONTH, offsetDay); // adds one day
        return formatter.format(cal.getTime());

    }

    public static String getDeviceCreatedDateOnlyString() {
        cal = Calendar.getInstance();
        YYYYMMDD.setTimeZone(TimeZone.getDefault());
        return YYYYMMDD.format(cal.getTime());
    }

    public static Long getDeviceCreatedDateOnlyLong() {
        cal = Calendar.getInstance();
        YYYYMMDDHHMM.setTimeZone(TimeZone.getDefault());
        String result = YYYYMMDDHHMM.format(cal.getTime());

        return Long.parseLong(result);
    }

    public static Long getDeviceCreatedDateOnlyLongAddTimeLimit(int timeLimitMinute) {
        cal = Calendar.getInstance();
        SimpleDateFormat formatterDateOnly = new SimpleDateFormat("yyyyMMddHHmm");
        formatterDateOnly.setTimeZone(TimeZone.getDefault());

        cal.setTime(cal.getTime());
        cal.add(Calendar.MINUTE, timeLimitMinute);

        String result = formatterDateOnly.format(cal.getTime());

        return Long.parseLong(result);
    }

    public static String getCurrentYear() {
        cal = Calendar.getInstance();
        int result = cal.get(Calendar.YEAR);
        return result + "";
    }

    public static String getCurrentMonth() {
        cal = Calendar.getInstance();
        int result = cal.get(Calendar.MONTH) + 1;
        return result + "";
    }

    public static String getCurrentDay() {
        cal = Calendar.getInstance();
        int result = cal.get(Calendar.DAY_OF_MONTH);
        return result + "";
    }

    public static String getCurrentHour() {
        cal = Calendar.getInstance();
        int result = cal.get(Calendar.HOUR_OF_DAY);
        return result + "";
    }

    public static String getCurrentMinute() {
        cal = Calendar.getInstance();
        int result = cal.get(Calendar.MINUTE);
        return result + "";
    }

    public static String getCurrentSecond() {
        cal = Calendar.getInstance();
        int result = cal.get(Calendar.SECOND);
        return result + "";
    }

    public static long getCurrentYearOnlyLong() {
        cal = Calendar.getInstance();
        return (long) cal.get(Calendar.YEAR);
    }

    public static long getCurrentMonthOnlyLong() {
        cal = Calendar.getInstance();
        return (long) (cal.get(Calendar.MONTH) + 1);
    }

    public static long getCurrentDayOnlyLong() {
        cal = Calendar.getInstance();
        return (long) cal.get(Calendar.DAY_OF_MONTH);
    }

    public static long getCurrentHourOnlyLong() {
        cal = Calendar.getInstance();
        return (long) cal.get(Calendar.HOUR_OF_DAY);
    }

    public static long getCurrentMinuteOnlyLong() {
        cal = Calendar.getInstance();
        return (long) cal.get(Calendar.MINUTE);
    }

    public static long getCurrentSecondOnlyLong() {
        cal = Calendar.getInstance();
        return (long) cal.get(Calendar.SECOND);
    }

    public static String getCurrentDayOfWeek() {
        cal = Calendar.getInstance();
        String result = "";
        switch (cal.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                result += "일";
                break;
            case 2:
                result += "월";
                break;
            case 3:
                result += "화";
                break;
            case 4:
                result += "수";
                break;
            case 5:
                result += "목";
                break;
            case 6:
                result += "금";
                break;
            case 7:
                result += "토";
                break;
            default:
                break;
        }
        return result;
    }

    public static String changeDayOfWeek(int day) {
        String result = "";
        switch (day) {
            case 0:
                result += "월";
                break;
            case 1:
                result += "화";
                break;
            case 2:
                result += "수";
                break;
            case 3:
                result += "목";
                break;
            case 4:
                result += "금";
                break;
            case 5:
                result += "토";
                break;
            case 6:
                result += "일";
                break;
            default:
                break;
        }
        return result;
    }

    public static String getMMDDE(String date) throws ParseException {
        Date date1 = YYYYMMDD.parse(date);
        String result = MMDDE.format(date1);
        return result;
    }

    public static String getCurrentTimeToMessage() {
        cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        String result = "";

        result += getCurrentYear().substring(2, 4); //년도
        result += "." + ((getCurrentMonthOnlyLong() < 10L) ? "0" + getCurrentMonth() : getCurrentMonth());  //월
        result += "." + ((getCurrentDayOnlyLong() < 10L) ? "0" + getCurrentDay() : getCurrentDay()) + " ";  //일
        result += "(" + getCurrentDayOfWeek() + ") ";

        if (hour > 12 || (hour > 11 && min > 59)) {
            result += "오후 ";
            hour -= 12;
        } else {
            result += "오전 ";
        }

        if (hour < 10) {
            result += "0" + hour;
        } else {
            result += hour;
        }

        result += ":";

        if (min < 10) {
            result += "0" + min;
        } else {
            result += min;
        }

        return result;
    }

    public static Long parseDateTimeStringToLong(String timeStr) {
        if (timeStr == null) {
            return 0L;
        }
        String result = timeStr.substring(0, 4) +
                timeStr.substring(5, 7) +
                timeStr.substring(8, 10) +
                timeStr.substring(11, 13) +
                timeStr.substring(14, 16);

        return Long.parseLong(result);
    }

    public static Long parseDateStringToLong(String dateStr) {
        if (dateStr == null) {
            return 0L;
        }
        String result = dateStr.substring(0, 4) +
                dateStr.substring(5, 7) +
                dateStr.substring(8, 10);
        return Long.parseLong(result);
    }


    public static String getChangeDate(int date) {
        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, date);
        YYYYMMDD.setTimeZone(TimeZone.getDefault());
        return YYYYMMDD.format(cal.getTime());
    }

    public static String getChangeDateFormatYYMMDD(int date) {
        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, date);
        YYMMDD.setTimeZone(TimeZone.getDefault());
        return YYMMDD.format(cal.getTime());
    }

    public static String getAddTimeSecond(int second) {
        cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, second);
        HHMM.setTimeZone(TimeZone.getDefault());
        return HHMM.format(cal.getTime());
    }

    public static int timeCompareWithCurrent(boolean isNextDay, String time) {
        //시간 설정
        String end;
        if (!isNextDay)
            end = getDeviceCreatedDateTimeString(0, 0, 0).substring(0, 10) + " " + time + ":00";
        else
            end = getDeviceCreatedDateTimeString(1, 0, 0).substring(0, 10) + " " + time + ":00";
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date endDay = sf.parse(end, new ParsePosition(0));//
        long endTime = endDay.getTime();

        // 현재의 시간 설정
        Calendar cal = Calendar.getInstance();
        Date startDate = cal.getTime();
        long startTime = startDate.getTime();
        long mills = endTime - startTime;
        long sec = mills / 1000;
        return (int) sec;
    }

    public static String getDateDay(String dateSting, String dateType) throws Exception {

        String day = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateType);
        Date date = dateFormat.parse(dateSting);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int dayNum = cal.get(Calendar.DAY_OF_WEEK);


        switch (dayNum) {
            case 1:
                day = "일";
                break;
            case 2:
                day = "월";
                break;
            case 3:
                day = "화";
                break;
            case 4:
                day = "수";
                break;
            case 5:
                day = "목";
                break;
            case 6:
                day = "금";
                break;
            case 7:
                day = "토";
                break;

        }


        return day;
    }

    public static boolean isBetweenCurrentTime(String starTime, String endTime) {
        try {
            SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
            String curTime = parser.format(new Date());
            Date start = parser.parse(starTime);
            Date end = parser.parse(endTime);
            Date current = parser.parse(curTime);

            if (end.before(start)) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(end);
                cal.add(Calendar.DAY_OF_YEAR, 1);
                end.setTime(cal.getTimeInMillis());
            }

            if (current.after(start) && current.before(end)) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }
    }


}
