package in.koreatech.koin;

import android.content.res.Resources;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static org.hamcrest.Matchers.is;

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
