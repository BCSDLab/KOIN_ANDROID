package in.koreatech.koin.AnonymousBoard;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import in.koreatech.koin.R;
import in.koreatech.koin.ui.main.MainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static in.koreatech.koin.matcher.RecyclerViewMatcher.atPositionOnView;
import static in.koreatech.koin.matcher.ToastMatcher.withMessage;
import static in.koreatech.koin.viewaction.NestedScrollViewAction.nestedScrollTo;
import static org.hamcrest.Matchers.allOf;

@LargeTest
public class AnonymouseBoardCommentTest {
    public String comment = "comment";      //댓글창에 입력할 댓글

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    /**
     * 게시글에 댓글 생성하는 테스트
     */
    @Test
    public void testCaseForArticleCommentCreate() {
        onView((withId(R.id.activity_main_anonymous_board_linear_layout))).perform(click());

        onView(withId(R.id.freeboard_recyclerview)).perform(actionOnItemAtPosition(0, click()));

        onView(withId(R.id.article_comment_write_button)).perform(click());                                                         //댓글 버튼 클릭

        onView(withId(R.id.article_comment_content_recyclerview)).perform(nestedScrollTo());                                        //화면을 밑으로 스크롤한다
        onView(withId(R.id.article_comment_password_edittext)).perform(typeText("1234"), closeSoftKeyboard());       //비밀번호 "1234" 입력력
        onView(withId(R.id.article_comment_password_edittext)).check(matches(withText("1234")));                                    //비밀번호 확인
        onView(withId(R.id.article_comment_nickname_edittext)).perform(typeText("Nickname"), closeSoftKeyboard());   //닉네임 "Nickname"입력
        onView(withId(R.id.article_comment_nickname_edittext)).check(matches(withText("Nickname")));                                //닉네임 확인
        onView(withId(R.id.article_comment_content_edittext)).perform(typeText(comment), closeSoftKeyboard());                      //댓글창에 "comment"입력
        onView(withId(R.id.article_comment_content_edittext)).check(matches(withText(comment)));                                    //입력한 댓글이 "comment"인지 확인

        onView(withId(R.id.article_comment_anonymous_register_button)).perform(click());                                            //등록 버튼 클릭
        onView(withText("댓글이 등록되었습니다.")).inRoot(withMessage("댓글이 등록되었습니다.")).check(matches(isDisplayed()));        //토스트 메시지 확인

    }

    /**
     * 댓글 내용을 빈칸으로 작성할 경우 토스트 메시지 확인
     */
    @Test
    public void testcaseForCreateBlinkContents() {
        onView((withId(R.id.activity_main_anonymous_board_linear_layout))).perform(click());

        onView(withId(R.id.freeboard_recyclerview)).perform(actionOnItemAtPosition(0, click()));

        onView(withId(R.id.article_comment_write_button)).perform(click());
        onView(withId(R.id.article_comment_anonymous_register_button)).perform(click());                                                   //완료 버튼 클릭
        onView(withText(R.string.comment_input_content)).inRoot(withMessage("내용을 입력해주세요.")).check(matches(isDisplayed()));         //토스트 메시지 확인
    }

    /**
     * 댓글내용 입력 후  작성할 경우 토스트 메시지 확인
     */
    @Test
    public void testcaseForCreateBlinkPassword() {
        onView((withId(R.id.activity_main_anonymous_board_linear_layout))).perform(click());

        onView(withId(R.id.freeboard_recyclerview)).perform(actionOnItemAtPosition(0, click()));

        onView(withId(R.id.article_comment_write_button)).perform(click());
        onView(withId(R.id.article_comment_content_edittext)).perform(typeText(comment), closeSoftKeyboard());                             //댓글창에 "comment"입력
        onView(withId(R.id.article_comment_content_edittext)).check(matches(withText(comment)));                                           //입력한 댓글이 "comment"인지 확인

        onView(withId(R.id.article_comment_anonymous_register_button)).perform(click());                                                   //등록 버튼 클릭
        onView(withText(R.string.comment_input_password)).inRoot(withMessage("비밀번호를 입력해주세요")).check(matches(isDisplayed()));      //토스트메시지 도출출
    }

