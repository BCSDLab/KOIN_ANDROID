package in.koreatech.koin.service_bus.adpaters;

import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import in.koreatech.koin.service_bus.ui.BusMainFragment;


/**
 * Created by yunjae on 2018. 8. 27....
 */
public class BusMainViewPagerAdapter extends FragmentStatePagerAdapter {
    private final String TAG = BusMainViewPagerAdapter.class.getSimpleName();

    private int mTabCount;
    private final String[] mTabTitle = new String[]{"운행정보","운행 정보 검색", "시간표"};

    private final SparseArray<Fragment> registeredFragments = new SparseArray<>();

    public BusMainViewPagerAdapter(FragmentManager fragmentManager, int tabCount) {
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
        return BusMainFragment.newInstance(position);
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
