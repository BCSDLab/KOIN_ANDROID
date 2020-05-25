package in.koreatech.koin.ui.navigation;

import androidx.annotation.IdRes;

import java.util.HashMap;

import in.koreatech.koin.R;

public class NavigationManager {
    private int currentService;
    private int beforeService;
    private HashMap<Integer, Integer> textViewHashMap;

    private final Integer[] menuId = {
            R.id.navi_item_myinfo,
            R.id.navi_item_store, R.id.navi_item_bus,
            R.id.navi_item_dining, R.id.navi_item_cirlce,
            R.id.navi_item_timetable, R.id.navi_item_anonymous_board,
            R.id.navi_item_free_board, R.id.navi_item_recruit_board,
            R.id.navi_item_land, R.id.navi_item_lostfound
            , R.id.navi_item_usedmarket, R.id.navi_item_kakao_talk,
           };

    private final Integer[] menuTextViewId = {
            R.id.navi_item_myinfo_textview,
            R.id.navi_item_store_textview, R.id.navi_item_bus_textview,
            R.id.navi_item_dining_textview, R.id.navi_item_cirlce_textview,
            R.id.navi_item_timetable_textview, R.id.navi_item_anonymous_board_textview,
            R.id.navi_item_free_board_textview, R.id.navi_item_recruit_board_textview,
            R.id.navi_item_land_textview, R.id.navi_item_lostfound_textview
            , R.id.navi_item_usedmarket_textview, R.id.navi_item_kakao_talk_textview,
    };

    private NavigationManager() {
        currentService = -1;
        beforeService = -1;
        textViewHashMap = new HashMap<>();
        for (int i = 0; i < menuId.length; i++) {
            textViewHashMap.put(menuId[i], menuTextViewId[i]);
        }
    }

    public Integer[] getMenuIdArray(){
        return menuId;
    }

    public Integer[] getMenuTextViewId(){
        return menuTextViewId;
    }

    private static class Holder {
        private static NavigationManager INSTANCE = new NavigationManager();
    }

    public static NavigationManager getInstance() {
        return Holder.INSTANCE;
    }

    public boolean goToService(@IdRes int navigationId) {
        if (currentService == navigationId)
            return false;

        beforeService = currentService;
        currentService = navigationId;
        return true;
    }

    public void goToBeforeService() {
        currentService = beforeService;
    }

    public int getCurrentService() {
        return currentService;
    }

    public int getBeforeService() {
        return beforeService;
    }

    public boolean isServiceSame(@IdRes int serviceId) {
        return getCurrentService() == serviceId;
    }

    public Integer getCurrentMenuTextView() {
        return getMenuTextView(getCurrentService());
    }

    public Integer getMenuTextView(@IdRes int id) {
        if (textViewHashMap.containsKey(id))
            return textViewHashMap.get(id);
        return null;
    }
}
