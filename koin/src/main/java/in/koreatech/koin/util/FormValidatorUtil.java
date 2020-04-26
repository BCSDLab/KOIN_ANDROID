package in.koreatech.koin.util;

import android.text.Html;

public class FormValidatorUtil {
    public static boolean validateStringIsEmpty(String str) {
        return str == null || str.trim().replace(" ","").length() < 1;
    }

    public static boolean validateStartPlaceIsEmpty(String str) {
        return str == null || str.length() < 1 || str.compareTo("출발지 선택") == 0;
    }

    public static boolean validateEndPlaceIsEmpty(String str) {
        return str == null || str.length() < 1 || str.compareTo("목적지 선택") == 0;
    }

    public static boolean validateHTMLStringIsEmpty(String str) {
        str = Html.fromHtml(str).toString();
        return str.trim().replace(" ","").length() < 1;
    }
}
