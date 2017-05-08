package StockMarket;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShareTest {

    Share share;


    @Test
    void testSetAndGetSharePrice() {
        Share share = new Share("Test Company Name", "Test Commodity", (double)1.23);
        share.setSharePrice((double)1.23);
        assertTrue(share.getSharePrice() == (double)1.23);
    }
    @Test
    void testSetAndGetCompanyName() {
        Share share = new Share("Test Company Name", "Test Commodity", (double)1.23);
        share.setCompanyName("Test");
        assertTrue(share.getCompanyName().equals("Test"));
    }
    @Test
    void testSetAndGetCommodity() {
        Share share = new Share("Test Company Name", "Test Commodity", (double)1.23);
        share.setCommodity("Test");
        assertTrue(share.getCommodity().equals("Test"));
    }
}

