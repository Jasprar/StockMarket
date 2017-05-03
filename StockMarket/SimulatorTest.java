package StockMarket;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import static org.junit.jupiter.api.Assertions.*;

class SimulatorTest {
    Simulator simulator;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        simulator = new Simulator(1);
    }

    @org.junit.jupiter.api.Test
    void runSimulation() {
    }

    @org.junit.jupiter.api.Test
    void getNetWorth() {
        // At initialisation:
        assertEquals(simulator.getNetWorth("Pear Computing"), 32500000);
    }

    @org.junit.jupiter.api.Test
    void getTime() {
        // At initialisation:
        assertEquals(simulator.getTime(), "00:00:00");
    }

    @org.junit.jupiter.api.Test
    void getDate() {
        // At initialisation:
        assertEquals(simulator.getDate(), "01-01-2017");
    }

    @org.junit.jupiter.api.Test
    void getEvent() {
        // At initialisation:
        assertNull(simulator.getEvent());
    }

    @org.junit.jupiter.api.Test
    void getShareIndex() {
        // At initialisation:
        assertEquals(simulator.getShareIndex(), 239.5);
    }

    @org.junit.jupiter.api.Test
    void getMarketType() {
        // At initialisation:
        assertEquals(simulator.getMarketType(), "Stable");
    }

    @org.junit.jupiter.api.Test
    void getSharePrice() throws IOException {
        // At initialisation:
        Iterator<String> companyNames = simulator.getCompanyNames().iterator(); // Tests getCompanyNames.
        int i = 0;
        HashMap<String, Integer> sharePrices = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader("InitialDataV2.csv"));
        String line;
        while((line = br.readLine()) != null) {
            String[] row = line.split(",");
            if(row.length == Simulator.SIZE_DATA && row[0].length() != 0) {
                sharePrices.put(row[0], Integer.parseInt(row[3]));
            }
        }
        while(companyNames.hasNext()) {
            String companyName = companyNames.next();
            assertEquals((int)simulator.getSharePrice(companyName), (int)sharePrices.get(companyName));
            i++;
        }
    }

}
