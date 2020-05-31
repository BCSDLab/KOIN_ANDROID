package in.koreatech.koin.AnonymousBoard;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import in.koreatech.koin.R;
import in.koreatech.koin.ui.main.MainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static in.koreatech.koin.viewaction.KoinRichEditorViewAction.koinRichEditorTypeText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
public class AnonymouseBoardEditTest {
    public String title = "Title";          //게시글 제목에 입력할 제목

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    /**
     * 게시글 수정 중 뒤로가기 물리적 버튼 클릭 시 스낵바 도출 테스트
     */
    @Test
    public void testCaseForArticleEditButtonClickAndBackButton() {
        onView((withId(R.id.activity_main_anonymous_board_linear_layout))).perform(click());

        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());                     //게시글 작성하기 버튼 클릭

        onView(withId(R.id.article_edittext_title)).perform(typeText("Title"), closeSoftKeyboard());     //게시글 제목 "Title"입력
        onView(withId(R.id.article_editor_content)).perform(koinRichEditorTypeText("Contents"), closeSoftKeyboard());   //게시글 내용에 "Contents"입력
        onView(withId(R.id.article_edittext_nickname)).perform(typeText("Nickname"));                    //닉네임에 "Nickname" 입력
        onView(withId(R.id.article_edittext_password)).perform(typeText("1234"));                        //비밀번호 "1234"입력


        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());                      //게시글 작성완료 버튼 클릭

        onView(withId(R.id.article_edittext_password)).perform(typeText("1234"));                         //비밀번호 "1234"입력
        onView(withId(R.id.article_edit_button)).check(matches(withText("수정")));                                       //방금 작성한 게시글에서 수정버튼 클릭
        onView(withId(R.id.article_edit_button)).perform(click());

        onView(isRoot()).perform(pressBack());                                                                           //뒤로가기 버튼 클릭
        onView(isRoot()).perform(pressBack());
        onView(allOf(withText(R.string.back_button_pressed))).check(matches(isDisplayed()));                             //스낵바 도출 확인
        onView(withText("YES")).perform(click());                                                                        //YES 버튼 클릭

    }

    /**
     * 게시글 수정하는 테스트
     */
    @Test
    public void testCaseForArticleEditButtonClick() {
        onView((withId(R.id.activity_main_anonymous_board_linear_layout))).perform(click());

        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());                         //게시글 작성하기 버튼 클릭

        onView(withId(R.id.article_edittext_title)).perform(typeText("Title"));                             //제목에 "Title"입력
        onView(withId(R.id.article_edittext_title)).check(matches(withText(title)));                                       //Title이 맞는지 확인
        onView(withId(R.id.article_edittext_nickname)).perform(typeText("Nickname"));                       //닉네임에 "Nickname" 입력
        onView(withId(R.id.article_edittext_nickname)).check(matches(withText("Nickname")));                               //Nickname이 맞는지 확인
        onView(withId(R.id.article_edittext_password)).perform(typeText("1234"));                           //비밀번호에 "1234"입력
        onView(withId(R.id.article_edittext_password)).check(matches(withText("1234")));                                   //1234 맞는지 확인
        onView(withId(R.id.article_editor_content)).perform(koinRichEditorTypeText("Contents"), closeSoftKeyboard());      //게시글 내용에 "Contents"입력

        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());                        //게시글 작성완료 버튼 클릭

        onView(withId(R.id.article_edittext_password)).perform(typeText("1234"));                            //비밀번호 "1234"입력
        onView(withId(R.id.article_edittext_password)).check(matches(withText("1234")));                                    //비밀번호 확인
        onView(withId(R.id.article_edit_button)).check(matches(withText("수정")));                                          //방금 작성한 게시글에서 수정버튼 클릭
        onView(withId(R.id.article_edit_button)).perform(click());
        onView(withId(R.id.article_edittext_title)).perform(closeSoftKeyboard());                                           //키보드 닫기
        onView(withId(R.id.article_edittext_title)).perform(clearText());                                                   //게시글 제목 지우기
        onView(withId(R.id.article_edittext_title)).perform(typeText("Title2"), closeSoftKeyboard());        //게시글 제목을 Title2로 입력
        onView(withId(R.id.article_edittext_title)).check(matches(withText("Title2")));                                     //게시글 제목 확인


        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());                         //게시글 작성 버튼 클릭

    }
}
