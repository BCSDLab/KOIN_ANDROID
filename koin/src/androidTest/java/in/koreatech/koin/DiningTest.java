package in.koreatech.koin;

import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.test.espresso.Root;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.platform.app.InstrumentationRegistry;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;

import java.text.ParseException;

import in.koreatech.koin.ui.circle.CircleActivity;
import in.koreatech.koin.ui.dining.DiningActivity;
import in.koreatech.koin.util.TimeUtil;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static in.koreatech.koin.matcher.TextColorMatcher.textViewTextColorMatcher;
import static in.koreatech.koin.matcher.ToastMatcher.withMessage;

public class DiningTest {
    @Rule
    public IntentsTestRule<DiningActivity> activityRule =
            new IntentsTestRule<>(DiningActivity.class);
    /**
     * 앱바 타이틀 검사
     */
    @Test
    public void testcaseForAppBarTitle() {  //앱바 이름 확인
        onView(withId(R.id.koin_base_app_bar_dark)).check(matches(hasDescendant(withText(R.string.navigation_item_dining))));
    }

    @Test
    public void testcaseForAppBarBackButton() {
        onView(withId(R.id.koin_base_app_bar_dark)).perform(click());
    }

    /**
     * 오늘 날짜와 식단화면 날짜가 일치하는지 검사
     */
    @Test
    public void testcaseForDate(){
        onView(withId(R.id.dining_swiperefreshlayout)).perform(ViewActions.swipeDown());

        String today = TimeUtil.getDeviceCreatedDateOnlyString();
        onView(withId(R.id.dining_date_textView)).check(matches(withText(today)));
    }

    /**
     * 왼쪽화살표 버튼 클릭 시 오늘 날짜 기준으로 하루씩 감소하고 8번 클릭 시 토스트 메시지 표시 테스트
    */
    @Test
    public void testcaseForBeforeDateButton(){
        int changeDate = 0;
        onView(withId(R.id.dining_swiperefreshlayout)).perform(ViewActions.swipeDown());

        String today = TimeUtil.getDeviceCreatedDateOnlyString();
        onView(withId(R.id.dining_date_textView)).check(matches(withText(today)));  //오늘 날짜와 일치하는지 확인

        for(int i = 0;i<8;i++) {
            onView(withId(R.id.dining_before_date_button)).perform(click());
            if(i == 7){
                onView(withText("더 이상 데이터를 불러올 수 없습니다.")).inRoot(withMessage("더 이상 데이터를 불러올 수 없습니다."))
                        .check(matches(isDisplayed()));
                break;
            }
            changeDate -= 1;
            today = TimeUtil.getChangeDate(changeDate);
            onView(withId(R.id.dining_date_textView)).check(matches(withText(today)));
        }

    }
    /**
     * 오른쪽 화살표 버튼 클릭 시 오늘 날짜 기준으로 하루씩 증가하고 8번 클릭 시 토스트 메시지 표시 테스트
     */
    @Test
    public void testcaseForNextDateButton(){
        int changeDate = 0;
        onView(withId(R.id.dining_swiperefreshlayout)).perform(ViewActions.swipeDown());

        String today = TimeUtil.getDeviceCreatedDateOnlyString();
        onView(withId(R.id.dining_date_textView)).check(matches(withText(today)));  //오늘 날짜와 일치하는지 확인

        for(int i = 0;i<8;i++) {
            onView(withId(R.id.dining_next_date_button)).perform(click());
            if(i == 7){
                onView(withText("더 이상 데이터를 불러올 수 없습니다.")).inRoot(withMessage("더 이상 데이터를 불러올 수 없습니다."))
                        .check(matches(isDisplayed()));
                break;
            }
            changeDate += 1;
            today = TimeUtil.getChangeDate(changeDate);
            onView(withId(R.id.dining_date_textView)).check(matches(withText(today)));
        }

    }

    /**
     * 아침, 점심, 저녁 버튼 클릭 시 클릭한 버튼은 주황색, 나머지 버튼은 검은색인지 확인하는 테스트
     */
    @Test
    public void testcaseForDiningButtonClick(){
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        onView(withId(R.id.dining_swiperefreshlayout)).perform(ViewActions.swipeDown());

        onView(withId(R.id.dining_breakfast_button)).perform(click());  //아침 버튼 클릭
        checkTextStatus(R.id.dining_breakfast_button);


        onView(withId(R.id.dining_lunch_button)).perform(click());      //점심 버튼 클릭
        checkTextStatus(R.id.dining_lunch_button);

        onView(withId(R.id.dining_dinner_button)).perform(click());     //저녁 버튼 클릭
        checkTextStatus(R.id.dining_dinner_button);



    }

    /**
     * 클릭한 버튼만 주황색, 나머지 버튼은 검은색인지 확인하는 함수
     * @param selectedCategoryTextViewId
     */
    void checkTextStatus(@IdRes int selectedCategoryTextViewId) {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        @IdRes int[] ids = {
                R.id.dining_breakfast_button,
                R.id.dining_lunch_button,
                R.id.dining_dinner_button
        };

        for (int i = 0; i < ids.length; i++) {
            if(selectedCategoryTextViewId == ids[i])
                onView(withId(ids[i])).check(matches(textViewTextColorMatcher(context.getResources().getColor(R.color.colorAccent))));
            else
                onView(withId(ids[i])).check(matches(textViewTextColorMatcher(context.getResources().getColor(R.color.black))));
        }
    }

}