    /**
     * 댓글내용, 비밀번호 입력 후 작성할 경우 토스트 메시지 확인
     */
    @Test
    public void testcaseForCreateBlinkNickname() {
        onView((withId(R.id.activity_main_anonymous_board_linear_layout))).perform(click());

        onView(withId(R.id.freeboard_recyclerview)).perform(actionOnItemAtPosition(0, click()));

        onView(withId(R.id.article_comment_write_button)).perform(click());
        onView(withId(R.id.article_comment_content_edittext)).perform(typeText(comment), closeSoftKeyboard());                             //댓글창에 "comment"입력
        onView(withId(R.id.article_comment_content_edittext)).check(matches(withText(comment)));                                           //입력한 댓글이 "comment"인지 확인
        onView(withId(R.id.article_comment_password_edittext)).perform(typeText("1234"), closeSoftKeyboard());              //비밀번호 "1234" 입력력
        onView(withId(R.id.article_comment_password_edittext)).check(matches(withText("1234")));                                           //비밀번호 확인

        onView(withId(R.id.article_comment_anonymous_register_button)).perform(click());                                                   //등록 버튼 클릭
        onView(withText(R.string.comment_input_nickname)).inRoot(withMessage("닉네임을 입력해주세요.")).check(matches(isDisplayed()));      //토스트메시지 도출출
    }

    /**
     * 댓글 수정 테스트
     */
    @Test
    public void testCaseForArticleCommentEdit() {

        onView((withId(R.id.activity_main_anonymous_board_linear_layout))).perform(click());

        onView(withId(R.id.freeboard_recyclerview)).perform(actionOnItemAtPosition(0, click()));

        onView(withId(R.id.article_comment_write_button)).perform(click());                                                                //댓글 버튼 클릭
        /*댓글 생성*/
        onView(withId(R.id.article_comment_content_recyclerview)).perform(nestedScrollTo());                                               //화면을 밑으로 스크롤한다
        onView(withId(R.id.article_comment_password_edittext)).perform(typeText("1234"), closeSoftKeyboard());              //비밀번호 "1234" 입력력
        onView(withId(R.id.article_comment_password_edittext)).check(matches(withText("1234")));                                           //비밀번호 확인
        onView(withId(R.id.article_comment_nickname_edittext)).perform(typeText("Nickname"), closeSoftKeyboard());          //닉네임 "Nickname"입력
        onView(withId(R.id.article_comment_nickname_edittext)).check(matches(withText("Nickname")));                                       //닉네임 확인
        onView(withId(R.id.article_comment_content_edittext)).perform(typeText(comment), closeSoftKeyboard());                             //댓글창에 "comment"입력
        onView(withId(R.id.article_comment_content_edittext)).check(matches(withText(comment)));                                           //입력한 댓글이 "comment"인지 확인

        onView(withId(R.id.article_comment_anonymous_register_button)).perform(click());                                                   //등록 버튼 클릭
        onView(withText(R.string.comment_created)).inRoot(withMessage("댓글이 등록되었습니다.")).check(matches(isDisplayed()));             //토스트 메시지 확인

        /*댓글 수정*/

        onView(atPositionOnView(R.id.article_comment_content_recyclerview, 0, R.id.comment_edit)).perform(click());               //댓글 목록중 첫전째 댓글의 수정 버튼 클릭

        onView(withId(R.id.article_comment_content_recyclerview)).perform(nestedScrollTo());                                              //화면을 밑으르 스크롤한다
        onView(withId(R.id.article_comment_anonymous_register_button)).perform(click());                                                  //등록 버튼 클릭
        onView(withText(R.string.comment_input_password)).inRoot(withMessage("비밀번호를 입력해주세요")).check(matches(isDisplayed()));     //토스트메시지 도출출


        onView(withId(R.id.article_comment_password_edittext)).perform(typeText("1234"), closeSoftKeyboard());             //비밀번호 "1234"입력
        onView(withId(R.id.article_comment_password_edittext)).check(matches(withText("1234")));                                          //비밀번호 확인

        onView(withId(R.id.article_comment_content_edittext)).perform(typeText("comment2"), closeSoftKeyboard());          //댓글창에 "comment2"입력
        onView(withId(R.id.article_comment_anonymous_register_button)).perform(click());                                                  //등록 버튼 클릭
        onView(withText(R.string.comment_edited)).inRoot(withMessage("댓글이 수정되었습니다.")).check(matches(isDisplayed()));             //토스트메시지 도출


    }

