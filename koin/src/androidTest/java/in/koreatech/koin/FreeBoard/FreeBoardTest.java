package in.koreatech.koin.FreeBoard;

import android.content.Context;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Rule;
import org.junit.Test;

import in.koreatech.koin.R;
import in.koreatech.koin.ui.board.BoardActivity;

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
import static in.koreatech.koin.matcher.TextColorMatcher.textViewTextColorMatcher;
import static in.koreatech.koin.viewaction.KoinRichEditorViewAction.koinRichEditorTypeText;
import static org.hamcrest.Matchers.allOf;

public class FreeBoardTest {
    public String title = "Title";          //게시글 제목에 입력할 제목
    @Rule
    public IntentsTestRule<BoardActivity> activityRule = new IntentsTestRule<>(BoardActivity.class);

    /**
     * 앱바 이름 확인
     */
    @Test
    public void testcaseForAppBarTitle() {
        onView(ViewMatchers.withId(R.id.koin_base_app_bar_dark)).check(matches(hasDescendant(withText(R.string.navigation_item_free_board))));
    }


    /**
     * 게시글 작성 이후 수정, 완료 버튼이 보이는지 확인
     */
    @Test
    public void testCaseForCreateArticle() {
        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());                                         //게시글 작성하기 버튼 클릭

        onView(withId(R.id.article_edittext_title)).perform(typeText("Title"));                                              //게시글 제목 "Title"입력
        onView(withId(R.id.article_edittext_title)).check(matches(withText(title)));                                                        //"Title"이 맞는지 홗인
        onView(withId(R.id.article_editor_content)).perform(koinRichEditorTypeText("Contents"), closeSoftKeyboard());                       //게시글 내용에 "Contents"입력

        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());                                         //게시글 작성완료 버튼 클릭

        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        onView(withId(R.id.article_edit_button)).check(matches(withText("수정")));                                                            //수정버튼 확인
        onView(withId(R.id.article_delete_button)).check(matches(withText("삭제")));                                                          //삭제버튼 확인
        onView(withId(R.id.article_delete_button)).check(matches(textViewTextColorMatcher(context.getResources().getColor(R.color.red1))));   //삭제버튼 글자색이 빨간색인지 확인
    }

    /**
     * 게시글 수정 중 뒤로가기 물리적 버튼 클릭 시 스낵바 도출 테스트
     */
    @Test
    public void testCaseForArticleEditButtonClickAndBackButton() {
        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());                         //게시글 작성하기 버튼 클릭

        onView(withId(R.id.article_edittext_title)).perform(typeText("Title"), closeSoftKeyboard());         //게시글 제목 "Title"입력
        onView(withId(R.id.article_editor_content)).perform(koinRichEditorTypeText("Contents"), closeSoftKeyboard());       //게시글 내용에 "Contents"입력

        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());                         //게시글 작성완료 버튼 클릭

        onView(withId(R.id.article_edit_button)).check(matches(withText(R.string.board_edit)));                             //방금 작성한 게시글에서 수정버튼 클릭
        onView(withId(R.id.article_edit_button)).perform(click());
        onView(withId(R.id.article_edittext_title)).perform(clearText());                                                   //게시글 제목을 지운다
        onView(withId(R.id.article_edittext_title)).perform(typeText("Title2"), closeSoftKeyboard());       //게시글 제목을 Title2로 입력
        onView(withId(R.id.article_edittext_title)).check(matches(withText("Title2")));                                    //게시글 제목 확인

        onView(isRoot()).perform(pressBack());                                                                             //뒤로가기 버튼 클릭
        onView(isRoot()).perform(pressBack());
        onView(allOf(withText(R.string.back_button_pressed))).check(matches(isDisplayed()));                           //스낵바 도출 확인
        onView(withText("YES")).perform(click());                                                                          //YES 버튼 클릭
    }

    /**
     * 게시글 수정하는 테스트
     */
    @Test
    public void testCaseForArticleEditButtonClick() {
        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());                     //게시글 작성하기 버튼 클릭

        onView(withId(R.id.article_edittext_title)).perform(typeText("Title"), closeSoftKeyboard());     //게시글 제목 "Title"입력
        onView(withId(R.id.article_editor_content)).perform(koinRichEditorTypeText("Contents"), closeSoftKeyboard());   //게시글 내용에 "Contents"입력

        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());                     //게시글 작성완료 버튼 클릭

        onView(withId(R.id.article_edit_button)).check(matches(withText(R.string.board_edit)));                         //방금 작성한 게시글에서 수정버튼 클릭
        onView(withId(R.id.article_edit_button)).perform(click());
        onView(withId(R.id.article_edittext_title)).perform(clearText());                                               //게시글 제목을 지운다
        onView(withId(R.id.article_edittext_title)).perform(typeText("Title2"), closeSoftKeyboard());    //게시글 제목을 Title2로 입력
        onView(withId(R.id.article_edittext_title)).check(matches(withText("Title2")));                                 //게시글 제목 확인

        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());                     //게시글 작성 버튼 클릭
    }

    /**
     * 게시글 삭제 테스트
     */
    @Test
    public void testCaseForArticleDeleteButtonClick() {

        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());                      //게시글 작성하기 버튼 클릭

        onView(withId(R.id.article_edittext_title)).perform(typeText("Title"));                           //게시글 제목 "Title"입력
        onView(withId(R.id.article_edittext_title)).check(matches(withText(title)));                                     //"Title"이 맞는지 홗인
        onView(withId(R.id.article_editor_content)).perform(koinRichEditorTypeText("Contents"), closeSoftKeyboard());    //게시글 내용에 "Contents"입력

        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());                      //게시글 작성완료 버튼 클릭

        onView(withId(R.id.article_delete_button)).check(matches(withText(R.string.comment_remove)));                    //삭제버튼 확인
        onView(withId(R.id.article_delete_button)).perform(click());                                                     //삭제 버튼 클릭

        onView(allOf(withText(R.string.board_remove_question ))).check(matches(isDisplayed()));                          //토스트메시지 도출
        onView(withText("YES")).perform(click());                                                                        //YES 버튼 클릭
    }


}
