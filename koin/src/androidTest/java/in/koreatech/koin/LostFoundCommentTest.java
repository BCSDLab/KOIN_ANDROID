package in.koreatech.koin;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import in.koreatech.koin.ui.lostfound.LostFoundCommentActivity;
import in.koreatech.koin.ui.lostfound.LostFoundEditActivity;
import in.koreatech.koin.ui.lostfound.LostFoundMainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static in.koreatech.koin.matcher.RecyclerViewMatcher.atPositionOnView;
import static in.koreatech.koin.matcher.ToastMatcher.withMessage;
import static org.hamcrest.Matchers.allOf;

public class LostFoundCommentTest {
    @Rule
    public ActivityTestRule<LostFoundMainActivity> activityRule =
            new ActivityTestRule<>(LostFoundMainActivity.class);
    /**
     * 앱바 이름 확인
     */
    @Test
    public void testcaseForAppBarTitle() {
        onView(withId(R.id.koin_base_app_bar_dark)).check(matches(hasDescendant(withText(R.string.navigation_item_lostfound))));
    }

    /**
     * 1. 아무것도 입력하지 않았을경우 토스트 메시지 확인
     */
    @Test
    public void testcaseForBlinkTextView(){
        onView(withId(R.id.lostfound_main_recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));    //첫번째 분실물게시글 클릭

        onView(withId(R.id.lostfound_detail_comment_button)).perform(click());                //등록 버튼 클릭

        onView(withId(R.id.lostfound_comment_register_button)).perform(click());                //등록 버튼 클릭
        onView(withText("내용을 입력해주세요")).inRoot(withMessage("내용을 입력해주세요"))       //토스트메시지 도출
                .check(matches(isDisplayed()));
    }

    /**
     * 댓글을 입력하고 취소버튼을 누를 경우 입력한 글이 지워지는지 확인
     */
    @Test
    public void testcaseForCancelButtonClick(){
        onView(withId(R.id.lostfound_main_recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));    //첫번째 분실물게시글 클릭

        onView(withId(R.id.lostfound_detail_comment_button)).perform(click());                //등록 버튼 클릭

        onView(withId(R.id.lostfound_comment_content_edittext)).perform(typeText("I found it"), closeSoftKeyboard());   //"I found it" 입력
        onView(withId(R.id.lostfound_comment_cancel_button)).perform(click());                                                          //취소 버튼 클릭
        onView(withId(R.id.lostfound_comment_content_edittext)).check(matches(withText("")));                                           //입력한 글자가 지워졌는지 확인
    }

    /**
     * 댓글을 작성하고 토스트 메시지 확인
     */
    @Test
    public void testcaseForCreateComment(){
        onView(withId(R.id.lostfound_main_recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));    //첫번째 분실물게시글 클릭

        onView(withId(R.id.lostfound_detail_comment_button)).perform(click());                //등록 버튼 클릭


        onView(withId(R.id.lostfound_comment_content_edittext)).perform(typeText("I found it"), closeSoftKeyboard());   //"I found it"입력
        onView(withId(R.id.lostfound_comment_register_button)).perform(click());                                                        //등록 버튼 클릭
        onView(withText(R.string.lost_and_found_created)).inRoot(withMessage("생성되었습니다.")).check(matches(isDisplayed()));           //토스트메시지 확인
    }
    /**
     * 댓글을 수정하고 토스트 메시지 확인
     */
    @Test
    public void testcaseForEditComment(){
        onView(withId(R.id.lostfound_main_recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));    //첫번째 분실물게시글 클릭

        onView(withId(R.id.lostfound_detail_comment_button)).perform(click());                //등록 버튼 클릭

        onView(withId(R.id.lostfound_comment_content_edittext)).perform(typeText("I found it"), closeSoftKeyboard());   //"I found it"입력
        onView(withId(R.id.lostfound_comment_register_button)).perform(click());            //등록 버튼 클릭
        onView(withText(R.string.lost_and_found_created)).inRoot(withMessage("생성되었습니다.")).check(matches(isDisplayed()));        //토스트메시지 확인

        onView(atPositionOnView(R.id.lostfound_comment_content_recyclerview, 0, R.id.lostfound_comment_edit)).perform(click()); //첫번째 댓글의 수정버튼 클릭
        onView(withId(R.id.lostfound_comment_content_edittext)).perform(clearText(), closeSoftKeyboard());
        onView(withId(R.id.lostfound_comment_content_edittext)).perform(typeText("I found it2"), closeSoftKeyboard());  //"I found it2"입력
        onView(withId(R.id.lostfound_comment_register_button)).perform(click());                                                        //등록 버튼 클릭
        onView(withText(R.string.lost_and_found_edited)).inRoot(withMessage("수정되었습니다.")).check(matches(isDisplayed()));         //토스트메시지 확인
    }

    /**
     * 댓글 삭제 확인
     */
    @Test
    public void testcaseForDeleteComment(){
        onView(withId(R.id.lostfound_main_recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));    //첫번째 분실물게시글 클릭

        onView(withId(R.id.lostfound_detail_comment_button)).perform(click());                //등록 버튼 클릭


        onView(withId(R.id.lostfound_comment_content_edittext)).perform(typeText("I found it"), closeSoftKeyboard());   //"I found it"입력
        onView(withId(R.id.lostfound_comment_register_button)).perform(click());                                                        //등록 버튼 클릭
        onView(withText(R.string.lost_and_found_created)).inRoot(withMessage("생성되었습니다.")).check(matches(isDisplayed()));           //토스트메시지 확인

        onView(atPositionOnView(R.id.lostfound_comment_content_recyclerview, 0, R.id.lostfound_comment_remove)).perform(click()); //첫번째 댓글의 삭제버튼 클릭

        onView(allOf(withText("댓글을 삭제할까요?"))).check(matches(isDisplayed()));                            //스낵바 도출
        onView(withText("YES")).perform(click());                                                               //YES 버튼 클릭


        onView(withText(R.string.lost_and_found_deleted)).inRoot(withMessage("삭제되었습니다.")).check(matches(isDisplayed()));        //토스트메시지 확인
    }


}
