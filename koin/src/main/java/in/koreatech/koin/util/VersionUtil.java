package in.koreatech.koin.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by hyerim on 2018. 4. 9....
 */


public class VersionUtil {

    public static String getVersionName(Context context) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (packageInfo == null) {
            return "1.0.0";
        }
        return packageInfo.versionName;
    }

    public static int getVersionCode(Context context) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (packageInfo == null) {
            return 10000;
        }
        return packageInfo.versionCode;
    }

    public static int getVersionCodeStringToInt(String version) {
        int idx = version.indexOf(".");
        int idx2 = version.indexOf(".", 2);
        String majorCode = version.substring(0, idx);
        String subCode = version.substring(idx + 1, idx2);
        String patchCode = version.substring(idx2 + 1);
        return Integer.parseInt(majorCode
                + ((subCode.length() == 1) ? "0" : "") + subCode
                + ((patchCode.length() == 1) ? "0" : "") + patchCode);
    }
}
