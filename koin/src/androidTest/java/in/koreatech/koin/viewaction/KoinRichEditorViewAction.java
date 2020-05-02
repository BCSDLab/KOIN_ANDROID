package in.koreatech.koin.viewaction;

import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import org.hamcrest.Matcher;

import in.koreatech.koin.ui.board.KoinRichEditor;

import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.Matchers.allOf;

public class KoinRichEditorViewAction {
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
}
