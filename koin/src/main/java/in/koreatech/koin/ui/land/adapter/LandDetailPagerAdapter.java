package in.koreatech.koin.ui.land.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import in.koreatech.koin.R;

/**
 * 복덕방 상세페이지에서 건물의 외관,내관을 보여주는 PagerAdapter
 */
public class LandDetailPagerAdapter extends PagerAdapter {
    private ArrayList<String> landImageUrls;
    private Context context;
    private final RequestOptions glideOption;

    /**
     * PagerAdapter 생성자
     *
     * @param context       상세페이지의 context
     * @param landImageUrls 이미지의 URLs
     */
    public LandDetailPagerAdapter(Context context, ArrayList<String> landImageUrls) {
        this.context = context;
        this.landImageUrls = landImageUrls;
        this.glideOption = new RequestOptions()
                .fitCenter()
                .override(400, 300);
    }

    /**
     * position 에 해당되는 URL을 Glide를 통해 업로드하여 뷰페이저를 생성하고 별도의 TextView를 통해 현재 position과 전체 개수를 출력
     *
     * @param container 뷰페이저 객체
     * @param position  현재 페이지의 위치값
     * @return 뷰페이저 객체를 반환
     */
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        String landViewUrl = landImageUrls.get(position);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.land_detail_viewpager_page, container, false);

        ImageView landDetailRoomImageView = view.findViewById(R.id.land_detail_land_imageview);
        Glide.with(context).load(landViewUrl).apply(glideOption).into(landDetailRoomImageView);

        String page = (position + 1) + " / " + getCount();
        TextView landDetailPageTextview = view.findViewById(R.id.land_detail_page_textview);
        landDetailPageTextview.setText(page);

        ((ViewPager) container).addView(view);

        return view;
    }

    /**
     * 뷰페이저에 포함된 전체 페이지수를 반환
     *
     * @return 뷰페이저에 포함된 전체 페이지수(뷰페이저어댑터를 생성할 때 받은 URL의 개수)
     */
    @Override
    public int getCount() {
        return landImageUrls.size();
    }

    /**
     * 뷰페이저 내부적으로 관리되는 키 객체와 뷰페이저가 연관이 되어있는지 확인하는 메소드
     *
     * @param view   instantiateItem 에서 반환하는 view
     * @param object 뷰페이저 내부적으로 관리되는 키객체
     * @return 키 객체와 view 가 동일한지 여부
     */
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View) object);
    }

    /**
     * 페이지를 제거하는 메소드
     *
     * @param container 제거 할 페이지를 포함한 뷰페이저 객체
     * @param position  제거 할 페이지의 위치
     * @param object    instantiateItem 에서 반환한 객체
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}