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
    public Trader(ArrayList<Portfolio> portfolios, ArrayList<Share> allShares) {
        this.portfolios = portfolios;
        // Initialise ClientTrackers.
        clientTrackers = new ArrayList<>();
        for(Portfolio p : portfolios) {
            initialiseTrackers(allShares, p.getClientName());
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




    public void setEvent(String event) {
        this.event = event;
    }

    public void checkTrackers() {
        ArrayList<ClientTracker> trackers = new ArrayList<>();
        for(int i = 0; i < clientTrackers.size(); i++) {
            ClientTracker ct = clientTrackers.get(i);
            ct.resetAmountSold();
            ct.resetAmountBought();
        }
    }

    private void updateTrackers(ArrayList<Share> shares, String clientName) {
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
                System.err.println("THIS SHOULD NOT HAVE OCCURRED");
            }
            j++;
        }
    }

    // For when a company is removed from the sim.
    public void removeTrackers(String companyName) {
        ArrayList<ClientTracker> toRemove = new ArrayList<>();
        for(ClientTracker ct : clientTrackers) {
            if(ct.getCompanyName().equals(companyName)) {
                toRemove.add(ct);
            }
        }
        clientTrackers.removeAll(toRemove);
    }

    private void initialiseTrackers(ArrayList<Share> allShares, String clientName) {
        for(Share s : allShares) {
            boolean done = false;
            for(ClientTracker ct : clientTrackers) {
                if(ct.getCompanyName().equals(s.getCompanyName()) && ct.getClientName().equals(clientName)) {
                    // Already done this share.
                    done = true;
                }
            }
            if(!done) {
                //System.out.println("Creating a new ClientTracker for " + clientName + " tracking " + s.getCompanyName());
                clientTrackers.add(new ClientTracker(clientName, s.getCompanyName(), s.getCommodity(), s.getSharePrice()));
            }
        }
    }

    public int getShares() {
        int total = 0;
        for(Portfolio p : portfolios) {
            total += p.getShares().size();
        }
        return total;
    }

    public void returnShares(ArrayList<Share> shares, String companyName, int sellTotal) {
        //System.out.println("Returning Shares...");
        HashMap<Portfolio, Integer> amountForEachPortfolio = new HashMap<>();
        for(Portfolio p : portfolios) {
            for(ClientTracker ct : clientTrackers) {
                if(p.getClientName().equals(ct.getClientName()) && companyName.equals(ct.getCompanyName())) {
                    //System.out.println(p.getClientName() + ": " + shares.size() + " * " + ct.getAmountSold() + " / " + sellTotal + " = " + (int) Math.floor((double) shares.size() * ((double)ct.getAmountSold() / (double)sellTotal)));
                    amountForEachPortfolio.put(p, (int) Math.floor((double) shares.size() * ((double)ct.getAmountSold() / (double)sellTotal)));
                    //System.out.println(p.getClientName() + ": " + amountForEachPortfolio.get(p));
                }
            }
            if(!amountForEachPortfolio.containsKey(p)) {
                amountForEachPortfolio.put(p, 0);
            }
        }
        ArrayList<Share> leftOverShares = addShares(shares, companyName, new HashMap<>(amountForEachPortfolio));
        while(!leftOverShares.isEmpty()) {
            //System.out.println("While leftovershares isnt empty...");
            for (Portfolio p : portfolios) {
                int amount = amountForEachPortfolio.get(p);
                for (ClientTracker ct : clientTrackers) {
                    if(shares.isEmpty()) {
                        break;
                    }
                    if (ct.getClientName().equals(p.getClientName()) && ct.getCompanyName().equals(companyName) && (ct.getAmountSold() - amount) > -1 && leftOverShares.get(0).getSharePrice() < p.getCashHolding()) {
                        Share s = leftOverShares.remove(0);
                        ArrayList<Share> temp = new ArrayList<>();
                        temp.add(s);
                        p.addShares(temp);
                        //System.out.println("Total requested to buy of " + ct.getCompanyName() + " by " + ct.getClientName() + " = " + ct.getAmountSold());
                        updateTrackers(temp, p.getClientName());
                        amount--;
                    }
                }
                amountForEachPortfolio.put(p, amount);
            }
        }
    }

    public void addNewShares(ArrayList<Share> shares, String companyName, int buyTotal) {
        //System.out.println("Adding shares of " + companyName);
        HashMap<Portfolio, Integer> amountForEachPortfolio = new HashMap<>();
        for(Portfolio p : portfolios) {
            for(ClientTracker ct : clientTrackers) {
                if(p.getClientName().equals(ct.getClientName()) && companyName.equals(ct.getCompanyName())) {
                    //System.out.println(p.getClientName() + ": " + shares.size() + " * " + ct.getAmountBought() + " / " + buyTotal + " = " + (int) Math.floor((double)ct.getAmountBought() * ((double) shares.size() / (double)buyTotal)));
                    amountForEachPortfolio.put(p, (int) Math.floor((double)ct.getAmountBought() * ((double) shares.size() / (double)buyTotal)));
                }
            }
            if(!amountForEachPortfolio.containsKey(p)) {
                amountForEachPortfolio.put(p, 0);
            }
        }
        ArrayList<Share> leftOverShares = addShares(shares, companyName, new HashMap<>(amountForEachPortfolio));
        int i = 0;
        while(!leftOverShares.isEmpty()) {
            if(i > 100) {
                System.out.println("Still getting stuck here...");
            }
            for (Portfolio p : portfolios) {
                int amount = amountForEachPortfolio.get(p);
                for (ClientTracker ct : clientTrackers) {
                    if(shares.isEmpty()) {
                        break;
                    }
                    if (ct.getClientName().equals(p.getClientName()) && ct.getCompanyName().equals(companyName) && (ct.getAmountBought() - amount) > -1 && leftOverShares.get(0).getSharePrice() < p.getCashHolding()) {
                        Share s = leftOverShares.remove(0);
                        ArrayList<Share> temp = new ArrayList<>();
                        temp.add(s);
                        p.addShares(temp);
                        //System.out.println("Total requested to buy of " + ct.getCompanyName() + " by " + ct.getClientName() + " = " + ct.getAmountBought());
                        updateTrackers(temp, p.getClientName());
                        amount--;
                    }
                }
                amountForEachPortfolio.put(p, amount);
            }
            i++;
        }
    }

    private ArrayList<Share> addShares(ArrayList<Share> shares, String companyName, HashMap<Portfolio, Integer> amountForEachPortfolio) {
        int i = 0;
        while(!shares.isEmpty() & i < portfolios.size()) {
            //System.out.println("Remaining shares = " + shares.size());
            for (Portfolio p : portfolios) {
                //System.out.println(p.getClientName() + "'s turn");
                int amount = amountForEachPortfolio.get(p);
                //System.out.println(p.getClientName() + " has " + amount + " shares to add...");
                for (ClientTracker ct : clientTrackers) {
                    if(shares.isEmpty()) {
                        break;
                    }
                    if (ct.getClientName().equals(p.getClientName()) && ct.getCompanyName().equals(companyName) && amount > 0 && shares.get(0).getSharePrice() < p.getCashHolding()) {
                        i = 0;
                        Share s = shares.remove(0);
                        ArrayList<Share> temp = new ArrayList<>();
                        temp.add(s);
                        p.addShares(temp);
                        //System.out.println("Total put up for sale by " + ct.getClientName() + " of " + ct.getCompanyName() + " = " + ct.getAmountSold() + ", total requested to be bought = " + ct.getAmountBought());
                        updateTrackers(temp, p.getClientName());
                        amount--;
                    } else {
                        i++;
                    }
                }
                amountForEachPortfolio.put(p, amount);
            }
        }
        return shares;
    }
}
