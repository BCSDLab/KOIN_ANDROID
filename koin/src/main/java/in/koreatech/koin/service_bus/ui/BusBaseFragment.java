package in.koreatech.koin.service_bus.ui;

import in.koreatech.koin.core.bases.BaseFragment;
import in.koreatech.koin.service_used_market.ui.MarketUsedBaseFragment;
import in.koreatech.koin.service_used_market.ui.MarketUsedBuyFragment;
import in.koreatech.koin.service_used_market.ui.MarketUsedSellFragment;


/**
 * @author yunjae na
 * @since 2018.09.16
 */
public class BusBaseFragment extends BaseFragment{

    /**
     * Change fragment method from TabView
     * @param page page that selected
     * @return fragment fragment that selected
     */
    public static BusBaseFragment newInstance(int page) {
        BusBaseFragment fragment = null;
        switch (page) {
            case 0:
                fragment = new  BusMainFragment();
                break;
            case 1:
                fragment =new BusTimeTableSearchFragment();
                break;
            case 2:
                fragment =  new  BusTimeTableFragment();
                break;
        }
        return fragment;
    }

}
