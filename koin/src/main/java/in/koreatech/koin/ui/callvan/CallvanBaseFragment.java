package in.koreatech.koin.ui.callvan;

import in.koreatech.koin.core.fragment.BaseFragment;

/**
 * Created by hyerim on 2018. 6. 17....
 */
public class CallvanBaseFragment extends BaseFragment{
    /**
     * Change fragment method from TabView
     * @param page 선택한 탭 (콜밴쉐어링, 콜밴 연락처)
     * @return fragment 선택한 fragment
     */
    public static CallvanBaseFragment newInstance(int page) {
        CallvanBaseFragment fragment = null;
        switch (page) {
            case 0:
                fragment = new RoomFragment();
                break;
            case 1:
                fragment = new CompanyFragment();
                break;
        }
        return fragment;
    }

}
