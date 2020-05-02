package in.koreatech.koin;

import android.view.View;
import android.widget.Spinner;
import android.widget.TableLayout;

import androidx.annotation.IdRes;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.google.android.material.tabs.TabLayout;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import in.koreatech.koin.matcher.TabLayoutMatcher;
import in.koreatech.koin.ui.bus.BusActivity;
import in.koreatech.koin.viewaction.TabLayoutViewAction;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static in.koreatech.koin.matcher.TabLayoutMatcher.checkTabSelectedAndText;
import static in.koreatech.koin.viewaction.SpinnerViewAction.selectSpinnerPosition;
import static in.koreatech.koin.viewaction.TabLayoutViewAction.selectTabAtPosition;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class BusInstrumentedTest {
    @Rule
    public IntentsTestRule<BusActivity> activityRule =
            new IntentsTestRule<>(BusActivity.class);

    @Test
    public void testCaseForBusInfo() { //운행 정보 클릭 테스트
        onView(withId(R.id.bus_main_tabs))
                .perform(selectTabAtPosition(0))
                .check(matches(checkTabSelectedAndText(R.id.bus_main_tabs, 0, "운행정보", true)));
        onView(isRoot()).perform(waitFor(1000));

        onView(withId(R.id.bus_main_fragment_shuttle)).check(matches(isDisplayed()));
    }

    @Test
    public void testCaseForBusSearchInfo() { //운행 정보 검색 클릭 테스트
        onView(withId(R.id.bus_main_tabs))
                .perform(selectTabAtPosition(1))
                .check(matches(checkTabSelectedAndText(R.id.bus_main_tabs, 1, "운행 정보 검색", true)));
        onView(isRoot()).perform(waitFor(1000));

        onView(withId(R.id.bus_search_timePicker)).check(matches(isDisplayed()));
    }

    @Test
    public void testCaseForBusTimetableInfo() { //시간표 클릭 테스트
        onView(withId(R.id.bus_main_tabs))
                .perform(selectTabAtPosition(2))
                .check(matches(checkTabSelectedAndText(R.id.bus_main_tabs, 2, "시간표", true)));
        onView(isRoot()).perform(waitFor(1000));

        onView(withId(R.id.bus_timetable_bustype_shuttle)).check(matches(isDisplayed()));
    }

    @Test
    public void testCaseForCalendarDialog() { //날짜 아이콘 클릭 시 달력 팝업 표시
        onView(withId(R.id.bus_main_tabs))
                .perform(selectTabAtPosition(1));
        onView(isRoot()).perform(waitFor(1000));

        onView(withId(R.id.bus_timetable_search_date_imageButton)).perform(click());

        onView(withId(android.R.id.button1)).check(matches(isDisplayed()));
    }

    @Test
    public void testCaseForSearchDialog() { //날짜 아이콘 클릭 시 달력 팝업 표시
        onView(withId(R.id.bus_main_tabs))
                .perform(selectTabAtPosition(1));
        onView(isRoot()).perform(waitFor(1000));

        onView(withId(R.id.bus_timetable_search_fragment_search_button)).perform(click());

        onView(withId(R.id.bus_timetable_search_result_shuttle_bus_textview)).check(matches(isDisplayed()));
    }

    @Test
    public void testCaseForTimetableSubTabClick() throws InterruptedException { //학교셔틀, 대성, 시내버스 탭 클릭 테스트
        onView(withId(R.id.bus_main_tabs))
                .perform(selectTabAtPosition(2));
        onView(isRoot()).perform(waitFor(1000));

        onView(withId(R.id.bus_timetable_bustype_shuttle)).perform(click());

        onView(withId(R.id.bus_timetable_fragment_cheonan_start_endspinner)).check(matches(isDisplayed()));
        onView(withId(R.id.bus_timetable_fragment_daesung_spinner)).check(matches(not(isDisplayed())));
        onView(withId(R.id.bus_timetable_fragment_cheonan_start_endspinner)).check(matches(isDisplayed()));

        onView(withId(R.id.bus_timetable_bustype_daesung)).perform(click());

        onView(withId(R.id.bus_timetable_fragment_cheonan_start_endspinner)).check(matches(not(isDisplayed())));
        onView(withId(R.id.bus_timetable_fragment_daesung_spinner)).check(matches(isDisplayed()));
        onView(withId(R.id.bus_timetable_fragment_cheonan_start_endspinner)).check(matches(not(isDisplayed())));

        onView(withId(R.id.bus_timetable_bustype_city)).perform(click());

        onView(withId(R.id.bus_timetable_fragment_cheonan_start_endspinner)).check(matches(not(isDisplayed())));
        onView(withId(R.id.bus_timetable_fragment_daesung_spinner)).check(matches(not(isDisplayed())));
        onView(withId(R.id.bus_timetable_fragment_cheonan_start_endspinner)).check(matches(not(isDisplayed())));
    }

    private static ViewAction waitFor(final long millis) {
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
}



