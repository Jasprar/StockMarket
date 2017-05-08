package StockMarket;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by shahs on 08/05/2017.
 */
class ClientDataTest {

    ClientData clientData;

    @Test
    void setClient() throws NoSuchFieldException, IllegalAccessException {
        ClientData clientData = new ClientData("Test Client", "Test Wealth", "Test Cash Holding", "Test Shares");
        clientData.setClient("TEST");
        assertTrue(clientData.getClient().equals("TEST"));
    }


    @Test
    void getClient() throws NoSuchFieldException, IllegalAccessException {
        ClientData clientData = new ClientData("Test Client", "Test Wealth", "Test Cash Holding", "Test Shares");
        clientData.setClient("TEST");
        assertTrue(clientData.getClient().equals("TEST"));
    }


    @Test
    void getWealth() {
        ClientData clientData = new ClientData("Test Client", "Test Wealth", "Test Cash Holding", "Test Shares");
        clientData.setWealth("TEST");
        assertTrue(clientData.getWealth().equals("TEST"));
    }

    @Test
    void setWealth() {
        ClientData clientData = new ClientData("Test Client", "Test Wealth", "Test Cash Holding", "Test Shares");
        clientData.setWealth("TEST");
        assertTrue(clientData.getWealth().equals("TEST"));
    }

    @Test
    void getCashHolding() {
        ClientData clientData = new ClientData("Test Client", "Test Wealth", "Test Cash Holding", "Test Shares");
        clientData.setCashHolding("TEST");
        assertTrue(clientData.getCashHolding().equals("TEST"));
    }

    @Test
    void setCashHolding() {
        ClientData clientData = new ClientData("Test Client", "Test Wealth", "Test Cash Holding", "Test Shares");
        clientData.setCashHolding("TEST");
        assertTrue(clientData.getCashHolding().equals("TEST"));
    }

    @Test
    void getShares() {
        ClientData clientData = new ClientData("Test Client", "Test Wealth", "Test Cash Holding", "Test Shares");
        clientData.setShares("TEST");
        assertTrue(clientData.getShares().equals("TEST"));
    }

    @Test
    void setShares() {
        ClientData clientData = new ClientData("Test Client", "Test Wealth", "Test Cash Holding", "Test Shares");
        clientData.setShares("TEST");
        assertTrue(clientData.getShares().equals("TEST"));
    }
}