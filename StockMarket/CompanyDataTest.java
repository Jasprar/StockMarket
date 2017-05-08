package StockMarket;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by shahs on 08/05/2017.
 */
class CompanyDataTest {

    CompanyData companyData;
    @Test
    void setCompanyName() {
        CompanyData companyData = new CompanyData("Test Client", "Test Wealth", "Test Cash Holding", "Test Shares");
        companyData.setCompanyName("TEST");
        assertTrue(companyData.getCompanyName().equals("TEST"));
    }

    @Test
    void setShareValues() {
        CompanyData companyData = new CompanyData("Test Client", "Test Wealth", "Test Cash Holding", "Test Shares");
        companyData.setShareValues("TEST");
        assertTrue(companyData.getShareValues().equals("TEST"));
    }

    @Test
    void setTotalShares() {
        CompanyData companyData = new CompanyData("Test Client", "Test Wealth", "Test Cash Holding", "Test Shares");
        companyData.setTotalShares("TEST");
        assertTrue(companyData.getTotalShares().equals("TEST"));
    }

    @Test
    void setNetWorth() {
        CompanyData companyData = new CompanyData("Test Client", "Test Wealth", "Test Cash Holding", "Test Shares");
        companyData.setNetWorth("TEST");
        assertTrue(companyData.getNetWorth().equals("TEST"));
    }

    @Test
    void getCompanyName() {
        CompanyData companyData = new CompanyData("Test Client", "Test Wealth", "Test Cash Holding", "Test Shares");
        companyData.setCompanyName("TEST");
        assertTrue(companyData.getCompanyName().equals("TEST"));
    }

    @Test
    void getShareValues() {
        CompanyData companyData = new CompanyData("Test Client", "Test Wealth", "Test Cash Holding", "Test Shares");
        companyData.setShareValues("TEST");
        assertTrue(companyData.getShareValues().equals("TEST"));
    }

    @Test
    void getTotalShares() {
        CompanyData companyData = new CompanyData("Test Client", "Test Wealth", "Test Cash Holding", "Test Shares");
        companyData.setTotalShares("TEST");
        assertTrue(companyData.getTotalShares().equals("TEST"));
    }

    @Test
    void getNetWorth() {
        CompanyData companyData = new CompanyData("Test Client", "Test Wealth", "Test Cash Holding", "Test Shares");
        companyData.setNetWorth("TEST");
        assertTrue(companyData.getNetWorth().equals("TEST"));
    }
}