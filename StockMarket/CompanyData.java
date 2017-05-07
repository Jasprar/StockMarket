package StockMarket;

import javafx.beans.property.*;


/**
 * Created by shahs on 27/04/2017.
 */
public class CompanyData {

    private final StringProperty companyName;
    private final StringProperty shareValues;
    private final StringProperty totalShares;
    private final StringProperty netWorth;

    /***
     * Constructor, creates the object
     * @param companyName
     * @param shareValue
     * @param netWorth
     * @param totalShares
     */
    public CompanyData(String companyName, String shareValue, String netWorth, String totalShares) {
        this.companyName = new SimpleStringProperty(companyName);
        this.shareValues = new SimpleStringProperty(shareValue);
        this.netWorth = new SimpleStringProperty(netWorth);
        this.totalShares = new SimpleStringProperty(totalShares);

    }

    /***
     * Sets the company name
     * @param companyName
     */
    public void setCompanyName(String companyName) {
        this.companyName.set(companyName);
    }


    /***
     * Sets the share values belonging to the company
     * @param shareValues
     */
    public void setShareValues(String shareValues) {
        this.shareValues.set(shareValues);
    }


    /***
     * Sets the total shares issued belonging to the company
     * @param totalShares
     */
    public void setTotalShares(String totalShares) {
        this.totalShares.set(totalShares);
    }


    /**
     * Sets the companys netWorth
     * @param netWorth
     */
    public void setNetWorth(String netWorth) {
        this.netWorth.set(netWorth);
    }
}