package in.koreatech.koin.constant;

public enum BusType {
    KOREATECH("koreatech", 0),
    TERMINAL("terminal", 1),
    STATION("station", 2);

    String destination;
    int type;

    BusType(String destination, int type) {
        this.destination = destination;
        this.type = type;
    }

    public static String getValueOf(int type) {
        return BusType.values()[type].destination;
    }

    public String getDestination() {
        return this.destination;
    }

    public int getType(){
        return this.type;
    }
}
