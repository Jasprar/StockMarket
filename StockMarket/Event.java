package StockMarket;

import java.util.Date;
import java.util.GregorianCalendar;

public class Event {
    private Date startDateTime;
    private Date endDateTime;
    private boolean isBuy;
    private String name;
    private String message;

    public Event(Date startDateTime, String message, String action) {
        this.startDateTime = startDateTime;
        this.message = message;
        this.isBuy = action.contains("buy");
        // Set name:
        if(action.contains("Q1Q")) {
            name = "Q1Q Tech";
        } else if(action.contains("food")) {
            name = "Food";
        } else if(action.contains("property")) {
            name = "Property";
        } else if(action.contains("hard")) {
            name = "Hard";
        } else {
            name = "Any";
        }
        // Set endDateTime:
        String[] actions = action.split(" ");
        int days = Integer.parseInt(actions[actions.length - 2]); // Location of number of days.
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(startDateTime);
        gc.add(gc.DATE, days);
        this.endDateTime = gc.getTime();
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    public boolean isBuy() {
        return isBuy;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }
}
