package StockMarket;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shahs on 27/04/2017.
 */
public class CompanyData {

    private final List<String> PFCompanyName = new ArrayList<>();
    private final List<Integer> PFShareValues = new ArrayList<Integer>();
    private final IntegerProperty PFclosingPence = new SimpleIntegerProperty();


    /*public CompanyData() {
        //   this.PFCompanyName = new SimpleStringProperty(CompanyName);
        this.PFCompanyName = CompanyName;
        this.PFShareValues = new SimpleIntegerProperty(ShareValue);
        this.PFclosingPence = new SimpleIntegerProperty(ClosingPence);
    }
*/


/*
    public String getPFCompanyName() {
        return PFCompanyName.get();
    }

   public StringProperty PFCompanyNameProperty() {
        return PFCompanyName;
    }

    public void setPFCompanyName(String PFCompanyName) {
        this.PFCompanyName.set(PFCompanyName);
    }
*/

    public List<String> getPFCompanyName() {
        return PFCompanyName;
    }

   /* public int getPFShareValues() {
        return PFShareValues.get();
    }

    public IntegerProperty PFShareValuesProperty() {
        return PFShareValues;
    }

    public void setPFShareValues(int PFShareValues) {
        this.PFShareValues.set(PFShareValues);
    }
    */

   public List<Integer> getPFShareValues(){
       return PFShareValues;
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
