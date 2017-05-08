package StockMarket;

/**
 * The ClientTracker class keeps track of one Client-Company pairing - how many shares of this company the client owns,
 * how many the client has requested this cycle, how many the client has put up for sale this cycle, the most recent buy
 * price of the share, the original price the share cost when they first bought it, and the amount of fluctuation there
 * has been (has the share price increased/decreased in the last few times the client has purchased it?).
 * @Author 164875 and 146803
 * @Version 05/05/2017
 */
public class ClientTracker {
    private double originalPrice;
    private String clientName;
    private String companyName;
    private String commodityType;
    private int amount;
    private int amountSold;
    private int amountBought;
    private double buyPrice; // Most recently bought
    private double fluctuation; // Negative if buyPrice dropping since bought, positive otherwise.

    /**
     * Initialises the ClientTracker (at the beginning of the Simulation). Initially, the client owns no shares (this is
     * before any are added), and so the original/most recent buy prices and amount of shares owned are all initialized
     * to 0 (or less).
     * @param clientName The name of the client (from Portfolio).
     * @param companyName The name of the company (from Share).
     * @param commodityType The name of the commodity (from Share).
     */
    public ClientTracker(String clientName, String companyName, String commodityType) {
        this.clientName = clientName;
        this.companyName = companyName;
        this.commodityType = commodityType;
        amount = 0;
        amountSold = 0;
        amountBought = 0;
        this.buyPrice = -1;
        this.originalPrice = -1;
        fluctuation = 0;
    }

    /**
     * Returns the amount of fluctuation there has been in this share price while this client has been buying it.
     * @return The double representing the amount of fluctuation there has been.
     */
    public double getFluctuation() { return fluctuation; }

    /**
     * Returns the number of shares of this company the client owns.
     * @return The number of shares this client owns of this company.
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Returns the name of the client.
     * @return The String representation of the client's name.
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * Returns the name of the company.
     * @return The String representation of the company's name.
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Returns the name of the commodity type that this company belongs to.
     * @return The String representation of the commodity's name.
     */
    public String getCommodityType() { return commodityType; }

    /**
     * Decrements the amount of shares owned of this type by the client by one, and (as this only occurs when a client is
     * selling a share of this type) increments the amount sold this cycle.
     */
    public void decrementAmount() {
        this.amount--;
        amountSold++;
    }

    /**
     * Returns the most recent price that the client bought this share type at.
     * @return The double representing the price (in pence) of the Share at the last time the client had one added to their
     * Portfolio.
     */
    public double getBuyPrice() {
        return buyPrice;
    }

    /**
     * Sets the buy price to the new buy price (as the client has purchased a new share) and updates the fluctuation to
     * match: fluctuation += (buyPrice - oldPrice).
     * @param buyPrice The new price of the share at the time of most recent purchase.
     */
    public void setBuyPrice(double buyPrice) {
        double oldPrice = this.buyPrice;
        this.buyPrice = buyPrice;
        if(oldPrice == -1) {
            originalPrice = buyPrice;
        } else {
            fluctuation += (buyPrice - oldPrice);
        }
    }

    /**
     * Returns the amount sold by the client this cycle.
     * @return The number of shares of this type the client's trader put up for sale from this client's Portfolio.
     */
    public int getAmountSold() { return amountSold; }

    /**
     * Resets the amount sold this cycle to zero, so the values are correct for the next cycle.
     */
    public void resetAmountSold() { amountSold = 0; }

    /**
     * Increments the amount of shares of this type kept in this client's portfolio by one. Called when a share is added
     * to this client' Portfolio.
     */
    public void incrementAmount() { amount++; }

    /**
     * Resets the amount bought to zero ready for the next cycle to begin.
     */
    public void resetAmountBought() {
        //System.out.println("Resetting amount bought for " + clientName + "...");
        amountBought = 0;
    }

    /**
     * Increments the amount requested to be bought of this company by one. Called by the buy() method in the Trader class
     * if one share of this type is requested.
     */
    public void incrementAmountBought() {
        amountBought++;
    }

    /**
     * Adds this amount to the amount of shares requested to be bought this cycle. Called by buy() whenever there are
     * multiple shares of the same type requested.
     * @param amountBought The number of shares requested to be bought.
     */
    public void addAmountBought(int amountBought) {
        this.amountBought += amountBought;
    }

    /**
     * Returns the number of shares requested to be bought this cycle.
     * @return The number of shares that were requested by this client's trader for this client's Portfolio of this
     * company.
     */
    public int getAmountBought() {
        //System.out.println(clientName + " has bought " + amountBought + " shares of " + companyName);
        return amountBought;
    }

    /**
     * Returns the price that the client first bought this share at.
     * @return The price (in pence) of the share of this type when a share of this company was first purchased.
     */
    public double getOriginalPrice() {
        return originalPrice;
    }
}