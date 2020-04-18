package in.koreatech.koin;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.runner.RunWith;
import android.view.View;
import android.widget.TextView;

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
import in.koreatech.koin.ui.store.StoreActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
@RunWith(AndroidJUnit4.class)
@LargeTest
public class StoreTest {
    @Rule
    public IntentsTestRule<StoreActivity> activityRule =
            new IntentsTestRule<>(StoreActivity.class);

    @Test
    public void testcaseForAppBarTitle(){
        onView(withId(R.id.koin_base_app_bar_dark)).check(matches(hasDescendant(withText(R.string.navigation_item_store))));
    }
    @Test
    public void testcaseForCategoryName(){
        onView(withId(R.id.store_category_chicken_textview)).check(matches(withText("치킨")));
        onView(withId(R.id.store_category_pizza_textview)).check(matches(withText("asfgsfg")));
        onView(withId(R.id.store_category_sweet_pork_textview)).check(matches(withText("탕수육")));
        onView(withId(R.id.store_category_dosirak_textview)).check(matches(withText("도시락")));
        onView(withId(R.id.store_category_sweet_pork_feet_textview)).check(matches(withText("족발")));
        onView(withId(R.id.store_category_chinese_textview)).check(matches(withText("중국집")));
        onView(withId(R.id.store_category_normal_textview)).check(matches(withText("일반음식점")));
        onView(withId(R.id.store_category_hair_textview)).check(matches(withText("미용실")));
        onView(withId(R.id.store_category_etc_textview)).check(matches(withText("기타")));

    }
    @Test
    public void testcaseForButtonClick(){
        //onView(isRoot()).perform(waitFor(10000));
        //onView(withId(R.id.store_category_sweet_pork_textview)).check(matches(isDisplayed()));
        //onView(isRoot()).perform(waitFor(10000));
        onView(isRoot()).perform(waitFor(10000));
        onView(withId(R.id.store_category_dosirak_textview)).perform(click());
        onView(isRoot()).perform(waitFor(10000));

        //onView(withId(R.id.store_category_sweet_pork_textview)).check(matches(withText("탕수육")));
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
}
