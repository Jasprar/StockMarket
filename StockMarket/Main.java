package StockMarket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws IOException {
       Simulator sim = new Simulator();
       sim.runSimulation();
       /*while(true) {
           System.out.println("Press enter to run one 15 minute cycle.");
           BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
           reader.readLine();
           sim.run15Mins();
       }*/
    }

}