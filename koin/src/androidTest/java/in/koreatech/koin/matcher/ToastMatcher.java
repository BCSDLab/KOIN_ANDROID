package in.koreatech.koin.matcher;

import android.os.IBinder;
import android.view.WindowManager;

import androidx.test.espresso.Root;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class ToastMatcher extends TypeSafeMatcher<Root> {
    private String message;

    private ToastMatcher(String message) {
        this.message = message;
    }

    public static ToastMatcher withMessage(String message){
        return new ToastMatcher(message);
    }
    @Override
    protected boolean matchesSafely(Root root) {
        int type = root.getWindowLayoutParams().get().type;
        if (type == WindowManager.LayoutParams.TYPE_TOAST) {
            IBinder windowToken = root.getDecorView().getWindowToken();
            IBinder appToken = root.getDecorView().getApplicationWindowToken();
            if (windowToken == appToken) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(message);
    }
}
//ex)
// onView(withText("토스트메시지")).inRoot(withMessage("토스트메시지")).check(matches(isDisplayed()));
