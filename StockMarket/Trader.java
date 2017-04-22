package StockMarket;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Trader {
    private ArrayList<Portfolio> portfolios;
    private HashMap<Portfolio, ArrayList<Share>> sharesRemoved; // Used for returning shares to their respective portfolios if they don't sell.

    public Trader(ArrayList<Portfolio> portfolios) {
        this.portfolios = portfolios;
    }

    public ArrayList<Portfolio> getPortfolios() {
        return portfolios;
    }

    public void setMode(int mode) {
        // Does nothing for IntelligentTrader (but required for iteration through Trader list), overridden in RandomTrader.
    }

    public void switchMode() {
        // Does nothing for IntelligentTrader (but required for iteration through Trader list), overridden in RandomTrader
    }

    // HashMap is company name : # sought for purchase - remember to increment totalWorth.
    public abstract HashMap<String,Integer> buy();

    // ArrayList is the Shares the trader wishes to sell - remember to remove them from the portfolios & decrement totalWorth!
    public abstract ArrayList<Share> sell();
}
