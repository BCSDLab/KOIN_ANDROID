package in.koreatech.koin.data.network.entity;

import in.koreatech.koin.data.network.entity.Bus;

public class VacationBus extends Bus {

    private static final String[][] shuttleFromKoreatechToTerminal = {
            { // 월요일
                    "14:00",
            },
            { // 화요일
                    "14:00",
            },
            { // 수요일
                    "14:00",
            },
            { // 목요일
                    "14:00",
            },
            { // 금요일
                    "14:00",
            },
            { // 토요일
            },
            { // 일요일
            }
    };

    private static final String[][] shuttleFromKoreatechToStation = {
            { // 월요일
                    "14:00",
            },
            { // 화요일
                    "14:00",
            },
            { // 수요일
                    "14:00",
            },
            { // 목요일
                    "14:00",
            },
            { // 금요일
                    "14:00",
            },
            { // 토요일
            },
            { // 일요일
            }
    };

    private static final String[][] shuttleFromTerminalToKoreatech = {
            { // 월요일
                    "08:00",
                    "14:25"
            },
            { // 화요일
                    "08:00",
                    "14:25"
            },
            { // 수요일
                    "08:00",
                    "14:25"
            },
            { // 목요일
                    "08:00",
                    "14:25"
            },
            { // 금요일
                    "08:00",
                    "14:25"
            },
            { // 토요일

            },
            { // 일요일

            }
    };

    private static final String[][] shuttleFromTerminalToStation = {
            { // 월요일
                    "08:00",
                    "14:25"
            },
            { // 화요일
                    "08:00",
                    "14:25"
            },
            { // 수요일
                    "08:00",
                    "14:25"
            },
            { // 목요일
                    "08:00",
                    "14:25"
            },
            { // 금요일
                    "08:00",
                    "14:25"
            },
            { // 토요일

            },
            { // 일요일

            }
    };

    private static final String[][] shuttleFromStationToKoreatech = {
            { // 월요일
                    "08:05",
                    "14:30"
            },
            { // 화요일
                    "08:05",
                    "14:30"
            },
            { // 수요일
                    "08:05",
                    "14:30"
            },
            { // 목요일
                    "08:05",
                    "14:30"
            },
            { // 금요일
                    "08:05",
                    "14:30"
            },
            { // 토요일
            },
            { // 일요일
            }
    };

    private static final String[][] shuttleFromStationToTerminal = {
            { // 월요일
            },
            { // 화요일
            },
            { // 수요일
            },
            { // 목요일
            },
            { // 금요일
            },
            { // 토요일
            },
            { // 일요일
            }
    };

    @Override
    public String[][] getShuttleFromStationToKoreatech() {
        return shuttleFromStationToKoreatech;
    }

    @Override
    public String[][] getShuttleFromStationToTerminal() {
        return shuttleFromStationToTerminal;
    }


    @Override
    public String[][] getShuttleFromKoreatechToStation() {
        return  shuttleFromKoreatechToStation;
    }

    @Override
    public String[][] getShuttleFromKoreatechToTerminal() {
        return shuttleFromKoreatechToTerminal;
    }

    @Override
    public String[][] getShuttleFromTerminalToKoreatech() {
        return shuttleFromTerminalToKoreatech;
    }

    @Override
    public String[][] getShuttleFromTerminalToStation() {
        return shuttleFromTerminalToStation;
    }
}
