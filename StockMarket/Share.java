package StockMarket;

public class Share {
    private String companyName;
    private String commodity;
    private double sharePrice;

    public Share(String companyName, String commodity, double sharePrice) {
        this.companyName = companyName;
        this.commodity = commodity;
        this.sharePrice = sharePrice;
    }

    public double getSharePrice() {
        return sharePrice;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setSharePrice(double sharePrice) {
        this.sharePrice = sharePrice;
    }

    public String getCommodity() {
        return commodity;
    }
}