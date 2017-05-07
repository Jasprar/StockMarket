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

    public CompanyData(String companyName, String shareValue, String netWorth, String totalShares) {
        this.companyName = new SimpleStringProperty(companyName);
        this.shareValues = new SimpleStringProperty(shareValue);
        this.netWorth = new SimpleStringProperty(netWorth);
        this.totalShares = new SimpleStringProperty(totalShares);

    }

    public String getCompanyName() {
        return companyName.get();
    }

    public StringProperty companyNameProperty() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName.set(companyName);
    }

    public String getShareValues(int j) {
        return shareValues.get();
    }

    public StringProperty shareValuesProperty() {
        return shareValues;
    }

    public void setShareValues(String shareValues) {
        this.shareValues.set(shareValues);
    }

    public String getTotalShares() {
        return totalShares.get();
    }

    public StringProperty totalSharesProperty() {
        return totalShares;
    }

    public void setTotalShares(String totalShares) {
        this.totalShares.set(totalShares);
    }

    public String getNetWorth() {
        return netWorth.get();
    }

    public StringProperty netWorthProperty() {
        return netWorth;
    }

    public void setNetWorth(String netWorth) {
        this.netWorth.set(netWorth);
    }
}