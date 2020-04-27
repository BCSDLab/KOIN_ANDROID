package in.koreatech.koin;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Matchers;
import org.junit.runner.RunWith;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.platform.app.InstrumentationRegistry;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import in.koreatech.koin.ui.store.StoreActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
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
        onView(withId(R.id.koin_base_appbar)).check(matches(hasDescendant(withText(R.string.navigation_item_store))));
    }
    @Test
    public void testcaseForCategoryName(){      //카테고리 목록이 맞는지 확인
        onView(withId(R.id.store_category_chicken_textview)).check(matches(withText("치킨")));
        onView(withId(R.id.store_category_pizza_textview)).check(matches(withText("피자")));
        onView(withId(R.id.store_category_sweet_pork_textview)).check(matches(withText("탕수육")));
        onView(withId(R.id.store_category_dosirak_textview)).check(matches(withText("도시락")));
        onView(withId(R.id.store_category_sweet_pork_feet_textview)).check(matches(withText("족발")));
        onView(withId(R.id.store_category_chinese_textview)).check(matches(withText("중국집")));
        onView(withId(R.id.store_category_normal_textview)).check(matches(withText("일반음식점")));
        onView(withId(R.id.store_category_hair_textview)).check(matches(withText("미용실")));
        onView(withId(R.id.store_category_etc_textview)).check(matches(withText("기타")));

    }

    @Test
    public void testCaseForRecyclerScroll(){    //리사이클러뷰가 스크롤되는지 확인
        onView(withId(R.id.store_root_scroll_view)).inRoot(RootMatchers.withDecorView(
                Matchers.is(activityRule.getActivity().getWindow().getDecorView()))).perform(ViewActions.swipeDown());

        RecyclerView recyclerView = activityRule.getActivity().findViewById(R.id.store_recyclerview);
        int itemCount = recyclerView.getAdapter().getItemCount();

        onView(withId(R.id.store_recyclerview)).inRoot(RootMatchers.withDecorView(
                Matchers.is(activityRule.getActivity().getWindow().getDecorView()))).perform(RecyclerViewActions.scrollToPosition(itemCount-1));
    }

    @Test
    public void testCaseForRecyclerClick(){     //리사이클러뷰의 항목을 클릭하면 해당 항목의 상세화면으로 이동하는지 확인
        onView(withId(R.id.store_root_scroll_view)).inRoot(RootMatchers.withDecorView(
                Matchers.is(activityRule.getActivity().getWindow().getDecorView()))).perform(ViewActions.swipeDown());

        onView(withId(R.id.store_recyclerview)).inRoot(RootMatchers.withDecorView(
                Matchers.is(activityRule.getActivity().getWindow().getDecorView())))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1,click()));
        onView(withId(R.id.store_detail_title_textview)).check(matches(withText("가나콜밴")));

    }


    @Test
    public void testcaseForCategoryButtonClick(){       //카테고리에 속한 버튼을 클릭 시 해당 버튼이 주황색으로 변화는지 확인
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        onView(withId(R.id.store_root_scroll_view)).inRoot(RootMatchers.withDecorView(
                Matchers.is(activityRule.getActivity().getWindow().getDecorView()))).perform(ViewActions.swipeDown());

        int [] storeCategoryId = {R.id.store_category_chicken_textview, R.id.store_category_pizza_textview,R.id.store_category_sweet_pork_textview, R.id.store_category_dosirak_textview
        , R.id.store_category_sweet_pork_feet_textview, R.id.store_category_chinese_textview, R.id.store_category_normal_textview, R.id.store_category_hair_textview, R.id.store_category_etc_textview};

        for(int i=0;i<storeCategoryId.length;i++){
            onView(withId(storeCategoryId[i])).perform(click());
            onView(withId(storeCategoryId[i])).check(matches(textViewTextColorMatcher(context.getResources().getColor(R.color.colorAccent))));
        }

    }
    @Test
    public void testcaseForChickenCategory(){       //치킨 카테고리 클릭 후 아래 리사이클러뷰가 바뀌는지 확인
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        onView(withId(R.id.store_root_scroll_view)).inRoot(RootMatchers.withDecorView(
                Matchers.is(activityRule.getActivity().getWindow().getDecorView()))).perform(ViewActions.swipeDown());


        onView(withId(R.id.store_category_chicken_textview)).perform(click());
        onView(withId(R.id.store_category_chicken_textview)).check(matches(textViewTextColorMatcher(context.getResources().getColor(R.color.colorAccent))));

        onView(withId(R.id.store_recyclerview)).inRoot(RootMatchers.withDecorView(
                Matchers.is(activityRule.getActivity().getWindow().getDecorView())))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.store_detail_title_textview)).check(matches(withText("BHC천안병천점")));

    }
    @Test
    public void testcaseForPizzaCategory(){     //피자 카테고리 클릭 후 아래 리사이클러뷰가 바뀌는지 확인
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        onView(withId(R.id.store_root_scroll_view)).inRoot(RootMatchers.withDecorView(
                Matchers.is(activityRule.getActivity().getWindow().getDecorView()))).perform(ViewActions.swipeDown());

        onView(withId(R.id.store_category_pizza_textview)).perform(click());
        onView(withId(R.id.store_category_pizza_textview)).check(matches(textViewTextColorMatcher(context.getResources().getColor(R.color.colorAccent))));

        onView(withId(R.id.store_recyclerview)).inRoot(RootMatchers.withDecorView(
                Matchers.is(activityRule.getActivity().getWindow().getDecorView())))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.store_detail_title_textview)).check(matches(withText("쉐프피자")));

    }
    @Test
    public void testcaseForSweetPorkCategory(){     //탕수육 카테고리 클릭 후 아래 리사이클러뷰가 바뀌는지 확인
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        onView(withId(R.id.store_root_scroll_view)).inRoot(RootMatchers.withDecorView(
                Matchers.is(activityRule.getActivity().getWindow().getDecorView()))).perform(ViewActions.swipeDown());

        onView(withId(R.id.store_category_sweet_pork_textview)).perform(click());
        onView(withId(R.id.store_category_sweet_pork_textview)).check(matches(textViewTextColorMatcher(context.getResources().getColor(R.color.colorAccent))));

        onView(withId(R.id.store_recyclerview)).inRoot(RootMatchers.withDecorView(
                Matchers.is(activityRule.getActivity().getWindow().getDecorView())))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.store_detail_title_textview)).check(matches(withText("주르가즘")));

    }

    @Test
    public void testcaseForDosirakCategory(){       //도시락 카테고리 클릭 후 아래 리사이클러뷰가 바뀌는지 확인
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        onView(withId(R.id.store_root_scroll_view)).inRoot(RootMatchers.withDecorView(
                Matchers.is(activityRule.getActivity().getWindow().getDecorView()))).perform(ViewActions.swipeDown());

        onView(withId(R.id.store_category_dosirak_textview)).perform(click());
        onView(withId(R.id.store_category_dosirak_textview)).check(matches(textViewTextColorMatcher(context.getResources().getColor(R.color.colorAccent))));

        onView(withId(R.id.store_recyclerview)).inRoot(RootMatchers.withDecorView(
                Matchers.is(activityRule.getActivity().getWindow().getDecorView())))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.store_detail_title_textview)).check(matches(withText("고릴라밥")));


    }

    @Test
    public void testcaseForSweetPorkFeetCategory(){     //족발 카테고리 클릭 후 아래 리사이클러뷰가 바뀌는지 확인
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        onView(withId(R.id.store_root_scroll_view)).inRoot(RootMatchers.withDecorView(
                Matchers.is(activityRule.getActivity().getWindow().getDecorView()))).perform(ViewActions.swipeDown());

        onView(withId(R.id.store_category_sweet_pork_feet_textview)).perform(click());
        onView(withId(R.id.store_category_sweet_pork_feet_textview)).check(matches(textViewTextColorMatcher(context.getResources().getColor(R.color.colorAccent))));

        onView(withId(R.id.store_recyclerview)).inRoot(RootMatchers.withDecorView(
                Matchers.is(activityRule.getActivity().getWindow().getDecorView())))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1,click()));
        onView(withId(R.id.store_detail_title_textview)).check(matches(withText("궁중 왕 족발")));

    }

    @Test
    public void testcaseForChineseCategory(){       //중국집 카테고리 클릭 후 아래 리사이클러뷰가 바뀌는지 확인
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        onView(withId(R.id.store_root_scroll_view)).inRoot(RootMatchers.withDecorView(
                Matchers.is(activityRule.getActivity().getWindow().getDecorView()))).perform(ViewActions.swipeDown());

        onView(withId(R.id.store_category_chinese_textview)).perform(click());
        onView(withId(R.id.store_category_chinese_textview)).check(matches(textViewTextColorMatcher(context.getResources().getColor(R.color.colorAccent))));

        onView(withId(R.id.store_recyclerview)).inRoot(RootMatchers.withDecorView(
                Matchers.is(activityRule.getActivity().getWindow().getDecorView())))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.store_detail_title_textview)).check(matches(withText("대왕성")));

    }

    @Test
    public void testcaseForNormalCategory(){        //일반음식점 카테고리 클릭 후 아래 리사이클러뷰가 바뀌는지 확인
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        onView(withId(R.id.store_root_scroll_view)).inRoot(RootMatchers.withDecorView(
                Matchers.is(activityRule.getActivity().getWindow().getDecorView()))).perform(ViewActions.swipeDown());

        onView(withId(R.id.store_category_normal_textview)).perform(click());
        onView(withId(R.id.store_category_normal_textview)).check(matches(textViewTextColorMatcher(context.getResources().getColor(R.color.colorAccent))));

        onView(withId(R.id.store_recyclerview)).inRoot(RootMatchers.withDecorView(
                Matchers.is(activityRule.getActivity().getWindow().getDecorView())))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.store_detail_title_textview)).check(matches(withText("감성떡볶이")));

    }

    @Test
    public void testcaseForHairCategory(){      //미용실 카테고리 클릭 후 아래 리사이클러뷰가 바뀌는지 확인
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        onView(withId(R.id.store_root_scroll_view)).inRoot(RootMatchers.withDecorView(
                Matchers.is(activityRule.getActivity().getWindow().getDecorView()))).perform(ViewActions.swipeDown());

        onView(withId(R.id.store_category_hair_textview)).perform(click());
        onView(withId(R.id.store_category_hair_textview)).check(matches(textViewTextColorMatcher(context.getResources().getColor(R.color.colorAccent))));

        onView(withId(R.id.store_recyclerview)).inRoot(RootMatchers.withDecorView(
                Matchers.is(activityRule.getActivity().getWindow().getDecorView())))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.store_detail_title_textview)).check(matches(withText("그래")));

    }

    @Test
    public void testcaseForEtcCategory(){       //기타 카테고리 클릭 후 아래 리사이클러뷰가 바뀌는지 확인
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        onView(withId(R.id.store_root_scroll_view)).inRoot(RootMatchers.withDecorView(
                Matchers.is(activityRule.getActivity().getWindow().getDecorView()))).perform(ViewActions.swipeDown());

        onView(withId(R.id.store_category_etc_textview)).perform(click());
        onView(withId(R.id.store_category_etc_textview)).check(matches(textViewTextColorMatcher(context.getResources().getColor(R.color.colorAccent))));

        onView(withId(R.id.store_recyclerview)).inRoot(RootMatchers.withDecorView(
                Matchers.is(activityRule.getActivity().getWindow().getDecorView())))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.store_detail_title_textview)).check(matches(withText("가나콜밴")));

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
}
