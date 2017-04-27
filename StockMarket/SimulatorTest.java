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
        simulator = new Simulator();
    }

    @org.junit.jupiter.api.Test
    void runSimulation() {
    }

    @org.junit.jupiter.api.Test
    void getSharePrice() {
    }

    @org.junit.jupiter.api.Test
    void getNetWorth() {
    }

    @org.junit.jupiter.api.Test
    void getTime() {
    }

    @org.junit.jupiter.api.Test
    void getDate() {
    }

    @org.junit.jupiter.api.Test
    void getEvent() {
    }

    @org.junit.jupiter.api.Test
    void getShareIndex() {
    }

    @org.junit.jupiter.api.Test
    void getMarketType() {
    }

    @org.junit.jupiter.api.Test
    void getPortfolios() {
    }

    @org.junit.jupiter.api.Test
    void getCompanyNames() {
    }

    @org.junit.jupiter.api.Test
    void initialiseData() throws IOException {
        Iterator<String> companyNames = simulator.getCompanyNames().iterator();
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