package in.koreatech.koin.util;

/**
 * Created by hyerim on 2018. 4. 9....
 */
public class FormValidatorUtil {
    public static boolean validateStringIsEmpty(String str) {
        return str == null || str.trim().length() < 1;
    }

    public static boolean validateStartPlaceIsEmpty(String str) {
        return str == null || str.length() < 1 || str.compareTo("출발지 선택") == 0;
    }

    public static boolean validateEndPlaceIsEmpty(String str) {
        return str == null || str.length() < 1 || str.compareTo("목적지 선택") == 0;
    }
}
