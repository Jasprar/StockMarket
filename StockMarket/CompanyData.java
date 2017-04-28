package StockMarket;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by shahs on 27/04/2017.
 */
public class CompanyData {

    private final StringProperty PFCompanyName;
    private final IntegerProperty PFShareValues;
    private final IntegerProperty PFclosingPence;

    public CompanyData(String CompanyName, int ShareValue, int ClosingPence) {
        this.PFCompanyName = new SimpleStringProperty(CompanyName);
        this.PFShareValues = new SimpleIntegerProperty(ShareValue);
        this.PFclosingPence = new SimpleIntegerProperty(ClosingPence);
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

    public int getPFShareValues(int j) {
        return PFShareValues.get();
    }

    public IntegerProperty PFShareValuesProperty() {
        return PFShareValues;
    }

    public void setPFShareValues(int PFShareValues) {
        this.PFShareValues.set(PFShareValues);
    }

    public int getPFclosingPence() {
        return PFclosingPence.get();
    }

    public IntegerProperty PFclosingPenceProperty() {
        return PFclosingPence;
    }

    public void setPFclosingPence(int PFclosingPence) {
        this.PFclosingPence.set(PFclosingPence);
    }


}