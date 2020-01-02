package in.koreatech.koin.core.networks.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Advertising {
    @SerializedName("lands")
    @Expose
    public ArrayList<Advertising> ads;

    /*test다 지워 바로 실전이다*/
    public String testTitle;
    public String testPrice;

    public Advertising(String testTitle, String testPrice) {
        this.testTitle = testTitle;
        this.testPrice = testPrice;
    }

    public String getTestPrice() {
        return testPrice;
    }

    public void setTestPrice(String testPrice) {
        this.testPrice = testPrice;
    }

    public String getTestTitle() {
        return testTitle;
    }

    public void setTestTitle(String testTitle) {
        this.testTitle = testTitle;
    }
}
