package StockMarket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Random;


public class RandomTrader extends Trader {
    static final int BALANCED = 0;
    static final int SELLER = -1;
    static final int BUYER = 1;
    static final int EVENTBUYER = 2;
    static final int EVENTSELLER = -2;
    private int mode;
    private int cashAvailable;

    public RandomTrader(ArrayList<Portfolio> portfolios) {
        super(portfolios);
        System.out.println("Creating a new RandomTrader with " + portfolios.size() + " portfolios...");
        ArrayList<Portfolio> ports = new ArrayList<>();
        mode = RandomTrader.BALANCED;
    }

    // NOTE: PROBABLE BUG. Not getPortfolio or super.portfolio. Should use port

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
        int randomNextDayMode = ThreadLocalRandom.current().nextInt(0, 101);
        if (mode == RandomTrader.BALANCED) {
            if (randomNextDayMode <= 10) {
                mode = RandomTrader.SELLER;
            } else if (randomNextDayMode > 10 && randomNextDayMode <= 80) {
                mode = RandomTrader.BALANCED;
            } else {
                mode = RandomTrader.BUYER;
            }
        } else if (mode == RandomTrader.SELLER) {
            if (randomNextDayMode <= 40) {
                mode = RandomTrader.SELLER;
            } else {
                mode = RandomTrader.BALANCED;
            }
        } else if (mode == RandomTrader.BUYER) {
            if (randomNextDayMode <= 70) {
                mode = RandomTrader.BALANCED;
            } else {
                mode = RandomTrader.BUYER;
            }
        } // Else event is in progress, do not switch mode.
    }

    // Remember that with these methods, if an event is in progress ( String event != null), then the RandomTraders should
    // only be trading in shares of that name (and I talked to Bradley - If it is a buy event, selling should happen
    // AS IT USUALLY WOULD and vice-versa).

    @Override
    public HashMap<String, Integer> buy(HashMap<String, Double> sharePrices) {
        System.out.println("RandomTrader buying begins...");
        if (mode == RandomTrader.EVENTBUYER) {
            return eventBuy(sharePrices);
        } else {
            HashMap<String, Integer> sharesBuying = new HashMap<>();
            for(Portfolio p : portfolios) {
                int shareSize = p.getShares().size();
                int randomNoToBuy = modeSelector(true, p.getShares().size());
                for (int i = 0; i < randomNoToBuy; i++) {
                    int randomCompany = new Random().nextInt(sharePrices.size());
                    String randomlyChosenCompany = new ArrayList<>(sharePrices.keySet()).get(randomCompany);
                    if (sharesBuying.containsKey(randomlyChosenCompany)) {
                        sharesBuying.put(randomlyChosenCompany, sharesBuying.get(randomlyChosenCompany) + 1);
                    } else {
                        sharesBuying.put(randomlyChosenCompany, 1);
                    }
                    p.addCashHolding(sharePrices.get(randomlyChosenCompany));
                }
            }
            return sharesBuying;
        }
    }

    private HashMap<String, Integer> eventBuy(HashMap<String, Double> sharePrices) {
        HashMap<String, Integer> sharesBuying = new HashMap<>();
        for (Portfolio p : portfolios) {
            int randomNoToBuy = modeSelector(true, p.getShares().size());
            while(randomNoToBuy > 0) {
                for (ClientTracker ct : clientTrackers) {
                    if ((ct.getCompanyName().equals(event) || ct.getCommodityType().equals(event)) && ct.getClientName().equals(p.getClientName())) {
                        String c = ct.getCompanyName();
                        if (sharesBuying.containsKey(c)) {
                            sharesBuying.put(c, sharesBuying.get(c) + 1);
                        } else {
                            sharesBuying.put(c, 1);
                        }
                        p.addCashHolding(sharePrices.get(c));
                        randomNoToBuy--;
                    }
                }
            }
        }
        if (event.equals("Any")) {
            for (Portfolio p : portfolios) {
                for (ClientTracker ct : clientTrackers) {
                    if (ct.getClientName().equals(p.getClientName())) {
                        int randomNoToBuy = modeSelector(true, p.getShares().size());
                        for (int i = 0; i < randomNoToBuy; i++) {
                            if (sharesBuying.containsKey(ct.getCompanyName())) {
                                sharesBuying.put(ct.getCompanyName(), sharesBuying.get(ct.getCompanyName()) + 1);
                            } else {
                                sharesBuying.put(ct.getCompanyName(), 1);
                            }
                            p.addCashHolding(sharePrices.get(ct.getCompanyName()));
                        }
                    }
                }
            }
        }
        return sharesBuying;
    }

    //sell
    @Override
    public ArrayList<Share> sell() {
        System.out.println("RandomTrader selling begins...");
        //work on the random selector
        //each share is an object so all object selling needs to be moved over to simulator class!
        ArrayList<Share> sharesSelling = new ArrayList<>();
            if (mode == RandomTrader.EVENTSELLER) {
                return eventSell();
            } else {
                for (Portfolio p : portfolios) {
                    int shareSize = p.getShares().size();
                    int randomNoToSell = modeSelector(false, shareSize);
                    for (int i = 0; i < randomNoToSell; i++) {
                        shareSize = p.getShares().size();
                        int currentRandomShare = ThreadLocalRandom.current().nextInt(0, shareSize);
                        Share s = p.getShares().remove(currentRandomShare);
                        for(ClientTracker ct : clientTrackers) {
                            if(ct.getClientName().equals(p.getClientName()) && ct.getCompanyName().equals(s.getCompanyName())) {
                                ct.decrementAmount();
                            }
                        }
                        sharesSelling.add(s);
                        p.addCashHolding(s.getSharePrice());
                    }
                }
        }
        return sharesSelling;
    }


    private ArrayList<Share> eventSell() {
        ArrayList<Share> sharesSelling = new ArrayList<>();
        for (Portfolio p : portfolios) {
            int shareSize = p.getShares().size();
            int randomNoToSell = modeSelector(false, shareSize);
            int iterations = 0;
            while(randomNoToSell > 0 && iterations < 10) {
                for (int i = 0; i < p.getShares().size(); i++) {
                    if (p.getShares().get(i).getCommodity().equals(event) || event == "Any" || p.getShares().get(i).getCompanyName().equals(event)) {
                        System.out.println("Number left to choose = " + randomNoToSell);
                        Share shareToSell = p.getShares().remove(i);
                        sharesSelling.add(shareToSell);
                        p.addCashHolding(shareToSell.getSharePrice());
                        randomNoToSell--;
                        for(ClientTracker ct : clientTrackers) {
                            if(ct.getClientName().equals(p.getClientName()) && ct.getCompanyName().equals(shareToSell.getCompanyName())) {
                                ct.decrementAmount();
                            }
                        }
                        if(randomNoToSell == 0) {
                            break;
                        }
                    }
                }
                iterations++;
            }
        }
        return sharesSelling;

    }

    private int modeSelector(boolean buyMode, int shareSize) {
        int randomNoToBuySell;
        if (mode == RandomTrader.BALANCED) {
            randomNoToBuySell = ThreadLocalRandom.current().nextInt(0, (int) Math.round((shareSize + 1) * 0.01));
        } else if (mode == RandomTrader.SELLER) {
            randomNoToBuySell = ThreadLocalRandom.current().nextInt(0, (int) Math.round((shareSize + 1) * 0.02));
        } else {
            randomNoToBuySell = ThreadLocalRandom.current().nextInt(0, (int) Math.round((shareSize + 1) * 0.005));
        }
        return randomNoToBuySell;
    }
}
