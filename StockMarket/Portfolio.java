package StockMarket;

import java.util.ArrayList;

public class Portfolio {
    private String clientName;
    private int totalWorth;
    ArrayList<Share> shares;

    public Portfolio(String clientName) {
        this.clientName = clientName;
    }


    public String getClientName() {
        return clientName;
    }

    public void addShares(ArrayList<Share> shares) {
        shares.addAll(shares);
    }
}