    /**
     * 댓글 삭제 테스트
     */
    @Test
    public void testCaseForArticleCommentDelete() {

        onView((withId(R.id.activity_main_anonymous_board_linear_layout))).perform(click());

        onView(withId(R.id.freeboard_recyclerview)).perform(actionOnItemAtPosition(0, click()));

        onView(withId(R.id.article_comment_write_button)).perform(click());                                                                //댓글 버튼 클릭
        /*댓글 생성*/
        onView(withId(R.id.article_comment_content_recyclerview)).perform(nestedScrollTo());                                               //화면을 밑으로 스크롤한다
        onView(withId(R.id.article_comment_password_edittext)).perform(typeText("1234"), closeSoftKeyboard());              //비밀번호 "1234" 입력력
        onView(withId(R.id.article_comment_password_edittext)).check(matches(withText("1234")));                                           //비밀번호 확인
        onView(withId(R.id.article_comment_nickname_edittext)).perform(typeText("Nickname"), closeSoftKeyboard());          //닉네임 "Nickname"입력
        onView(withId(R.id.article_comment_nickname_edittext)).check(matches(withText("Nickname")));                                       //닉네임 확인
        onView(withId(R.id.article_comment_content_edittext)).perform(typeText(comment), closeSoftKeyboard());                             //댓글창에 "comment"입력
        onView(withId(R.id.article_comment_content_edittext)).check(matches(withText(comment)));                                           //입력한 댓글이 "comment"인지 확인

        onView(withId(R.id.article_comment_anonymous_register_button)).perform(click());                                                   //등록 버튼 클릭
        onView(withText(R.string.comment_created)).inRoot(withMessage("댓글이 등록되었습니다.")).check(matches(isDisplayed()));             //토스트 메시지 확인

        /*댓글 삭제*/
        onView(atPositionOnView(R.id.article_comment_content_recyclerview, 0, R.id.comment_edit)).perform(click());                //댓글 목록중 첫전째 댓글의 수정 버튼 클릭

        onView(withId(R.id.article_comment_content_recyclerview)).perform(nestedScrollTo());                                                //화면을 밑으로 스크롤한다
        onView(withId(R.id.article_comment_anonymous_delete_button)).perform(click());                                                      //삭제 버튼 클릭
        onView(withText(R.string.comment_input_password)).inRoot(withMessage("비밀번호를 입력해주세요.")).check(matches(isDisplayed()));     //토스트 메시지 도출


        onView(withId(R.id.article_comment_content_recyclerview)).perform(nestedScrollTo());                                                //화면을 밑으로 스크롤한다
        onView(withId(R.id.article_comment_password_edittext)).perform(typeText("1234"), closeSoftKeyboard());               //비밀번호 "1234"입력
        onView(withId(R.id.article_comment_password_edittext)).check(matches(withText("1234")));                                            //비밀번호 확인
        onView(withId(R.id.article_comment_anonymous_delete_button)).perform(click());

        onView(allOf(withText(R.string.comment_delete_check_question))).check(matches(isDisplayed()));                                      //스낵바 도출
        onView(withText("YES")).perform(click());                                                                                           //YES 버튼 클릭

        onView(withText(R.string.comment_deleted)).inRoot(withMessage("댓글이 삭제되었습니다.")).check(matches(isDisplayed()));              //토스트 메시지 도출

    }


}
