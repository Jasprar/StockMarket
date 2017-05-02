package StockMarket;

public class ClientTracker {
    private String clientName;
    private String companyName;
    private int amount;
    private int buyPrice;

    public ClientTracker(String clientName, String companyName, int amount, int buyPrice) {
        this.clientName = clientName;
        companyName = null;
        amount = 0;
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
    public void setAmount(int amount) {
        this.amount = amount;
    }
    public int getBuyPrice() {
        return buyPrice;
    }
    public void setBuyPrice(int buyPrice) {
        this.buyPrice = buyPrice;
    }
}