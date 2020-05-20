package in.koreatech.koin.FreeBoard;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import in.koreatech.koin.R;
import in.koreatech.koin.ui.board.BoardActivity;

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
import static in.koreatech.koin.matcher.RecyclerViewMatcher.atPositionOnView;
import static in.koreatech.koin.matcher.ToastMatcher.withMessage;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

public class FreeBoardCommentTest {
    String comment = "comment";
    @Rule
    public ActivityTestRule<BoardActivity> activityRule = new ActivityTestRule<>(BoardActivity.class);

    /**
     * 앱바 이름 확인
     */
    @Test
    public void testcaseForAppBarTitle() {
        onView(ViewMatchers.withId(R.id.koin_base_app_bar_dark)).check(matches(hasDescendant(withText(R.string.navigation_item_free_board))));
    }


    /**
     * 댓글을 입력하고 취소버튼을 누를 경우 입력한 글이 지워지는지 확인
     */
    @Test
    public void testcaseForCancelButtonClick() {
        onView(withId(R.id.freeboard_recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));            //첫번째 게시글 클릭

        onView(withId(R.id.article_comment_write_button)).perform(click());                                                             //댓글 버튼 클릭

        onView(withId(R.id.article_comment_content_edittext)).perform(typeText(comment), closeSoftKeyboard());                          //댓글창에 "comment"입력
        onView(withId(R.id.article_comment_content_edittext)).check(matches(withText(comment)));                                        //입력한 댓글이 "comment"인지 확인


        onView(withId(R.id.article_comment_cancel_button)).perform(click());                                                            //취소 버튼 클릭

        onView(withId(R.id.article_comment_content_edittext)).check(matches(withText("")));                                             //댓글창에 글자가 지워졌는지 확인
    }

    /**
     * 댓글을 작성하고 토스트 메시지 확인
     */
    @Test
    public void testcaseForCreateComment() {
        onView(withId(R.id.freeboard_recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));            //첫번째 게시글 클릭

        onView(withId(R.id.article_comment_write_button)).perform(click());                                                             //댓글 버튼 클릭

        onView(withId(R.id.article_comment_content_edittext)).perform(typeText(comment), closeSoftKeyboard());                          //댓글창에 "comment"입력
        onView(withId(R.id.article_comment_content_edittext)).check(matches(withText(comment)));                                        //입력한 댓글이 "comment"인지 확인

        onView(withId(R.id.article_comment_register_button)).perform(click());                                                          //등록 버튼 클릭
        onView(withText(R.string.comment_created)).inRoot(withMessage("댓글이 등록되었습니다.")).check(matches(isDisplayed()));           //토스트메시지 확인
    }

    /**
     * 댓글 수정 시 댓글 내용 빈칸으로 하고 등록 버튼 클릭 후 댓글 내용 수정 테스트
     */
    @Test
    public void testCaseForArticleCommentEdit() {

        onView(withId(R.id.freeboard_recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));           //첫번째 게시글 클릭

        onView(withId(R.id.article_comment_write_button)).perform(click());                                                            //댓글 버튼 클릭

        onView(withId(R.id.article_comment_content_edittext)).perform(typeText(comment), closeSoftKeyboard());                         //댓글창에 "comment"입력
        onView(withId(R.id.article_comment_content_edittext)).check(matches(withText(comment)));                                       //입력한 댓글이 "comment"인지 확인

        onView(withId(R.id.article_comment_register_button)).perform(click());                                                         //등록 버튼 클릭
        onView(withText(R.string.comment_created)).inRoot(withMessage("댓글이 생성되었습니다.")).check(matches(isDisplayed()));          //토스트메시지 확인


        onView(atPositionOnView(R.id.article_comment_content_recyclerview, 0, R.id.comment_edit)).perform(click());           //첫번째 댓글의 수정버튼 클릭


        onView(withId(R.id.article_comment_content_edittext)).perform(clearText());                                                   //댓글 내용을 빈칸으로 입력
        onView(withId(R.id.article_comment_register_button)).perform(click());                                                        //등록 버튼 클릭
        onView(withText(R.string.coment_typing_content)).inRoot(withMessage("내용을 입력해주세요.")).check(matches(isDisplayed()));    //토스트 메시지 도출


        onView(withId(R.id.article_comment_content_edittext)).perform(typeText("comment2"), closeSoftKeyboard());     //댓글 내용 수정
        onView(withId(R.id.article_comment_content_edittext)).check(matches(withText("comment2")));                                  //수정된 댓글 확인

        onView(withId(R.id.article_comment_register_button)).perform(click());                                                       //등록 버튼 클릭
        onView(withText(R.string.comment_edited)).inRoot(withMessage("댓글이 수정되었습니다.")).check(matches(isDisplayed()));        //토스트메시지 도출


    }


    /**
     * 댓글 삭제 확인
     */
    @Test
    public void testcaseForDeleteComment() {
        onView(withId(R.id.freeboard_recyclerview)).inRoot(RootMatchers.withDecorView(                                                  //첫번째 게시글 클릭
                is(activityRule.getActivity().getWindow().getDecorView())))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.article_comment_write_button)).perform(click());                                                             //댓글 버튼 클릭

        onView(withId(R.id.article_comment_content_edittext)).perform(typeText(comment), closeSoftKeyboard());                          //댓글창에 "comment"입력
        onView(withId(R.id.article_comment_content_edittext)).check(matches(withText(comment)));                                        //입력한 댓글이 "comment"인지 확인

        onView(withId(R.id.article_comment_register_button)).perform(click());                                                          //완료 버튼 클릭
        onView(withText(R.string.comment_created)).inRoot(withMessage("댓글이 등록되었습니다.")).check(matches(isDisplayed()));          //토스트 메시지 확인


        onView(atPositionOnView(R.id.article_comment_content_recyclerview, 0, R.id.comment_remove)).perform(click());           //첫번째 댓글의 삭제버튼 클릭

        onView(allOf(withText(R.string.comment_remove_question))).check(matches(isDisplayed()));                                        //스낵바 도출
        onView(withText("YES")).perform(click());                                                                                       //YES 버튼 클릭

        onView(withText(R.string.comment_deleted)).inRoot(withMessage("댓글이 삭제되었습니다.")).check(matches(isDisplayed()));          //토스트 메시지 도출

    }

}
