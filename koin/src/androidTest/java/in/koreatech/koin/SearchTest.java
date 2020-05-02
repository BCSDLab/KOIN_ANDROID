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

import in.koreatech.koin.ui.search.SearchActivity;
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
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SearchTest {
    @Rule
    public IntentsTestRule<SearchActivity> activityRule =
            new IntentsTestRule<>(SearchActivity.class);

    @Test
    public void testCaseForRemoveSearch() { //x를 눌러 검색어 지우기
        typeSomething();
        onView(withId(R.id.base_appbar_dark_erase_button)).perform(click());
        onView(withId(R.id.base_appbar_dark_title)).check(matches(withText("")));
    }

    @Test
    public void testCaseForSearch() { //검색어 입력 후 검색 버튼 클릭
        search();
        onView(anyOf(withId(R.id.search_result_textview), withText("일치하는 검색 결과가 없습니다."))).check(matches(isDisplayed()));
    }

    @Test
    public void testCaseForDeleteSearch() { //검색 후 검색어 지우기
        search();
        onView(withId(R.id.base_appbar_dark_title)).perform(replaceText(""));
        onView(withId(R.id.search_recent_textview)).check(matches(isDisplayed()));
    }

    private void typeSomething() {
        onView(withId(R.id.base_appbar_dark_title)).perform(typeText("asdf"));
    }

    private void search() {
        typeSomething();
        onView(withId(R.id.base_appbar_dark_right_button)).perform(click());
    }

}