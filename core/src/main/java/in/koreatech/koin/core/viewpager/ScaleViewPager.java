package in.koreatech.koin.core.viewpager;

import android.content.Context;
import android.util.AttributeSet;

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
        float density = context.getResources().getDisplayMetrics().density;
        int pageMargin = (int) (8 * density);

        setPageMargin(pageMargin);
    }

}
