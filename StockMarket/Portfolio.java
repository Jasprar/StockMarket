package StockMarket;

import java.util.ArrayList;

public class Portfolio {
    private String clientName;
    private double cashHolding;
    private ArrayList<Share> shares;
    private boolean sellAll;

    /**
     * Initializes Portfolio
     * @param clientName
     */
    public Portfolio(String clientName) {
        this.clientName = clientName;
        shares = new ArrayList<>();
        sellAll = false;
    }

    /**
     * Gets the client name of the portfolio
     * @return String clientName
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * Adds shares into all portfolios. Only runs at the start to put all shares in
     * @param shares
     */
    //ALSO FOR SETUP - HENCE += TOTALWORTH.
    public void addSharesInit(ArrayList<Share> shares) {
        this.shares.addAll(shares);
        for(Share share : shares) {
        }
    }

    /**
     * Adds shares into a portfolio.
     * @param shares
     */
    public void addShares(ArrayList<Share> shares) {
        //System.out.println(shares.size() + " shares added to " + clientName + ".");
        this.shares.addAll(shares);
        for(Share s : shares) {
            //System.out.println(s.getCompanyName() + " has a share price of " + s.getSharePrice());
            addCashHolding(-s.getSharePrice());
        }
        //System.out.println(clientName + "'s cash now = " + cashHolding);
        if(cashHolding < 0) {
            System.err.println("Houston, We have a problem: " + clientName + ": " + cashHolding);
            System.exit(-1);
        }


    }

    /**
     * Gets the shares of this portfolio
     * @return ArrayList Share shares
     */
    public ArrayList<Share> getShares() {
        return shares;
    }

    /**
     * Gets the total worth (cash holding + the shares the client owns)
     * @return double totalWorth
     */
    public double getTotalWorth() {
        return cashHolding + getSharesTotal();
    }

    /**
     * Sets the cash holding for the client. Used when updating their cash holding after
     * buying/selling shares
     * @param cashHolding
     */
    // MUST ONLY BE USED DURING SETUP, AS MULTIPLIES PARAMETER BY 100 & ADDS TO TOTALWORTH.
    public void setCashHolding(int cashHolding) {
        cashHolding = cashHolding * 100; // cashHolding is in pounds, we wish to store it in pence.
        this.cashHolding = cashHolding;
    }

    /**
     * Gets the cash holding for the client
     * @return double cashHolding
     */
    public double getCashHolding() {
        return cashHolding;
    }

    /**
     * Sets sell all to true. Used when the client wants to leave the stock market and would like
     * to sell all shares
     */
    public void setSellAll() {
        this.sellAll = true;
    }

    /**
     * Deletes all shares of a company. This is used when the company goes bankrupt
     * @param companyName
     */
    // Called when a company's share price reaches 0.
    public void removeAllShares(String companyName) {
        ArrayList<Share> sharesToRemove = new ArrayList<>();
        for(int i = 0; i < shares.size(); i++) {
            if(shares.get(i).getCompanyName().equals(companyName)) {
                sharesToRemove.add(shares.get(i));
                cashHolding -= shares.get(i).getSharePrice();
            }
        }
        shares.removeAll(sharesToRemove);
    }

    /**
     * Gets the Share Object and returns in String format
     * @return String shares toString
     */
    @Override
    public String toString(){
        return shares.toString();
    }

    /**
     * Adds cash holding into a portfolio. Used when shares are sold
     * and adds the share price into his/her cash holding
     * @param sharePrice
     */
    public void addCashHolding(double sharePrice) {
        cashHolding += sharePrice;
    }

    /**
     * Gets the total value of all the shares the client owns
     * @return double total
     */
    public double getSharesTotal() {
        double total = 0;
        for (Share s : shares) {
            total += s.getSharePrice();
        }
        return total;
    }



}