package in.koreatech.koin.core.networks.entity;

public class Advertising {
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
