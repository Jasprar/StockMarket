package StockMarket;

public class ClientTracker {
    private final int originalPrice;
    private String clientName;
    private String companyName;
    private String commodityType;
    private int amount;
    private int amountSold;
    private int buyPrice; // Most recently bought
    private int fluctuation; // Negative if buyPrice dropping since bought, positive otherwise.

    public ClientTracker(String clientName, String companyName, String commodityType, int buyPrice) {
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
    public void removeAmount(int removed) {
        this.amount -= removed;
        amountSold = removed;
    }
    public int getBuyPrice() {
        return buyPrice;
    }
    public void setBuyPrice(int buyPrice) {
        int oldPrice = this.buyPrice;
        this.buyPrice = buyPrice;
        fluctuation += (oldPrice - buyPrice);
    }
    public int getAmountSold() { return amountSold; }
    public void resetAmountSold() { amountSold = 0; }
    public void incrementAmount() { amount++; }
}