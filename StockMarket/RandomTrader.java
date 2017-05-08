package StockMarket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

/**
 * Represents a trader for a different company (one other than W&G). These traders manage all other portfolios.
 * @Author 164875 & 146803
 * @Version 06/05/2017
 */
public class RandomTrader extends Trader {
    static final int BALANCED = 0;
    static final int SELLER = -1;
    static final int BUYER = 1;
    static final int EVENTBUYER = 2;
    static final int EVENTSELLER = -2;
    private int mode;
    private Random rand;

    /**
     * Initializes RandomTraders. Initially sets the mode to balanced trading mode.
     * @param portfolios
     */
    public RandomTrader(ArrayList<Portfolio> portfolios, ArrayList<Share> allShares) {
        super(portfolios, allShares);
        mode = BALANCED;
        rand = new Random();
    }

    /**
     * Implements the buy method of the Trader superclass. Randomly chooses shares up to some percentage of each portfolio's
     * totalWorth (determined by the mode they are in).
     * @param sharePrices the HashMap of company name to share price used to make sure the trader does not spend more
     *                    than the client has.
     * @return The HashMap of company name to number of shares requested.
     */
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

    // Similar to the buy method, however only buys shares that match the event field.
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

    /**
     * Chooses shares from each portfolio randomly to sell (up to some percentage of total shares' worth - determined
     * by RNG and the mode they are in).
     * @param sharePrices Unused in the RandomTrader.
     * @return The list of shares put up for sale by this trader on the stock exchange.
     */
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

    // Similar to the sell method, but only sells shares matching the event field.
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

    /**
     * Sets the mode to the specified mode. Used after events have ended.
     * @param mode the integer representing the static field defining that mode.
     */
    @Override
    public void setMode(int mode) {
        this.mode = mode;
    }


    /**
     * Switches modes according to the following rules:
     * - If in balanced mode, there is a 10% chance the trader will switch to seller mode, a 20% chance they will switch
     * to buyer mode & a 70% chance they will stay in balanced mode.
     * - If in seller mode, there is a 40% chance they will stay in seller mode, and a 60% chance they will switch to balanced.
     * - If in buyer mode, there is a 70% chance they will switch to balanced mode & a 30% chance they will stay in buyer mode.
     */
    @Override
    public void switchMode() {
        int randomNextDayMode = rand.nextInt(100);
        switch (mode) {
            case BALANCED:
                //System.out.println("Trader was in balanced mode. is now in:");
                if (randomNextDayMode < 10) {
                    //System.out.println("SELLER MODE");
                    mode = RandomTrader.SELLER;
                } else if (randomNextDayMode >= 10 && randomNextDayMode < 80) {
                    //System.out.println("BALANCED MODE");
                    mode = RandomTrader.BALANCED;
                } else {
                    //System.out.println("BUYER MODE");
                    mode = RandomTrader.BUYER;
                }
            case SELLER:
                //System.out.println("Trader was in seller mode. is now in:");
                if (randomNextDayMode < 40) {
                    //System.out.println("SELLER MODE");
                    mode = RandomTrader.SELLER;
                } else {
                    //System.out.println("BALANCED MODE");
                    mode = RandomTrader.BALANCED;
                }
            case BUYER:
                //System.out.println("Trader was in buyer mode. is now in:");
                if (randomNextDayMode < 70) {
                    //System.out.println("BALANCED MODE");
                    mode = RandomTrader.BALANCED;
                } else {
                    //System.out.println("BUYER MODE");
                    mode = RandomTrader.BUYER;
                }
        } // Else event is in progress, do not switch mode.
    }

    /**
     *
     * @param buying
     * @param metric
     * @return
     */
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