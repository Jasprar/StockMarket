package StockMarket;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Represents an event that will occur in the simulation at some date & time, including all the information required
 * to know when this event should begin/end (and what the event actually does, e.g. makes traders BUY/SELL a particular stock).
 * @Author 146803
 * @Version 22/04/2017
 */
public class Event {
    private Date startDateTime;
    private Date endDateTime;
    private boolean isBuy;
    private String name;
    private String message;

    /**
     * Instantiates a new Event to occur at a specific startDateTime.
     * @param startDateTime The time at which this event should begin affecting the traders' behaviour.
     * @param message The message to be displayed to the user when the event is occurring.
     * @param action The message that is parsed by the Event to determine what this event does (e.g. buy/sell, which
     *               company/commodity, endDateTime).
     */
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
        if(gc.get(gc.DAY_OF_WEEK) == gc.SUNDAY) {
            gc.add(gc.DATE, 1);
        }
        this.endDateTime = gc.getTime();
    }

    /**
     * Returns the time & date at which this event will begin.
     * @return The Date object representing the start date & time of the event.
     */
    public Date getStartDateTime() {
        return startDateTime;
    }

    /**
     * Returns the time & date at which this event will stop occurring.
     * @return The Date object representing the end date & time of the event.
     */
    public Date getEndDateTime() {
        return endDateTime;
    }

    /**
     * Returns a boolean representing whether this event affects the buying of traders.
     * @return true if the event affects the buying of shares, false if it affects selling (otherwise).
     */
    public boolean isBuy() {
        return isBuy;
    }

    /**
     * Returns the name of the company (e.g. "Q1Q Tech") or commodity (e.g. "Property") that this event affects.
     * @return A String representation of the company or commodity affected by this Event.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the message to be displayed, describing what has caused the traders to change their buying/selling
     * approach temporarily.
     * @return The message to be displayed on-screen when the event occurs.
     */
    public String getMessage() {
        return message;
    }
}
