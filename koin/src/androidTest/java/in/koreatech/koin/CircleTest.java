package in.koreatech.koin;

import android.view.View;
import android.widget.TextView;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import in.koreatech.koin.ui.circle.CircleActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CircleTest {
    @Rule
    public IntentsTestRule<CircleActivity> activityRule =
            new IntentsTestRule<>(CircleActivity.class);

    @Test
    public void testcaseForAppBarTitle(){
        onView(withId(R.id.koin_base_app_bar_dark)).check(matches(hasDescendant(withText(R.string.navigation_item_circle))));
    }

    @Test
    public void testcaseForCategoryName(){
        onView(withId(R.id.circle_all_textview)).check(matches(withText("전체보기")));
        onView(withId(R.id.circle_art_textview)).check(matches(withText("예술분야")));
        onView(withId(R.id.circle_show_textview)).check(matches(withText("공연분야")));
        onView(withId(R.id.circle_sport_textview)).check(matches(withText("운동분야")));
        onView(withId(R.id.circle_study_textview)).check(matches(withText("학술분야")));
        onView(withId(R.id.circle_religion_textview)).check(matches(withText("종교분야")));
        onView(withId(R.id.circle_service_textview)).check(matches(withText("사회봉사")));
        onView(withId(R.id.circle_etc_textview)).check(matches(withText("준동아리")));

    }
    @Test
    public void testcaseForButtonClick(){
        onView(withId(R.id.circle_art_textview)).check(matches(withText("예술분야")));
        //onView(withId(R.id.circle_all_textview)).check(matches(textViewTextColorMatcher(500104)));
        onView(withId(R.id.circle_art_linear_layout)).perform(click());
        waitFor(10000);
        //onView(withId(R.id.circle_art_textview)).perform(click());
        //Log.e("dfdfdfdf","dfdfdfdfdfdf");

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



    public static Matcher<View> textViewTextColorMatcher(final int matcherColor) {
        return new BoundedMatcher<View, TextView>(TextView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("with text color: " + matcherColor);
            }
            @Override
            protected boolean matchesSafely(TextView textView) {
                return matcherColor == textView.getCurrentTextColor();
            }
        };
    }
/*
    @Test
    public void testCaseForRecyclerScroll(){
        RecyclerView recyclerView = activityRule.getActivity().findViewById(R.id.circle_recyclerview);
        int itemCount = recyclerView.getAdapter().getItemCount();

        onView(withId(R.id.circle_recyclerview)).inRoot(RootMatchers.withDecorView(
                Matchers.is(activityRule.getActivity().getWindow().getDecorView()))).perform(RecyclerViewActions.scrollToPosition(itemCount-1));
    }

 */
 /*
    @Test
    public void testCaseForRecyclerClick(){

        onView(withId(R.id.circle_recyclerview)).inRoot(RootMatchers.withDecorView(
                Matchers.is(activityRule.getActivity().getWindow().getDecorView())))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));

        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        String key = "Land_ID";

        //RecyclerView recyclerView = activityRule.getActivity().findViewById(R.id.land_recyclerlayout);
        //int itemCount = recyclerView.getAdapter().geTgetItemCount();

        intended(hasExtra(key, 11));
        intended(toPackage("in.koreatech.koin"));
        intended(hasComponent("in.koreatech.koin.ui.land.LandDetailActivity"));

        intended(allOf(
                hasExtra("Land_ID", 11),
                toPackage("in.koreatech.koin"),
                hasComponent("in.koreatech.koin.ui.land.LandDetailActivity")));


    }

  */
}
