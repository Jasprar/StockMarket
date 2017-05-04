package StockMarket;

import javax.sound.sampled.Port;
import java.lang.reflect.Array;
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

    public void addNewShares(ArrayList<Share> shares) {
        int i = 0;
        for(Share s : shares) {
            ArrayList<Share> temp = new ArrayList<>();
            temp.add(s);
            portfolios.get(i).addShares(temp);
            updateTrackers(temp, portfolios.get(i).getClientName());
            i = (i + 1) % portfolios.size();
        }
    }

    public void returnShares(ArrayList<Share> shares, String companyName, int sellTotal) {
        for(Portfolio p : portfolios) {
            ArrayList<Share> sharesReturned = new ArrayList<>();
            for(ClientTracker ct : clientTrackers) {
                if(ct.getClientName().equals(p.getClientName()) && ct.getCompanyName().equals(companyName)) {
                    int amount = (int) Math.floor((double) shares.size() * ((double)ct.getAmountSold() / (double)sellTotal));
                    sharesReturned.addAll(shares.subList(0, amount));
                }
            }
            p.addShares(sharesReturned);
            shares.removeAll(sharesReturned);
            updateTrackers(sharesReturned, p.getClientName());
        }
        if(!shares.isEmpty()) {
            for(Share s : shares) {
                Portfolio p = portfolios.get(new Random().nextInt(portfolios.size()));
                ArrayList<Share> temp = new ArrayList<>();
                temp.add(s);
                p.addShares(temp);
                updateTrackers(temp, p.getClientName());
            }
        }
    }


    public void setEvent(String event) {
        this.event = event;
    }

    public void checkTrackers() {
        ArrayList<ClientTracker> trackers = new ArrayList<>();
        for(int i = 0; i < clientTrackers.size(); i++) {
            ClientTracker ct = clientTrackers.get(i);
            if (ct.getAmount() == 0) {
                trackers.add(clientTrackers.get(i));
            } else {
                ct.resetAmountSold();
            }
        }
        clientTrackers.removeAll(trackers);
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
                System.out.println("Creating a new ClientTracker for " + clientName + " tracking " + s.getCompanyName());
                clientTrackers.add(new ClientTracker(clientName, s.getCompanyName(), s.getCommodity(),s.getSharePrice()));
            }
            j++;
        }
    }

    public int getShares() {
        int total = 0;
        for(Portfolio p : portfolios) {
            total += p.getShares().size();
        }
        return total;
    }

}
