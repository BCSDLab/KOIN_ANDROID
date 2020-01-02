package in.koreatech.koin.util;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hyerim on 2018. 4. 9....
 */
public class FilterUtil implements InputFilter {
    public final static String FILTER_E_N_H = "^[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\\u318D\\u119E\\u11A2\\u2022\\u2025a\\u00B7\\uFE55]+$";
    public final static String FILTER_E_H = "^[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z\\u318D\\u119E\\u11A2\\u2022\\u2025a\\u00B7\\uFE55]+$";
    public final static String FILTER_PASSWORD = "^(?=.*[a-zA-Z])(?=.*[`₩~!@#$%<>^&*()\\-=+_?<>:;\"',.{}|[]/\\\\]])(?=.*[0-9]).{6,18}$";
    public final static String FILTER_EMAIL = "^[a-z_0-9]{1,12}$";

    private final Pattern mPattern;

    public FilterUtil(String str) {
        if (str.compareTo(FILTER_E_N_H) == 0) {
            mPattern = Pattern.compile(FILTER_E_N_H);
        } else if (str.compareTo(FILTER_E_H) == 0) {
            mPattern = Pattern.compile(FILTER_E_H);
        } else {
            mPattern = Pattern.compile(FILTER_E_N_H);
        }
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        if (source.equals("") || mPattern.matcher(source).matches()) {
            return source;
        }

//        ToastUtil.makeShortToast(mContext, "입력할 수 없는 문자입니다");
        return "";
    }

    //비밀번호가 사용 가능한지 체크하는 메서드, 특수문자 1개 이상, 6~18
    public static boolean isPasswordValidate(String password) {
        Matcher matcher = Pattern.compile(FILTER_PASSWORD).matcher(password);
        return matcher.matches();
    }

    // 이메일이 portal email일과 같은지 체크하는 메서드, 1글자 이상 9글자 이하 특수 문자 '-' 만 허용
    public static boolean isEmailValidate(String email) {
        Matcher matcher = Pattern.compile(FILTER_EMAIL).matcher(email);
        return matcher.matches();
    }


}
