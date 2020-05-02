package in.koreatech.koin;

import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import in.koreatech.koin.ui.signup.SignupActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SignupInstrumentedTest {
    @Rule
    public IntentsTestRule<SignupActivity> activityRule =
            new IntentsTestRule<>(SignupActivity.class);

    @Test
    public void testCaseForEmailWithoutAnything() { //아무것도 입력하지 않고 이메일 인증 버튼 클릭
        onView(withId(R.id.signup_send_verification_button)).perform(click());
        onView(withText("이메일을 확인해 주세요")).inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
        onView(isRoot()).perform(waitFor(3000));
    }

    @Test
    public void testCaseForEmailWithOnlyEmail() { //이메일만 입력하고 이메일 인증 버튼 클릭
        onView(withId(R.id.signup_edittext_id)).perform(typeText("abc"));
        onView(isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.signup_send_verification_button)).perform(click());
        onView(withText("입력한 정보를 다시 확인해 주세요")).inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
        onView(isRoot()).perform(waitFor(3000));
    }

    @Test
    public void testCaseForEmailWithNotMatchingPasswords() { //비밀번호가 일치하지 않은 상태에서 이메일 인증 버튼 클릭
        onView(withId(R.id.signup_edittext_id)).perform(typeText("abc"));
        onView(withId(R.id.signup_edittext_pw)).perform(typeText("password12*"));
        onView(withId(R.id.signup_edittext_pw_confirm)).perform(typeText("passvvord12*"));
        onView(isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.signup_send_verification_button)).perform(click());
        onView(withText("입력한 정보를 다시 확인해 주세요")).inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
        onView(isRoot()).perform(waitFor(3000));
    }

    @Test
    public void testCaseForEmailWithPasswordsUnder5() { //비밀번호가 5자리 이하일 때 이메일 인증 버튼 클릭
        onView(withId(R.id.signup_edittext_id)).perform(typeText("abc"));
        onView(withId(R.id.signup_edittext_pw)).perform(typeText("abc1*"));
        onView(withId(R.id.signup_edittext_pw_confirm)).perform(typeText("abc1*"));
        onView(isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.signup_send_verification_button)).perform(click());
        onView(withText("입력한 정보를 다시 확인해 주세요")).inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
        onView(isRoot()).perform(waitFor(3000));
    }

    @Test
    public void testCaseForEmailWithPasswordsOver17() { //비밀번호가 19자리 이상일 때 이메일 인증 버튼 클릭
        onView(withId(R.id.signup_edittext_id)).perform(typeText("abc"));
        onView(withId(R.id.signup_edittext_pw)).perform(typeText("verylongpassword1234*"));
        onView(withId(R.id.signup_edittext_pw_confirm)).perform(typeText("verylongpassword1234*"));
        onView(isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.signup_send_verification_button)).perform(click());
        onView(withText("입력한 정보를 다시 확인해 주세요")).inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
        onView(isRoot()).perform(waitFor(3000));
    }

    @Test
    public void testCaseForEmailWithPasswordsNotIncludingSpecialMarks() { //비밀번호에 특수문자가 없을 때 이메일 인증 버튼 클릭
        onView(withId(R.id.signup_edittext_id)).perform(typeText("abc"));
        onView(withId(R.id.signup_edittext_pw)).perform(typeText("password1234"));
        onView(withId(R.id.signup_edittext_pw_confirm)).perform(typeText("password1234"));
        onView(isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.signup_send_verification_button)).perform(click());
        onView(withText("입력한 정보를 다시 확인해 주세요")).inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
        onView(isRoot()).perform(waitFor(3000));
    }

    @Test
    public void testCaseForEmailWithPasswordsNotIncludingEnglish() { //비밀번호에 영어가 없을 때 이메일 인증 버튼 클릭
        onView(withId(R.id.signup_edittext_id)).perform(typeText("abc"));
        onView(withId(R.id.signup_edittext_pw)).perform(typeText("12345678"));
        onView(withId(R.id.signup_edittext_pw_confirm)).perform(typeText("12345678"));
        onView(isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.signup_send_verification_button)).perform(click());
        onView(withText("입력한 정보를 다시 확인해 주세요")).inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
        onView(isRoot()).perform(waitFor(3000));
    }

    @Test
    public void testCaseForEmailWithPasswordsNotIncludingNumbers() { //비밀번호에 숫자가 없을 때 이메일 인증 버튼 클릭
        onView(withId(R.id.signup_edittext_id)).perform(typeText("abc"));
        onView(withId(R.id.signup_edittext_pw)).perform(typeText("password"));
        onView(withId(R.id.signup_edittext_pw_confirm)).perform(typeText("password"));
        onView(isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.signup_send_verification_button)).perform(click());
        onView(withText("입력한 정보를 다시 확인해 주세요")).inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
        onView(isRoot()).perform(waitFor(3000));
    }

    @Test
    public void testCaseForEmailWithoutTerms() { //조건에 맞는 이메일과 비밀번호를 입력하고 약관 동의 체크를 하지 않았을 때 이메일 인증 버튼 클릭
        onView(withId(R.id.signup_edittext_id)).perform(typeText("abc"));
        onView(withId(R.id.signup_edittext_pw)).perform(typeText("password123*"));
        onView(withId(R.id.signup_edittext_pw_confirm)).perform(typeText("password123*"));
        onView(isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.signup_send_verification_button)).perform(click());
        onView(withText("이용약관에 동의해 주세요")).inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
        onView(isRoot()).perform(waitFor(3000));
    }

    @Test
    public void testCaseForEmailWithOnlyTermsOfUse() { //조건에 맞는 이메일과 비밀번호를 입력하고 이용 약관 동의만 체크했을 때 이메일 인증 버튼 클릭
        onView(withId(R.id.signup_edittext_id)).perform(typeText("abc"));
        onView(withId(R.id.signup_edittext_pw)).perform(typeText("password123*"));
        onView(withId(R.id.signup_edittext_pw_confirm)).perform(typeText("password123*"));
        onView(isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.signup_check_box_personal_info_terms)).perform(click());
        onView(withId(R.id.signup_send_verification_button)).perform(click());
        onView(withText("이용약관에 동의해 주세요")).inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
        onView(isRoot()).perform(waitFor(3000));
    }

    @Test
    public void testCaseForEmailWithOnlyTermsOfKoin() { //조건에 맞는 이메일과 비밀번호를 입력하고 코인 약관 동의만 체크했을 때 이메일 인증 버튼 클릭
        onView(withId(R.id.signup_edittext_id)).perform(typeText("abc"));
        onView(withId(R.id.signup_edittext_pw)).perform(typeText("password123*"));
        onView(withId(R.id.signup_edittext_pw_confirm)).perform(typeText("password123*"));
        onView(isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.signup_check_box_signup_terms)).perform(click());
        onView(withId(R.id.signup_send_verification_button)).perform(click());
        onView(withText("이용약관에 동의해 주세요")).inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
        onView(isRoot()).perform(waitFor(3000));
    }

    @Test
    public void testCaseForEmail() { //모든 조건을 만족하고 이메일 인증 버튼 클릭
        onView(withId(R.id.signup_edittext_id)).perform(typeText(generateRandomId()));
        onView(withId(R.id.signup_edittext_pw)).perform(typeText("password123*"));
        onView(withId(R.id.signup_edittext_pw_confirm)).perform(typeText("password123*"));
        onView(isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.signup_check_box_personal_info_terms)).perform(click());
        onView(withId(R.id.signup_check_box_signup_terms)).perform(click());
        onView(withId(R.id.signup_send_verification_button)).perform(click());
        onView(withText("10분안에 학교 메일로 인증을 완료해 주세요. 이동하실래요?"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testCaseForAlreadySignedUpEmail() { //모든 조건을 만족하고 이메일 인증 버튼 클릭
        onView(withId(R.id.signup_edittext_id)).perform(typeText(generateRandomId()));
        onView(withId(R.id.signup_edittext_pw)).perform(typeText("password123*"));
        onView(withId(R.id.signup_edittext_pw_confirm)).perform(typeText("password123*"));
        onView(isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.signup_check_box_personal_info_terms)).perform(click());
        onView(withId(R.id.signup_check_box_signup_terms)).perform(click());
        onView(withId(R.id.signup_send_verification_button)).perform(click());
        onView(isRoot()).perform(waitFor(3000));

        onView(withId(R.id.signup_send_verification_button)).perform(click());
        onView(withText("이미 가입한 계정이거나\n이메일 전송을 요청하였습니다.")).inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
        onView(isRoot()).perform(waitFor(3000));
    }

    @Test
    public void testCaseForClickTerms() { //개인정보 이용약관 클릭
        onView(withId(R.id.signup_textview_personal_info_terms)).perform(click());
        onView(withId(android.R.id.button1)).check(matches(isDisplayed()));
    }

    @Test
    public void testCaseForClickTermsOfKoin() { //코인 이용약관 클릭
        onView(withId(R.id.signup_textview_signup_terms)).perform(click());
        onView(withId(android.R.id.button1)).check(matches(isDisplayed()));
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

    private String generateRandomId() {
        String availableChars = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder id = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i < random.nextInt(7) + 1; i++)
            id.append(availableChars.charAt(random.nextInt(availableChars.length())));

        return id.toString();
    }

}



