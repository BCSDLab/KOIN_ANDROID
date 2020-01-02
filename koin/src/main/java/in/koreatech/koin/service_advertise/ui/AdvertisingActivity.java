package in.koreatech.koin.service_advertise.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import in.koreatech.koin.KoinNavigationDrawerActivity;
import in.koreatech.koin.R;
import in.koreatech.koin.service_advertise.contracts.AdvertisingContract;
import in.koreatech.koin.service_advertise.presenters.AdvertisingPresenter;

/**
 * Created by hansol on 2020.1.1...
 */
public class AdvertisingActivity extends KoinNavigationDrawerActivity implements AdvertisingContract.View {
    Context mContext;
    AdvertisingContract.Presenter adPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertising);
        init();
    }

    void init(){
        mContext = this;
        setPresenter(new AdvertisingPresenter(this, new ));
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setPresenter(AdvertisingContract.Presenter presenter) {

    }
}
