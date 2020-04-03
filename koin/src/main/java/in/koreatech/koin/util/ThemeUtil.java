package in.koreatech.koin.util;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.util.Log;

import androidx.core.graphics.ColorUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ThemeUtil {
    public static boolean isDarkMode(Context context) {
        return (context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)
                == Configuration.UI_MODE_NIGHT_YES;
    }


    public static void reverseHtmlColor(StringBuilder stringBuilder) {
        Pattern pattern = Pattern.compile("<[^/!]\\s*[^/]*(?:color:\\s*(#[0-9A-F]{6})|rgb\\s*\\(\\s*([0-9]{1,3}\\s*,\\s*[0-9]{1,3}\\s*,\\s*[0-9]{1,3})\\s*\\)\\s*;)+\\s*[^>]*>");
        Matcher matcher = pattern.matcher(stringBuilder);

        while (matcher.find()) {
            String match = matcher.group(1);
            float[] outHsl = new float[3];
            if(match == null) {
                match = matcher.group(2);
                if(match == null) continue;

                String[] rgbString = match.split(", ");
                ColorUtils.colorToHSL(Color.rgb(Integer.parseInt(rgbString[0]), Integer.parseInt(rgbString[1]), Integer.parseInt(rgbString[2])), outHsl);
            } else {
                ColorUtils.colorToHSL(Color.parseColor(match), outHsl);
            }

            outHsl[2] = 1 - outHsl[2];
            stringBuilder.replace(matcher.start(), matcher.end(), matcher.group().replace(match, String.format("#%06X", ColorUtils.HSLToColor(outHsl) & 0xFFFFFF)));
        }
    }
}
