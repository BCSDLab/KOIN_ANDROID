package in.koreatech.koin;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import in.koreatech.koin.ui.circle.CircleActivity;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
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
    public void testcaseForBackButton() {
        onView(isRoot()).perform(pressBack());
    }

    @Test
    public void testcaseForAppBarTitle() {
        onView(withId(R.id.koin_base_app_bar_dark)).check(matches(hasDescendant(withText(R.string.navigation_item_circle))));
    }

    @Test
    public void testcaseForAppBarBackButton() {
        onView(withId(R.id.koin_base_app_bar_dark)).perform(click());
    }

    @Test
    public void testcaseForCategoryName() { //카테고리 텍스트 검사
        onView(withId(R.id.circle_category_textview)).check(matches(withText("CATEGORY"))).check(matches(isDisplayed()));
        onView(withId(R.id.circle_all_textview)).check(matches(withText("전체보기"))).check(matches(isDisplayed()));
        onView(withId(R.id.circle_art_textview)).check(matches(withText("예술분야"))).check(matches(isDisplayed()));
        onView(withId(R.id.circle_show_textview)).check(matches(withText("공연분야"))).check(matches(isDisplayed()));
        onView(withId(R.id.circle_sport_textview)).check(matches(withText("운동분야"))).check(matches(isDisplayed()));
        onView(withId(R.id.circle_study_textview)).check(matches(withText("학술분야"))).check(matches(isDisplayed()));
        onView(withId(R.id.circle_religion_textview)).check(matches(withText("종교분야"))).check(matches(isDisplayed()));
        onView(withId(R.id.circle_service_textview)).check(matches(withText("사회봉사"))).check(matches(isDisplayed()));
        onView(withId(R.id.circle_etc_textview)).check(matches(withText("준동아리"))).check(matches(isDisplayed()));
        onView(withId(R.id.circle_category_list_border)).check(matches(isDisplayed()));
    }

    @Test
    public void testcaseForCategoryAll() {  //전체 동아리 리스트 테스트
        //NestedScrollView 버튼 mismatch 문제를 해결하기 위해 추가함
        onView(withId(R.id.circle_layout)).perform(ViewActions.swipeDown());

        onView(withId(R.id.circle_all_linear_layout)).perform(click());
        checkTextStatus(R.id.circle_all_textview);
        checkImageStatus(R.id.circle_all_imageview);

        checkCircleList(
                "BCSDLab","K-오케스트라","으라차차 유도부","한소리","SHUTTER","세드뚜아",
                "극예술연구회","해비타트","LPB","스플릿","그라피토","ZEST","비상","소울메이트",
                "피아노사랑","빠샤","COF","로봇사랑","자연인","비전선교단","IVF","DFC",
                "YES KOREATECH","손짓나눔","CUT","SAM","SMASH","슛","아우나래","일월검당",
                "펜타곤","COK","Cube Club","FRONTIER RECORDS","COURSE","YM&ESF","스페셜리스트",
                "Cepheus","밥버러지"
        );
    }

    @Test
    public void testcaseForCategoryArt() {  //예술 동아리 리스트 테스트
        //NestedScrollView 버튼 mismatch 문제를 해결하기 위해 추가함
        onView(withId(R.id.circle_layout)).perform(ViewActions.swipeDown());

        onView(withId(R.id.circle_art_linear_layout)).perform(click());
        checkTextStatus(R.id.circle_art_textview);
        checkImageStatus(R.id.circle_art_imageview);

        checkCircleList(
                "SHUTTER", "극예술연구회", "그라피토", "Cube Club", "밥버러지"
        );
    }

    @Test
    public void testcaseForCategoryShow() {  //공연 동아리 리스트 테스트
        //NestedScrollView 버튼 mismatch 문제를 해결하기 위해 추가함
        onView(withId(R.id.circle_layout)).perform(ViewActions.swipeDown());

        onView(withId(R.id.circle_show_linear_layout)).perform(click());
        checkTextStatus(R.id.circle_show_textview);
        checkImageStatus(R.id.circle_show_imageview);

        checkCircleList(
                "K-오케스트라", "한소리", "ZEST", "비상", "소울메이트", "피아노사랑", "빠샤", "FRONTIER RECORDS"
        );
    }

    @Test
    public void testcaseForCategorySport() {  //운동 동아리 리스트 테스트
        //NestedScrollView 버튼 mismatch 문제를 해결하기 위해 추가함
        onView(withId(R.id.circle_layout)).perform(ViewActions.swipeDown());

        onView(withId(R.id.circle_sport_linear_layout)).perform(click());
        checkTextStatus(R.id.circle_sport_textview);
        checkImageStatus(R.id.circle_sport_imageview);

        checkCircleList(
                "으라차차 유도부", "LBP", "스플릿", "CUT", "SAM", "SMASH", "슛", "아우나래", "일월검당", "펜타곤", "COK", "COURSE"
        );
    }

    @Test
    public void testcaseForCategoryReligion() {  //종교 동아리 리스트 테스트
        //NestedScrollView 버튼 mismatch 문제를 해결하기 위해 추가함
        onView(withId(R.id.circle_layout)).perform(ViewActions.swipeDown());

        onView(withId(R.id.circle_religion_linear_layout)).perform(click());
        checkTextStatus(R.id.circle_religion_textview);
        checkImageStatus(R.id.circle_religion_imageview);

        checkCircleList(
                "BCSDLab", "COF", "로봇사랑", "자연인", "스페셜리스트", "Cepheus"
        );
    }

    @Test
    public void testcaseForCategoryStudy() {  //학술 동아리 리스트 테스트
        //NestedScrollView 버튼 mismatch 문제를 해결하기 위해 추가함
        onView(withId(R.id.circle_layout)).perform(ViewActions.swipeDown());

        onView(withId(R.id.circle_study_linear_layout)).perform(click());
        checkTextStatus(R.id.circle_study_textview);
        checkImageStatus(R.id.circle_study_imageview);

        checkCircleList(
                "세드뚜아", "비전선교단", "IVF", "DFC", "YM&ESF"
        );
    }

    @Test
    public void testcaseForCategoryService() {  //봉사 동아리 리스트 테스트
        //NestedScrollView 버튼 mismatch 문제를 해결하기 위해 추가함
        onView(withId(R.id.circle_layout)).perform(ViewActions.swipeDown());

        onView(withId(R.id.circle_service_linear_layout)).perform(click());
        checkTextStatus(R.id.circle_service_textview);
        checkImageStatus(R.id.circle_service_imageview);

        checkCircleList(
                "해비타트", "YES KOREATECH", "손짓나눔"
        );
    }

    @Test
    public void testcaseForCategoryEtc() {  //준동아리 리스트 테스트
        //NestedScrollView 버튼 mismatch 문제를 해결하기 위해 추가함
        onView(withId(R.id.circle_layout)).perform(ViewActions.swipeDown());

        onView(withId(R.id.circle_etc_linear_layout)).perform(click());
        checkTextStatus(R.id.circle_etc_textview);
        checkImageStatus(R.id.circle_etc_imageview);

        checkCircleList();
    }


    @Test
    public void testcaseForMoveDetail() {   //동아리 상세화면 이동 테스트
        onView(withId(R.id.circle_layout)).perform(ViewActions.swipeDown());

        onView(RecyclerViewMatcher.atPositionOnView(R.id.circle_recyclerview, 0, R.id.circle_item_name_textview))
                .perform(click());

        onView(withId(R.id.circle_layout)).check(matches(isDisplayed()));
    }


    void checkTextStatus(@IdRes int selectedCategoryTextViewId) {
        @IdRes int[] ids = {
                R.id.circle_all_textview,
                R.id.circle_art_textview,
                R.id.circle_show_textview,
                R.id.circle_sport_textview,
                R.id.circle_study_textview,
                R.id.circle_religion_textview,
                R.id.circle_service_textview,
                R.id.circle_etc_textview
        };

        for (int i = 0; i < ids.length; i++) {
            onView(withId(ids[i])).check(matches(TextColorMatcher.withTextColor(
                    ids[i] == selectedCategoryTextViewId ? R.color.colorAccent : R.color.black,
                    activityRule.getActivity().getResources()
            )));
        }
    }

    void checkImageStatus(@IdRes int selectedCategoryImageViewId) {
        @IdRes int[] ids = {
                R.id.circle_all_imageview,
                R.id.circle_art_imageview,
                R.id.circle_show_imageview,
                R.id.circle_sport_imageview,
                R.id.circle_study_imageview,
                R.id.circle_religion_imageview,
                R.id.circle_service_imageview,
                R.id.circle_etc_imageview
        };
        final int[][] mCircleCategoryIconListId = {
                {R.drawable.ic_club_all, R.drawable.ic_club_all_on},
                {R.drawable.ic_club_art, R.drawable.ic_club_art_on},
                {R.drawable.ic_club_show, R.drawable.ic_club_show_on},
                {R.drawable.ic_club_sport, R.drawable.ic_club_sport_on},
                {R.drawable.ic_club_study, R.drawable.ic_club_study_on},
                {R.drawable.ic_club_religion, R.drawable.ic_club_religion_on},
                {R.drawable.ic_club_service, R.drawable.ic_club_service_on},
                {R.drawable.ic_club_etc, R.drawable.ic_club_etc_on},
        };

        for (int i = 0; i < ids.length; i++) {
            onView(withId(ids[i])).check(matches(ImageViewSrcMatcher.withDrawable(
                    activityRule.getActivity().getResources().getDrawable(
                            mCircleCategoryIconListId[i][ids[i] == selectedCategoryImageViewId ? 1 : 0]
                    ))));
        }
    }

    void checkCircleList(String... circleNames) {

        for (int i = 0; i < circleNames.length; i++) {
            onView(withId(R.id.circle_recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition(i, scrollTo()));
            onView(RecyclerViewMatcher.atPositionOnView(R.id.circle_recyclerview, i, R.id.circle_item_name_textview)).check(matches(withText(circleNames[i])));
        }
    }


    @Test
    public void testcaseForButtonClick() {
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



