package in.koreatech.koin.service_advertise.presenters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import in.koreatech.koin.core.bases.BasePresenter;
import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.entity.Advertising;
import in.koreatech.koin.core.networks.interactors.AdvertisingInteractor;
import in.koreatech.koin.service_advertise.contracts.AdvertisingContract;

/**
 * Created by hansol on 2020.1.1...
 */
public class AdvertisingPresenter implements AdvertisingContract.Presenter {

    private final AdvertisingContract.View adView;
    private final AdvertisingInteractor advertisingInteractor;
    private ArrayList<Advertising> adArrayList;

    public AdvertisingPresenter(AdvertisingContract.View adView, AdvertisingInteractor advertisingInteractor) {
        this.adView = adView;
        this.advertisingInteractor = advertisingInteractor;
        adArrayList = new ArrayList<>();
    }

    private final ApiCallback apiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            Advertising ads = (Advertising) object;
            adArrayList.clear();
            adArrayList.addAll(ads.ads);
            adView.onAdvertisingDataReceived(adArrayList);
        }

        @Override
        public void onFailure(Throwable throwable) {
            adView.showMessage("원룸 리스트를 받아오지 못했습니다");
        }
    };

    @Override
    public void getAdList() {
        adArrayList.clear();
        advertisingInteractor.readAdList(apiCallback);
    }

    @Override
    public ArrayList<Advertising> displayProcessingEvent(boolean isChecked1, boolean isChecked2) {
        ArrayList<Advertising> subAdDate = new ArrayList<>();
        if(isChecked1 == true && isChecked2 == true){ //전체
            subAdDate.addAll(adArrayList);
        }
        if(isChecked1 == false && isChecked2 == false){ //아무것도 없음
            return subAdDate;
        }

        Date date = new Date();
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");

        if(isChecked1 == true && isChecked2 == false){ //진행중만
            for(int i = 0; i < adArrayList.size(); i++){
                try {
                    if ((date.compareTo(formatDate.parse(adArrayList.get(i).endDate))  == 1)){
                        subAdDate.add(adArrayList.get(i));
                    }
                }catch (ParseException e){
                    adView.showMessage("에러");
                }
            }
        }
        if(isChecked1 == false && isChecked2 == true){ //마감만
            for(int i = 0; i < adArrayList.size(); i++){
                try {
                    if ((date.compareTo(formatDate.parse(adArrayList.get(i).endDate))  == -1)
                            && (date.compareTo(formatDate.parse(adArrayList.get(i).startDate))  == 1)){
                        subAdDate.add(adArrayList.get(i));
                    }
                }catch (ParseException e){
                    adView.showMessage("에러");
                }
            }
        }

        return subAdDate;

    }
}
