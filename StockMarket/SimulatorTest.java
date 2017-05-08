package StockMarket;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.omg.CORBA.portable.UnknownException;

import javax.sound.sampled.Port;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SimulatorTest {
    Simulator simulator;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        simulator = new Simulator();
    }


    @Test
    void runSimulation() {
    }

    @Test
    void getNetWorth() {
        // At initialisation:
        assertEquals(simulator.getNetWorth("Pear Computing"), 32500000);
    }

    @Test
    void getTime() {
        // At initialisation:
        assertEquals(simulator.getTime(), "09:00:00");
    }

    @Test
    void getDate() {
        // At initialisation:
        assertEquals(simulator.getDate(), "02-01-2017");
    }

    @Test
    void getEvent() {
        // At initialisation:
        assertNull(simulator.getEvent());
    }

    @Test
    void getShareIndex() {
        // At initialisation:
        assertEquals(simulator.getShareIndex(), 239.5);
    }

    @Test
    void getMarketType() {
        // At initialisation:
        assertEquals(simulator.getMarketType(), "Stable");
    }

    @Test
    void getWealth() {
        //Initialisation:
        try {
            ArrayList<Double> wealth = new ArrayList<>();
            wealth.add(2.7578586E7);
            wealth.add(2.1968611E7);
            wealth.add(2.3219572E7);
            wealth.add(3.0846637E7);
            wealth.add(2.0290926E7);
            wealth.add(2.4105229E7);
            wealth.add(2.1926594E7);
            wealth.add(2.6121013E7);
            wealth.add(3.1583543E7);
            wealth.add(3.3436761E7);
            assertEquals(simulator.getTotalWorth(), wealth); //Should not exist
            System.out.println(simulator.getTotalWorth());
        } catch (IllegalArgumentException e) {
            throw (e);
        }

    }

    @Test
    void getIncorrectWealth() {
        //Initialisation:
        try {
            ArrayList<Double> wealth = new ArrayList<>();
            wealth.add(2.7578586E7);
            wealth.add(2.1968611E7);
            wealth.add(2.3219572E7);
            wealth.add(3.0846637E7);
            wealth.add(2.0290926E7);
            wealth.add(2.4105229E7);
            wealth.add(2.1);
            wealth.add(2.6121013E7);
            wealth.add(3.1583543E7);
            wealth.add(3.3436761E7);
            assertNotSame(simulator.getTotalWorth(), wealth, "Wealth does not match - Correct"); //Should not exist
        } catch (IllegalArgumentException e) {
            throw (e);
        }

    }


    @Test
    void getSharePrice() throws IOException {
        // At initialisation:
        Iterator<String> companyNames = simulator.getCompanyNames().iterator(); // Tests getCompanyNames.
        int i = 0;
        HashMap<String, Integer> sharePrices = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader("InitialDataV2.csv"));
        String line;
        while ((line = br.readLine()) != null) {
            String[] row = line.split(",");
            if (row.length == Simulator.SIZE_DATA && row[0].length() != 0) {
                sharePrices.put(row[0], Integer.parseInt(row[3]));
            }
        }
        while (companyNames.hasNext()) {
            String companyName = companyNames.next();
            assertEquals((int) simulator.getSharePrice(companyName), (int) sharePrices.get(companyName));
            i++;
        }
    }


    @Test
    void getTotalNetworth() {
        List<String> companyNames = new ArrayList<>();
        Set<String> companyNames1 = simulator.getCompanyNames();
        companyNames.addAll(companyNames1);

        for (String s : companyNames) {
            double x = simulator.getNetWorth(s);
            assertEquals(simulator.getNetWorth(s), x);

        }
    }

    @Test
    void getIncorrectTotalNetWorth() {
        List<String> companyNames = new ArrayList<>();
        Set<String> companyNames1 = simulator.getCompanyNames();
        companyNames.addAll(companyNames1);

        for (String s : companyNames) {
            double x = simulator.getNetWorth(s) + 1;
            assertNotSame(simulator.getNetWorth(s), x, "Wealth does not match with false data ");

        }

    }

    @Test
    void clientLeavesSimulation() {
        ArrayList clientNames = new ArrayList<String>();
        clientNames.addAll(simulator.getClientNames());
        for (Object s : clientNames) {
            Portfolio p = new Portfolio((String) s.toString());
            simulator.leaveSimulation((String) s.toString()); //set the sell all method to true in the clients portfolio

            //Sell the clients shares ..assert true.
        }


    }

    @Test
    void getCashHolding() {
        List<Double> cashHolding = new ArrayList<>();
        cashHolding.addAll(simulator.getCashHolding());

        assertSame(cashHolding, 100000);
    }

}