package StockMarket;

import java.util.ArrayList;

public class Portfolio {
    private String clientName;
    private int totalWorth;
    private int cashHolding;
    private ArrayList<Share> shares;

    public Portfolio(String clientName) {
        this.clientName = clientName;
        totalWorth = 0; // cashHolding is updated later (due to processing the spreadsheet row-by-row).
        shares = new ArrayList<>();
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

    public ArrayList<Share> getShares() {
        return shares;
    }

    public int getTotalWorth() {
        return totalWorth;
    }

    // MUST ONLY BE USED DURING SETUP, AS AFFECTS totalWorth!
    public void setCashHolding(int cashHolding) {
        cashHolding = cashHolding * 100; // cashHolding is in pounds, we wish to store it in pence.
        this.cashHolding = cashHolding;
        totalWorth += cashHolding;
    }

    public int getCashHolding() {
        return cashHolding;
    }
}
