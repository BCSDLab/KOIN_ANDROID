package in.koreatech.koin.service_used_market.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;
import in.koreatech.koin.service_used_market.ui.MarketUsedBaseFragment;

/**
 * Created by yunjae on 2018. 8. 27....
 */
public class MarketUsedMainViewPagerAdapter extends FragmentStatePagerAdapter {
    private final String TAG = MarketUsedMainViewPagerAdapter.class.getSimpleName();

    private int mTabCount = 2;
    private final String[] mTabTitle = new String[]{"팝니다", "삽니다"};

    private final SparseArray<Fragment> registeredFragments = new SparseArray<>();

    public MarketUsedMainViewPagerAdapter(FragmentManager fragmentManager, int tabCount) {
        super(fragmentManager);
        this.mTabCount = tabCount;
    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        return MarketUsedBaseFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return mTabCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabTitle[position];
    }

}
