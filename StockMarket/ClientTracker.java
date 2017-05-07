package StockMarket;

public class ClientTracker {
    private double originalPrice;
    private String clientName;
    private String companyName;
    private String commodityType;
    private int amount;
    private int amountSold;
    private int amountBought;
    private double buyPrice; // Most recently bought
    private double fluctuation; // Negative if buyPrice dropping since bought, positive otherwise.

    public ClientTracker(String clientName, String companyName, String commodityType) {
        this.clientName = clientName;
        this.companyName = companyName;
        this.commodityType = commodityType;
        amount = 0;
        amountSold = 0;
        amountBought = 0;
        this.buyPrice = -1;
        this.originalPrice = -1;
        fluctuation = 0;
    }

    public double getFluctuation() { return fluctuation; }
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
        if(oldPrice == -1) {
            originalPrice = buyPrice;
        } else {
            fluctuation += (buyPrice - oldPrice);
        }
    }
    public int getAmountSold() { return amountSold; }
    public void resetAmountSold() { amountSold = 0; }
    public void incrementAmount() { amount++; }
    public void resetAmountBought() {
        //System.out.println("Resetting amount bought for " + clientName + "...");
        amountBought = 0;
    }
    public void incrementAmountBought() {
        amountBought++;
    }

    public void addAmountBought(int amountBought) {
        this.amountBought += amountBought;
    }

    public int getAmountBought() {
        //System.out.println(clientName + " has bought " + amountBought + " shares of " + companyName);
        return amountBought;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }
}