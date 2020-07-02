package in.koreatech.koin.ui.bus;

import in.koreatech.koin.ui.koinfragment.KoinBaseFragment;

public class BusBaseFragment{

    /**
     * Change fragment method from TabView
     *
     * @param page page that selected
     * @return fragment fragment that selected
     */
    public static KoinBaseFragment newInstance(int page) {
        KoinBaseFragment fragment = null;
        switch (page) {
            case 0:
                fragment = new BusMainFragment();
                break;
            case 1:
                fragment = new BusTimeTableSearchFragment();
                break;
            case 2:
                fragment = new BusTimeTableFragment();
                break;
        }
        return fragment;
    }

}
