package StockMarket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class RandomTrader extends Trader {
    static final int BALANCED = 0;
    static final int SELLER = -1;
    static final int BUYER = 1;
    static final int EVENTBUYER = 2;
    static final int EVENTSELLER = -2;
    private int mode;
    private Random rand;

    /**
     * @param portfolios
     */
    public RandomTrader(ArrayList<Portfolio> portfolios, ArrayList<Share> allShares) {
        super(portfolios, allShares);
        mode = BALANCED;
        rand = new Random();
    }

    @Override
    public HashMap<String, Integer> buy(HashMap<String, Double> sharePrices) {
        if(mode == EVENTBUYER) {
            return eventBuy(sharePrices);
        } else {
            ArrayList<String> companyNames = new ArrayList<>(sharePrices.keySet());
            HashMap<String, Integer> buying = new HashMap<>();
            for (Portfolio p : portfolios) {
                double amountToSpend = modeSelector(true, p.getCashHolding()); // % of the cash the client has.
                int i = 0; // To prevent infinite while-loop.
                while (amountToSpend > Collections.min(sharePrices.values()) && i < 100) {
                    String chosenCompany = companyNames.get(rand.nextInt(companyNames.size()));
                    if (sharePrices.get(chosenCompany) < amountToSpend) {
                        if (buying.containsKey(chosenCompany)) {
                            buying.put(chosenCompany, buying.get(chosenCompany) + 1);
                        } else {
                            buying.put(chosenCompany, 1);
                        }
                        amountToSpend -= sharePrices.get(chosenCompany);
                        for (ClientTracker ct : clientTrackers) {
                            if (ct.getCompanyName().equals(chosenCompany) && ct.getClientName().equals(p.getClientName())) {
                                ct.incrementAmountBought();
                            }
                        }
                    }
                    i++;
                }
            }
            return buying;
        }
    }

    private HashMap<String, Integer> eventBuy(HashMap<String, Double> sharePrices) {
        ArrayList<String> companyNames = new ArrayList<>(sharePrices.keySet());
        HashMap<String, Integer> buying = new HashMap<>();
        for (Portfolio p : portfolios) {
            double amountToSpend = modeSelector(true, p.getCashHolding()); // 1% of the cash the client has.
            int i = 0;
            while(amountToSpend > Collections.min(sharePrices.values()) && i < 100) {
                String chosenCompany = "";
                if(companyNames.contains(event)) {
                    chosenCompany = event;
                } else if(event.equals("Any")) {
                    chosenCompany = companyNames.get(rand.nextInt(companyNames.size()));
                } else {
                    Collections.shuffle(clientTrackers);
                    for(ClientTracker ct : clientTrackers) {
                        if(ct.getCommodityType().equals(event)) {
                            chosenCompany = ct.getCompanyName();
                        }
                    }
                }
                if(sharePrices.get(chosenCompany) < amountToSpend) {
                    if (buying.containsKey(chosenCompany)) {
                        buying.put(chosenCompany, buying.get(chosenCompany) + 1);
                    } else {
                        buying.put(chosenCompany, 1);
                    }
                    amountToSpend -= sharePrices.get(chosenCompany);
                    for (ClientTracker ct : clientTrackers) {
                        if (ct.getCompanyName().equals(chosenCompany) && ct.getClientName().equals(p.getClientName())) {
                            ct.incrementAmountBought();
                        }
                    }
                }
                i++;
            }
        }
        return buying;
    }

    @Override
    public ArrayList<Share> sell(HashMap<String, Double> sharePrices) {
        if(mode == EVENTSELLER) {
            return eventSell();
        } else {
            ArrayList<Share> toSell = new ArrayList<>();
            for (Portfolio p : portfolios) {
                ArrayList<Share> shares = p.getShares();
                double amountToEarn = modeSelector(false, p.getSharesTotal());
                int i = 0;
                while (amountToEarn > 0 && i < 100) {
                    Share s = shares.remove(rand.nextInt(shares.size()));
                    toSell.add(s);
                    amountToEarn -= s.getSharePrice();
                    for(ClientTracker ct : clientTrackers) {
                        if(ct.getClientName().equals(p.getClientName()) && ct.getCompanyName().equals(s.getCompanyName())) {
                            ct.decrementAmount();
                        }
                    }
                    p.addCashHolding(s.getSharePrice());
                    i++;
                }
            }
            return toSell;
        }
    }


    private ArrayList<Share> eventSell() {
        ArrayList<Share> toSell = new ArrayList<>();
        for (Portfolio p : portfolios) {
            ArrayList<Share> shares = p.getShares();
            double amountToEarn = modeSelector(false, p.getSharesTotal());
            int i = 0;
            while (amountToEarn > 0 && i < 100) {
                Share s = shares.get(rand.nextInt(shares.size()));
                if(s.getCompanyName().equals(event) || event.equals("Any") || s.getCommodity().equals(event)) {
                    toSell.add(s);
                    shares.remove(s);
                    amountToEarn -= s.getSharePrice();
                    for(ClientTracker ct : clientTrackers) {
                        if(ct.getClientName().equals(p.getClientName()) && ct.getCompanyName().equals(s.getCompanyName())) {
                            ct.decrementAmount();
                        }
                    }
                    p.addCashHolding(s.getSharePrice());
                }
                i++;
            }
        }
        return toSell;
    }

    @Override
    public void setMode(int mode) {
        this.mode = mode;
    }


    @Override
    public void switchMode() {
        int randomNextDayMode = rand.nextInt(100);
        switch (mode) {
            case BALANCED:
                if (randomNextDayMode < 10) {
                    mode = RandomTrader.SELLER;
                } else if (randomNextDayMode >= 10 && randomNextDayMode < 80) {
                    mode = RandomTrader.BALANCED;
                } else {
                    mode = RandomTrader.BUYER;
                }
            case SELLER:
                if (randomNextDayMode < 40) {
                    mode = RandomTrader.SELLER;
                } else {
                    mode = RandomTrader.BALANCED;
                }
            case BUYER:
                if (randomNextDayMode < 70) {
                    mode = RandomTrader.BALANCED;
                } else {
                    mode = RandomTrader.BUYER;
                }
        } // Else event is in progress, do not switch mode.
    }

    private double modeSelector(boolean buying, double metric) {
        //System.out.println("metric =" + metric);
        double amount = 0;
        if (buying) {
            switch(mode) {
                case BALANCED:
                    amount = rand.nextInt((int)Math.ceil(0.01 * metric));
                case SELLER:
                    amount = rand.nextInt((int)Math.ceil(0.005 * metric));
                case BUYER:
                    amount = rand.nextInt((int)Math.ceil(0.02 * metric));
                case EVENTBUYER:
                    amount = rand.nextInt((int)Math.ceil(0.02 * metric));
                case EVENTSELLER:
                    amount = rand.nextInt((int)Math.ceil(0.005 * metric));
            }
        } else {
            switch(mode) {
                case BALANCED:
                    amount = rand.nextInt((int)Math.ceil(0.01 * metric));
                case SELLER:
                    amount = rand.nextInt((int)Math.ceil(0.02 * metric));
                case BUYER:
                    amount = rand.nextInt((int)Math.ceil(0.005 * metric));
                case EVENTBUYER:
                    amount = rand.nextInt((int)Math.ceil(0.005 * metric));
                case EVENTSELLER:
                    amount = rand.nextInt((int)Math.ceil(0.02 * metric));
            }
        }
        return amount;
    }

}