package in.koreatech.koin.core.viewpager;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

public class ScaleCardPagerTransformer implements ViewPager.PageTransformer {
    private float baseScale;
    private float smallScale;
    private float startOffset;

    public ScaleCardPagerTransformer(float baseScale, float smallScale, float startOffset) {
        this.baseScale = baseScale;
        this.smallScale = smallScale;
        this.startOffset = startOffset;
    }

    @Override
    public void transformPage(View page, float position) {
        float absPosition = Math.abs(position - startOffset);

        if (absPosition >= 1) {
            page.setScaleX(smallScale);
            page.setScaleY(smallScale);
        } else {
            // This will be during transformation
            page.setScaleX((smallScale - 1) * absPosition + 1);
            page.setScaleY((smallScale - 1) * absPosition + 1);
        }
    }
}
