package StockMarket;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Trader {
    private ArrayList<Portfolio> portfolios;

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

    public abstract HashMap<String,Integer> buy();

    public abstract ArrayList<Share> sell();
}
