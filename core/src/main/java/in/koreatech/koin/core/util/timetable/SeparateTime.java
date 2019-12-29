package in.koreatech.koin.core.util.timetable;

import java.util.ArrayList;
import java.util.Collections;

import in.koreatech.koin.core.util.TimeUtil;

public class SeparateTime {
    public static final int START_TIME_MIN = 540;

    public SeparateTime() {

    }

    public SeparateTime(int day, int startTimeHour, int startTimeMinute, int endTimeHour, int endTimeMinute) {
        this.day = day;
        this.startTimeHour = startTimeHour;
        this.startTimeMinute = startTimeMinute;
        this.endTimeHour = endTimeHour;
        this.endTimeMinute = endTimeMinute;
    }

    private int day;
    private int startTimeHour;

    @Override
    public String toString() {
        return "SeparateTime{" +
                "day=" + day +
                ", startTimeHour=" + startTimeHour +
                ", startTimeMinute=" + startTimeMinute +
                ", endTimeHour=" + endTimeHour +
                ", endTimeMinute=" + endTimeMinute +
                '}';
    }

    private int startTimeMinute;
    private int endTimeHour;
    private int endTimeMinute;

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getStartTimeHour() {
        return startTimeHour;
    }

    public void setStartTimeHour(int startTimeHour) {
        this.startTimeHour = startTimeHour;
    }

    public int getStartTimeMinute() {
        return startTimeMinute;
    }

    public void setStartTimeMinute(int startTimeMinute) {
        this.startTimeMinute = startTimeMinute;
    }

    public int getEndTimeHour() {
        return endTimeHour;
    }

    public void setEndTimeHour(int endTimeHour) {
        this.endTimeHour = endTimeHour;
    }

    public int getEndTimeMinute() {
        return endTimeMinute;
    }

    public void setEndTimeMinute(int endTimeMinute) {
        this.endTimeMinute = endTimeMinute;
    }

    public Time getStartTime() {
        return new Time(this.startTimeHour, startTimeMinute);
    }

    public Time getEndTime() {
        return new Time(this.endTimeHour, endTimeMinute);
    }


    public static ArrayList<SeparateTime> getSeparateTimes(ArrayList<Integer> times) {
        String timeString = "";
        ArrayList<SeparateTime> separateTimes = new ArrayList<>();
        ArrayList<Integer> continueTime = new ArrayList<>();
        int currentTime;
        for (int time : times) {
            currentTime = time;
            if (continueTime.size() == 0)
                continueTime.add(currentTime);
            else {
                if (currentTime - continueTime.get(continueTime.size() - 1) == 1) {
                    continueTime.add(currentTime);
                } else {
                    int startTime = continueTime.get(0);
                    int endTime = continueTime.get(continueTime.size() - 1);
                    if ((currentTime / 100) - (continueTime.get(continueTime.size() - 1) / 100) < 1) {
                        continueTime.clear();
                        separateTimes.add(getSeperateTime(startTime, endTime));
                        continueTime.add(currentTime);
                    } else {
                        separateTimes.add(getSeperateTime(startTime, endTime));
                        continueTime.clear();
                        continueTime.add(currentTime);

                    }
                }
            }
        }
        if (!continueTime.isEmpty()) {
            int startTime = continueTime.get(0);
            int endTime = continueTime.get(continueTime.size() - 1);
            separateTimes.add(getSeperateTime(startTime, endTime));
        }
        return separateTimes;
    }

    public static SeparateTime getSeperateTime(int startTime, int endTime) {
        String startTimeString = String.format("%03d", startTime);
        String endTimeString = String.format("%03d", endTime);
        int day = Character.getNumericValue(startTimeString.charAt(0));
        int startCode = Integer.parseInt(startTimeString) % 100;
        int endCode = Integer.parseInt(endTimeString) % 100;
        int start = START_TIME_MIN + (startCode * 30);
        int end = START_TIME_MIN + ((endCode + 1) * 30);
        return new SeparateTime(day, start / 60, start % 60, end / 60, end % 60);
    }

    public static String getSpertateTimeToString(ArrayList<Integer> times) {
        StringBuilder seperateStringBuilder = new StringBuilder();
        Collections.sort(times);
        int currentday = -1;
        int restTime;
        for (int time : times) {
            restTime = time % 100;
            if (currentday != time / 100) {
                currentday = time / 100;
                seperateStringBuilder.append(TimeUtil.changeDayOfWeek(currentday)).append((restTime / 2) + 1).append((restTime % 2 == 0) ? "A" : "B");
            } else {
                seperateStringBuilder.append(",").append((restTime / 2) + 1).append((restTime % 2 == 0) ? "A" : "B");
            }

        }
        return seperateStringBuilder.toString();

    }


}
