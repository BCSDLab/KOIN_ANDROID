package in.koreatech.koin;

import android.Manifest;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import in.koreatech.koin.matcher.TextColorMatcher;
import in.koreatech.koin.matcher.ToastMatcher;
import in.koreatech.koin.ui.signup.SignupActivity;
import in.koreatech.koin.ui.timetable.TimetableActivity;
import in.koreatech.koin.ui.timetable.TimetableAnonymousActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TimetableAnonymousInstrumentedTest {
    @Rule
    public IntentsTestRule<TimetableAnonymousActivity> activityRule =
            new IntentsTestRule<>(TimetableAnonymousActivity.class);

    @Rule
    public GrantPermissionRule permissionRule =
            GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);

    @Test
    public void testCaseForSaveToImage() { //이미지 저장 버튼 클릭
        onView(withId(R.id.timetable_save_timetable_image_linearlayout)).perform(click());
        onView(withText("저장되었습니다.")).inRoot(ToastMatcher.withMessage("저장되었습니다."))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testCaseForLecture() { //수업 추가 버튼 클릭
        viewAddLecture();
        onView(withId(R.id.timetable_save_timetable_image_linearlayout)).check(matches(isDisplayed()));
    }

    @Test
    public void testCaseForMajor() { //전공 선택 클릭
        viewAddLecture();
        viewSelectMajor();
        onView(withText("전공선택")).check(matches(isDisplayed()));
    }

    @Test
    public void testCaseForSelectMajor() { //전공 클릭
        viewAddLecture();
        viewSelectMajor();
        onView(withId(R.id.select_major_computer_engineering)).perform(click());
        onView(withId(R.id.select_major_computer_engineering)).check(matches(TextColorMatcher.textViewTextColorMatcher(
                activityRule.getActivity().getResources().getColor(R.color.white)
        )));
    }

    @Test
    public void testCaseForSelectSemester() { //학기 선택 스피너 클릭
        onView(withId(R.id.timetable_select_semester_linearlayout)).perform(click());
        onView(withId(R.id.timetable_select_semester_bottom_sheet_center_textview)).check(matches(isDisplayed()));
    }

    private void viewAddLecture() {
        onView(withId(R.id.base_appbar_dark_right_button)).perform(click());
    }

    private void viewSelectMajor() {
        onView(withId(R.id.timetable_add_category_imageview)).perform(click());
    }
}