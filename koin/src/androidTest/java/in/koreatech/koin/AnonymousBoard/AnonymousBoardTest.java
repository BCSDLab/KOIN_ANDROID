package in.koreatech.koin.AnonymousBoard;

import android.content.Context;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
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
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static in.koreatech.koin.matcher.TextColorMatcher.textViewTextColorMatcher;
import static in.koreatech.koin.matcher.ToastMatcher.withMessage;
import static in.koreatech.koin.viewaction.KoinRichEditorViewAction.koinRichEditorTypeText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
public class AnonymousBoardTest {
    public String comment = "comment";      //댓글창에 입력할 댓글
    public String title = "Title";          //게시글 제목에 입력할 제목
    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    /**
     * 앱바 이름 확인
     */
    @Test
    public void testcaseForAppBarTitle() {
        onView((ViewMatchers.withId(R.id.activity_main_anonymous_board_linear_layout))).perform(click());
        onView(withId(R.id.koin_base_app_bar_dark)).check(matches(hasDescendant(withText(R.string.navigation_item_anonymous_board))));
    }

    /**
     * 게시글 작성 시
     * 1. 제목 빈칸, 닉네임 작성, 비밀번호 작성
     * 2. 제목 작성, 닉네임 빈칸, 비밀번호 작성
     * 3. 제목 작성, 닉네임 작성, 비밀번호 빈칸
     * <p>
     * 4. 제목빈칸, 닉네임 빈칸, 비밀번호 작성
     * 5. 제목빈칸, 닉네임 작성, 비밀번호 빈칸
     * 6. 제목작성, 닉네임 빈칸, 비밀번호 빈칸
     * <p>
     * 7. 제목빈칸, 닉네임 빈칸, 비밀번호 빈칸
     * 8. 제목작성, 닉네임 작성, 비밀번호 작성
     * <p>
     * 아무것도 입력하지 않고 게시글 작성 완료 버튼 클릭 시 토스트메시지 확인
     */
    @Test
    public void testCaseForBlinkTextView() {
        onView((withId(R.id.activity_main_anonymous_board_linear_layout))).perform(click());
        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());                                 //게시글 작성하기 버튼 클릭
        //1번
        blinkAllTextView();
        onView(withId(R.id.article_edittext_nickname)).perform(typeText("Nickname"));
        onView(withId(R.id.article_edittext_nickname)).check(matches(withText("Nickname")));
        onView(withId(R.id.article_edittext_password)).perform(typeText("1234"));
        onView(withId(R.id.article_edittext_password)).check(matches(withText("1234")));
        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());
        onView(withText(R.string.board_title_check)).inRoot(withMessage("제목을 입력해주세요")).check(matches(isDisplayed()));      //토스트 메시지 확인


        //2번
        blinkAllTextView();
        onView(withId(R.id.article_edittext_title)).perform(typeText("Title"));
        onView(withId(R.id.article_edittext_title)).check(matches(withText(title)));
        onView(withId(R.id.article_edittext_password)).perform(typeText("1234"));
        onView(withId(R.id.article_edittext_password)).check(matches(withText("1234")));
        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());
        onView(withText(R.string.board_nickname_check)).inRoot(withMessage("닉네임을 입력해주세요")).check(matches(isDisplayed()));  //토스트 메시지 확인


        //3번
        blinkAllTextView();
        onView(withId(R.id.article_edittext_title)).perform(typeText("Title"));
        onView(withId(R.id.article_edittext_title)).check(matches(withText(title)));
        onView(withId(R.id.article_edittext_nickname)).perform(typeText("Nickname"));
        onView(withId(R.id.article_edittext_nickname)).check(matches(withText("Nickname")));
        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());
        onView(withText(R.string.board_password_check)).inRoot(withMessage("비밀번호를 입력해주세요")).check(matches(isDisplayed())); //토스트 메시지 확인


        //4번
        blinkAllTextView();
        onView(withId(R.id.article_edittext_password)).perform(typeText("1234"));
        onView(withId(R.id.article_edittext_password)).check(matches(withText("1234")));
        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());
        onView(withText(R.string.board_title_check)).inRoot(withMessage("제목을 입력해주세요")).check(matches(isDisplayed()));        //토스트 메시지 확인


        //5번
        blinkAllTextView();
        onView(withId(R.id.article_edittext_nickname)).perform(typeText("Nickname"));
        onView(withId(R.id.article_edittext_nickname)).check(matches(withText("Nickname")));
        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());
        onView(withText(R.string.board_title_check)).inRoot(withMessage("제목을 입력해주세요")).check(matches(isDisplayed()));         //토스트 메시지 확인


        //6번
        blinkAllTextView();
        onView(withId(R.id.article_edittext_title)).perform(typeText("Title"));
        onView(withId(R.id.article_edittext_title)).check(matches(withText(title)));
        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());
        onView(withText(R.string.board_password_check)).inRoot(withMessage("비밀번호를 입력해주세요")).check(matches(isDisplayed()));  //토스트 메시지 확인


        //7번
        blinkAllTextView();
        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());
        onView(withText(R.string.board_title_check)).inRoot(withMessage("제목을 입력해주세요")).check(matches(isDisplayed()));         //토스트 메시지 확인


        //8번
        blinkAllTextView();
        onView(withId(R.id.article_edittext_title)).perform(typeText("Title"));
        onView(withId(R.id.article_edittext_title)).check(matches(withText(title)));
        onView(withId(R.id.article_edittext_nickname)).perform(typeText("Nickname"));
        onView(withId(R.id.article_edittext_nickname)).check(matches(withText("Nickname")));
        onView(withId(R.id.article_edittext_password)).perform(typeText("1234"));
        onView(withId(R.id.article_edittext_password)).check(matches(withText("1234")));
    }

    /**
     * 게시글 작성 시 아무것도 입력하지 않고 이전화면으로 이동 시 스낵바 확인
     */
    @Test
    public void testCaseForAppbarLeftButton() {
        onView((withId(R.id.activity_main_anonymous_board_linear_layout))).perform(click());

        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());                         //게시글 작성하기 버튼 클릭
        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_left_button)).perform(click());                          //뒤로가기 버튼 클릭
        onView(allOf(withText(R.string.back_button_pressed))).check(matches(isDisplayed()));                                //스낵바 도출
        onView(withText("YES")).perform(click());                                                                           //YES버튼 눌러서 이전 화면으로 이동
    }

    /**
     * 게시글 작성 시 아무것도 입력하지 않고 이전화면으로 이동 시 스낵바 확인
     */
    @Test
    public void testCaseForBackButton() {
        onView((withId(R.id.activity_main_anonymous_board_linear_layout))).perform(click());

        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());     //게시글 작성하기 버튼 클릭
        onView(isRoot()).perform(pressBack());                                                          //뒤로가기 물리적 버튼 클릭
        onView(isRoot()).perform(pressBack());                                                          //뒤로가기 물리적 버튼 클릭
        onView(allOf(withText(R.string.back_button_pressed))).check(matches(isDisplayed()));            //스낵바 도출
        onView(withText("YES")).perform(click());                                                       //YES버튼 눌러서 이전 화면으로 이동
    }

    /**
     * 게시글 작성 이후 수정, 게시글 비밀번호, 완료 버튼이 보이는지 확인
     */
    @Test
    public void testCaseForCreateArticle() {
        onView((withId(R.id.activity_main_anonymous_board_linear_layout))).perform(click());

        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());                                         //게시글 작성하기 버튼 클릭

        onView(withId(R.id.article_edittext_title)).perform(typeText("Title"));                                              //제목에 "Title"입력
        onView(withId(R.id.article_edittext_title)).check(matches(withText(title)));                                                        //Title이 맞는지 확인
        onView(withId(R.id.article_edittext_nickname)).perform(typeText("Nickname"));                                        //닉네임에 "Nickname" 입력
        onView(withId(R.id.article_edittext_nickname)).check(matches(withText("Nickname")));                                                //Nickname이 맞는지 확인
        onView(withId(R.id.article_edittext_password)).perform(typeText("1234"));                                            //비밀번호에 "1234"입력
        onView(withId(R.id.article_edittext_password)).check(matches(withText("1234")));                                                    //1234 맞는지 확인
        onView(withId(R.id.article_editor_content)).perform(koinRichEditorTypeText("Contents"), closeSoftKeyboard());                       //게시글 내용에 "Contents"입력

        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());                                         //게시글 작성완료 버튼 클릭

        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        onView(withId(R.id.article_edit_button)).check(matches(withText(R.string.board_edit)));                                             //수정버튼 확인
        onView(withId(R.id.article_delete_button)).check(matches(withText(R.string.comment_remove)));                                       //삭제버튼 확인
        onView(withId(R.id.article_delete_button)).check(matches(textViewTextColorMatcher(context.getResources().getColor(R.color.red1)))); //삭제버튼 글자색이 빨간색인지 확인
    }

    /**
     * 게시글 삭제 테스트
     */
    @Test
    public void testCaseForArticleDeleteButtonClick() {

        onView((withId(R.id.activity_main_anonymous_board_linear_layout))).perform(click());

        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());                     //게시글 작성하기 버튼 클릭

        onView(withId(R.id.article_edittext_title)).perform(typeText("Title"));                          //제목에 "Title"입력
        onView(withId(R.id.article_edittext_title)).check(matches(withText(title)));                                    //Title이 맞는지 확인
        onView(withId(R.id.article_edittext_nickname)).perform(typeText("Nickname"));                    //닉네임에 "Nickname" 입력
        onView(withId(R.id.article_edittext_nickname)).check(matches(withText("Nickname")));                            //Nickname이 맞는지 확인
        onView(withId(R.id.article_edittext_password)).perform(typeText("1234"));                        //비밀번호에 "1234"입력
        onView(withId(R.id.article_edittext_password)).check(matches(withText("1234")));                                //1234 맞는지 확인
        onView(withId(R.id.article_editor_content)).perform(koinRichEditorTypeText("Contents"), closeSoftKeyboard());   //게시글 내용에 "Contents"입력

        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());                     //게시글 작성완료 버튼 클릭

        onView(withId(R.id.article_edittext_password)).perform(typeText("1234"));                        //비밀번호 "1234"입력
        onView(withId(R.id.article_delete_button)).perform(click());                                                    //삭제버튼 클릭

        onView(allOf(withText("게시글을 삭제할까요?\n댓글도 모두 사라집니다."))).check(matches(isDisplayed()));           //토스트메시지 도출
        onView(withText("확인")).perform(click());                                                                      //YES 버튼 클릭
    }

    /**
     * editTextview의 글자를 지우는 함수
     */
    public void blinkAllTextView() {
        onView(withId(R.id.article_edittext_title)).perform(clearText());
        onView(withId(R.id.article_edittext_nickname)).perform(clearText());
        onView(withId(R.id.article_edittext_password)).perform(clearText());
    }


}

