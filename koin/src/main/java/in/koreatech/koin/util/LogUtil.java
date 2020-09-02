package in.koreatech.koin.util;

import java.util.ArrayList;

public class LogUtil {
    public static <T> void logdArrayList(ArrayList<T> logArrayList) {
        StringBuilder sb = new StringBuilder();
        for (T s : logArrayList) {
            sb.append(s);
            sb.append("\t");
        }
        System.out.println(sb.toString());
    }
}
