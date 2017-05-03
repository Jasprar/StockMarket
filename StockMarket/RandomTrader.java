package StockMarket;

import javax.sound.sampled.Port;
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
    private int shareSize;
    private int randomShare;
    private int randomCompany;
    private int currentRandomShare;
    private int sharePrice;
    private int randomNoToBuySell;
    private int cashAvailable;
    private String companyName;
    private String commodityType;

    public RandomTrader(ArrayList<Portfolio> portfolios) {
        super(portfolios);
        System.out.println("Creating a new RandomTrader with " + portfolios.size() + " portfolios...");
        ArrayList<Portfolio> ports = new ArrayList<>();
        mode = RandomTrader.BALANCED;
        randomShare = ThreadLocalRandom.current().nextInt(0,shareSize + 1);
        companyName = this.getPortfolios().get(0).getShares().get(currentRandomShare).getCompanyName();
        commodityType = this.getPortfolios().get(0).getShares().get(currentRandomShare).getCommodity();
        sharePrice = this.getPortfolios().get(0).getShares().get(currentRandomShare).getSharePrice();
        shareSize = this.getPortfolios().get(0).getShares().size();
        cashAvailable = this.getPortfolios().get(0).getCashHolding();
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
        } else if(mode == RandomTrader.BUYER) {
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
    public HashMap<String, Integer> buy(ArrayList<String> availableCompanies) {
        if(mode == RandomTrader.EVENTBUYER) {
            return eventBuy(availableCompanies);
        }
        else {
            int randomNoToBuy = modeSelector(true);
            HashMap<String, Integer> sharesBuying = new HashMap<String, Integer>();

            for (int i = 0; i < randomNoToBuy; i++) {
                randomCompany = new Random().nextInt(availableCompanies.size());
                String randomlyChosenCompany = availableCompanies.get(randomCompany);
                if (sharesBuying.containsKey(randomlyChosenCompany)) {
                    sharesBuying.put(randomlyChosenCompany, sharesBuying.get(randomlyChosenCompany) + 1);
                } else {
                    sharesBuying.put(randomlyChosenCompany, 1);
                }
                getPortfolios().get(0).setCashHolding(getPortfolios().get(0).getCashHolding());
            }
            return sharesBuying;
        }
    }

    // IS IT REALLY THIS LONG?
    // TODO: SHALL DOUBLE CHECK CODE.
    private HashMap<String,Integer> eventBuy(ArrayList<String> availableCompanies) {
        HashMap<String, Integer> sharesBuying = new HashMap<>();
        String eventType;
        if (event.equals("Q1Q")) {
            eventType = "Q1Q";
        } else if (event.equals("Food")) {
            eventType = "Food";
        } else if (event.equals("Property")) {
            eventType = "Property";
        } else if (event.equals("Hard")) {
            eventType = "Hard";
            for (Portfolio p : portfolios) {
                for (String c : availableCompanies) {
                    for (ClientTracker ct : clientTrackers) {
                        if (ct.getCommodityType().equals(eventType) && ct.getClientName().equals(p.getClientName())) {
                            int randomNoToBuy = modeSelector(true);
                            for (int j = 0; j <= randomNoToBuy; j++) {
                                if (sharesBuying.containsKey(c)) {
                                    sharesBuying.put(c, sharesBuying.get(c) + 1);
                                } else {
                                    sharesBuying.put(c, 1);
                                }
                            }
                        }
                    }
                }
            }
            if (event.equals("Any")) {
                for (Portfolio p : portfolios) {
                    for (ClientTracker ct : clientTrackers) {
                        if (ct.getClientName().equals(p.getClientName())) {
                            int randomNoToBuy = modeSelector(true);
                            for (int i = 0; i <= randomNoToBuy; i++) {
                                if (sharesBuying.containsKey(ct.getCompanyName())) {
                                    sharesBuying.put(ct.getCompanyName(), sharesBuying.get(ct.getCompanyName()) + 1);
                                } else {
                                    sharesBuying.put(ct.getCompanyName(), 1);
                                }
                            }
                        }
                    }
                }
            }
        }
        return sharesBuying;
    }

    @Override
    public ArrayList<Share> sell() {
        //work on the random selector
        //each share is an object so all object selling needs to be moved over to simulator class!
        ArrayList<Share> sharesSelling = new ArrayList<>();
        for (Portfolio p : portfolios) {
            if (mode == RandomTrader.EVENTSELLER) {
                return eventSell();
            }/* else {
            int randomNoToSell = modeSelector(false);
            ArrayList<Share> sharesSelling = new ArrayList<>();

            for (int i = 0; i < randomNoToSell; i++) {
                currentRandomShare = randomShare;
                Share shareToSell = new Share(companyName, commodityType, sharePrice);
                sharesSelling.add(shareToSell);
                this.getPortfolios().get(0).getShares().remove(currentRandomShare);
                this.getPortfolios().get(i).setCashHolding(this.getPortfolios().get(i).getCashHolding() + sharePrice);
            }*/ else {
                int randomNoToSell = modeSelector(false);
                for (int i = 0; i < randomNoToSell; i++) {
                    currentRandomShare = randomShare;
                    Share shareToSell = new Share(companyName, commodityType, sharePrice);
                    sharesSelling.add(shareToSell);
                    p.getShares().remove(currentRandomShare);
                    p.setCashHolding(p.getCashHolding() + sharePrice);
                }
            }
        }
        return sharesSelling;
    }


    private ArrayList<Share> eventSell() {
        int randomNoToSell = modeSelector(false);
        ArrayList<Share> sharesSelling = new ArrayList<>();
        String eventType;
        if (event.equals("Q1Q")) {
            eventType = "Q1Q";
        } else if (event.equals("Food")) {
            eventType = "Food";
        } else if (event.equals("Property")) {
            eventType = "Property";
        } else if (event.equals("Hard")) {
            eventType = "Hard";
        } else {
            eventType = "Any";
        }
        for (Portfolio p : portfolios) {
            for (int i = 0; i < p.getShares().size(); i++) {
                if (p.getShares().get(i).getCommodity().equals(eventType) || eventType == "Any") {
                    for (int j = 0; j < randomNoToSell; j++) {
                        Share shareToSell = new Share(p.getShares().get(i).getCompanyName(), p.getShares().get(i).getCommodity(), p.getShares().get(i).getSharePrice());
                        sharesSelling.add(shareToSell);
                        p.getShares().remove(i);
                        p.setCashHolding(p.getCashHolding() + p.getShares().get(i).getSharePrice());
                    }
                }
            }
        }
        return sharesSelling;

    }

    private int modeSelector(boolean buyMode) {
        int randomWhat;
        if(buyMode) {
            randomWhat = cashAvailable;
        }
        else {
            randomWhat = shareSize;
        }

        if (mode == RandomTrader.BALANCED) {
            randomNoToBuySell = ThreadLocalRandom.current().nextInt(0, (int) Math.round((randomWhat + 1) * 0.01));
        }
        else if (mode == RandomTrader.SELLER) {
            randomNoToBuySell = ThreadLocalRandom.current().nextInt(0, (int) Math.round((randomWhat + 1) * 0.02));
        }
        else {
            randomNoToBuySell = ThreadLocalRandom.current().nextInt(0, (int) Math.round((randomWhat + 1) * 0.005));
        }
        return randomNoToBuySell;
    }

    @Override
    public void addNewShares(ArrayList<Share> sharesBought) {
        // TODO: Add these new shares into the portfolios (fairly sure you can just evenly divide these up) and decrement total worths.
        this.getPortfolios().get(0).getShares().addAll(sharesBought);
        for(int i = 0; i <= sharesBought.size(); i++){
            this.getPortfolios().get(0).setCashHolding(this.getPortfolios().get(0).getCashHolding() - sharesBought.get(i).getSharePrice());
        }
    }

    //selling
    @Override
    public void returnShares(ArrayList<Share> shares, String companyName) {

/*        for(Portfolio p : super.portfolios ) {
            for(Share s : p.getShares()) {
                if()
            }
        } */
        // finds out how many clients were involved with transaction with this company of shares
        int noOfClientSelling = 0;
        for(Portfolio p : portfolios) {
            for(ClientTracker ct : clientTrackers) {
                if (ct.getCompanyName().equals(companyName) && ct.getClientName().equals(p.getClientName())) {
                    noOfClientSelling++;
                }
            }
        }
        int temp = (int) Math.floor(shares.size() / noOfClientSelling);
        for(int i = 0; i < shares.size(); i++) {
            for (ClientTracker ct : clientTrackers) {
                for (Portfolio p : portfolios) {
                    if(ct.getCompanyName().equals(companyName) && ct.getClientName().equals(p.getClientName())) {
                        for(int j = 0; j < temp; j++) {
                            p.addOneShare(shares.get(j));
                            shares.remove(j);

                        }
                    }
                }
            }
        }
        if(!shares.isEmpty()) {
        }
        getPortfolios().get(0).setCashHolding(getPortfolios().get(0).getCashHolding());

    }
}
