package in.koreatech.koin;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import in.koreatech.koin.matcher.ToastMatcher;
import in.koreatech.koin.ui.userinfo.UserInfoActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anyOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserInfoInstrumentedTest {
    @Rule
    public IntentsTestRule<UserInfoActivity> activityRule =
            new IntentsTestRule<>(UserInfoActivity.class);

    @Test
    public void testCaseForRemoveAccount() { //회원 탈퇴 버튼 클릭 시
        onView(withId(R.id.userinfo_button_delete_user)).perform(click());
        onView(withText("정말 탈퇴하시려구요? 한번 더 생각해보세요 :)")).check(matches(isDisplayed()));
    }

    @Test
    public void testCaseForEditButton() { //우측 상단 수정 버튼 클릭 시
        gotoEdit();
        onView(withId(R.id.userinfoedited_scrollview)).check(matches(isDisplayed()));
    }

    @Test
    public void testCaseForCheckDuplicationButton() { //중복 확인 버튼 클릭 시
        gotoEdit();
        onView(withId(R.id.userinfoedited_button_nickname_check)).perform(click());
        onView(anyOf(withText("기존 닉네임과 동일 합니다."), withText("사용가능한 닉네임입니다.")))
                .inRoot(anyOf(ToastMatcher.withMessage("기존 닉네임과 동일 합니다."), ToastMatcher.withMessage("사용가능한 닉네임입니다.")))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testCaseForBackButton() { //물리적 뒤로가기 버튼 클릭 시
        gotoEdit();
        onView(isRoot()).perform(pressBack());
        onView(withText("정말 저장하지 않고 나가시겠어요?")).check(matches(isDisplayed()));
    }

    @Test
    public void testCaseForAppBarBackButton() { //앱바 뒤로가기 버튼 클릭 시
        gotoEdit();
        onView(withId(R.id.base_appbar_dark_left_button)).perform(click());
        onView(withText("정말 저장하지 않고 나가시겠어요?")).check(matches(isDisplayed()));
    }

    private void gotoEdit() {
        onView(withId(R.id.base_appbar_dark_right_button)).perform(click());
    }

}



