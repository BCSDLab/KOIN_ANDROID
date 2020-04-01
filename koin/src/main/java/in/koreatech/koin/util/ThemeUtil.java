package in.koreatech.koin.util;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.util.Log;

import androidx.core.graphics.ColorUtils;

public class ThemeUtil {
    public static boolean isDarkMode(Context context) {
        return (context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)
                == Configuration.UI_MODE_NIGHT_YES;
    }


    public static void reverseHtmlColor(StringBuilder stringBuilder) {
        int pos = -1;
        while (pos + 1 < stringBuilder.length() && (pos = stringBuilder.indexOf("<", pos + 1)) != -1) {
            if(pos + 1 >= stringBuilder.length() || stringBuilder.charAt(pos + 1) == '/') continue;
            int closingBracePos = stringBuilder.indexOf(">", pos);
            int stylePos = stringBuilder.indexOf("style=", pos);

            if (closingBracePos < stylePos) continue;

            int colorPos = stringBuilder.indexOf("color:", pos);

            if (stylePos < colorPos && colorPos < closingBracePos) {
                int endPos;
                float[] outHsl = new float[3];
                if (stringBuilder.charAt(colorPos + 6) == '#') {
                    Log.d("Substring Color", stringBuilder.substring(colorPos + 6, colorPos + 13));
                    ColorUtils.colorToHSL(Color.parseColor(stringBuilder.substring(colorPos + 6, colorPos + 13)), outHsl);
                    endPos = colorPos + 13;
                } else {
                    endPos = stringBuilder.indexOf(")", pos) + 1;
                    Log.d("Substring Color", stringBuilder.substring(
                            stringBuilder.indexOf("rgb(", pos) + 4,
                            stringBuilder.indexOf(")", pos)
                    ));
                    String[] rgbString = stringBuilder.substring(
                            stringBuilder.indexOf("rgb(", pos) + 4,
                            stringBuilder.indexOf(")", pos)
                    ).split(", ");
                    ColorUtils.colorToHSL(
                            Color.rgb(Integer.parseInt(rgbString[0]), Integer.parseInt(rgbString[1]), Integer.parseInt(rgbString[2])),
                            outHsl);
                }

                outHsl[2] = 1 - outHsl[2];
                String replaced = String.format("#%06X", ColorUtils.HSLToColor(outHsl) & 0xFFFFFF);
                stringBuilder.replace(colorPos + 6, endPos, replaced);
            }
        }
    }
}
