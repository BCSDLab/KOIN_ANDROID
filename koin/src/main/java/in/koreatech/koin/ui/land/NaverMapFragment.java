package in.koreatech.koin.ui.land;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMapOptions;
/**
 * 복덕방 navermap 스크롤을 위한 맵 프래그먼트
 * Created by hansol on 2019. 09. 07...
 */
public class NaverMapFragment extends MapFragment  {
    public static final String TAG = "NaverMapFragment";
    private MapView mapView;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mapView = getMapView();
        if (view != null)
            view.setOnTouchListener((touchView, event) -> {
                touchView.getParent().requestDisallowInterceptTouchEvent(true);
                mapView.onTouchEvent(event);
                view.performClick();
                return true;
            });
        return view;
    }


    public static NaverMapFragment newInstance(@Nullable NaverMapOptions options) {
        NaverMapFragment var1 = new NaverMapFragment();
        Bundle var2 = new Bundle();
        var2.putParcelable("NaverMapOptions", options);
        var1.setArguments(var2);
        return var1;
    }

}