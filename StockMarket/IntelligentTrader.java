package StockMarket;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class IntelligentTrader extends Trader {
    public IntelligentTrader(ArrayList<Portfolio> portfolios) {
        super(portfolios);
    }

    // SHOULD BE DONE WITH THE BUY, NOT SURE YET, TESTING NEEDED.
    @Override
    public HashMap<String, Integer> buy(ArrayList<String> availableCompanies) {
        HashMap<String, Integer> sharesBuying = new HashMap<>();
        for (Portfolio p : portfolios) {
            for (Share s : p.getShares()) {
                        for (ClientTracker ct : clientTrackers) {

                            if (ct.getClientName().equals(p.getClientName()) && p.getShares().equals(s) && s.getSharePrice() > ct.getBuyPrice()) {
                                int fluctScale = ct.getFluctuation();
                                int randomNoToBuy = 0;
                                randomNoToBuy = ThreadLocalRandom.current().nextInt(0, (int) Math.round((p.getCashHolding() + 1) * (fluctScale * 0.1)));
                                if (sharesBuying.containsKey(s.getCompanyName())) {
                                    sharesBuying.put(s.getCompanyName(), sharesBuying.get(s.getCompanyName()) + randomNoToBuy);
                                } else {
                                    sharesBuying.put(s.getCompanyName(), randomNoToBuy);
                                }

                            }
                        }
            }
        }
        return sharesBuying;
    }

    // AGAIN, SHOULD BE COMPLETED, NEED TESTING
    @Override
    public ArrayList<Share> sell() {
        ArrayList<Share> sharesSelling = new ArrayList<>();
        for (Portfolio p : portfolios) {
            for (ClientTracker ct : clientTrackers) {
                if (ct.getClientName().equals(p.getClientName())) {
                    for (Share s : p.getShares()) {
                        if (s.getCompanyName().equals(ct.getCompanyName()) && ct.getBuyPrice() < s.getSharePrice()) {
                            int fluctScale = ct.getFluctuation();
                            int randomNoToSell = ThreadLocalRandom.current().nextInt(0, (int) Math.round((p.getShares().size() + 1) * (fluctScale * 0.1)));
                            for (int i = 0; i < randomNoToSell; i++) {
                                Share shareToSell = new Share(s.getCompanyName(), s.getCommodity(), s.getSharePrice());
                                sharesSelling.add(shareToSell);
                                p.getShares().remove(s);
                                p.setCashHolding(p.getCashHolding() + s.getSharePrice());
                            }
                        }
                    }
                }
            }
        }
        return sharesSelling;
    }
}