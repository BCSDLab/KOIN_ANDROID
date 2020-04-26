package in.koreatech.koin;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.core.content.ContextCompat;
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
    public void testcaseForCategoryName() {
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
    public void testcaseForCategory() {
        onView(withId(R.id.circle_root_scroll_view)).inRoot(RootMatchers.withDecorView(
                Matchers.is(activityRule.getActivity().getWindow().getDecorView()))).perform(ViewActions.swipeDown());

        //전체보기
        onView(withId(R.id.circle_all_linear_layout)).perform(click());
        checkTextStatus(R.id.circle_all_textview);
        checkImageStatus(R.id.circle_all_imageview);

        //예술분야
        onView(withId(R.id.circle_art_linear_layout)).perform(click());
        checkTextStatus(R.id.circle_art_textview);
        checkImageStatus(R.id.circle_art_imageview);

        //공연분야
        onView(withId(R.id.circle_show_linear_layout)).perform(click());
        checkTextStatus(R.id.circle_show_textview);
        checkImageStatus(R.id.circle_show_imageview);

        //운동분야
        onView(withId(R.id.circle_sport_linear_layout)).perform(click());
        checkTextStatus(R.id.circle_sport_textview);
        checkImageStatus(R.id.circle_sport_imageview);

        //학술분야
        onView(withId(R.id.circle_study_linear_layout)).perform(click());
        checkTextStatus(R.id.circle_study_textview);
        checkImageStatus(R.id.circle_study_imageview);

        //종교분야
        onView(withId(R.id.circle_religion_linear_layout)).perform(click());
        checkTextStatus(R.id.circle_religion_textview);
        checkImageStatus(R.id.circle_religion_imageview);

        //사회봉사
        onView(withId(R.id.circle_service_linear_layout)).perform(click());
        checkTextStatus(R.id.circle_service_textview);
        checkImageStatus(R.id.circle_service_imageview);

        //준동아리
        onView(withId(R.id.circle_etc_linear_layout)).perform(click());
        checkTextStatus(R.id.circle_etc_textview);
        checkImageStatus(R.id.circle_etc_imageview);
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

    @Test
    public void testcaseForViewAll() {
        onView(withId(R.id.circle_all_textview)).check(matches(withText("전체보기")));
        onView(withId(R.id.circle_root_scroll_view)).inRoot(RootMatchers.withDecorView(
                Matchers.is(activityRule.getActivity().getWindow().getDecorView()))).perform(ViewActions.swipeDown());

        onView(withId(R.id.circle_all_linear_layout)).perform(click());

        onView(withId(R.id.circle_all_textview)).check(matches(TextColorMatcher.withTextColor(
                R.color.colorAccent, activityRule.getActivity().getResources()
        )));
        onView(withId(R.id.circle_all_imageview)).check(matches(ImageViewSrcMatcher.withDrawable(
                activityRule.getActivity().getResources().getDrawable(R.drawable.ic_club_all_on)
        )));

        RecyclerViewMatcher recyclerViewMatcher = new RecyclerViewMatcher(R.id.circle_recyclerview);

        /*onView(recyclerViewMatcher.atPositionOnView(0, R.id.circle_item_name_textview)).check(matches(withText("BCSDLab")));
        onView(recyclerViewMatcher.atPositionOnView(0, R.id.circle_item_detail_textview)).check(matches(withText("상용화 서비스 개발 동아리 BCSD LAB")));

        onView(recyclerViewMatcher.atPositionOnView(1, R.id.circle_item_name_textview)).check(matches(withText("K-오케스트라")));
        onView(recyclerViewMatcher.atPositionOnView(1, R.id.circle_item_detail_textview)).check(matches(withText("오케스트라 동아리")));

        onView(recyclerViewMatcher.atPositionOnView(2, R.id.circle_item_name_textview)).check(matches(withText("으라차차 유도부")));
        onView(recyclerViewMatcher.atPositionOnView(2, R.id.circle_item_detail_textview)).check(matches(withText("유도를 하며 심신을 강화하는 동아리입니다!")));

        onView(recyclerViewMatcher.atPositionOnView(3, R.id.circle_item_name_textview)).check(matches(withText("한소리")));
        onView(recyclerViewMatcher.atPositionOnView(3, R.id.circle_item_detail_textview)).check(matches(withText("중앙유일풍물패 한소리")));

        onView(recyclerViewMatcher.atPositionOnView(4, R.id.circle_item_name_textview)).check(matches(withText("SHUTTER")));
        onView(recyclerViewMatcher.atPositionOnView(4, R.id.circle_item_detail_textview)).check(matches(withText("사진교육 및 실습")));

        onView(recyclerViewMatcher.atPositionOnView(5, R.id.circle_item_name_textview)).check(matches(withText("세드뚜아")));
        onView(recyclerViewMatcher.atPositionOnView(5, R.id.circle_item_detail_textview)).check(matches(withText("한기대 유일의 천주교동아리 세드뚜아")));

        onView(recyclerViewMatcher.atPositionOnView(6, R.id.circle_item_name_textview)).check(matches(withText("극예술연구회")));
        onView(recyclerViewMatcher.atPositionOnView(6, R.id.circle_item_detail_textview)).check(matches(withText("연근기획공연동아리")));

        onView(recyclerViewMatcher.atPositionOnView(7, R.id.circle_item_name_textview)).check(matches(withText("해비타트")));
        onView(recyclerViewMatcher.atPositionOnView(7, R.id.circle_item_detail_textview)).check(matches(withText("건축봉사동아리 해비타트")));

        onView(recyclerViewMatcher.atPositionOnView(8, R.id.circle_item_name_textview)).check(matches(withText("LPB")));
        onView(recyclerViewMatcher.atPositionOnView(8, R.id.circle_item_detail_textview)).check(matches(withText("야구와 치킨을 사랑하는 동아리")));

        onView(recyclerViewMatcher.atPositionOnView(9, R.id.circle_item_name_textview)).check(matches(withText("스플릿")));
        onView(recyclerViewMatcher.atPositionOnView(9, R.id.circle_item_detail_textview)).check(matches(withText("볼링동아리")));

        onView(recyclerViewMatcher.atPositionOnView(10, R.id.circle_item_name_textview)).check(matches(withText("그라피토")));
        onView(recyclerViewMatcher.atPositionOnView(10, R.id.circle_item_detail_textview)).check(matches(withText("만화를 좋아하는 학생들이 모인 동아리")));

        onView(recyclerViewMatcher.atPositionOnView(11, R.id.circle_item_name_textview)).check(matches(withText("ZEST")));
        onView(recyclerViewMatcher.atPositionOnView(11, R.id.circle_item_detail_textview)).check(matches(withText("KOREATECH 유일한 밴드동아리!열정으로 가득찬 락 동아리 ZEST!")));

        onView(recyclerViewMatcher.atPositionOnView(12, R.id.circle_item_name_textview)).check(matches(withText("비상")));
        onView(recyclerViewMatcher.atPositionOnView(12, R.id.circle_item_detail_textview)).check(matches(withText("통기타 연주와 함께 공연을 목적으로 하는 동아리")));

        onView(recyclerViewMatcher.atPositionOnView(13, R.id.circle_item_name_textview)).check(matches(withText("소울메이트")));
        onView(recyclerViewMatcher.atPositionOnView(13, R.id.circle_item_detail_textview)).check(matches(withText("한기대 최고의 vocal 동아리")));

        onView(recyclerViewMatcher.atPositionOnView(14, R.id.circle_item_name_textview)).check(matches(withText("피아노사랑")));
        onView(recyclerViewMatcher.atPositionOnView(14, R.id.circle_item_detail_textview)).check(matches(withText("피아노를 사랑하는 사람들이 모여 함꼐 즐기는 동아리")));

        onView(recyclerViewMatcher.atPositionOnView(15, R.id.circle_item_name_textview)).check(matches(withText("빠샤")));
        onView(recyclerViewMatcher.atPositionOnView(15, R.id.circle_item_detail_textview)).check(matches(withText("랩댄스힙합동아리 BASIA")));

        onView(recyclerViewMatcher.atPositionOnView(16, R.id.circle_item_name_textview)).check(matches(withText("COF")));
        onView(recyclerViewMatcher.atPositionOnView(16, R.id.circle_item_detail_textview)).check(matches(withText("컴퓨터동아리")));

        onView(recyclerViewMatcher.atPositionOnView(17, R.id.circle_item_name_textview)).check(matches(withText("로봇사랑")));
        onView(recyclerViewMatcher.atPositionOnView(17, R.id.circle_item_detail_textview)).check(matches(withText("젊음 그리고 로봇. 로봇 제작 동아리")));

        onView(recyclerViewMatcher.atPositionOnView(18, R.id.circle_item_name_textview)).check(matches(withText("자연인")));
        onView(recyclerViewMatcher.atPositionOnView(18, R.id.circle_item_detail_textview)).check(matches(withText("KOREATECH 자동차 제작 동아리")));

        onView(recyclerViewMatcher.atPositionOnView(19, R.id.circle_item_name_textview)).check(matches(withText("비전선교단")));
        onView(recyclerViewMatcher.atPositionOnView(19, R.id.circle_item_detail_textview)).check(matches(withText("예수 그리스도의 복음적 사랑과 가치를 실현")));

        onView(recyclerViewMatcher.atPositionOnView(20, R.id.circle_item_name_textview)).check(matches(withText("IVF")));
        onView(recyclerViewMatcher.atPositionOnView(20, R.id.circle_item_detail_textview)).check(matches(withText("교내 기독교 동아리")));

        onView(recyclerViewMatcher.atPositionOnView(21, R.id.circle_item_name_textview)).check(matches(withText("DFC")));
        onView(recyclerViewMatcher.atPositionOnView(21, R.id.circle_item_detail_textview)).check(matches(withText("기독교 캠퍼스 사역 선교단체")));

        onView(recyclerViewMatcher.atPositionOnView(22, R.id.circle_item_name_textview)).check(matches(withText("YES KOREATECH")));
        onView(recyclerViewMatcher.atPositionOnView(22, R.id.circle_item_detail_textview)).check(matches(withText("타학교와 가장 많이 교류하는 교내 최대 규모의 봉사동아리")));

        onView(recyclerViewMatcher.atPositionOnView(23, R.id.circle_item_name_textview)).check(matches(withText("손짓나눔")));
        onView(recyclerViewMatcher.atPositionOnView(23, R.id.circle_item_detail_textview)).check(matches(withText("손 끝에 맺히는 언어, 사랑의 수화봉사 동아리")));

        onView(recyclerViewMatcher.atPositionOnView(24, R.id.circle_item_name_textview)).check(matches(withText("CUT")));
        onView(recyclerViewMatcher.atPositionOnView(24, R.id.circle_item_detail_textview)).check(matches(withText("교내 탁구 동아리")));

        onView(recyclerViewMatcher.atPositionOnView(25, R.id.circle_item_name_textview)).check(matches(withText("SAM")));
        onView(recyclerViewMatcher.atPositionOnView(25, R.id.circle_item_detail_textview)).check(matches(withText("운동도 잘하고 잘 노는 스쿼시 동아리")));

        onView(recyclerViewMatcher.atPositionOnView(26, R.id.circle_item_name_textview)).check(matches(withText("SMASH")));
        onView(recyclerViewMatcher.atPositionOnView(26, R.id.circle_item_detail_textview)).check(matches(withText("테니스동아리")));

        onView(recyclerViewMatcher.atPositionOnView(27, R.id.circle_item_name_textview)).check(matches(withText("슛")));
        onView(recyclerViewMatcher.atPositionOnView(27, R.id.circle_item_detail_textview)).check(matches(withText("축구를 사랑하는 사람들이 모인 동아리")));

        onView(recyclerViewMatcher.atPositionOnView(28, R.id.circle_item_name_textview)).check(matches(withText("아우나래")));
        onView(recyclerViewMatcher.atPositionOnView(28, R.id.circle_item_detail_textview)).check(matches(withText("자연과 하나가 되어 즐기는 항공스포츠 동아리")));

        onView(recyclerViewMatcher.atPositionOnView(29, R.id.circle_item_name_textview)).check(matches(withText("일월검당")));
        onView(recyclerViewMatcher.atPositionOnView(29, R.id.circle_item_detail_textview)).check(matches(withText("검도로 심신을 수련하고 친목을 다지는 동아리")));

        onView(recyclerViewMatcher.atPositionOnView(30, R.id.circle_item_name_textview)).check(matches(withText("펜타곤")));
        onView(recyclerViewMatcher.atPositionOnView(30, R.id.circle_item_detail_textview)).check(matches(withText("한기대 중앙농구동아리")));

        onView(recyclerViewMatcher.atPositionOnView(31, R.id.circle_item_name_textview)).check(matches(withText("COK")));
        onView(recyclerViewMatcher.atPositionOnView(31, R.id.circle_item_detail_textview)).check(matches(withText("교내 배트민턴 동아리")));

        onView(recyclerViewMatcher.atPositionOnView(32, R.id.circle_item_name_textview)).check(matches(withText("Cube Club")));
        onView(recyclerViewMatcher.atPositionOnView(32, R.id.circle_item_detail_textview)).check(matches(withText("큐브&보드게임 동아리")));

        onView(recyclerViewMatcher.atPositionOnView(33, R.id.circle_item_name_textview)).check(matches(withText("FRONTIER RECORDS")));
        onView(recyclerViewMatcher.atPositionOnView(33, R.id.circle_item_detail_textview)).check(matches(withText("음악예술동아리 Frontier Records")));

        onView(recyclerViewMatcher.atPositionOnView(34, R.id.circle_item_name_textview)).check(matches(withText("COURSE")));
        onView(recyclerViewMatcher.atPositionOnView(34, R.id.circle_item_detail_textview)).check(matches(withText("수영 동아리")));

        onView(recyclerViewMatcher.atPositionOnView(35, R.id.circle_item_name_textview)).check(matches(withText("YM&ESF")));
        onView(recyclerViewMatcher.atPositionOnView(35, R.id.circle_item_detail_textview)).check(matches(withText("한기대 예수전도단")));

        onView(recyclerViewMatcher.atPositionOnView(36, R.id.circle_item_name_textview)).check(matches(withText("스페셜리스트")));
        onView(recyclerViewMatcher.atPositionOnView(36, R.id.circle_item_detail_textview)).check(matches(withText("해킹, 정보보안 전문 동아리")));

        onView(recyclerViewMatcher.atPositionOnView(37, R.id.circle_item_name_textview)).check(matches(withText("Cepheus")));
        onView(recyclerViewMatcher.atPositionOnView(37, R.id.circle_item_detail_textview)).check(matches(withText("천체관측 및 관련활동")));

        onView(recyclerViewMatcher.atPositionOnView(38, R.id.circle_item_name_textview)).check(matches(withText("밥버러지")));
        onView(recyclerViewMatcher.atPositionOnView(38, R.id.circle_item_detail_textview)).check(matches(withText("맛있는 음식을 먹으러 다니는 음식탐방동아리입니다.")));*/ //동아리 목록 체크
    }

    @Test
    public void testcaseForViewArt() {
        onView(withId(R.id.circle_art_textview)).check(matches(withText("예술분야")));
        onView(withId(R.id.circle_root_scroll_view)).inRoot(RootMatchers.withDecorView(
                Matchers.is(activityRule.getActivity().getWindow().getDecorView()))).perform(ViewActions.swipeDown());

        onView(withId(R.id.circle_art_linear_layout)).perform(click());

        onView(withId(R.id.circle_art_textview)).check(matches(TextColorMatcher.withTextColor(
                R.color.colorAccent, activityRule.getActivity().getResources()
        )));
        onView(withId(R.id.circle_art_imageview)).check(matches(ImageViewSrcMatcher.withDrawable(
                activityRule.getActivity().getResources().getDrawable(R.drawable.ic_club_art_on)
        )));
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
