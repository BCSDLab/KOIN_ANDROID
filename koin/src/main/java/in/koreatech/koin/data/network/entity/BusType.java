package in.koreatech.koin.data.network.entity;

public enum BusType {
    KOREATECH("koreatech", 0),
    STATION("terminal", 1),
    TERMINAL("station", 2);

    String destination;
    int type;

    BusType(String destination, int type) {
        this.destination = destination;
        this.type = type;
    }

    public static String getValueOf(int type) {
        return BusType.values()[type].destination;
    }
}
