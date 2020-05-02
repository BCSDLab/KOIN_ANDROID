package in.koreatech.koin.matcher;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import androidx.test.espresso.matcher.BoundedMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import static org.hamcrest.Matchers.is;
public class ImageViewSrcMatcher extends BoundedMatcher<View, ImageView> {
    BitmapDrawable drawable;
    private ImageViewSrcMatcher(BitmapDrawable drawable) {
        super(ImageView.class);
        this.drawable = drawable;
    }
    public static Matcher<View> withDrawable(Drawable drawable) {
        return new ImageViewSrcMatcher((BitmapDrawable) drawable);
    }
    @Override
    protected boolean matchesSafely(ImageView item) {
        return drawable.getBitmap().equals(((BitmapDrawable) item.getDrawable()).getBitmap());
    }
    @Override
    public void describeMismatch(Object item, Description mismatchDescription) {
        mismatchDescription.appendText("ImageView with src: " +
                ((ImageView) item).getDrawable().toString() + ", expected: ");
        is(drawable).describeMismatch(item, mismatchDescription);
    }
    @Override
    public void describeTo(Description description) {
        description.appendText("with src ");
        is(drawable).describeTo(description);
    }
}
//onView(withId(R.id.YOUR_VIEW_ID)).check(matches(ImageViewSrcMatcher.withDrawable(YOUR_DRAWABLE));