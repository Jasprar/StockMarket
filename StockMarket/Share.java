package StockMarket;

/**
 * The Share class represents one share (with a share price and company / commodity names) in the stock market. When this
 * share is sold by a trader to another, it is 'physically' moved from one Portfolio to another.
 * @Author 146803
 * @Version 24/04/2017
 */
public class Share {
    private String companyName;
    private String commodity;
    private double sharePrice;

    /**
     * Constructor for the Share class.
     * @param companyName The name of the company that this share is for.
     * @param commodity The commodity (e.g. Hard, Property) that this company is a part of - used for events.
     * @param sharePrice The cost to buy this share on the trading exchange.
     */
    public Share(String companyName, String commodity, double sharePrice) {
        this.companyName = companyName;
        this.commodity = commodity;
        this.sharePrice = sharePrice;
    }

    /**
     * Returns the price of this share.
     * @return The double representing the amount (in pence) it costs to buy this share on the Trading exchange.
     */
    public double getSharePrice() {
        return sharePrice;
    }

    /**
     * Returns the String representation of this Share's company name.
     * @return The String of the name of the company that this share is a member of.
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Sets the share price to this new share price.
     * @param sharePrice The double representing (in pence) the new share price.
     */
    public void setSharePrice(double sharePrice) {
        this.sharePrice = sharePrice;
    }

    /**
     * Returns the name of the commodity that this Share's company is a part of.
     * @return The String representation of the Share's commodity.
     */
    public String getCommodity() {
        return commodity;
    }

    /**
     * Returns a String representation of the Share object.
     * @return A String representation of the share object, showing companyName, commodity and sharePrice.
     */
    @Override
    public String toString() {
        return "Share{" +
                "companyName='" + companyName + '\'' +
                ", commodity='" + commodity + '\'' +
                ", sharePrice=" + sharePrice +
                '}';
    }
}