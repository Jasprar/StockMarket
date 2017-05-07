package StockMarket;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class IntelligentTrader extends Trader {
    /**
     * Initializes Intelligent traders
     * @param portfolios
     * @param allShares
     */
    public IntelligentTrader(ArrayList<Portfolio> portfolios, ArrayList<Share> allShares) {
        super(portfolios, allShares);
    }

    /**
     * Overrides the trader's buy method. If the price is going up, it will buy, or else
     * it will not do anything for that share
     * @param sharePrices
     * @return A HashMap of shares it wants to buy
     */
    @Override
    public HashMap<String, Integer> buy(HashMap<String, Double> sharePrices) {
        //System.out.println("IntelligentTrader buying begins...");
        HashMap<String, Integer> sharesBuying = new HashMap<>();
        for (Portfolio p : portfolios) {
            for (Share s : p.getShares()) {
                for (ClientTracker ct : clientTrackers) {
                    if (ct.getClientName().equals(p.getClientName()) && ct.getCompanyName().equals(s.getCompanyName()) && s.getSharePrice() > ct.getBuyPrice()) {
                        int fluctScale = ct.getFluctuation();
                        int randomNoToBuy = 0;
                        if(fluctScale < 0 && !sharesBuying.containsKey(s.getCompanyName()) && randomNoToBuy != 0) {
                            fluctScale = fluctScale * -1;
                            int amount = ThreadLocalRandom.current().nextInt(0, (int) Math.ceil(((p.getCashHolding() * 0.01)) * (fluctScale)));
                            sharesBuying.put(s.getCompanyName(), amount);
                            ct.addAmountBought(amount);
                        } else if(fluctScale == 0 && !sharesBuying.containsKey(s.getCompanyName()) && randomNoToBuy != 0) {
                            int amount = ThreadLocalRandom.current().nextInt(0, (int) Math.ceil(p.getCashHolding() * 0.01));
                            sharesBuying.put(s.getCompanyName(), amount);
                            ct.addAmountBought(amount);
                        }
                    }
                }
            }
        }
        return sharesBuying;
    }

    /**
     * Overrides the trader's sell method. If the price is higher than what it previously
     * bought it at and the price is falling, sell, or else do nothing for
     * that share
     * @return An ArrayList of shares it wants to sell
     */
    @Override
    public ArrayList<Share> sell() {
        //System.out.println("Intelligent trader selling begins...");
        ArrayList<Share> sharesSelling = new ArrayList<>();
        for (Portfolio p : portfolios) {
            HashMap<String, Integer> randomNoToSell = new HashMap<>();
            ArrayList<String> companyNames = new ArrayList<>();
            for (ClientTracker ct : clientTrackers) {
                if (ct.getClientName().equals(p.getClientName())) {
                    for (Share s : p.getShares()) {
                        if (s.getCompanyName().equals(ct.getCompanyName()) && p.getClientName().equals(ct.getClientName()) && ct.getBuyPrice() > s.getSharePrice()) {
                            int fluctScale = ct.getFluctuation();
                            if(fluctScale > 0 && !randomNoToSell.containsKey(s.getCompanyName())) {
                                randomNoToSell.put(s.getCompanyName(), ThreadLocalRandom.current().nextInt(0, (int) Math.ceil((p.getSharesTotal() * 0.01) * (fluctScale))));
                                companyNames.add(s.getCompanyName());
                            } else if (fluctScale == 0 && !!randomNoToSell.containsKey(s.getCompanyName())) {
                                randomNoToSell.put(s.getCompanyName(), ThreadLocalRandom.current().nextInt(0, (int) Math.ceil(p.getSharesTotal() * 0.01)));
                                companyNames.add(s.getCompanyName());
                            }
                        }
                    }
                }
            }
            for(String companyName : companyNames) {
                int sharesToChoose = randomNoToSell.get(companyName) / companyNames.size();
                int i = 0;
                while(sharesToChoose > 0 && i < p.getShares().size()) {
                    if(p.getShares().get(i).getCompanyName().equals(companyName)) {
                        Share s = p.getShares().remove(i);
                        sharesSelling.add(s);
                        p.addCashHolding(s.getSharePrice());
                        sharesToChoose--;
                    }
                    i++;
                }
            }
            for(ClientTracker ct : clientTrackers) {
                for(Share s : sharesSelling) {
                    if (ct.getCompanyName().equals(s.getCompanyName()) && ct.getClientName().equals(p.getClientName())) {
                        ct.decrementAmount();
                    }
                }
            }
        }
        return sharesSelling;
    }
}