import java.io.PrintWriter;
import java.util.*;


/**
 * This class blueprints what a generic event would look like.
 * It implements the comparable interface so that events can be compared to one another for sorting
 * @author Owain
 * @version 1 30/03/2022
 */
public class Event implements Comparable<Event>{
    private Calendar startDateTime;
    private Calendar endDateTime;
    private String name;
    private Venue venue;
    private boolean dataProjectorRequired;

    /**
     * Constructor to create the object
     */
    public Event() {
    }

    /**
     * This is a constructor for creating an instance of the class
     * @param name holds the name of the event
     * @param startDateTime holds the start date and time of the event
     * @param endDateTime holds the end date and time of the event
     * @throws IllegalArgumentException if the startDateTime is the same as or comes after the endDateTime
     */
    public Event(String name, Calendar startDateTime, Calendar endDateTime){
        this.name = name;

        if (!(startDateTime.before(endDateTime))){ //checks that start time is before end time
            throw new IllegalArgumentException("Start date and time for event " + name + "must be BEFORE endDateTime");
        }
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    /**
     * Method to return the name of the event
     * @return returns the name of the event
     */
    public String getName() {
        return name;
    }

    /**
     * Method to change the name of the event
     * @param name holds the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the venue. This will only be allowed
     * if the meets the data projector requirement.
     * Otherwise, throw an exception
     * @throws IllegalArgumentException if requirement not met
     * @param venue The venue for the talk
     */
    public void setVenue(Venue venue) throws IllegalArgumentException{
        if (dataProjectorRequired && !venue.hasDataProjector()) {
            throw new IllegalArgumentException("Event " + name + " requires a data projector. " +
                    "Venue " + venue.getName() + " does not have one");
        } else {
            this.venue = venue;
        }
    }

    /**
     * Method to return the venue that the event is being held in
     * @return returns the venue object
     */
    public Venue getVenue() {
        return venue;
    }

    /**
     * Method to check if the event needs a data projector
     * @return returns the boolean value
     */
    public boolean isDataProjectorRequired() {
        return dataProjectorRequired;
    }

    /**
     * Sets the data projector requirement. This will only be allowed
     * if there is an associated venue that meets the requirement.
     * Otherwise, throw an exception
     * @throws IllegalArgumentException if requirement not met
     * @param dataProjectorRequired Whether required or not
     */
    public void setDataProjectorRequired(boolean dataProjectorRequired) throws IllegalArgumentException{
        if (venue != null && (dataProjectorRequired && !venue.hasDataProjector())) {
            throw new IllegalArgumentException("Event " + name + " currently has a venue " +
                    venue.getName() + " that does not have a data projector. Change the venue first");
        } else {
            this.dataProjectorRequired = dataProjectorRequired;
        }
    }

    /**
     * Method to return the start date and time of the event
     * @return returns the Calendar object
     */
    public Calendar getStartDateTime() {
        return startDateTime;
    }

    /**
     * Method to set the start date time of the event
     * @param startDateTime is the new end date and time to set
     * @throws IllegalArgumentException if the start date and time is before the start date and time
     * NOTE: the method only does the comparison if the other date object isn't null (means that no error is thrown when the event is loaded and the date is initially added)
     */
    public void setStartDateTime(Calendar startDateTime) throws IllegalArgumentException{
        if (endDateTime != null && !startDateTime.before(endDateTime)){
            throw new IllegalArgumentException("Event start time and date should be BEFORE end time and date.");
        }
        this.startDateTime = startDateTime;
    }

    /**
     * Method to return the end date and time of the event
     * @return returns the Calendar object
     */
    public Calendar getEndDateTime() {
        return endDateTime;
    }

    /**
     * Method to set the end date time of the event
     * @param endDateTime is the new end date and time to set
     * @throws IllegalArgumentException if the end date and time is before the start date and time
     * NOTE: the method only does the comparison if the other date object isn't null (means that no error is thrown when the event is loaded and the date is initially added)
     */
    public void setEndDateTime(Calendar endDateTime) throws IllegalArgumentException{
        if (startDateTime != null && !endDateTime.after(startDateTime)){
            throw new IllegalArgumentException("Event end time and date should be AFTER start time and date.");
        }
        this.endDateTime = endDateTime;
    }

    /**
     * This is a method to turn a calendar object into a readable string.
     * It is private as it is only required in this class only.
     * @param dateTime holds the calendar object to be converted
     * @return returns the converted string
     */
    private String dateTimeToString(Calendar dateTime) {

        int year = dateTime.get(Calendar.YEAR);
        int month = dateTime.get(Calendar.MONTH) + 1; // We have to add 1 since months start from 0
        int day = dateTime.get(Calendar.DAY_OF_MONTH);
        int hour = dateTime.get(Calendar.HOUR_OF_DAY);
        int minutes = dateTime.get(Calendar.MINUTE);

        return "" + year + ":" + month + ":" + day + ":" + hour + ":" + minutes;
    }

    /**
     * A basic implementation to just return all the data in string form.
     * CHANGE THIS TO USE StringBuilder
     *
     * @return All the string data for the talk
     */
    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder("startDateTime=" + dateTimeToString(startDateTime) +
                ", endDateTime=" + dateTimeToString(endDateTime) +
                ", name='" + name + '\'' +
                ", venue=" + venue +
                ", dataProjectorRequired=" + dataProjectorRequired);

        return stringBuilder.toString();
    }

