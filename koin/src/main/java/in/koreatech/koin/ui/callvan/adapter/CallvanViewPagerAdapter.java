package in.koreatech.koin.ui.callvan.adapter;

import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import in.koreatech.koin.ui.callvan.CallvanBaseFragment;

/**
 * Created by hyerim on 2019-05-06...
 */
public class CallvanViewPagerAdapter extends FragmentStatePagerAdapter {
    private final String TAG = "CallvanViewPagerAdapter";

    private int tabCount = 2;
    private final String[] tabTitle = new String[]{"콜밴쉐어링", "콜밴 연락처"};

    private final SparseArray<Fragment> registeredFragments = new SparseArray<>();

    public CallvanViewPagerAdapter(@NonNull FragmentManager fragmentManager, int tabCount) {
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
        return CallvanBaseFragment.newInstance(position);
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
