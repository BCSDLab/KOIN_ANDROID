package in.koreatech.koin.core.util.timetable;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;
import android.util.TypedValue;

public class CustomTypefaceSpan extends TypefaceSpan {
    private final Typeface newType;
    private int fontSize;
    private Context context;

    public CustomTypefaceSpan(String family, Typeface type, int fontSize, Context context) {
        super(family);
        this.context = context;
        newType = type;
        this.fontSize = fontSize;

    }

    @Override
    public void updateDrawState(TextPaint ds) {
        float scaledSizeInPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                fontSize, context.getResources().getDisplayMetrics());
        ds.setTextSize(scaledSizeInPixels);
        applyCustomTypeFace(ds, newType);
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
        float scaledSizeInPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                fontSize, context.getResources().getDisplayMetrics());
        paint.setTextSize(scaledSizeInPixels);
        applyCustomTypeFace(paint, newType);
    }

    private static void applyCustomTypeFace(Paint paint, Typeface tf) {
        int oldStyle;
        Typeface old = paint.getTypeface();
        if (old == null) {
            oldStyle = 0;
        } else {
            oldStyle = old.getStyle();
        }

        int fake = oldStyle & ~tf.getStyle();
        if ((fake & Typeface.BOLD) != 0) {
            paint.setFakeBoldText(true);
        }

        if ((fake & Typeface.ITALIC) != 0) {
            paint.setTextSkewX(-0.25f);
        }
        paint.setTypeface(tf);
    }
}

