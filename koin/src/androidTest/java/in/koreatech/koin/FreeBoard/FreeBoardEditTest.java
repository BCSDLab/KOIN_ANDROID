package in.koreatech.koin.FreeBoard;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;

import in.koreatech.koin.R;
import in.koreatech.koin.ui.board.BoardActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static in.koreatech.koin.matcher.ToastMatcher.withMessage;
import static org.hamcrest.Matchers.allOf;

@LargeTest
public class FreeBoardEditTest {
    @Rule
    public ActivityTestRule<BoardActivity> activityRule = new ActivityTestRule<>(BoardActivity.class);

    /**
     * 게시글 작성 시 아무것도 입력하지 않고 게시글 작성 완료 버튼 클릭 시 토스트메시지 확인
     */
    @Test
    public void testCaseForBlinkTitleAndBlinkContent() {
        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());                                     //게시글 작성하기 버튼 클릭

        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());                                     //게시글 작성완료 버튼 클릭

        onView(withText(R.string.board_title_check)).inRoot(withMessage("제목을 입력해주세요")).check(matches(isDisplayed()));           //토스트 메시지 확인

    }


    /**
     * 게시글 작성 시 아무것도 입력하지 않고 이전화면으로 이동 시 스낵바 확인
     */
    @Test
    public void testCaseForAppbarLeftButton() {
        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());                                 //게시글 작성하기 버튼 클릭

        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_left_button)).perform(click());                                  //뒤로가기 버튼 클릭
        onView(Matchers.allOf(ViewMatchers.withText(R.string.back_button_pressed))).check(matches(isDisplayed()));              //스낵바 도출
        onView(withText("YES")).perform(click());                                                                                   //YES버튼 눌러서 이전 화면으로 이동
    }

    /**
     * 게시글 작성 시 아무것도 입력하지 않고 이전화면으로 이동 시 스낵바 확인
     */
    @Test
    public void testCaseForBackButton() {
        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());     //게시글 작성하기 버튼 클릭

        onView(isRoot()).perform(pressBack());                                                          //뒤로가기 물리적 버튼 클릭
        onView(isRoot()).perform(pressBack());                                                          //뒤로가기 물리적 버튼 클릭
        onView(allOf(withText(R.string.back_button_pressed))).check(matches(isDisplayed()));        //스낵바 도출
        onView(withText("YES")).perform(click());                                                       //YES버튼 눌러서 이전 화면으로 이동
    }


}
