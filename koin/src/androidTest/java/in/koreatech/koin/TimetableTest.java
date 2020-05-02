package in.koreatech.koin;

import android.Manifest;
import android.view.View;
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
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TimetableTest {
    @Rule
    public IntentsTestRule<TimetableAnonymousActivity> activityRule =
            new IntentsTestRule<>(TimetableAnonymousActivity.class);

    @Rule public GrantPermissionRule permissionRule =
            GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);

    @Test
    public void testCaseForSaveToImage() { //이미지 저장 버튼 클릭
        onView(withId(R.id.timetable_save_timetable_image_linearlayout)).perform(click());
        onView(withText("저장되었습니다.")).inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testCaseForLecture() { //수업 추가 버튼 클릭
        onView(withId(R.id.base_appbar_dark_right_button)).perform(click());
        onView(withId(R.id.timetable_save_timetable_image_linearlayout)).check(matches(isDisplayed()));
    }

    @Test
    public void testCaseForMajor() { //전공 선택 클릭
        onView(withId(R.id.base_appbar_dark_right_button)).perform(click());
        onView(withId(R.id.timetable_add_category_imageview)).perform(click());
        onView(withText("전공선택")).check(matches(isDisplayed()));
    }

    @Test
    public void testCaseForSelectMajor() { //전공 클릭
        onView(withId(R.id.base_appbar_dark_right_button)).perform(click());
        onView(withId(R.id.timetable_add_category_imageview)).perform(click());
        onView(withId(R.id.select_major_computer_engineering)).perform(click());
        onView(withId(R.id.select_major_computer_engineering)).check(matches(TextColorMatcher.withTextColor(
                R.color.white, activityRule.getActivity().getResources()
        )));
    }

    @Test
    public void testCaseForSelectSemester() { //학기 선택 스피너 클릭
        onView(withId(R.id.timetable_select_semester_linearlayout)).perform(click());
        onView(withId(R.id.timetable_select_semester_bottom_sheet_center_textview)).check(matches(isDisplayed()));
    }

    @Test
    public void testCaseForAddLectureAndDelete() { //수업 추가 -> 추가된 강의 클릭 -> 강의 삭제
        //수업 추가
        onView(withId(R.id.base_appbar_dark_right_button)).perform(click());
        onView(withId(R.id.timetable_add_schedule_search_edittext)).perform(replaceText("a"));
        onView(withId(R.id.timetable_add_schedule_search_imageview)).perform(click());
        RecyclerViewMatcher recyclerViewMatcher = new RecyclerViewMatcher(R.id.timetable_search_recyclerview);
        onView(recyclerViewMatcher.atPositionOnView(0, R.id.add_lecture_button))
                .perform(click());
        String lectureName = getText(recyclerViewMatcher.atPositionOnView(0, R.id.lecture_infromation2)).split("/")[3];
        onView(withId(R.id.timetable_add_schedule_bottom_sheet_right_textview)).perform(click());

        //시간표에서 수업 클릭
        onView(isRoot()).perform(waitFor(500));
        onView(withText(lectureName)).perform(click());
        onView(withId(R.id.timetable_detail_schedule_bottom_sheet_center_textview)).check(matches(isDisplayed()));

        //수업 삭제
        onView(withId(R.id.timetable_detail_schedule_bottom_sheet_left_textview)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());

        onView(withText(lectureName)).check(matches(not(isDisplayed())));
    }

    public static ViewAction waitFor(final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "Wait for " + millis + " milliseconds.";
            }

            @Override
            public void perform(UiController uiController, final View view) {
                uiController.loopMainThreadForAtLeast(millis);
            }
        };
    }

    String getText(final Matcher<View> matcher) {
        final String[] stringHolder = { null };
        onView(matcher).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "getting text from a TextView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextView tv = (TextView)view; //Save, because of check in getConstraints()
                stringHolder[0] = tv.getText().toString();
            }
        });
        return stringHolder[0];
    }
}