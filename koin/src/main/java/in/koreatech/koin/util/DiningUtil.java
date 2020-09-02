package in.koreatech.koin.util;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import in.koreatech.koin.data.network.entity.Dining;

public class DiningUtil {
    public final static String[] TYPE = {"BREAKFAST", "LUNCH", "DINNER"};
    /**
     * 서버로 받아온 식단에 우선순위를 주어 한식-일품식-양식-특식-능수관-수박여 순으로 재배열하는 메소드
     *
     * @param diningArrayList 서버에서 받아온 식단 정보
     * @return 재정렬된 식단 ArrayList
     */
    public static ArrayList<Dining> arrangeDinings(ArrayList<Dining> diningArrayList) {
        Map<Integer, Dining> tempDiningMap = new TreeMap<>();

        for (Dining dining : diningArrayList) {
            int priority = 0;

            if (dining.getType().equals(TYPE[0])) {
                priority += 0;
            } else if (dining.getType().equals(TYPE[1])) {
                priority += 7;
            } else if (dining.getType().equals(TYPE[2])) {
                priority += 14;
            }

            switch (dining.getPlace()) {
                case "한식":
                    priority += 0;
                    break;
                case "일품식":
                    priority += 1;
                    break;
                case "양식":
                    priority += 2;
                    break;
                case "특식":
                    priority += 3;
                    break;
                case "능수관":
                    priority += 4;
                    break;
                case "수박여":
                    priority += 5;
                    break;
                default: // 위 6가지 외 종류가 나올 경우 능수, 수박여 다음으로 출력
                    priority += 6;
            }
            tempDiningMap.put(priority, dining);
        }

        diningArrayList.clear();

        for (int key : tempDiningMap.keySet()) {
            diningArrayList.add(tempDiningMap.get(key));
        }

        return diningArrayList;
    }
}
