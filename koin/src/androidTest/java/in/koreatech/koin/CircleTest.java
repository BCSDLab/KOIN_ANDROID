package in.koreatech.koin;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import in.koreatech.koin.ui.circle.CircleActivity;
import in.koreatech.koin.ui.land.LandActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

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
    public void testCaseForRecyclerScroll(){
        RecyclerView recyclerView = activityRule.getActivity().findViewById(R.id.circle_recyclerview);
        int itemCount = recyclerView.getAdapter().getItemCount();

        onView(withId(R.id.circle_recyclerview)).inRoot(RootMatchers.withDecorView(
                Matchers.is(activityRule.getActivity().getWindow().getDecorView()))).perform(RecyclerViewActions.scrollToPosition(itemCount-1));
    }
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
}
