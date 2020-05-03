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
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static in.koreatech.koin.matcher.RecyclerViewMatcher.atPositionOnView;
import static in.koreatech.koin.matcher.TextColorMatcher.textViewTextColorMatcher;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class StoreTest {
    @Rule
    public IntentsTestRule<StoreActivity> activityRule =
            new IntentsTestRule<>(StoreActivity.class);

    /**
     * 앱바 타이틀 검사
     */
    @Test
    public void testcaseForAppBarTitle(){
        onView(withId(R.id.koin_base_appbar)).check(matches(hasDescendant(withText(R.string.navigation_item_store))));
    }

    /**
     * 카테고리 목록 글자 검사
     */
    @Test
    public void testcaseForCategoryName(){
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

    /**
     * 주변상점 상세화면 이동 테스트
     */
    @Test
    public void testcaseForMoveDetail() {
        onView(withId(R.id.store_root_scroll_view)).perform(ViewActions.swipeDown());

        onView(atPositionOnView(R.id.store_recyclerview, 1, R.id.store_name_textview))
                .perform(click());

        onView(withId(R.id.store_detail_title_textview)).check(matches(isDisplayed()));
    }




    /**
     * 카테고리에 속한 버튼 클릭 시 클릭한 버튼이 주황색으로 변화는지 확인
     */
    @Test
    public void testcaseForCategoryButtonClick(){
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        onView(withId(R.id.store_root_scroll_view)).perform(ViewActions.swipeDown());
        int [] storeCategoryId = {R.id.store_category_chicken_textview, R.id.store_category_pizza_textview,R.id.store_category_sweet_pork_textview, R.id.store_category_dosirak_textview
                , R.id.store_category_sweet_pork_feet_textview, R.id.store_category_chinese_textview, R.id.store_category_normal_textview, R.id.store_category_hair_textview, R.id.store_category_etc_textview};

        for(int i=0;i<storeCategoryId.length;i++){
            onView(withId(storeCategoryId[i])).perform(click());
            onView(withId(storeCategoryId[i])).check(matches(textViewTextColorMatcher(context.getResources().getColor(R.color.colorAccent))));
        }

    }

    /**
     * 카테고리 치킨 버튼 클릭 후 리사이클러뷰의 첫번째 항목이 BHC천안병천점인지 확인
     */
    @Test
    public void testcaseForChickenCategory(){
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        onView(withId(R.id.store_root_scroll_view)).perform(ViewActions.swipeDown());
        //치킨 글자가 주황색으로 변경되는지 확인
        onView(withId(R.id.store_category_chicken_textview)).perform(click());
        onView(withId(R.id.store_category_chicken_textview)).check(matches(textViewTextColorMatcher(context.getResources().getColor(R.color.colorAccent))));

        //치킨 버튼 클릭 후 리사이클러뷰의 첫전째 항목이 BHC천안병천점인지 확인
        onView(withId(R.id.store_recyclerview)).inRoot(RootMatchers.withDecorView(
                Matchers.is(activityRule.getActivity().getWindow().getDecorView())))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.store_detail_title_textview)).check(matches(withText("BHC천안병천점")));

    }
    /**
     * 카테고리 피자 버튼 클릭 후 리사이클러뷰의 첫번째 항목이 쉐프피자인지 확인
     */
    @Test
    public void testcaseForPizzaCategory(){     //피자 카테고리 클릭 후 아래 리사이클러뷰가 바뀌는지 확인
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        onView(withId(R.id.store_root_scroll_view)).perform(ViewActions.swipeDown());
        //피자 글자가 주황색으로 변경되는지 확인
        onView(withId(R.id.store_category_pizza_textview)).perform(click());
        onView(withId(R.id.store_category_pizza_textview)).check(matches(textViewTextColorMatcher(context.getResources().getColor(R.color.colorAccent))));

        //피자 버튼 클릭 후 리사이클러뷰의 첫번째 항목이 쉐프피자인지 확인
        onView(withId(R.id.store_recyclerview)).inRoot(RootMatchers.withDecorView(
                Matchers.is(activityRule.getActivity().getWindow().getDecorView())))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.store_detail_title_textview)).check(matches(withText("쉐프피자")));

    }
    /**
     * 카테고리 탕수육 버튼 클릭 후 리사이클러뷰의 첫번째 항목이 주르가즘인지 확인
     */
    @Test
    public void testcaseForSweetPorkCategory(){
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        onView(withId(R.id.store_root_scroll_view)).perform(ViewActions.swipeDown());
        //탕수육 글자가 주황색으로 변경되는지 확인
       onView(withId(R.id.store_category_sweet_pork_textview)).perform(click());
        onView(withId(R.id.store_category_sweet_pork_textview)).check(matches(textViewTextColorMatcher(context.getResources().getColor(R.color.colorAccent))));

        //탕수육 버튼 클릭 후 리사이클러뷰의 첫번째 항목이 주르가즘인지 확인
        onView(withId(R.id.store_recyclerview)).inRoot(RootMatchers.withDecorView(
                Matchers.is(activityRule.getActivity().getWindow().getDecorView())))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.store_detail_title_textview)).check(matches(withText("주르가즘")));

    }
    /**
     * 카테고리 도시락 버튼 클릭 후 리사이클러뷰의 첫번째 항목이 고릴라밥인지 확인
     */
    @Test
    public void testcaseForDosirakCategory(){
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        onView(withId(R.id.store_root_scroll_view)).perform(ViewActions.swipeDown());
        //도시락 글자가 주황색으로 변경되는지 확인
        onView(withId(R.id.store_category_dosirak_textview)).perform(click());
        onView(withId(R.id.store_category_dosirak_textview)).check(matches(textViewTextColorMatcher(context.getResources().getColor(R.color.colorAccent))));

        //도시락 버튼 클릭 후 리사이클러뷰의 첫번째 항목이 고릴라밥인지 확인
       onView(withId(R.id.store_recyclerview)).inRoot(RootMatchers.withDecorView(
                Matchers.is(activityRule.getActivity().getWindow().getDecorView())))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.store_detail_title_textview)).check(matches(withText("고릴라밥")));


    }
    /**
     * 카테고리 족발 버튼 클릭 후 리사이클러뷰의 두번째 항목이 궁중 왕 족발인지 확인
     */
    @Test
    public void testcaseForSweetPorkFeetCategory(){
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        onView(withId(R.id.store_root_scroll_view)).perform(ViewActions.swipeDown());
        //족발 글자가 주황색으로 변경되는지 확인
        onView(withId(R.id.store_category_sweet_pork_feet_textview)).perform(click());
        onView(withId(R.id.store_category_sweet_pork_feet_textview)).check(matches(textViewTextColorMatcher(context.getResources().getColor(R.color.colorAccent))));

        //족발 버튼 클릭 후 리사이클러뷰의 두번째 항목이 궁중 왕 족발인지 확인
        onView(withId(R.id.store_recyclerview)).inRoot(RootMatchers.withDecorView(
                Matchers.is(activityRule.getActivity().getWindow().getDecorView())))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1,click()));
        onView(withId(R.id.store_detail_title_textview)).check(matches(withText("궁중 왕 족발")));

    }
    /**
     * //카테고리 중국집 버튼 클릭 후 리사이클러뷰의 첫번째 항목이 대왕성인지 확인
     */
    @Test
    public void testcaseForChineseCategory(){
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        onView(withId(R.id.store_root_scroll_view)).perform(ViewActions.swipeDown());
        //중국집 글자가 주황색으로 변경되는지 확인
        onView(withId(R.id.store_category_chinese_textview)).perform(click());
        onView(withId(R.id.store_category_chinese_textview)).check(matches(textViewTextColorMatcher(context.getResources().getColor(R.color.colorAccent))));

        //중국집 버튼 클릭 후 리사이클러뷰의 첫번째 항목이 대왕성인지 확인
        onView(withId(R.id.store_recyclerview)).inRoot(RootMatchers.withDecorView(
                Matchers.is(activityRule.getActivity().getWindow().getDecorView())))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.store_detail_title_textview)).check(matches(withText("대왕성")));

    }
    /**
     * //카테고리 일반음식점 버튼 클릭 후 리사이클러뷰의 첫번째 항목이 감성떡볶이인지 확인
     */
    @Test
    public void testcaseForNormalCategory(){
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        onView(withId(R.id.store_root_scroll_view)).perform(ViewActions.swipeDown());
        //일반음식점 글자가 주황색으로 변경되는지 확인
        onView(withId(R.id.store_category_normal_textview)).perform(click());
        onView(withId(R.id.store_category_normal_textview)).check(matches(textViewTextColorMatcher(context.getResources().getColor(R.color.colorAccent))));

        //일반음식점 버튼 클릭 후 리사이클러뷰의 첫번째 항목이 감성떡볶이인지 확인
        onView(withId(R.id.store_recyclerview)).inRoot(RootMatchers.withDecorView(
                Matchers.is(activityRule.getActivity().getWindow().getDecorView())))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.store_detail_title_textview)).check(matches(withText("감성떡볶이")));

    }
    /**
     * //카테고리 미용실 버튼 클릭 후 리사이클러뷰의 첫번째 항목이 그래인지 확인
     */
    @Test
    public void testcaseForHairCategory(){
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        onView(withId(R.id.store_root_scroll_view)).perform(ViewActions.swipeDown());
        //미용실 글자가 주황색으로 변경되는지 확인
        onView(withId(R.id.store_category_hair_textview)).perform(click());
        onView(withId(R.id.store_category_hair_textview)).check(matches(textViewTextColorMatcher(context.getResources().getColor(R.color.colorAccent))));

        //미용실 버튼 클릭 후 리사이클러뷰의 첫번째 항목이 그래인지 확인
        onView(withId(R.id.store_recyclerview)).inRoot(RootMatchers.withDecorView(
                Matchers.is(activityRule.getActivity().getWindow().getDecorView())))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.store_detail_title_textview)).check(matches(withText("그래")));

    }
    /**
     * //카테고리 기타 버튼 클릭 후 리사이클러뷰의 첫번째 항목이 가나콜밴인지 확인
     */
    @Test
    public void testcaseForEtcCategory(){
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        onView(withId(R.id.store_root_scroll_view)).perform(ViewActions.swipeDown());
        //기타 글자가 주황색으로 변경되는지 확인
        onView(withId(R.id.store_category_etc_textview)).perform(click());
        onView(withId(R.id.store_category_etc_textview)).check(matches(textViewTextColorMatcher(context.getResources().getColor(R.color.colorAccent))));

        //기타 버튼 클릭 후 리사이클러뷰의 첫번째 항목이 가나콜밴인지 확인
        onView(withId(R.id.store_recyclerview)).inRoot(RootMatchers.withDecorView(
                Matchers.is(activityRule.getActivity().getWindow().getDecorView())))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.store_detail_title_textview)).check(matches(withText("가나콜밴")));

    }


}
