package StockMarket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

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
    public abstract HashMap<String,Integer> buy(ArrayList<String> availableCompanies);

    // ArrayList is the Shares the trader wishes to sell - remember to remove them from the portfolios & increment totalWorth!
    public abstract ArrayList<Share> sell();

    public void returnShares(ArrayList<Share> shares, String companyName) {
        // TODO: Add these shares back to their respective portfolios (you know where they came from due to sharesRemoved).
    }

    public void addNewShares(ArrayList<Share> sharesBought) {
        // TODO: Add these new shares into the portfolios (fairly sure you can just evenly divide these up).
        // At some point, you need to call updateTrackers().
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void checkTrackers() {
        for(ClientTracker ct : clientTrackers) {
            if(ct.getAmount() == 0) {
                clientTrackers.remove(ct);
            }
        }
    }

    private void updateTrackers(ArrayList<Share> shares, String clientName) {
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
            }
            if(!found) { // No such ClientTracker exists, create one.
                clientTrackers.add(new ClientTracker(clientName, s.getCompanyName(), s.getCommodity(),s.getSharePrice()));
            }
        }
    }
}
