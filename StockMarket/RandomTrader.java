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
     * Initializes random traders
     * @param portfolios
     * @param allShares
     */
    public RandomTrader(ArrayList<Portfolio> portfolios, ArrayList<Share> allShares) {
        super(portfolios, allShares);
        mode = BALANCED;
        rand = new Random();
    }

    /**
     * Overrides the trader's buy method. If there is no buy event, the trader randomly
     * picks a number from 0 to 1% of its total assets and randomly chooses a share to buy.
     * It randomly picks shares 0 to 1% times with a for loop. If there is a buy event, it
     * calls the EventBuy method.
     * @param sharePrices
     * @return A HashMap of shares it wants to buy
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
                while(amountToSpend > Collections.min(sharePrices.values()) && i < 100) {
                    String chosenCompany = companyNames.get(rand.nextInt(companyNames.size()));
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
                double amountClientSpends = 0;
                for(ClientTracker ct : clientTrackers) {
                    if(ct.getClientName().equals(p.getClientName())) {
                        //System.out.println(ct.getClientName() + " wants to buy " + ct.getAmountBought() + " of " + ct.getCompanyName());
                        try {
                            amountClientSpends += ((double) ct.getAmountBought() * sharePrices.get(ct.getCompanyName()));
                        } catch(NullPointerException e) {}
                    }
                }
                //System.out.println("Total for " + p.getClientName() + ": " + amountClientSpends + ", client has " + p.getCashHolding());
            }
            //System.out.println("This trader has requested shares of Dawn Technology: " + buying.containsKey("Dawn Technology"));
            return buying;
        }
    }

    /**
     * If there is a buy event. It will find the company it needs to buy and buy 0 - 1% of
     * its total available assets.
     * @param sharePrices
     * @return A HashMap of shares it wants to buy
     */
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
     * If there is no sell event, it randomly chooses 0 - 1% of shares and then randomly sells
     * that amount of shares. If there is a sell event, it calls the eventSell method
     * @return An ArrayList of shares to sell
     */
    @Override
    public ArrayList<Share> sell() {
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

    /**
     * If there is a sell event, it will find shares that the client owns
     * and needs to sell and then sells it
     * @return An ArrayList of shares it wants to sell
     */
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
     * This method sets the mode to what is in the field
     * @param mode
     */
    @Override
    public void setMode(int mode) {
        this.mode = mode;
    }

    /**
     * This method randomly switches mode. The numbers are taken from the requirements document
     */
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

    /**
     * This method overrides the 0 - 1% to buy/sell. If the mode is Balanced, it will
     * want to buy and sell at 1% but if the modes are not balanced, numbers are different,
     * which is all included in the requirements document
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