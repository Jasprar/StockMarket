package StockMarket;

import java.util.*;

public abstract class Trader {
    protected ArrayList<Portfolio> portfolios;
    protected String event; // Contains the name of the company or commodity (or "Any") that the RandomTrader has to buy/sell during an event.
    protected ArrayList<ClientTracker> clientTrackers;

    /**
     *
     * @param portfolios
     */
    public Trader(ArrayList<Portfolio> portfolios) {
        this.portfolios = portfolios;
        // Initialise ClientTrackers.
        clientTrackers = new ArrayList<>();
        for(Portfolio p : portfolios) {
            updateTrackers(p.getShares(), p.getClientName());
        }
    }


    /**
     * This method returns the list of portfolios..
     * @return An ArrayList of portfolios that are managed by this Trader.
     */
    public ArrayList<Portfolio> getPortfolios() {
        return portfolios;
    }

    public void setMode(int mode) {
        // Does nothing for IntelligentTrader (but required for iteration through Trader list), overridden in RandomTrader.
    }

    public void switchMode() {
        // Does nothing for IntelligentTrader (but required for iteration through Trader list), overridden in RandomTrader
    }

    // HashMap is company name : # sought for purchase.
    public abstract HashMap<String,Integer> buy(HashMap<String, Double> sharePrices);

    // ArrayList is the Shares the trader wishes to sell - remember to remove them from the portfolios & increment totalWorth!
    public abstract ArrayList<Share> sell();

    public void returnShares(ArrayList<Share> shares, String companyName, int sellTotal) {
        for(Portfolio p : portfolios) {
            for (ClientTracker ct : clientTrackers) {
                if(ct.getCompanyName().equals(companyName) && ct.getClientName().equals(p.getClientName())) {
                    int shareOfShares = shares.size() * (int)(Math.floor(ct.getAmountSold() / sellTotal));
                    ArrayList<Share> sharesReturned = new ArrayList<>(shares.subList(0, shareOfShares));
                    p.getShares().addAll(sharesReturned);
                    updateTrackers(sharesReturned, p.getClientName());
                    for (Share s : sharesReturned) {
                        p.addCashHolding(s.getSharePrice());
                    }
                    shares.removeAll(sharesReturned);
                }
            }
        }
        if(!shares.isEmpty()) {
            Random rand = new Random();
            int index = rand.nextInt(portfolios.size());
            Portfolio p = portfolios.get(index);
            p.getShares().addAll(shares);
            for(Share s : shares) {
                p.addCashHolding(s.getSharePrice());
            }
            updateTrackers(shares, portfolios.get(index).getClientName());
        }
    }

    public void addNewShares(ArrayList<Share> sharesBought) {
        if (!sharesBought.isEmpty()) {
            int split = 0;
            int i = 0;
            while(sharesBought.size() > split) {
                split = (int)Math.floor(sharesBought.size() / portfolios.size());
                System.out.println("Split size = " + split + ", Shares bought = " + sharesBought.size());
                ArrayList<Share> shares = new ArrayList<>(sharesBought.subList(0, split));
                Portfolio p = portfolios.get(i);
                p.getShares().addAll(shares);
                updateTrackers(shares, p.getClientName());
                for (Share s : shares) {
                    p.addCashHolding(s.getSharePrice());
                }
                sharesBought.removeAll(shares);
                i = (i + 1) % portfolios.size();
            }
            if(!sharesBought.isEmpty()) {
                Random rand = new Random();
                int index = rand.nextInt(portfolios.size());
                Portfolio p = portfolios.get(index);
                p.getShares().addAll(sharesBought);
                for(Share s : sharesBought) {
                    p.addCashHolding(s.getSharePrice());
                }
                updateTrackers(sharesBought, portfolios.get(index).getClientName());
            }
        }
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void checkTrackers() {
        for(ClientTracker ct : clientTrackers) {
            if(ct.getAmount() == 0) {
                clientTrackers.remove(ct);
            } else {
                ct.resetAmountSold();
            }
        }
    }

    private void updateTrackers(ArrayList<Share> shares, String clientName) {
        System.out.println("Updating ClientTrackers for " + shares.size() + " shares...");
        int j = 1;
        for(Share s : shares) {
            boolean found = false;
            int i = 0;
            while(!found && i < clientTrackers.size()) {
                ClientTracker ct = clientTrackers.get(i);
                if(ct.getCompanyName().equals(s.getCompanyName()) && ct.getClientName().equals(clientName)) {
                    found = true;
                    ct.incrementAmount();
                    ct.setBuyPrice(s.getSharePrice());
                }
                i++;
            }
            if(!found) { // No such ClientTracker exists, create one.
                System.out.println("Creating a new ClientTracker for " + clientName + " tracking " + s.getCompanyName());
                clientTrackers.add(new ClientTracker(clientName, s.getCompanyName(), s.getCommodity(),s.getSharePrice()));
            }
            j++;
        }
    }
}
