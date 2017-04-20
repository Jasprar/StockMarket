import java.util.*;

public class Simulator {
    private GregorianCalendar calendar;
    HashMap<Trader, ArrayList<Share>> toBeSold;
    HashMap<Trader, HashMap<String, Integer>> toBeBought; // Inner HashMap maps Company Name to number of shares sought for purchase.
    ArrayList<Trader> traders;
    ArrayList<Event> events;
    int stockIndex; // in pence.
    String marketType; // Bull, Bear, Stable.

    public static void main(String[] args) {
        // TODO: Create main method to start the simulation.
    }

    public void initialiseData() {
        // TODO: import data from initialData.xls and setup fields.
    }

}
