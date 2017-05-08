package StockMarket;

import javafx.beans.property.*;


/**
 * Used by the GUI and Controller to keep track of one Company (for the table in the 'back end' of the GUI).
 * @Author 132224
 * @Version 04/05/2017
 */
public class CompanyData {

    private final StringProperty companyName;
    private final StringProperty shareValues;
    private final StringProperty totalShares;
    private final StringProperty netWorth;

    /**
     * Constructor, creates the object.
     * @param companyName The String representing the name of the company.
     * @param shareValue The value of the sharePrice (as a String).
     * @param netWorth The net worth of the company (as a String).
     * @param totalShares The total number of shares there are for the company (as a String).
     */
    public CompanyData(String companyName, String shareValue, String netWorth, String totalShares) {
        this.companyName = new SimpleStringProperty(companyName);
        this.shareValues = new SimpleStringProperty(shareValue);
        this.netWorth = new SimpleStringProperty(netWorth);
        this.totalShares = new SimpleStringProperty(totalShares);

    }

    /**
     * Sets the company name to String companyName.
     * @param companyName The new String representing the company name.
     */
    public void setCompanyName(String companyName) {
        this.companyName.set(companyName);
    }


    /**
     * Sets the share price of the company.
     * @param shareValues The String representation of the new share price.
     */
    public void setShareValues(String shareValues) {
        this.shareValues.set(shareValues);
    }


    /**
     * Sets the total shares issued belonging to the company.
     * @param totalShares The (new) total number of shares for this company, as a String.
     */
    public void setTotalShares(String totalShares) {
        this.totalShares.set(totalShares);
    }


    /**
     * Sets the company's net worth.
     * @param netWorth The String representation of the company's new net worth.
     */
    public void setNetWorth(String netWorth) {
        this.netWorth.set(netWorth);
    }

    /**
     * Returns the company name as a String.
     * @return A String representing the company's name.
     */
    public String getCompanyName() {
        return companyName.get();
    }

    /**
     * Returns the company name StringProperty.
     * @return The StringProperty representing the name of the company.
     */
    public StringProperty companyNameProperty() {
        return companyName;
    }

    /**
     * Returns the share price of the company as a String.
     * @return A String representing the share price that this company is traded at.
     */
    public String getShareValues() {
        return shareValues.get();
    }

    /**
     * Returns the shareValues StringProperty.
     * @return The StringProperty representing the price of one share.
     */
    public StringProperty shareValuesProperty() {
        return shareValues;
    }

    /**
     * Returns the total number of shares as a String.
     * @return A String representing the amount of shares there are for this company.
     */
    public String getTotalShares() {
        return totalShares.get();
    }

    /**
     * Returns the total shares StringProperty.
     * @return The StringProperty representing the total number of shares the company has.
     */
    public StringProperty totalSharesProperty() {
        return totalShares;
    }

    /**
     * Returns the net worth of the company as a String.
     * @return A String representing the company's net worth.
     */
    public String getNetWorth() {
        return netWorth.get();
    }

    /**
     * Returns the net worth StringProperty.
     * @return The StringProperty representing the amount of money the company is worth.
     */
    public StringProperty netWorthProperty() {
        return netWorth;
    }


}