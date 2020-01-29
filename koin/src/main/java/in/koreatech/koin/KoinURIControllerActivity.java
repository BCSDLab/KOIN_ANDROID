package in.koreatech.koin;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;

import in.koreatech.koin.core.constants.URLConstant;
import in.koreatech.koin.service_board.ui.ArticleActivity;
import in.koreatech.koin.service_board.ui.BoardActivity;
import in.koreatech.koin.service_bus.ui.BusActivity;
import in.koreatech.koin.service_circle.ui.CircleActivity;
import in.koreatech.koin.service_dining.ui.DiningActivity;
import in.koreatech.koin.service_store.ui.StoreActivity;
import in.koreatech.koin.service_timetable.ui.TimetableActivity;
import in.koreatech.koin.service_used_market.ui.MarketUsedActivity;
import in.koreatech.koin.service_used_market.ui.MarketUsedBuyDetailActivity;
import in.koreatech.koin.service_used_market.ui.MarketUsedSellDetailActivity;


public class KoinURIControllerActivity extends Activity {
    private final int BUS = R.string.uri_path_bus;
    private final int STORE = R.string.uri_path_store;
    private final int BOARD_FREE = R.string.uri_path_board_free;
    private final int BOARD_JOB = R.string.uri_path_board_job;
    private final int BOARD_ANONYMOUS = R.string.uri_path_board_anonymous;
    private final int DIET = R.string.uri_path_diet;
    private final int TIMETABLE = R.string.uri_path_timetable;
    private final int CIRCLE = R.string.uri_path_circle;
    private final int MARKET_SELL = R.string.uri_path_market_sell;
    private final int MARKET_BUY = R.string.uri_path_market_buy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri uri = getIntent().getData();
        Intent intent = null;
        if (uri == null) {
            Toast.makeText(getApplicationContext(), "잘못된 경로 입니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (isURIContainPath(uri, BUS)) {
            intent = new Intent(this, BusActivity.class);
        } else if (isURIContainPath(uri, STORE)) {
            intent = new Intent(this, StoreActivity.class);
        } else if (isURIContainPath(uri, BOARD_FREE) && isDetailPath(uri)) {
            intent = new Intent(this, ArticleActivity.class);
            intent.putExtra("ARTICLE_UID", Integer.parseInt(uri.getLastPathSegment()));
            intent.putExtra("BOARD_UID", URLConstant.COMMUNITY.ID_FREE);
        } else if (isURIContainPath(uri, BOARD_FREE) && !isDetailPath(uri)) {
            intent = new Intent(this, BoardActivity.class);
            intent.putExtra("BOARD_UID", URLConstant.COMMUNITY.ID_FREE);
        } else if (isURIContainPath(uri, BOARD_JOB) && isDetailPath(uri)) {
            intent = new Intent(this, ArticleActivity.class);
            intent.putExtra("ARTICLE_UID", Integer.parseInt(uri.getLastPathSegment()));
            intent.putExtra("BOARD_UID", URLConstant.COMMUNITY.ID_RECRUIT);
        } else if (isURIContainPath(uri, BOARD_JOB) && !isDetailPath(uri)) {
            intent = new Intent(this, BoardActivity.class);
            intent.putExtra("BOARD_UID", URLConstant.COMMUNITY.ID_RECRUIT);
        } else if (isURIContainPath(uri, BOARD_ANONYMOUS) && isDetailPath(uri)) {
            intent = new Intent(this, ArticleActivity.class);
            intent.putExtra("ARTICLE_UID", Integer.parseInt(uri.getLastPathSegment()));
            intent.putExtra("BOARD_UID", URLConstant.COMMUNITY.ID_ANONYMOUS);
        } else if (isURIContainPath(uri, BOARD_ANONYMOUS) && !isDetailPath(uri)) {
            intent = new Intent(this, BoardActivity.class);
            intent.putExtra("BOARD_UID", URLConstant.COMMUNITY.ID_ANONYMOUS);
        } else if (isURIContainPath(uri, DIET)) {
            intent = new Intent(this, DiningActivity.class);
        } else if (isURIContainPath(uri, TIMETABLE)) {
            intent = new Intent(this, TimetableActivity.class);
        } else if (isURIContainPath(uri, CIRCLE)) {
            intent = new Intent(this, CircleActivity.class);
        } else if (isURIContainPath(uri, MARKET_SELL) && isDetailPath(uri)) {
            intent = new Intent(this, MarketUsedSellDetailActivity.class);
            intent.putExtra("ITEM_ID", Integer.parseInt(uri.getLastPathSegment()));
            intent.putExtra("MARKET_ID", 0);
        } else if (isURIContainPath(uri, MARKET_BUY) && isDetailPath(uri)) {
            intent = new Intent(this, MarketUsedBuyDetailActivity.class);
            intent.putExtra("ITEM_ID", Integer.parseInt(uri.getLastPathSegment()));
            intent.putExtra("MARKET_ID", 1);
        } else if ((isURIContainPath(uri, MARKET_BUY) || isURIContainPath(uri, MARKET_SELL)) && !isDetailPath(uri)) {
            intent = new Intent(this, MarketUsedActivity.class);
        } else {
            Toast.makeText(getApplicationContext(), "잘못된 경로 입니다.", Toast.LENGTH_SHORT).show();
            finish();

        }
        if (intent != null)
            startActivity(intent);
        finish();
    }


    public boolean isURIContainPath(Uri uri, int pathStringId) {
        if (uri.getPath() == null)
            return false;
        return uri.getPath().contains(getResources().getString(pathStringId));
    }

    public boolean isDetailPath(Uri uri) {
        String path;
        if (uri.getLastPathSegment() == null)
            return false;
        path = uri.getLastPathSegment();
        for (int i = 0; i < uri.getLastPathSegment().length(); i++) {
            if (!Character.isDigit(path.charAt(i)))
                return false;
        }
        return true;
    }
}
