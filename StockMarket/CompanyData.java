package StockMarket;

import javafx.beans.property.*;

/**
 * Created by shahs on 27/04/2017.
 */
public class CompanyData {

    private final StringProperty PFCompanyName;
    private final SimpleDoubleProperty PFShareValues;
    private final SimpleDoubleProperty PFTotalShares;
    private final SimpleDoubleProperty PFNetWorth;

    public CompanyData(String CompanyName, int ShareValue, int networth, int TotalShares) {
        this.PFCompanyName = new SimpleStringProperty(CompanyName);
        this.PFShareValues = new SimpleDoubleProperty(ShareValue);
        this.PFTotalShares = new SimpleDoubleProperty(TotalShares);
        this.PFNetWorth = new SimpleDoubleProperty(networth);
    }

    public String getPFCompanyName() {
        return PFCompanyName.get();
    }

    public StringProperty PFCompanyNameProperty() {
        return PFCompanyName;
    }

    public void setPFCompanyName(String PFCompanyName) {
        this.PFCompanyName.set(PFCompanyName);
    }

    public double getPFShareValues(int j) {
        return PFShareValues.get();
    }

    public SimpleDoubleProperty PFShareValuesProperty() {
        return PFShareValues;
    }

    public void setPFShareValues(Double PFShareValues) {
        this.PFShareValues.set(PFShareValues);
    }

    public double getPFTotalShares() {
        return PFTotalShares.get();
    }

    public SimpleDoubleProperty PFTotalSharesProperty() {
        return PFTotalShares;
    }

    public void setPFTotalShares(int PFTotalShares) {
        this.PFTotalShares.set(PFTotalShares);
    }

    public double getPFNetWorth() {
        return PFNetWorth.get();
    }

    public SimpleDoubleProperty PFNetWorthProperty() {
        return PFNetWorth;
    }

    public void setPFNetWorth(int PFNetWorth) {
        this.PFNetWorth.set(PFNetWorth);
    }
}