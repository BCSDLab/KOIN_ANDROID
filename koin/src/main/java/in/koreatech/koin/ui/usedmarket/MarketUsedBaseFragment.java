package in.koreatech.koin.ui.usedmarket;

import androidx.fragment.app.Fragment;

public class MarketUsedBaseFragment {

    /**
     * Change fragment method from TabView
     *
     * @param page page that selected
     * @return fragment fragment that selected
     */
    public static Fragment newInstance(int page) {
        Fragment fragment = null;
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
