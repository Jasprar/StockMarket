package StockMarket;

import java.util.ArrayList;
import java.util.HashMap;

public class RandomTrader extends Trader {

    public RandomTrader(ArrayList<Portfolio> portfolios) {
        super(portfolios);
    }

    @Override
    public void setMode(int mode) {
        // TODO
    }

    @Override
    public void switchMode() {
        // TODO
    }

    @Override
    public HashMap<String, Integer> buy() {
        return null;
    }

    @Override
    public ArrayList<Share> sell() {
        return null;
    }
}
