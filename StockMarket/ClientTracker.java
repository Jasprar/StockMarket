package StockMarket;

public class ClientTracker {
    private String clientName;
    private String companyName;
    private int amount;
    private int amountSold;
    private int buyPrice;

    public ClientTracker(String clientName, String companyName, int amount, int buyPrice) {
        this.clientName = clientName;
        companyName = null;
        amount = 0;
        amountSold = 0;
        buyPrice = 0;
    }
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
        this.buyPrice = buyPrice;
    }
    public int getAmountSold() { return amountSold; }
    public void resetAmountSold() { amountSold = 0; }
    public void setAmount(int amount) { this.amount = amount; }
}