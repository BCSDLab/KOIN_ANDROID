package in.koreatech.koin.ui.usedmarket.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import android.util.SparseArray;
import android.view.ViewGroup;

import in.koreatech.koin.ui.usedmarket.MarketUsedBaseFragment;

/**
 * Created by yunjae on 2018. 8. 27....
 */
public class MarketUsedMainViewPagerAdapter extends FragmentStatePagerAdapter {
    private final String TAG = "MarketUsedMainViewPagerAdapter";

    private int tabCount;
    private final String[] tabTitle = new String[]{"팝니다", "삽니다"};

    private final SparseArray<Fragment> registeredFragments = new SparseArray<>();

    public MarketUsedMainViewPagerAdapter(FragmentManager fragmentManager, int tabCount) {
        super(fragmentManager);
        this.tabCount = tabCount;
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
        return tabCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitle[position];
    }

}
