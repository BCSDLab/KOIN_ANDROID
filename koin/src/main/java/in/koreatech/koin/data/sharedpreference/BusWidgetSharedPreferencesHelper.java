package in.koreatech.koin.data.sharedpreference;


import android.content.Context;

import in.koreatech.koin.core.sharedpreference.BaseSharedPreferencesHelper;

public class BusWidgetSharedPreferencesHelper extends BaseSharedPreferencesHelper {
    private final String TAG = "BusWidgetSharedPreferencesHelper";
    private final String KOIN_BUS_WIDGET_SHARED_PREFERENCES = "in.koin.sharedpreferences.bus.widget";
    public static final String BUS_BUTTON_SELECTED_POSITION = ".position";
    public static final String BUS_SELECTION_REVERSED = ".reversed";
    public static final String BUS_DESTINATION_TYPE = ".type";
    private static volatile BusWidgetSharedPreferencesHelper instance = null;


    public BusWidgetSharedPreferencesHelper() {
        sharedPreferences = null;
    }

    public static BusWidgetSharedPreferencesHelper getInstance() {
        if (instance == null) {
            synchronized (BusWidgetSharedPreferencesHelper.class) {
                instance = new BusWidgetSharedPreferencesHelper();
            }
        }
        return instance;
    }


    public void init(Context context) {
        super.init(context, context.getSharedPreferences(KOIN_BUS_WIDGET_SHARED_PREFERENCES, Context.MODE_PRIVATE));
    }

    public void saveLastButtonSelectedPosition(int position) {
        putInt(BUS_BUTTON_SELECTED_POSITION, position);
    }

    public int loadLastButtonSelectedPosition() {
        return getInt(BUS_BUTTON_SELECTED_POSITION, 0);
    }

    public void saveBusSelectionReversed(boolean isReversed) {
        putBoolean(BUS_SELECTION_REVERSED, isReversed);
    }

    public boolean loadBusSelectionReversed() {
        return getBoolean(BUS_SELECTION_REVERSED, false);
    }

    public void saveBusDestinationType(int type) {
        putInt(BUS_DESTINATION_TYPE, type);
    }

    public int loadBusDestinationType() {
        return getInt(BUS_DESTINATION_TYPE, 0);
    }
}

