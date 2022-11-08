package in.koreatech.koin.ui.bus;

import in.koreatech.koin.core.fragment.BaseFragment;


public class BusBaseFragment extends BaseFragment {

    /**
     * Change fragment method from TabView
     *
     * @param page page that selected
     * @return fragment fragment that selected
     */
    public static BusBaseFragment newInstance(int page) {
        BusBaseFragment fragment = null;
        switch (page) {
            case 0:
                fragment = new BusMainFragment();
                break;
            case 1:
                break;
            case 2:
                fragment = new BusTimeTableFragment();
                break;
        }
        return fragment;
    }

}
