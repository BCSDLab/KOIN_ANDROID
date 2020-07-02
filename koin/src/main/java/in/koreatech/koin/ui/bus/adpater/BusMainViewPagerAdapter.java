package in.koreatech.koin.ui.bus.adpater;

import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import in.koreatech.koin.ui.bus.BusBaseFragment;
import in.koreatech.koin.ui.bus.BusMainFragment;
import in.koreatech.koin.ui.bus.BusViewPagerFragment;

public class BusMainViewPagerAdapter extends FragmentStatePagerAdapter {
    private final String TAG = "BusMainViewPagerAdapter";

    private int tabCount;
    private final String[] mTabTitle = new String[]{"운행정보", "운행 정보 검색", "시간표"};
    private FragmentManager fragmentManager;

    private final SparseArray<Fragment> registeredFragments = new SparseArray<>();

    public BusMainViewPagerAdapter(FragmentManager fragmentManager, int tabCount) {
        super(fragmentManager);
        this.fragmentManager = fragmentManager;
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
        return BusBaseFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return this.tabCount;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabTitle[position];
    }
}
