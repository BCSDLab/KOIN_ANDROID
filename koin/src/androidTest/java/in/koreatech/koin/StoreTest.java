package in.koreatech.koin;

import android.content.Context;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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
    public IntentsTestRule<StoreActivity> activityRule = new IntentsTestRule<>(StoreActivity.class);

    private int[] categoryTextViewId = {R.id.store_category_chicken_textview, R.id.store_category_pizza_textview, R.id.store_category_sweet_pork_textview, R.id.store_category_dosirak_textview,
            R.id.store_category_sweet_pork_feet_textview, R.id.store_category_chinese_textview, R.id.store_category_normal_textview, R.id.store_category_hair_textview, R.id.store_category_etc_textview};
    private String[] categoryText = {"치킨", "피자", "탕수육", "도시락", "족발", " 중국집", "일반음식점", " 미용실", "기타"};

    /**
     * 앱바 타이틀 검사
     */
    @Test
    public void testcaseForAppBarTitle() {
        onView(withId(R.id.koin_base_appbar)).check(matches(hasDescendant(withText(R.string.navigation_item_store))));
    }

    /**
     * 카테고리 목록 글자 검사
     */
    @Test
    public void testcaseForCategoryName() {
        for (int i = 0; i < categoryText.length; i++) {
            onView(withId(categoryTextViewId[i])).check(matches(withText(categoryText[i])));
        }
    }

    /**
     * 주변상점 상세화면 이동 테스트
     */
    @Test
    public void testcaseForMoveDetail() {
        onView(withId(R.id.store_root_scroll_view)).perform(ViewActions.swipeDown());

        onView(atPositionOnView(R.id.store_recyclerview, 1, R.id.store_name_textview)).perform(click());
        onView(withId(R.id.store_detail_title_textview)).check(matches(isDisplayed()));
    }


    /**
     * 카테고리에 속한 버튼 클릭 시 클릭한 버튼이 주황색으로 변화는지 확인
     */
    @Test
    public void testcaseForCategoryButtonClick() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        onView(withId(R.id.store_root_scroll_view)).perform(ViewActions.swipeDown());

        for (int i = 0; i < categoryTextViewId.length; i++) {
            onView(withId(categoryTextViewId[i])).perform(click());
            onView(withId(categoryTextViewId[i])).check(matches(textViewTextColorMatcher(context.getResources().getColor(R.color.colorAccent))));
        }

    }


}
