package StockMarket;

import java.util.ArrayList;

public class Portfolio {
    private String clientName;
    private int totalWorth;
    ArrayList<Share> shares;

    public Portfolio(String clientName) {
        this.clientName = clientName;
    }


}