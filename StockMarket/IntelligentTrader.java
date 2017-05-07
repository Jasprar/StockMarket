package StockMarket;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
        HashMap<String, Integer> traderBuys = new HashMap<>();
        for(Portfolio p : portfolios) {
            double amountToSpend = 0.01 * p.getCashHolding();
            int i = 0;
            while(amountToSpend > Collections.min(sharePrices.values()) && i < 100) {
                Collections.shuffle(clientTrackers);
                for(ClientTracker ct : clientTrackers) {
                    if(amountToSpend < Collections.min(sharePrices.values())) {
                        break;
                    }
                    if(ct.getClientName().equals(p.getClientName())) {
                        double fluctScale = ct.getFluctuation() / ct.getBuyPrice();
                        int numberToBuy = 0;
                        if(fluctScale < 0 && ct.getBuyPrice() > ct.getOriginalPrice()) {
                            numberToBuy = (int) Math.floor((1 - fluctScale) * (amountToSpend / sharePrices.get(ct.getCompanyName()))) % 50;
                        } else if(fluctScale > 0) {
                            numberToBuy = (int) Math.floor(fluctScale * (amountToSpend / sharePrices.get(ct.getCompanyName()))) % 50;
                        } else if(fluctScale == 0){
                            numberToBuy = (int) Math.floor(0.5 * (amountToSpend / sharePrices.get(ct.getCompanyName()))) % 50;
                        }
                        if(traderBuys.containsKey(ct.getCompanyName())) {
                            traderBuys.put(ct.getCompanyName(), traderBuys.get(ct.getCompanyName()) + numberToBuy);
                        } else {
                            traderBuys.put(ct.getCompanyName(), numberToBuy);
                        }
                        amountToSpend -= (numberToBuy * sharePrices.get(ct.getCompanyName()));
                        ct.addAmountBought(numberToBuy);
                    }
                }
                i++;
            }
        }
        return traderBuys;
    }

    /**
     * Overrides the trader's sell method. If the price is higher than what it previously
     * bought it at and the price is falling, sell, or else do nothing for
     * that share
     * @return An ArrayList of shares it wants to sell
     */
    @Override
    public ArrayList<Share> sell(HashMap<String, Double> sharePrices) {
        ArrayList<Share> traderSells = new ArrayList<>();
        for(Portfolio p : portfolios) {
            ArrayList<Share> shares = p.getShares();
            double amountToEarn = 0.01 * p.getSharesTotal();
            int i = 0;
            while(amountToEarn > 0 && i < 100) {
                Collections.shuffle(clientTrackers);
                for(ClientTracker ct: clientTrackers) {
                    if(amountToEarn < 0) {
                        break;
                    } else if(ct.getAmount() == 0) {
                        continue;
                    }
                    if(ct.getClientName().equals(p.getClientName())) {
                        double fluctScale = ct.getFluctuation() / ct.getBuyPrice();
                        int numberToSell = 0;
                        if(fluctScale < 0) {
                            numberToSell = (int) Math.floor(fluctScale * (amountToEarn / sharePrices.get(ct.getCompanyName()))) % ct.getAmount();
                        } else if(fluctScale > 0) {
                            numberToSell = (int) Math.floor((1 - fluctScale) * (amountToEarn / sharePrices.get(ct.getCompanyName()))) % ct.getAmount();
                        } else {
                            numberToSell = (int) Math.floor(0.5 * (amountToEarn / sharePrices.get(ct.getCompanyName()))) % ct.getAmount();
                        }
                        for(int j = 0; j < numberToSell; j++) {
                            traderSells.add(findAndRemove(p.getShares(), ct.getCompanyName()));
                            amountToEarn -= sharePrices.get(ct.getCompanyName());
                            ct.decrementAmount();
                        }
                    }
                }
                i++;
            }
        }
        return traderSells;
    }

    private Share findAndRemove(ArrayList<Share> shares, String companyName) {
        for(int i = 0; i < shares.size(); i++) {
            if(shares.get(i).getCompanyName().equals(companyName)) {
                return shares.remove(i);
            }
        }
        System.err.println("Should not have made it here...");
        return null;
    }

}