package in.koreatech.koin;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.IBinder;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.Root;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.util.HumanReadables;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import in.koreatech.koin.ui.board.BoardActivity;
import in.koreatech.koin.ui.board.KoinRichEditor;
import in.koreatech.koin.ui.main.MainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
@LargeTest
public class AnonymousBoardTest {
    public String comment = "comment";      //댓글창에 입력할 댓글
    public String title = "Title";          //게시글 제목에 입력할 제목
    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class);

    /**
     * 앱바 이름 확인
     */
    @Test
    public void testcaseForAppBarTitle() {
        onView((withId(R.id.activity_main_anonymous_board_linear_layout))).perform(click());
        onView(withId(R.id.koin_base_app_bar_dark)).check(matches(hasDescendant(withText(R.string.navigation_item_anonymous_board))));    }

    /**
     * 게시글 작성 시
     * 1. 제목 빈칸, 닉네임 작성, 비밀번호 작성
     * 2. 제목 작성, 닉네임 빈칸, 비밀번호 작성
     * 3. 제목 작성, 닉네임 작성, 비밀번호 빈칸
     *
     * 4. 제목빈칸, 닉네임 빈칸, 비밀번호 작성
     * 5. 제목빈칸, 닉네임 작성, 비밀번호 빈칸
     * 6. 제목작성, 닉네임 빈칸, 비밀번호 빈칸
     *
     * 7. 제목빈칸, 닉네임 빈칸, 비밀번호 빈칸
     * 8. 제목작성, 닉네임 작성, 비밀번호 작성
    *
     * 아무것도 입력하지 않고 게시글 작성 완료 버튼 클릭 시 토스트메시지 확인
     */
    @Test
    public void testCaseForBlinkTextView(){
        onView((withId(R.id.activity_main_anonymous_board_linear_layout))).perform(click());
        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());     //게시글 작성하기 버튼 클릭
        //1번
        blinkAllTextView();
        onView(withId(R.id.article_edittext_nickname)).perform(typeText("Nickname"));
        onView(withId(R.id.article_edittext_nickname)).check(matches(withText("Nickname")));
        onView(withId(R.id.article_edittext_password)).perform(typeText("1234"));
        onView(withId(R.id.article_edittext_password)).check(matches(withText("1234")));
        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());
        onView(withText("제목을 입력해주세요")).inRoot(new ToastMatcher("제목을 입력해주세요"))                 //토스트 메시지 확인
                .check(matches(isDisplayed()));

        //2번
        blinkAllTextView();
        onView(withId(R.id.article_edittext_title)).perform(typeText("Title"));
        onView(withId(R.id.article_edittext_title)).check(matches(withText(title)));
        onView(withId(R.id.article_edittext_password)).perform(typeText("1234"));
        onView(withId(R.id.article_edittext_password)).check(matches(withText("1234")));
        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());
        onView(withText("닉네임을 입력해주세요")).inRoot(new ToastMatcher("닉네임을 입력해주세요"))                 //토스트 메시지 확인
                .check(matches(isDisplayed()));

        //3번
        blinkAllTextView();
        onView(withId(R.id.article_edittext_title)).perform(typeText("Title"));
        onView(withId(R.id.article_edittext_title)).check(matches(withText(title)));
        onView(withId(R.id.article_edittext_nickname)).perform(typeText("Nickname"));
        onView(withId(R.id.article_edittext_nickname)).check(matches(withText("Nickname")));
        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());
        onView(withText("비밀번호를 입력해주세요")).inRoot(new ToastMatcher("비밀번호를 입력해주세요"))                 //토스트 메시지 확인
                .check(matches(isDisplayed()));

        //4번
        blinkAllTextView();
        onView(withId(R.id.article_edittext_password)).perform(typeText("1234"));
        onView(withId(R.id.article_edittext_password)).check(matches(withText("1234")));
        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());
        onView(withText("제목을 입력해주세요")).inRoot(new ToastMatcher("제목을 입력해주세요"))                 //토스트 메시지 확인
                .check(matches(isDisplayed()));

        //5번
        blinkAllTextView();
        onView(withId(R.id.article_edittext_nickname)).perform(typeText("Nickname"));
        onView(withId(R.id.article_edittext_nickname)).check(matches(withText("Nickname")));
        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());
        onView(withText("제목을 입력해주세요")).inRoot(new ToastMatcher("제목을 입력해주세요"))                 //토스트 메시지 확인
                .check(matches(isDisplayed()));

        //6번
        blinkAllTextView();
        onView(withId(R.id.article_edittext_title)).perform(typeText("Title"));
        onView(withId(R.id.article_edittext_title)).check(matches(withText(title)));
        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());
        onView(withText("비밀번호를 입력해주세요")).inRoot(new ToastMatcher("비밀번호를 입력해주세요"))                 //토스트 메시지 확인
                .check(matches(isDisplayed()));

        //7번
        blinkAllTextView();
        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());
        onView(withText("제목을 입력해주세요")).inRoot(new ToastMatcher("제목을 입력해주세요"))                 //토스트 메시지 확인
                .check(matches(isDisplayed()));

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
    public void testCaseForAppbarLeftButton(){
        onView((withId(R.id.activity_main_anonymous_board_linear_layout))).perform(click());

        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());     //게시글 작성하기 버튼 클릭
        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_left_button)).perform(click());      //뒤로가기 버튼 클릭
        onView(allOf(withText(R.string.back_button_pressed))).check(matches(isDisplayed()));            //스낵바 도출
        onView(withText("YES")).perform(click());                                                       //YES버튼 눌러서 이전 화면으로 이동
    }
    /**
     * 게시글 작성 시 아무것도 입력하지 않고 이전화면으로 이동 시 스낵바 확인
     */
    @Test
    public void testCaseForBackButton(){
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
    public void testCaseForCreateArticle(){
        onView((withId(R.id.activity_main_anonymous_board_linear_layout))).perform(click());

        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());     //게시글 작성하기 버튼 클릭

        onView(withId(R.id.article_edittext_title)).perform(typeText("Title"));         //제목에 "Title"입력
        onView(withId(R.id.article_edittext_title)).check(matches(withText(title)));                    //Title이 맞는지 확인
        onView(withId(R.id.article_edittext_nickname)).perform(typeText("Nickname"));   //닉네임에 "Nickname" 입력
        onView(withId(R.id.article_edittext_nickname)).check(matches(withText("Nickname")));            //Nickname이 맞는지 확인
        onView(withId(R.id.article_edittext_password)).perform(typeText("1234"));       //비밀번호에 "1234"입력
        onView(withId(R.id.article_edittext_password)).check(matches(withText("1234")));                //1234 맞는지 확인
        onView(withId(R.id.article_editor_content)).perform(koinRichEditorTypeText("Contents"), closeSoftKeyboard());    //게시글 내용에 "Contents"입력

        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());                 //게시글 작성완료 버튼 클릭

        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        onView(withId(R.id.article_edittext_password)).check(matches(editTextwithItemHint("게시글 비밀번호"))); //게시글 비밀번호 hint메시지 확인
        onView(withId(R.id.article_edit_button)).check(matches(withText("수정")));                                //수정버튼 확인
        onView(withId(R.id.article_delete_button)).check(matches(withText("삭제")));                              //삭제버튼 확인
        onView(withId(R.id.article_delete_button)).check(matches(textViewTextColorMatcher(context.getResources().getColor(R.color.red1)))); //삭제버튼 글자색이 빨간색인지 확인
    }

    /**
     * 게시글 수정 중 뒤로가기 물리적 버튼 클릭 시 스낵바 도출 테스트
     */
    @Test
    public void testCaseForArticleEditButtonClickAndBackButton(){
        onView((withId(R.id.activity_main_anonymous_board_linear_layout))).perform(click());

        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());     //게시글 작성하기 버튼 클릭

        onView(withId(R.id.article_edittext_title)).perform(typeText("Title"), closeSoftKeyboard());    //게시글 제목 "Title"입력
        onView(withId(R.id.article_editor_content)).perform(koinRichEditorTypeText("Contents"), closeSoftKeyboard());    //게시글 내용에 "Contents"입력
        onView(withId(R.id.article_edittext_nickname)).perform(typeText("Nickname"));                   //닉네임에 "Nickname" 입력
        onView(withId(R.id.article_edittext_password)).perform(typeText("1234"));                       //비밀번호 "1234"입력


        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());                 //게시글 작성완료 버튼 클릭

        onView(withId(R.id.article_edittext_password)).perform(typeText("1234"));                       //비밀번호 "1234"입력
        onView(withId(R.id.article_edit_button)).check(matches(withText("수정")));                                    //방금 작성한 게시글에서 수정버튼 클릭
        onView(withId(R.id.article_edit_button)).perform(click());

        onView(isRoot()).perform(pressBack());                                                                          //뒤로가기 버튼 클릭
        onView(isRoot()).perform(pressBack());
        onView(allOf(withText(R.string.back_button_pressed))).check(matches(isDisplayed()));                            //스낵바 도출 확인
        onView(withText("YES")).perform(click());                                                                       //YES 버튼 클릭

    }

    /**
     * 게시글 수정하는 테스트
     */
    @Test
    public void testCaseForArticleEditButtonClick(){
        onView((withId(R.id.activity_main_anonymous_board_linear_layout))).perform(click());

        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());     //게시글 작성하기 버튼 클릭

        onView(withId(R.id.article_edittext_title)).perform(typeText("Title"));         //제목에 "Title"입력
        onView(withId(R.id.article_edittext_title)).check(matches(withText(title)));                    //Title이 맞는지 확인
        onView(withId(R.id.article_edittext_nickname)).perform(typeText("Nickname"));   //닉네임에 "Nickname" 입력
        onView(withId(R.id.article_edittext_nickname)).check(matches(withText("Nickname")));            //Nickname이 맞는지 확인
        onView(withId(R.id.article_edittext_password)).perform(typeText("1234"));       //비밀번호에 "1234"입력
        onView(withId(R.id.article_edittext_password)).check(matches(withText("1234")));                //1234 맞는지 확인
        onView(withId(R.id.article_editor_content)).perform(koinRichEditorTypeText("Contents"), closeSoftKeyboard());    //게시글 내용에 "Contents"입력

        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());                 //게시글 작성완료 버튼 클릭

        onView(withId(R.id.article_edittext_password)).perform(typeText("1234"));                       //비밀번호 "1234"입력
        onView(withId(R.id.article_edittext_password)).check(matches(withText("1234")));                               //비밀번호 확인
        onView(withId(R.id.article_edit_button)).check(matches(withText("수정")));                                    //방금 작성한 게시글에서 수정버튼 클릭
        onView(withId(R.id.article_edit_button)).perform(click());
        onView(withId(R.id.article_edittext_title)).perform(closeSoftKeyboard());                                       //키보드 닫기
        onView(withId(R.id.article_edittext_title)).perform(clearText());                                               //게시글 제목 지우기
        onView(withId(R.id.article_edittext_title)).perform(typeText("Title2"), closeSoftKeyboard());   //게시글 제목을 Title2로 입력
        onView(withId(R.id.article_edittext_title)).check(matches(withText("Title2")));                                 //게시글 제목 확인


        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());                     //게시글 작성 버튼 클릭

    }

    /**
     * 게시글에 댓글 생성하는 테스트
     */

    @Test
    public void testCaseForArticleCommentCreate(){
        onView((withId(R.id.activity_main_anonymous_board_linear_layout))).perform(click());

        onView(withId(R.id.freeboard_recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));

        onView(withId(R.id.article_comment_write_button)).perform(click());                     //댓글 버튼 클릭

        onView(withId(R.id.article_comment_content_recyclerview)).perform(new CustomScrollActions().nestedScrollTo());  //화면을 밑으로 스크롤한다
        onView(withId(R.id.article_comment_password_edittext)).perform(typeText("1234"), closeSoftKeyboard());  //비밀번호 "1234" 입력력
        onView(withId(R.id.article_comment_password_edittext)).check(matches(withText("1234")));                //비밀번호 확인
        onView(withId(R.id.article_comment_nickname_edittext)).perform(typeText("Nickname"), closeSoftKeyboard());  //닉네임 "Nickname"입력
        onView(withId(R.id.article_comment_nickname_edittext)).check(matches(withText("Nickname")));                //닉네임 확인
        onView(withId(R.id.article_comment_content_edittext)).perform(typeText(comment), closeSoftKeyboard());  //댓글창에 "comment"입력
        onView(withId(R.id.article_comment_content_edittext)).check(matches(withText(comment)));                //입력한 댓글이 "comment"인지 확인

        onView(withId(R.id.article_comment_anonymous_register_button)).perform(click());                          //완료 버튼 클릭
        onView(withText("댓글이 등록되었습니다.")).inRoot(new ToastMatcher("댓글이 등록되었습니다."))               //토스트 메시지 확인
                .check(matches(isDisplayed()));
    }

    /**
     * 댓글 수정버튼 클릭 후 댓글내용 수정하지 않는다
     * 1. 비밀번호 입력하지 않고 등록 버튼 클릭 시 토스트 메시지 확인
     * 2. 비밀번호 입력하고 등록 버튼 클릭 시 토스트 메시지 확인
     */
    @Test
    public void testCaseForArticleCommentNoEdit(){

        onView((withId(R.id.activity_main_anonymous_board_linear_layout))).perform(click());

        onView(withId(R.id.freeboard_recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));

        onView(withId(R.id.article_comment_write_button)).perform(click());                     //댓글 버튼 클릭

        onView(withId(R.id.article_comment_content_recyclerview)).perform(new CustomScrollActions().nestedScrollTo());  //화면을 밑으로 스크롤한다
        onView(withId(R.id.article_comment_password_edittext)).perform(typeText("1234"), closeSoftKeyboard());  //비밀번호 "1234" 입력력
        onView(withId(R.id.article_comment_password_edittext)).check(matches(withText("1234")));                //비밀번호 확인
        onView(withId(R.id.article_comment_nickname_edittext)).perform(typeText("Nickname"), closeSoftKeyboard());  //닉네임 "Nickname"입력
        onView(withId(R.id.article_comment_nickname_edittext)).check(matches(withText("Nickname")));                //닉네임 확인
        onView(withId(R.id.article_comment_content_edittext)).perform(typeText(comment), closeSoftKeyboard());  //댓글창에 "comment"입력
        onView(withId(R.id.article_comment_content_edittext)).check(matches(withText(comment)));                //입력한 댓글이 "comment"인지 확인

        onView(withId(R.id.article_comment_anonymous_register_button)).perform(click());                                  //완료 버튼 클릭
        onView(withText("댓글이 등록되었습니다.")).inRoot(new ToastMatcher("댓글이 등록되었습니다."))               //토스트 메시지 확인
                .check(matches(isDisplayed()));

        RecyclerViewMatcher recyclerViewMatcher = new RecyclerViewMatcher(R.id.article_comment_content_recyclerview);  //RecyclerViewMatcher 생성
        onView(recyclerViewMatcher.atPositionOnView(0, R.id.comment_edit)).perform(click());             //댓글 목록중 첫전째 댓글의 수정 버튼 클릭

        onView(withId(R.id.article_comment_content_recyclerview)).perform(new CustomScrollActions().nestedScrollTo());  //화면을 밑으르 스크롤한다
        onView(withId(R.id.article_comment_anonymous_register_button)).perform(click());                                  //등록 버튼 클릭
        onView(withText("비밀번호를 입력해주세요.")).inRoot(new ToastMatcher("비밀번호를 입력해주세요"))               //토스트메시지 도출출
                .check(matches(isDisplayed()));

        onView(withId(R.id.article_comment_password_edittext)).perform(typeText("1234"), closeSoftKeyboard());  //비밀번호 "1234"입력
        onView(withId(R.id.article_comment_password_edittext)).check(matches(withText("1234")));                               //비밀번호 확인

        onView(withId(R.id.article_comment_anonymous_register_button)).perform(click());                                       //등록 버튼 클릭
        onView(withText("댓글이 수정되었습니다.")).inRoot(new ToastMatcher("댓글이 수정되었습니다."))       //토스트메시지 도출
                .check(matches(isDisplayed()));


    }

    /**
     *  댓글 수정 시 댓글 내용 빈칸으로 하고 등록 테스트
     */
    @Test
    public void testCaseForArticleBlinkComment(){

        onView((withId(R.id.activity_main_anonymous_board_linear_layout))).perform(click());

        onView(withId(R.id.freeboard_recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));

        onView(withId(R.id.article_comment_write_button)).perform(click());                     //댓글 버튼 클릭

        onView(withId(R.id.article_comment_content_recyclerview)).perform(new CustomScrollActions().nestedScrollTo());  //화면을 밑으로 스크롤한다
        onView(withId(R.id.article_comment_password_edittext)).perform(typeText("1234"), closeSoftKeyboard());  //비밀번호 "1234" 입력력
        onView(withId(R.id.article_comment_password_edittext)).check(matches(withText("1234")));                //비밀번호 확인
        onView(withId(R.id.article_comment_nickname_edittext)).perform(typeText("Nickname"), closeSoftKeyboard());  //닉네임 "Nickname"입력
        onView(withId(R.id.article_comment_nickname_edittext)).check(matches(withText("Nickname")));                //닉네임 확인
        onView(withId(R.id.article_comment_content_edittext)).perform(typeText(comment), closeSoftKeyboard());  //댓글창에 "comment"입력
        onView(withId(R.id.article_comment_content_edittext)).check(matches(withText(comment)));                //입력한 댓글이 "comment"인지 확인

        onView(withId(R.id.article_comment_anonymous_register_button)).perform(click());                                  //완료 버튼 클릭
        onView(withText("댓글이 등록되었습니다.")).inRoot(new ToastMatcher("댓글이 등록되었습니다."))               //토스트 메시지 확인
                .check(matches(isDisplayed()));

        RecyclerViewMatcher recyclerViewMatcher = new RecyclerViewMatcher(R.id.article_comment_content_recyclerview);  //RecyclerViewMatcher 생성
        onView(recyclerViewMatcher.atPositionOnView(0, R.id.comment_edit)).perform(click());             //댓글 목록중 첫전째 댓글의 수정 버튼 클릭

        onView(withId(R.id.article_comment_content_recyclerview)).perform(new CustomScrollActions().nestedScrollTo());          //화면을 밑으로 스크롤한다
        onView(withId(R.id.article_comment_password_edittext)).perform(typeText("1234"), closeSoftKeyboard());  //비밀번호 "1234"입력
        onView(withId(R.id.article_comment_password_edittext)).check(matches(withText("1234")));                               //비밀번호 확인
        onView(withId(R.id.article_comment_content_edittext)).perform(clearText());  //댓글창 빈칸 만들기

        onView(withId(R.id.article_comment_anonymous_register_button)).perform(click());                                  //등록 버튼 클릭
        onView(withText("내용을 입력해주세요.")).inRoot(new ToastMatcher("내용을 입력해주세요."))                           //토스트메시지 도출
                .check(matches(isDisplayed()));

    }
    /**
     *  댓글 수정 테스트
     */
    @Test
    public void testCaseForArticleCommentEdit(){

        onView((withId(R.id.activity_main_anonymous_board_linear_layout))).perform(click());

        onView(withId(R.id.freeboard_recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));

        onView(withId(R.id.article_comment_write_button)).perform(click());                     //댓글 버튼 클릭

        onView(withId(R.id.article_comment_content_recyclerview)).perform(new CustomScrollActions().nestedScrollTo());  //화면을 밑으로 스크롤한다
        onView(withId(R.id.article_comment_password_edittext)).perform(typeText("1234"), closeSoftKeyboard());  //비밀번호 "1234" 입력력
        onView(withId(R.id.article_comment_password_edittext)).check(matches(withText("1234")));                //비밀번호 확인
        onView(withId(R.id.article_comment_nickname_edittext)).perform(typeText("Nickname"), closeSoftKeyboard());  //닉네임 "Nickname"입력
        onView(withId(R.id.article_comment_nickname_edittext)).check(matches(withText("Nickname")));                //닉네임 확인
        onView(withId(R.id.article_comment_content_edittext)).perform(typeText(comment), closeSoftKeyboard());  //댓글창에 "comment"입력
        onView(withId(R.id.article_comment_content_edittext)).check(matches(withText(comment)));                //입력한 댓글이 "comment"인지 확인

        onView(withId(R.id.article_comment_anonymous_register_button)).perform(click());                                  //완료 버튼 클릭
        onView(withText("댓글이 등록되었습니다.")).inRoot(new ToastMatcher("댓글이 등록되었습니다."))               //토스트 메시지 확인
                .check(matches(isDisplayed()));

        RecyclerViewMatcher recyclerViewMatcher = new RecyclerViewMatcher(R.id.article_comment_content_recyclerview);  //RecyclerViewMatcher 생성
        onView(recyclerViewMatcher.atPositionOnView(0, R.id.comment_edit)).perform(click());             //댓글 목록중 첫전째 댓글의 수정 버튼 클릭

        onView(withId(R.id.article_comment_content_recyclerview)).perform(new CustomScrollActions().nestedScrollTo());          //화면을 밑으로 스크롤한다
        onView(withId(R.id.article_comment_password_edittext)).perform(typeText("1234"), closeSoftKeyboard());  //비밀번호 "1234"입력
        onView(withId(R.id.article_comment_password_edittext)).check(matches(withText("1234")));                               //비밀번호 확인
        onView(withId(R.id.article_comment_content_edittext)).perform(typeText("comment2"), closeSoftKeyboard());  //댓글창에 "comment2"입력

        onView(withId(R.id.article_comment_anonymous_register_button)).perform(click());                                  //등록 버튼 클릭
        onView(withText("댓글이 수정되었습니다.")).inRoot(new ToastMatcher("댓글이 수정되었습니다."))       //토스트메시지 도출
                .check(matches(isDisplayed()));

    }

    /**
     * 댓글 삭제 테스트
     */
    @Test
    public void testCaseForArticleCommentDelete(){

        onView((withId(R.id.activity_main_anonymous_board_linear_layout))).perform(click());

        onView(withId(R.id.freeboard_recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));

        onView(withId(R.id.article_comment_write_button)).perform(click());                     //댓글 버튼 클릭

        onView(withId(R.id.article_comment_content_recyclerview)).perform(new CustomScrollActions().nestedScrollTo());  //화면을 밑으로 스크롤한다
        onView(withId(R.id.article_comment_password_edittext)).perform(typeText("1234"), closeSoftKeyboard());  //비밀번호 "1234" 입력력
        onView(withId(R.id.article_comment_password_edittext)).check(matches(withText("1234")));                //비밀번호 확인
        onView(withId(R.id.article_comment_nickname_edittext)).perform(typeText("Nickname"), closeSoftKeyboard());  //닉네임 "Nickname"입력
        onView(withId(R.id.article_comment_nickname_edittext)).check(matches(withText("Nickname")));                //닉네임 확인
        onView(withId(R.id.article_comment_content_edittext)).perform(typeText(comment), closeSoftKeyboard());  //댓글창에 "comment"입력
        onView(withId(R.id.article_comment_content_edittext)).check(matches(withText(comment)));                //입력한 댓글이 "comment"인지 확인

        onView(withId(R.id.article_comment_anonymous_register_button)).perform(click());                                  //완료 버튼 클릭
        onView(withText("댓글이 등록되었습니다.")).inRoot(new ToastMatcher("댓글이 등록되었습니다."))               //토스트 메시지 확인
                .check(matches(isDisplayed()));

        RecyclerViewMatcher recyclerViewMatcher = new RecyclerViewMatcher(R.id.article_comment_content_recyclerview);  //RecyclerViewMatcher 생성
        onView(recyclerViewMatcher.atPositionOnView(0, R.id.comment_edit)).perform(click());             //댓글 목록중 첫전째 댓글의 수정 버튼 클릭

        onView(withId(R.id.article_comment_content_recyclerview)).perform(new CustomScrollActions().nestedScrollTo());  //화면을 밑으로 스크롤한다
        onView(withId(R.id.article_comment_anonymous_delete_button)).perform(click());                                  //삭제 버튼 클릭
        onView(withText("비밀번호를 입력해주세요.")).inRoot(new ToastMatcher("비밀번호를 입력해주세요."))                 //토스트 메시지 도출
                .check(matches(isDisplayed()));

        onView(withId(R.id.article_comment_content_recyclerview)).perform(new CustomScrollActions().nestedScrollTo());          //화면을 밑으로 스크롤한다
        onView(withId(R.id.article_comment_password_edittext)).perform(typeText("1234"), closeSoftKeyboard());  //비밀번호 "1234"입력
        onView(withId(R.id.article_comment_password_edittext)).check(matches(withText("1234")));                               //비밀번호 확인
        onView(withId(R.id.article_comment_anonymous_delete_button)).perform(click());

        onView(allOf(withText("삭제하시겠습니까?"))).check(matches(isDisplayed()));                            //스낵바 도출
        onView(withText("YES")).perform(click());                                                               //YES 버튼 클릭

        onView(withText("댓글이 삭제되었습니다.")).inRoot(new ToastMatcher("댓글이 삭제되었습니다."))     //토스트 메시지 도출
                .check(matches(isDisplayed()));
    }

    /**
     * 게시글 삭제 테스트
     */
    @Test
    public void testCaseForArticleDeleteButtonClick(){

        onView((withId(R.id.activity_main_anonymous_board_linear_layout))).perform(click());

        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());     //게시글 작성하기 버튼 클릭

        onView(withId(R.id.article_edittext_title)).perform(typeText("Title"));         //제목에 "Title"입력
        onView(withId(R.id.article_edittext_title)).check(matches(withText(title)));                    //Title이 맞는지 확인
        onView(withId(R.id.article_edittext_nickname)).perform(typeText("Nickname"));   //닉네임에 "Nickname" 입력
        onView(withId(R.id.article_edittext_nickname)).check(matches(withText("Nickname")));            //Nickname이 맞는지 확인
        onView(withId(R.id.article_edittext_password)).perform(typeText("1234"));       //비밀번호에 "1234"입력
        onView(withId(R.id.article_edittext_password)).check(matches(withText("1234")));                //1234 맞는지 확인
        onView(withId(R.id.article_editor_content)).perform(koinRichEditorTypeText("Contents"), closeSoftKeyboard());    //게시글 내용에 "Contents"입력

        onView(withId(in.koreatech.koin.core.R.id.base_appbar_dark_right_button)).perform(click());                 //게시글 작성완료 버튼 클릭

        onView(withId(R.id.article_edittext_password)).check(matches(editTextwithItemHint("게시글 비밀번호")));    //게시글 비밀번호 hint 메시지 확인
        onView(withId(R.id.article_edittext_password)).perform(typeText("1234"));                               //비밀번호 "1234"입력
        onView(withId(R.id.article_delete_button)).perform(click());                                                            //삭제버튼 클릭

        onView(allOf(withText("게시글을 삭제할까요?\n댓글도 모두 사라집니다."))).check(matches(isDisplayed()));        //토스트메시지 도출
        onView(withText("확인")).perform(click());                                                                   //YES 버튼 클릭


    }
    public void blinkAllTextView(){
        onView(withId(R.id.article_edittext_title)).perform(clearText());
        onView(withId(R.id.article_edittext_nickname)).perform(clearText());
        onView(withId(R.id.article_edittext_password)).perform(clearText());
    }

    /**
     * 토스트 메시지 확인하는 클래스
     */
    public class ToastMatcher extends TypeSafeMatcher<Root> {
        private String message;

        public ToastMatcher(String message) {
            this.message = message;
        }

        @Override
        protected boolean matchesSafely(Root root) {
            int type = root.getWindowLayoutParams().get().type;
            if (type == WindowManager.LayoutParams.TYPE_TOAST) {
                IBinder windowToken = root.getDecorView().getWindowToken();
                IBinder appToken = root.getDecorView().getApplicationWindowToken();
                if (windowToken == appToken) {
                    // means this window isn't contained by any other windows.
                    return true;
                }
            }
            return false;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText(message);
        }
    }

    /**
     * NestedScrollView 아래로 스크롤 하는 클래스
     */
    public class CustomScrollActions {

        public  ViewAction nestedScrollTo() {
            return new ViewAction() {

                @Override
                public Matcher<View> getConstraints() {
                    return Matchers.allOf(
                            isDescendantOfA(isAssignableFrom(NestedScrollView.class)),
                            withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE));
                }

                @Override
                public String getDescription() {
                    return "Change view text";
                }

                @Override
                public void perform(UiController uiController, View view) {

                    try {
                        NestedScrollView nestedScrollView = (NestedScrollView)
                                findFirstParentLayoutOfClass(view, NestedScrollView.class);
                        if (nestedScrollView != null) {
                            nestedScrollView.scrollTo(0, view.getTop());
                        } else {
                            throw new Exception("Unable to find NestedScrollView parent.");
                        }
                    } catch (Exception e) {
                        throw new PerformException.Builder()
                                .withActionDescription(this.getDescription())
                                .withViewDescription(HumanReadables.describe(view))
                                .withCause(e)
                                .build();
                    }
                    uiController.loopMainThreadUntilIdle();
                }
            };
        }
        private View findFirstParentLayoutOfClass(View view, Class<? extends View> parentClass) {
            ViewParent parent = new FrameLayout(view.getContext());
            ViewParent incrementView = null;
            int i = 0;
            while (parent != null && !(parent.getClass() == parentClass)) {
                if (i == 0) {
                    parent = findParent(view);
                } else {
                    parent = findParent(incrementView);
                }
                incrementView = parent;
                i++;
            }
            return (View) parent;
        }

        private ViewParent findParent(View view) {
            return view.getParent();
        }

        private ViewParent findParent(ViewParent view) {
            return view.getParent();
        }
    }

    /**
     * EditText hint 글자 확인 함수
     * @param matcherText
     * @return
     */
    public static Matcher<View> editTextwithItemHint(final String matcherText) {
        return new BoundedMatcher<View, EditText>(EditText.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("with item hint: " + matcherText);
            }

            @Override
            protected boolean matchesSafely(EditText editTextField) {
                return matcherText.equalsIgnoreCase(editTextField.getHint().toString());
            }
        };
    }

    /**
     * RecyclerView의 item의 내용을 확인 하는 클래스
     */
    public class RecyclerViewMatcher {
        private final int recyclerViewId;

        public RecyclerViewMatcher(int recyclerViewId) {
            this.recyclerViewId = recyclerViewId;
        }

        public Matcher<View> atPositionOnView(final int position, final int viewId) {
            return new TypeSafeMatcher<View>() {
                Resources resources = null;
                View childView;

                @Override
                public void describeTo(Description description) {
                    String idDescription = Integer.toString(recyclerViewId);

                    if (this.resources != null) {
                        try {
                            idDescription = this.resources.getResourceName(recyclerViewId);
                        } catch (Resources.NotFoundException exception) {
                            idDescription = String.format("resource name %s not found", recyclerViewId);
                        }
                    }

                    description.appendText("with id ");
                    is(viewId).describeTo(description);
                }

                @Override
                protected boolean matchesSafely(View item) {
                    this.resources = item.getResources();

                    if (childView == null) {
                        RecyclerView recyclerView = item.getRootView().findViewById(recyclerViewId);
                        if (recyclerView != null && recyclerView.getId() == recyclerViewId) {
                            childView = recyclerView.findViewHolderForAdapterPosition(position).itemView;
                        }
                        else {
                            return false;
                        }
                    }

                    if (viewId == -1) {
                        return item == childView;
                    } else {
                        View targetView = childView.findViewById(viewId);
                        return item == targetView;
                    }
                }
            };
        }
    }

    /**
     * 텍스트뷰 생상 확인하는 함수
     * @param matcherColor
     * @return
     */
    public static Matcher<View> textViewTextColorMatcher(final int matcherColor) {
        return new BoundedMatcher<View, TextView>(TextView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("with text color: " + matcherColor);
            }
            @Override
            protected boolean matchesSafely(TextView textView) {
                return matcherColor == textView.getCurrentTextColor();
            }
        };
    }


    /**
     * KoinRichEditor에 텍스트를 입력하는 함수
     * @param text
     * @return
     */
    public static ViewAction koinRichEditorTypeText(final String text){
        return new ViewAction(){
            @Override
            public Matcher<View> getConstraints() {
                return allOf(isDisplayed(), isAssignableFrom(KoinRichEditor.class));
            }

            @Override
            public String getDescription() {
                return "Change view text";
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((KoinRichEditor) view).render("<p>"+text+"</P>");
            }

        };
    }
    public static ViewAction scrollToBottom(){
        return new ViewAction(){
            @Override
            public Matcher<View> getConstraints() {
                return allOf(isDisplayed(), isAssignableFrom(RecyclerView.class));
            }

            @Override
            public String getDescription() {
                return "Change view text";
            }

            @Override
            public void perform(UiController uiController, View view) {
                RecyclerView recyclerView = (RecyclerView) view;
                int itemCount = recyclerView.getAdapter().getItemCount();
                int position;
                if(itemCount == 0 )
                    position = 0;
                else
                    position = itemCount -1;
                recyclerView.scrollToPosition(position);
                uiController.loopMainThreadUntilIdle();
            }

        };
    }
}

