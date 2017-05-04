package StockMarket;

import javafx.beans.property.*;


/**
 * Created by shahs on 27/04/2017.
 */
public class CompanyData {

    private final StringProperty companyName;
    private final SimpleDoubleProperty shareValues;
    private final SimpleDoubleProperty totalShares;
    private final SimpleDoubleProperty netWorth;

    public CompanyData(String companyName, double shareValue, double netWorth, double totalShares) {
        this.companyName = new SimpleStringProperty(companyName);
        this.shareValues = new SimpleDoubleProperty(shareValue);
        this.netWorth = new SimpleDoubleProperty(netWorth);
        this.totalShares = new SimpleDoubleProperty(totalShares);

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

    public double getShareValues(int j) {
        return shareValues.get();
    }

    public DoubleProperty shareValuesProperty() {
        return shareValues;
    }

    public void setShareValues(Double shareValues) {
        this.shareValues.set(shareValues);
    }

    public double getTotalShares() {
        return totalShares.get();
    }

    public DoubleProperty totalSharesProperty() {
        return totalShares;
    }

    public void setTotalShares(int totalShares) {
        this.totalShares.set(totalShares);
    }

    public double getNetWorth() {
        return netWorth.get();
    }

    public DoubleProperty netWorthProperty() {
        return netWorth;
    }

    public void setNetWorth(int netWorth) {
        this.netWorth.set(netWorth);
    }
}