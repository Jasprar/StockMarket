package StockMarket;

public class ClientTracker {
    private String clientName;
    private String companyName;
    private int amount;
    private int amountSold;
    private int buyPrice;
    private int fluctuation; // Negative if buyPrice dropping since bought, positive otherwise.

    public ClientTracker(String clientName, String companyName, int buyPrice) {
        this.clientName = clientName;
        companyName = null;
        amount = 1;
        amountSold = 0;
        this.buyPrice = buyPrice;
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
    public void addAmount(int amount) { this.amount += amount; }
}