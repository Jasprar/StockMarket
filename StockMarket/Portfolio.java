package StockMarket;

import java.util.ArrayList;

public class Portfolio {
    private String clientName;
    private int totalWorth;
    private int cashHolding;
    private ArrayList<Share> shares;

    public Portfolio(String clientName) {
        this.clientName = clientName;
        cashHolding = 100000 * 100; // Stored in pence.
        totalWorth = cashHolding;
    }


    public String getClientName() {
        return clientName;
    }

    public void addShares(ArrayList<Share> shares) {
        this.shares.addAll(shares);
        for(Share share : shares) {
            totalWorth += share.getSharePrice();
        }
    }
}
