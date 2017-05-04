package StockMarket;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class IntelligentTrader extends Trader {
    public IntelligentTrader(ArrayList<Portfolio> portfolios) {
        super(portfolios);
    }

    @Override
    public HashMap<String, Integer> buy(HashMap<String, Double> sharePrices) {
        System.out.println("IntelligentTrader buying begins...");
        HashMap<String, Integer> sharesBuying = new HashMap<>();
        for (Portfolio p : portfolios) {
            for (Share s : p.getShares()) {
                for (ClientTracker ct : clientTrackers) {
                    if (ct.getClientName().equals(p.getClientName()) && ct.getCompanyName().equals(s.getCompanyName()) && s.getSharePrice() > ct.getBuyPrice()) {
                        int fluctScale = ct.getFluctuation();
                        int randomNoToBuy = 0;
                        if(fluctScale < 0 && !sharesBuying.containsKey(s.getCompanyName()) && randomNoToBuy != 0) {
                            fluctScale = fluctScale * -1;
                            sharesBuying.put(s.getCompanyName(), ThreadLocalRandom.current().nextInt(0, (int) Math.round(((p.getCashHolding() / 1000)) * (fluctScale)) + 1));
                        } else if(fluctScale == 0 && !sharesBuying.containsKey(s.getCompanyName()) && randomNoToBuy != 0) {
                            sharesBuying.put(s.getCompanyName(), ThreadLocalRandom.current().nextInt(0, (int) Math.round(p.getCashHolding() / 1000) + 1));
                        }
                    }
                }
            }
        }
        return sharesBuying;
    }

    @Override
    public ArrayList<Share> sell() {
        System.out.println("Intelligent trader selling begins...");
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
                                randomNoToSell.put(s.getCompanyName(), ThreadLocalRandom.current().nextInt(0, (int) Math.round((p.getSharesTotal() / 1000) * (fluctScale)) + 1));
                                companyNames.add(s.getCompanyName());
                            } else if (fluctScale == 0 && !!randomNoToSell.containsKey(s.getCompanyName())) {
                                randomNoToSell.put(s.getCompanyName(), ThreadLocalRandom.current().nextInt(0, (int) Math.round(p.getSharesTotal() / 1000) + 1));
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