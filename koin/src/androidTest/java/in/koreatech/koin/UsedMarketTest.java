package in.koreatech.koin;

import android.Manifest;
import android.view.View;
import android.widget.Spinner;

import androidx.annotation.IdRes;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import com.google.android.material.tabs.TabLayout;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import in.koreatech.koin.ui.bus.BusActivity;
import in.koreatech.koin.ui.usedmarket.MarketUsedActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static in.koreatech.koin.CircleTest.waitFor;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UsedMarketTest {
    @Rule
    public IntentsTestRule<MarketUsedActivity> activityRule =
            new IntentsTestRule<>(MarketUsedActivity.class);

    /*@Rule public GrantPermissionRule permissionRule =
            GrantPermissionRule.grant(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.CAMERA);*/

    @Test
    public void testcaseForBackButton() {
        onView(isRoot()).perform(pressBack());
    }

    @Test
    public void testcaseForAppBarBackButton() {
        onView(withId(R.id.koin_base_app_bar_dark)).perform(click());
    }


    @Test
    public void testCaseForSell() { //팝니다 클릭 테스트
        onView(withId(R.id.market_used_main_tabs))
                .perform(selectTabAtPosition(0))
                .check(matches(checkTabSelectedAndText(R.id.market_used_main_tabs, 0, "팝니다", true)));

        onView(withId(R.id.market_used_sell_recyclerview)).check(matches(isDisplayed()));
    }

    @Test
    public void testCaseForBusSearchInfo() { //운행 정보 검색 클릭 테스트
        onView(withId(R.id.market_used_main_tabs))
                .perform(selectTabAtPosition(1))
                .check(matches(checkTabSelectedAndText(R.id.market_used_main_tabs, 1, "삽니다", true)));

        onView(withId(R.id.market_used_buy_recyclerview)).check(matches(isDisplayed()));
    }

    /*@Test
    public void testCaseForNewSellButton() { //팝니다 글 작성 버튼 클릭릭
        onView(withId(R.id.market_used_main_tabs))
                .perform(selectTabAtPosition(0))
                .check(matches(checkTabSelectedAndText(R.id.market_used_main_tabs, 0, "팝니다", true)));

        onView(withId(R.id.base_appbar_dark_right_button)).check(matches(isDisplayed())).perform(click());

        waitFor(10000);
    }

    @Test
    public void testCaseForNewBuyButton() { //삽니다 글 작성 버튼 클릭

    }*/ //권한 허용 메시지는 별도의 패키지 위에서 동작하기 때문에 체크가 불가능해 보임

    @Test
    public void testCaseForSellImage() { //팝니다 대표이미지
        onView(withId(R.id.market_used_main_tabs))
                .perform(selectTabAtPosition(0))
                .check(matches(checkTabSelectedAndText(R.id.market_used_main_tabs, 0, "팝니다", true)));

        onView(withId(R.id.base_appbar_dark_right_button)).check(matches(isDisplayed())).perform(click());

        onView(withId(R.id.market_used_sell_create_thumbnail_change_button)).check(matches(isDisplayed())).perform(click());
        onView(withText("이미지를 불러올 방법을 골라주세요.")).inRoot(isDialog()).check(matches(isDisplayed()));
    }

    @Test
    public void testCaseForSellContacts() { //팝니다 연락처
        onView(withId(R.id.market_used_main_tabs))
                .perform(selectTabAtPosition(0))
                .check(matches(checkTabSelectedAndText(R.id.market_used_main_tabs, 0, "팝니다", true)));

        onView(withId(R.id.base_appbar_dark_right_button)).check(matches(isDisplayed())).perform(click());

        onView(withId(R.id.market_used_sell_create_is_phone_public_radiobutton)).check(matches(isDisplayed())).perform(click());
        onView(withText("휴대폰 번호를 기입해주세요")).inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testCaseForSellUploadWithoutTitleContents() { //팝니다 제목 내용 입력 없이 작성버튼 클릭
        onView(withId(R.id.market_used_main_tabs))
                .perform(selectTabAtPosition(0))
                .check(matches(checkTabSelectedAndText(R.id.market_used_main_tabs, 0, "팝니다", true)));

        onView(withId(R.id.base_appbar_dark_right_button)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.base_appbar_dark_right_button)).check(matches(isDisplayed())).perform(click());
        onView(withText("제목과 내용을 입력해주세요")).inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testCaseForSellUploadWithoutContents() { //팝니다 내용 입력 없이 작성버튼 클릭
        onView(withId(R.id.market_used_main_tabs))
                .perform(selectTabAtPosition(0))
                .check(matches(checkTabSelectedAndText(R.id.market_used_main_tabs, 0, "팝니다", true)));

        onView(withId(R.id.base_appbar_dark_right_button)).check(matches(isDisplayed())).perform(click());

        onView(withId(R.id.market_used_sell_create_title_textview)).perform(replaceText("UITest"));

        onView(withId(R.id.base_appbar_dark_right_button)).check(matches(isDisplayed())).perform(click());
        onView(withText("내용을 입력해주세요")).inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testCaseForSellUploadWithoutTitle() { //팝니다 제목 입력 없이 작성버튼 클릭
        onView(withId(R.id.market_used_main_tabs))
                .perform(selectTabAtPosition(0))
                .check(matches(checkTabSelectedAndText(R.id.market_used_main_tabs, 0, "팝니다", true)));

        onView(withId(R.id.base_appbar_dark_right_button)).check(matches(isDisplayed())).perform(click());

       onView(withId(R.id.market_used_sell_create_content)).perform(replaceText("UITest"));

        onView(withId(R.id.base_appbar_dark_right_button)).check(matches(isDisplayed())).perform(click());
        onView(withText("제목을 입력해주세요")).inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    /*@Test
    public void testCaseForSellUpload() { //팝니다 정상동작 확인
        onView(withId(R.id.market_used_main_tabs))
                .perform(selectTabAtPosition(0))
                .check(matches(checkTabSelectedAndText(R.id.market_used_main_tabs, 0, "팝니다", true)));

        onView(withId(R.id.base_appbar_dark_right_button)).check(matches(isDisplayed())).perform(click());

        onView(withId(R.id.market_used_sell_create_title_textview)).perform(replaceText("UITest 팝니다"));
        onView(withId(R.id.market_used_sell_create_content)).perform(replaceText("UITest"));

        onView(withId(R.id.base_appbar_dark_right_button)).check(matches(isDisplayed())).perform(click());

        onView(withId(R.id.market_used_sell_detail_title_textview)).check(matches(isDisplayed()))
                .check(matches(withText("UITest 팝니다")));

        testcaseForBackButton();

        RecyclerViewMatcher recyclerViewMatcher = new RecyclerViewMatcher(R.id.market_used_sell_recyclerview);

        onView(recyclerViewMatcher.atPositionOnView(0, R.id.market_used_sell_title_textview))
                .check(matches(withText("UITest 팝니다")));
    }*/

    @Test
    public void testCaseForBuyImage() { //삽니다 대표이미지
        onView(withId(R.id.market_used_main_tabs))
                .perform(selectTabAtPosition(1))
                .check(matches(checkTabSelectedAndText(R.id.market_used_main_tabs, 1, "삽니다", true)));

        onView(withId(R.id.base_appbar_dark_right_button)).check(matches(isDisplayed())).perform(click());

        onView(withId(R.id.market_used_buy_create_thumbnail_change_button)).check(matches(isDisplayed())).perform(click());
        onView(withText("이미지를 불러올 방법을 골라주세요.")).inRoot(isDialog()).check(matches(isDisplayed()));
    }

    @Test
    public void testCaseForBuyContacts() { //삽니다 연락처
        onView(withId(R.id.market_used_main_tabs))
                .perform(selectTabAtPosition(1))
                .check(matches(checkTabSelectedAndText(R.id.market_used_main_tabs, 1, "삽니다", true)));

        onView(withId(R.id.base_appbar_dark_right_button)).check(matches(isDisplayed())).perform(click());

        onView(withId(R.id.market_used_buy_create_is_phone_public_radiobutton)).check(matches(isDisplayed())).perform(click());
        onView(withText("휴대폰 번호를 기입해주세요")).inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testCaseForBuyUploadWithoutTitleContents() { //삽니다 제목 내용 입력 없이 작성버튼 클릭
        onView(withId(R.id.market_used_main_tabs))
                .perform(selectTabAtPosition(1))
                .check(matches(checkTabSelectedAndText(R.id.market_used_main_tabs, 1, "삽니다", true)));

        onView(withId(R.id.base_appbar_dark_right_button)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.base_appbar_dark_right_button)).check(matches(isDisplayed())).perform(click());
        onView(withText("제목과 내용을 입력해주세요")).inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testCaseForBuyUploadWithoutContents() { //삽니다 내용 입력 없이 작성버튼 클릭
        onView(withId(R.id.market_used_main_tabs))
                .perform(selectTabAtPosition(1))
                .check(matches(checkTabSelectedAndText(R.id.market_used_main_tabs, 1, "삽니다", true)));

        onView(withId(R.id.base_appbar_dark_right_button)).check(matches(isDisplayed())).perform(click());

        onView(withId(R.id.market_used_buy_create_title_textview)).perform(replaceText("UITest 삽니다"));

        onView(withId(R.id.base_appbar_dark_right_button)).check(matches(isDisplayed())).perform(click());
        onView(withText("내용을 입력해주세요")).inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testCaseForBuyUploadWithoutTitle() { //삽니다 제목 입력 없이 작성버튼 클릭
        onView(withId(R.id.market_used_main_tabs))
                .perform(selectTabAtPosition(1))
                .check(matches(checkTabSelectedAndText(R.id.market_used_main_tabs, 1, "삽니다", true)));

        onView(withId(R.id.base_appbar_dark_right_button)).check(matches(isDisplayed())).perform(click());

        onView(withId(R.id.market_used_buy_create_content)).perform(replaceText("UITest"));

        onView(withId(R.id.base_appbar_dark_right_button)).check(matches(isDisplayed())).perform(click());
        onView(withText("제목을 입력해주세요")).inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    /*@Test
    public void testCaseForBuyUpload() { //삽니다 정상동작 확인
        onView(withId(R.id.market_used_main_tabs))
                .perform(selectTabAtPosition(1))
                .check(matches(checkTabSelectedAndText(R.id.market_used_main_tabs, 1, "삽니다", true)));

        onView(withId(R.id.base_appbar_dark_right_button)).check(matches(isDisplayed())).perform(click());

        onView(withId(R.id.market_used_buy_create_title_textview)).perform(replaceText("UITest 삽니다"));
        onView(withId(R.id.market_used_buy_create_content)).perform(replaceText("UITest"));

        onView(withId(R.id.base_appbar_dark_right_button)).check(matches(isDisplayed())).perform(click());

        onView(withId(R.id.market_used_buy_detail_title_textview)).check(matches(isDisplayed()))
                .check(matches(withText("UITest 삽니다")));

        testcaseForBackButton();

        RecyclerViewMatcher recyclerViewMatcher = new RecyclerViewMatcher(R.id.market_used_buy_recyclerview);

        onView(recyclerViewMatcher.atPositionOnView(0, R.id.market_used_buy_title_textview))
                .check(matches(withText("UITest 삽니다")));
    }*/

    @Test
    public void testCaseForSellUploadAndDelete() {
        onView(withId(R.id.market_used_main_tabs))
                .perform(selectTabAtPosition(0))
                .check(matches(checkTabSelectedAndText(R.id.market_used_main_tabs, 0, "팝니다", true)));

        onView(withId(R.id.base_appbar_dark_right_button)).check(matches(isDisplayed())).perform(click());

        onView(withId(R.id.market_used_sell_create_title_textview)).perform(replaceText("UITest 팝니다"));
        onView(withId(R.id.market_used_sell_create_content)).perform(replaceText("UITest"));

        onView(withId(R.id.base_appbar_dark_right_button)).check(matches(isDisplayed())).perform(click());

        testcaseForBackButton();

        RecyclerViewMatcher recyclerViewMatcher = new RecyclerViewMatcher(R.id.market_used_sell_recyclerview);

        onView(recyclerViewMatcher.atPositionOnView(0, R.id.market_used_sell_title_textview))
                .check(matches(withText("UITest 팝니다")));

        onView(withId(R.id.market_used_sell_recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.market_used_sell_delete_button)).check(matches(isDisplayed()))
                .perform(click());
        onView(withText("YES")).perform(click());

        onView(recyclerViewMatcher.atPositionOnView(0, R.id.market_used_sell_title_textview))
                .check(matches(not(withText("UITest 팝니다"))));
    }

    @Test
    public void testCaseForBuyUploadAndDelete() {
        onView(withId(R.id.market_used_main_tabs))
                .perform(selectTabAtPosition(1))
                .check(matches(checkTabSelectedAndText(R.id.market_used_main_tabs, 1, "삽니다", true)));

        onView(withId(R.id.base_appbar_dark_right_button)).check(matches(isDisplayed())).perform(click());

        onView(withId(R.id.market_used_buy_create_title_textview)).perform(replaceText("UITest 삽니다"));
        onView(withId(R.id.market_used_buy_create_content)).perform(replaceText("UITest"));

        onView(withId(R.id.base_appbar_dark_right_button)).check(matches(isDisplayed())).perform(click());

        testcaseForBackButton();

        RecyclerViewMatcher recyclerViewMatcher = new RecyclerViewMatcher(R.id.market_used_buy_recyclerview);

        onView(recyclerViewMatcher.atPositionOnView(0, R.id.market_used_buy_title_textview))
                .check(matches(withText("UITest 삽니다")));

        onView(withId(R.id.market_used_buy_recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.market_used_buy_delete_button)).check(matches(isDisplayed()))
                .perform(click());

        onView(withText("YES")).perform(click());

        onView(recyclerViewMatcher.atPositionOnView(0, R.id.market_used_buy_title_textview))
                .check(matches(not(withText("UITest 삽니다"))));
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



