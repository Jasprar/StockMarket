package StockMarket;

import java.util.ArrayList;
import java.util.HashMap;

public class RandomTrader extends Trader {
    private static final int BALANCED = 0;
    static final int SELLER = -1;
    static final int BUYER = 1;
    private int mode;

    public RandomTrader(ArrayList<Portfolio> portfolios) {
        super(portfolios);
        mode = RandomTrader.BALANCED;
    }

    @Override
    public void setMode(int mode) {
        this.mode = mode;
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
