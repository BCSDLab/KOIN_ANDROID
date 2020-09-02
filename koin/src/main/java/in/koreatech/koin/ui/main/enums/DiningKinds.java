package in.koreatech.koin.ui.main.enums;

public enum DiningKinds {
    KOREAN(0), ONEDISH(1), WESTERN(2), SPECIAL(3);

    int i = 0;

    DiningKinds(int i) {
        this.i = 0;
    }

    public int getPosition() {
        return i;
    }
}