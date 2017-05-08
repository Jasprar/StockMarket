package StockMarket;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PortfolioTest {
    Portfolio p;
    ArrayList<Share> shares;
    int numOfShares;
    double totalSharePrices;
    double totalWorth;
    double cashHolding;

   Portfolio portfolio;
    @BeforeEach
    void setUp() {
     p = new Portfolio("Test Client");
     ArrayList<Share> initShares = new ArrayList<>();
     for(int i = 0; i < 100; i++) {
         initShares.add(new Share("Test Company", "Test Commodity", 1000));
     }
     for(int i = 0; i < 50; i++) {
         initShares.add(new Share("Test Company to Remove", "Test Commodity to Remove", 1500));
     }
     p.addSharesInit(initShares);
     p.setCashHolding(1000);
     totalSharePrices = p.getSharesTotal();
     numOfShares = p.getShares().size();
     totalWorth = p.getTotalWorth();
     cashHolding = p.getCashHolding();
     shares  = new ArrayList<>();
     shares.add(new Share("Another Company", "Another Commodity", 500));
    }

    @Test
    void getClientName() {

    }

    @Test
    void addSharesInit() {
        p.addSharesInit(shares);
        assertEquals(p.getShares().size(), numOfShares + 1);
        assertEquals(p.getSharesTotal(), totalSharePrices + 500);
        assertEquals(p.getTotalWorth(), totalWorth + 500);
        assertEquals(p.getCashHolding(), cashHolding); // Init method does not deduct from cash holding.
    }

    @Test
    void addShares() {
        p.addShares(shares); // Also tests addCashHolding.
        assertEquals(p.getShares().size(), numOfShares + 1);
        assertEquals(p.getSharesTotal(), totalSharePrices + 500);
        assertEquals(p.getTotalWorth(), totalWorth); // - 500 from cashHolding, + 500 to totalSharePrices.
        assertEquals(p.getCashHolding(), cashHolding - 500);
    }

    @Test
    void setSellAll() {
        assertFalse(p.getSellAll());
        p.setSellAll();
        assertTrue(p.getSellAll());
    }

    @Test
    void removeAllShares() {
        double totalRemoved = 1500 * 50; // share price * number of shares for the company.
        p.removeAllShares("Test Company to Remove");
        assertEquals(p.getCashHolding(), cashHolding);
        assertEquals(p.getTotalWorth(), totalWorth - totalRemoved);
        assertEquals(p.getSharesTotal(), totalSharePrices - totalRemoved);
        assertEquals(p.getShares().size(), numOfShares - 50);
    }

}