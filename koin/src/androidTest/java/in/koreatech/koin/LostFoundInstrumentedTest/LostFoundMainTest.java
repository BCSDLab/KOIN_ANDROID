package in.koreatech.koin.LostFoundInstrumentedTest;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import in.koreatech.koin.R;
import in.koreatech.koin.ui.lostfound.LostFoundMainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static in.koreatech.koin.matcher.TextColorMatcher.textViewTextColorMatcher;
import static in.koreatech.koin.matcher.ToastMatcher.withMessage;
import static org.hamcrest.Matchers.allOf;

public class LostFoundMainTest {
    @Rule
    public ActivityTestRule<LostFoundMainActivity> activityRule = new ActivityTestRule<>(LostFoundMainActivity.class);

    /**
     * 앱바 이름 확인
     */
    @Test
    public void testcaseForAppBarTitle() {
        onView(ViewMatchers.withId(R.id.koin_base_app_bar_dark)).check(matches(hasDescendant(withText(R.string.navigation_item_lostfound))));
    }

    /**
     * 게시판 생성 확인
     */
    @Test
    public void testcaseForBlinkTextView() {
        onView(withId(R.id.base_appbar_dark_right_button)).perform(click());                                                            //분실물 게시글 작성버튼 클릭

        onView(withId((R.id.lostfound_create_title_edittext))).perform(typeText("I lost phone"), closeSoftKeyboard());   //제목입력
        onView(withId(R.id.lostfound_create_content_ediitext)).perform(typeText("call me"), closeSoftKeyboard());        //내용 입력

        onView(withId(R.id.base_appbar_dark_right_button)).perform(click());                                                            //작성 완료 버튼 클릭

        onView(withId(R.id.lostfound_detail_title_textview)).check(matches(isDisplayed()));                                             //입력한 제목이 생성되었는지 확인
    }

    /**
     * 게시판 삭제 확인
     */
    @Test
    public void testcasdForBackButton() {
        onView(withId(R.id.base_appbar_dark_right_button)).perform(click());                                                                                              //분실물 게시글 작성버튼 클릭

        onView(withId((R.id.lostfound_create_title_edittext))).perform(typeText("I lost phone"), closeSoftKeyboard());                                     //제목입력
        onView(withId(R.id.lostfound_create_content_ediitext)).perform(typeText("call me"), closeSoftKeyboard());                                          //내용 입력

        onView(withId(R.id.base_appbar_dark_right_button)).perform(click());                                                                                              //작성 완료 버튼 클릭

        onView(withId(R.id.lostfound_detail_delete_button)).check(matches(textViewTextColorMatcher(activityRule.getActivity().getResources().getColor(R.color.red1))));   //삭제 버튼 색상 확인
        onView(withId(R.id.lostfound_detail_delete_button)).perform(click());                                                                                             //삭제 버튼 클릭


        onView(allOf(withText(R.string.lost_and_found_remove_question))).check(matches(isDisplayed()));                                                                   //스낵바 도출
        onView(withText("YES")).perform(click());                                                                                                                         //YES 버튼 클릭
    }

    /**
     * 게시판 수정 확인
     */
    @Test
    public void testcaseForTextViewChange() {
        onView(withId(R.id.base_appbar_dark_right_button)).perform(click());                                                            //분실물 게시글 작성버튼 클릭

        onView(withId((R.id.lostfound_create_title_edittext))).perform(typeText("I lost phone"), closeSoftKeyboard());  //제목입력
        onView(withId(R.id.lostfound_create_content_ediitext)).perform(typeText("call me"), closeSoftKeyboard());       //내용 입력

        onView(withId(R.id.base_appbar_dark_right_button)).perform(click());                                                            //작성 완료 버튼 클릭

        onView(withId(R.id.lostfound_detail_edit_button)).perform(click());

        onView(withId((R.id.lostfound_create_title_edittext))).perform(clearText());                                                    //제목글자 삭제
        onView(withId((R.id.lostfound_create_title_edittext))).perform(typeText("I lost bag"), closeSoftKeyboard());     //"I lost bag"입력
        onView(withId(R.id.base_appbar_dark_right_button)).perform(click());                                                            //분실물 게시글 작성버튼 클릭

        onView(withText(R.string.lost_and_found_edited)).inRoot(withMessage("수정되었습니다.")).check(matches(isDisplayed()));           //토스트메시지 확인

    }
}
