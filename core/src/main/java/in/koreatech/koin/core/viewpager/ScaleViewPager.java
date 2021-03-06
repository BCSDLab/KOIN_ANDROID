package in.koreatech.koin.core.viewpager;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class ScaleViewPager extends ViewPager {
    public ScaleViewPager(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ScaleViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        Point point = new Point();
        ((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(point);
        float startOffset = (float)(getPaddingLeft())/(point.x - 2 * getPaddingLeft());

        setPageTransformer(false, new ScaleCardPagerTransformer(1.0f, 0.9f, startOffset));
    }
}
