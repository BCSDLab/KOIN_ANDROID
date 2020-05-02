package in.koreatech.koin;

import android.view.View;
import android.widget.Spinner;

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

import in.koreatech.koin.ui.bus.BusActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class BusTest {
    @Rule
    public IntentsTestRule<BusActivity> activityRule =
            new IntentsTestRule<>(BusActivity.class);

    @Test
    public void testCaseForBusInfo() { //운행 정보 클릭 테스트
        onView(withId(R.id.bus_main_tabs))
                .perform(selectTabAtPosition(0))
                .check(matches(checkTabSelectedAndText(R.id.bus_main_tabs, 0, "운행정보", true)));

        onView(withId(R.id.bus_main_fragment_shuttle)).check(matches(isDisplayed()));
    }

    @Test
    public void testCaseForBusSearchInfo() { //운행 정보 검색 클릭 테스트
        onView(withId(R.id.bus_main_tabs))
                .perform(selectTabAtPosition(1))
                .check(matches(checkTabSelectedAndText(R.id.bus_main_tabs, 1, "운행 정보 검색", true)));

        onView(withId(R.id.bus_search_timePicker)).check(matches(isDisplayed()));
    }

    @Test
    public void testCaseForBusTimetableInfo() { //시간표 클릭 테스트
        onView(withId(R.id.bus_main_tabs))
                .perform(selectTabAtPosition(2))
                .check(matches(checkTabSelectedAndText(R.id.bus_main_tabs, 2, "시간표", true)));

        onView(withId(R.id.bus_timetable_bustype_shuttle)).check(matches(isDisplayed()));
    }

    @Test
    public void testCaseForCalendarDialog() { //날짜 아이콘 클릭 시 달력 팝업 표시
        onView(withId(R.id.bus_main_tabs))
                .perform(selectTabAtPosition(1));

        onView(withId(R.id.bus_timetable_search_date_imageButton)).perform(click());

        onView(withId(android.R.id.button1)).check(matches(isDisplayed()));
    }

    @Test
    public void testCaseForSearchDialog() { //날짜 아이콘 클릭 시 달력 팝업 표시
        onView(withId(R.id.bus_main_tabs))
                .perform(selectTabAtPosition(1));

        onView(withId(R.id.bus_timetable_search_fragment_search_button)).perform(click());

        onView(withId(R.id.bus_timetable_search_result_shuttle_bus_textview)).check(matches(isDisplayed()));
    }

    @Test
    public void testCaseForBusSpinnerClick() {  //Spinner 클릭 후 버스 정보와 일치하는지 확인
        String[] busPositionStrings = {"한기대", "야우리", "천안역"};
        onView(withId(R.id.bus_main_tabs))
                .perform(selectTabAtPosition(0));

        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                if(i != j) {
                    onView(withId(R.id.bus_main_fragment_bus_departure_spinner)).perform(selectSpinnerPosition(i));
                    onView(withId(R.id.bus_main_fragment_bus_arrival_spinner)).perform(selectSpinnerPosition(j));

                    onView(withId(R.id.bus_main_fragment_shuttle_departure_textview)).check(matches(withText(busPositionStrings[i])));
                    onView(withId(R.id.bus_main_fragment_shuttle_arrival_textview)).check(matches(withText(busPositionStrings[j])));
                }
            }
        }
    }

    @Test
    public void testCaseForTimetableSubTabClick() { //학교셔틀, 대성, 시내버스 탭 클릭 테스트
        onView(withId(R.id.bus_main_tabs))
                .perform(selectTabAtPosition(2));

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

    private ViewAction selectTabAtPosition(int position) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return Matchers.allOf(isDisplayed(), isAssignableFrom(TabLayout.class));
            }

            @Override
            public String getDescription() {
                return "with tab at index" + position;
            }

            @Override
            public void perform(UiController uiController, View view) {
                TabLayout tabLayout = (TabLayout) view;
                TabLayout.Tab tab = tabLayout.getTabAt(position);
                if(tab == null) throw new PerformException.Builder()
                        .withCause(new Throwable("No tab at index $tabIndex"))
                        .build();
                else tab.select();
            }
        };
    }

    private BoundedMatcher<View, TabLayout> checkTabSelectedAndText(@IdRes int tabLayoutId, int position, String tabText, boolean isSelected) {
        return new BoundedMatcher<View, TabLayout>(TabLayout.class) {
            @Override
            protected boolean matchesSafely(TabLayout item) {
                TabLayout.Tab tab = item.getTabAt(position);
                if(tab == null) throw new PerformException.Builder()
                        .withCause(new Throwable("No tab at index $tabIndex"))
                        .build();
                else return tab.isSelected() == isSelected && tab.getText().equals(tabText);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with selected tab check");
                is(onView(withId(tabLayoutId))).describeTo(description);
            }
        };
    }

    private ViewAction selectSpinnerPosition(int position) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return Matchers.allOf(isDisplayed(), isAssignableFrom(Spinner.class));
            }

            @Override
            public String getDescription() {
                return "with spinner at index" + position;
            }

            @Override
            public void perform(UiController uiController, View view) {
                Spinner spinner = (Spinner) view;
                spinner.setSelection(position);
            }
        };
    }
}



