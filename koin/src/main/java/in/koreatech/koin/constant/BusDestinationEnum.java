package in.koreatech.koin.constant;

import android.util.Pair;

/**
 *
 * 버스 도착지 및 출발지 열거형
 */
public enum BusDestinationEnum {
    KOREATECH_TO_TERMINAL("koreatech", "terminal"),
    TERMINAL_TO_STATION("terminal", "station"),
    KOREATCH_TO_STATION("koreatech", "station");


    private String depature;
    private String destination;

    BusDestinationEnum(String departure, String destination) {
        this.depature = departure;
        this.destination = destination;
    }

    /**
     * 출발지 및 도착지를 반환 해준다.
     *
     * @param type       버스 타입
     * @param isReversed 출발지 도착지 변환 여부
     * @return
     */
    public static Pair<String, String> getValueOf(int type, boolean isReversed) {
        if (type < 0 || type > 2)
            type = 0;
        String departure = BusDestinationEnum.values()[type].getDepature();
        String destination = BusDestinationEnum.values()[type].getDestination();
        if (!isReversed)
            return new Pair<>(departure, destination);
        else
            return new Pair<>(destination, departure);

    }

    public String getDepature() {
        return depature;
    }

    public String getDestination() {
        return destination;
    }
}
