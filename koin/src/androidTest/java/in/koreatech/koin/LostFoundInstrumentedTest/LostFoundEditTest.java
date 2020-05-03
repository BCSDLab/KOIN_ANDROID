package in.koreatech.koin.LostFoundInstrumentedTest;

import android.os.IBinder;
import android.view.WindowManager;

import androidx.test.espresso.Root;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import in.koreatech.koin.R;
import in.koreatech.koin.ui.lostfound.LostFoundEditActivity;
import in.koreatech.koin.ui.lostfound.LostFoundMainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static in.koreatech.koin.matcher.ToastMatcher.withMessage;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LostFoundEditTest {
    @Rule
    public ActivityTestRule<LostFoundEditActivity> activityRule =
            new ActivityTestRule<>(LostFoundEditActivity.class);
    /**
     * 앱바 이름 확인
     */
    @Test
    public void testcaseForAppBarTitle() {
        onView(ViewMatchers.withId(R.id.koin_base_app_bar_dark)).check(matches(hasDescendant(withText(R.string.navigation_item_lostfound))));
    }

    /**
     * 1. 아무것도 입력하지 않았을경우 토스트 메시지 확인
     * 2. 제목만 입력했을경우 토스트 메시지 확인
     * 3. 내용만 입력했을경우 토스트 메시지 확인
     * 4. 제목과 내용을 입력했을경우 토스트 메시지 확인
     */
    @Test
    public void testcaseForBlinkTextView(){


        onView(withId(R.id.base_appbar_dark_right_button)).perform(click());
        onView(withText("제목을 입력해주세요")).inRoot(withMessage("제목을 입력해주세요"))       //토스트메시지 도출
                .check(matches(isDisplayed()));

        onView(withId((R.id.lostfound_create_title_edittext))).perform(typeText("I lost phone"), closeSoftKeyboard());
        onView(withId(R.id.base_appbar_dark_right_button)).perform(click());
        onView(withText("내용을 입력해주세요")).inRoot(withMessage("내용을 입력해주세요"))       //토스트메시지 도출
                .check(matches(isDisplayed()));

        onView(withId((R.id.lostfound_create_title_edittext))).perform(clearText());
        onView(withId(R.id.lostfound_create_content_ediitext)).perform(typeText("call me"), closeSoftKeyboard());
        onView(withId(R.id.base_appbar_dark_right_button)).perform(click());
        onView(withText("제목을 입력해주세요")).inRoot(withMessage("제목을 입력해주세요"))       //토스트메시지 도출
                .check(matches(isDisplayed()));
    }

    /**
     * 분실물 게시글 작성중 뒤로가기 동작 시 스낵바 확인
     * 1. 앱바 왼쪽 버튼 클릭 시 스낵바 확인
     * 2. 물리적 뒤로버튼 클릭 시 스낵바 확인
     */
    @Test
    public void testcasdForBackButton(){


        onView(isRoot()).perform(ViewActions.pressBack());
        onView(allOf(withText("취소하시겠습니까?"))).check(matches(isDisplayed()));                            //스낵바 도출


        onView(withId(R.id.base_appbar_dark_left_button)).perform(click());
        onView(allOf(withText("취소하시겠습니까?"))).check(matches(isDisplayed()));                            //스낵바 도출



    }

    /**
     * 분실물 찾기 라디오버튼 클릭 시 분실일, 분실장소 글자로 변화하는지 확인
     * 분실물 습득 라이도버튼 클릭 시 습득일, 습득장소 글자로 변화하는지 확인
     */
    @Test
    public void testcaseForTextViewChange(){
        onView(withId(R.id.lostfound_create_found_radiobutton)).perform(click());
        onView(withId(R.id.lostfound_create_date_textview)).check(matches(withText("분실일")));
        onView(withId(R.id.lostfound_create_placename_textview)).check(matches(withText("분실장소")));

        onView(withId(R.id.lostfound_create_get_radiobutton)).perform(click());
        onView(withId(R.id.lostfound_create_date_textview)).check(matches(withText("습득일")));
        onView(withId(R.id.lostfound_create_placename_textview)).check(matches(withText("습득장소")));
    }

    /**
     * 연락처 공개 버튼 클릭 시 핸드폰 번호 입력 가능한지 확인
     */
    @Test
    public void testcaseForContactButtonClick(){
        onView(withId(R.id.lostfound_create_phone_public_radiobutton)).perform(click());
        onView(withId(R.id.lostfound_create_phone_num_ediitext)).perform(typeText("010-1234-1234"));    //핸드폰 번호 입력
        onView(withId(R.id.lostfound_create_phone_num_ediitext)).check(matches(withText("010-1234-1234")));            //핸드폰 번호 입력 가능한지 확인
    }
}
