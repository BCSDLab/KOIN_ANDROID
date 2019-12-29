package in.koreatech.koin.service_used_market.ui;

import in.koreatech.koin.core.bases.BaseFragment;

/**
 * @author yunjae na
 * @since 2018.09.16
 */
public class MarketUsedBaseFragment extends BaseFragment{

    /**
     * Change fragment method from TabView
     * @param page page that selected
     * @return fragment fragment that selected
     */
    public static MarketUsedBaseFragment newInstance(int page) {
        MarketUsedBaseFragment fragment = null;
        switch (page) {
            case 0:
                fragment = new MarketUsedSellFragment();
                break;
            case 1:
                fragment = new MarketUsedBuyFragment();
                break;
        }
        return fragment;
    }

}