    /**
     * Reads in Talk specific information from the file
     * @param infile An open file
     * @throws IllegalArgumentException if infile is null
     */
    public void load(Scanner infile) {
        if (infile == null) {
            throw new IllegalArgumentException("infile must not be null");
        }
        setName(infile.next());
        setStartDateTime(readDateTime(infile));
        setEndDateTime(readDateTime(infile));

        setDataProjectorRequired(infile.nextBoolean());
    }

    /**
     * Method to turn relevant date information from a file into a Calendar object
     * @param scan holds the scanner object that will be used to read the data
     * @return returns the converted calendar object
     */
    private Calendar readDateTime(Scanner scan) {
        Calendar result = Calendar.getInstance();

        int year = scan.nextInt();
        int month = scan.nextInt();
        int day = scan.nextInt();
        int hour = scan.nextInt();
        int minutes = scan.nextInt();

        result.clear();
        result.set(year, month, day, hour, minutes);

        return result;
    }

    /**
     * Writes out information about the Talk to the file
     *
     * @param outfile An open file
     * @throws IllegalArgumentException if outfile is null
     */
    public void save(PrintWriter outfile) {
        if (outfile == null)
            throw new IllegalArgumentException("outfile must not be null");
        outfile.println(getName());
        writeDateTime(outfile, getStartDateTime());
        writeDateTime(outfile, getEndDateTime());

        outfile.println(isDataProjectorRequired());

    }


    /**
     * Method to use a Calendar object and write it into a file to save it
     * @param outfile holds the writer object that will write the data into the file
     * @param dateTime holds the Calendar object that holds the data to be written
     */
    private void writeDateTime(PrintWriter outfile, Calendar dateTime) {
        outfile.println(dateTime.get(Calendar.YEAR));
        outfile.println(dateTime.get(Calendar.MONTH));
        outfile.println(dateTime.get(Calendar.DAY_OF_MONTH));
        outfile.println(dateTime.get(Calendar.HOUR_OF_DAY));
        outfile.println(dateTime.get(Calendar.MINUTE));
    }

    /**
     * Note that this only compares equality based on a
     * talk's name.
     *
     * @param o the other talk to compare against.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;  // Are they the same object?
        if (o == null || getClass() != o.getClass()) return false; // Are they the same class?
        Event event = (Event) o;  // Do the cast to Talk
        // Now just check the names
        return Objects.equals(getName(), event.getName()); // Another way of checking equality. Also checks for nulls
    }

    /**
     * This method is used compare Talk objects to each other
     * @param eventToCompare holds the talk object that the current object is being compared to
     * @return returns the result of the comparison (negative int if less than, 0 if equal to, positive int if greater than)
     */
    @Override
    public int compareTo(Event eventToCompare){
        int result;
        result = this.startDateTime.compareTo(eventToCompare.getStartDateTime());
        return result;
    }

}
