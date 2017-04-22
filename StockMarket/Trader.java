package StockMarket;

import java.util.ArrayList;

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
        // Does nothing for IntelligentTrader (but required for iteration through Trader list), overridden in RandomTrader.
    }
}
