package StockMarket;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class RandomTrader extends Trader {
    static final int BALANCED = 0;
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
        // TODO: Switch according to the rules in the requirements document.
        // NOTE: When there is an event occurring, this method will still be called, however we will know when an event
        //       isn't occurring when String event (in Trader superclass) is null - it will be set back to null when an
        //       event ends.
    }

    // Remember that with these methods, if an event is in progress (String event != null), then the RandomTraders should
    // only be trading in shares of that name (and I talked to Bradley - If it is a buy event, selling should happen
    // AS IT USUALLY WOULD and vice-versa).
    @Override
    public HashMap<String, Integer> buy(ArrayList<String> availableCompanies) {
        return null;
    }

    @Override
    public ArrayList<Share> sell() {
        return null;
    }
}
