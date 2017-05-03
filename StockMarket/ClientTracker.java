package StockMarket;

public class ClientTracker {
    private final double originalPrice;
    private String clientName;
    private String companyName;
    private String commodityType;
    private int amount;
    private int amountSold;
    private double buyPrice; // Most recently bought
    private int fluctuation; // Negative if buyPrice dropping since bought, positive otherwise.

    public ClientTracker(String clientName, String companyName, String commodityType, double buyPrice) {
        this.clientName = clientName;
        this.companyName = companyName;
        this.commodityType = commodityType;
        amount = 1;
        amountSold = 0;
        this.buyPrice = buyPrice;
        this.originalPrice = buyPrice;
        fluctuation = 0;
    }

    public int getFluctuation() { return fluctuation; }
    public int getAmount() {
        return amount;
    }
    public String getClientName() {
        return clientName;
    }
    public String getCompanyName() {
        return companyName;
    }
    public String getCommodityType() { return commodityType; }
    public void decrementAmount() {
        this.amount--;
        amountSold++;
    }
    public double getBuyPrice() {
        return buyPrice;
    }
    public void setBuyPrice(double buyPrice) {
        double oldPrice = this.buyPrice;
        this.buyPrice = buyPrice;
        if(oldPrice > buyPrice) {
            fluctuation--;
        } else if(oldPrice < buyPrice) {
            fluctuation++;
        }
    }
    public int getAmountSold() { return amountSold; }
    public void resetAmountSold() { amountSold = 0; }
    public void incrementAmount() { amount++; }
}