package StockMarket;

public class Share {
    private String companyName;
    public String commodity;
    private int sharePrice;

    public Share(String companyName, String commodity, int sharePrice) {
        this.companyName = companyName;
        this.commodity = commodity;
        this.sharePrice = sharePrice;
    }

    public int getSharePrice() {
        return sharePrice;
    }
}
