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
}
