import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Scanner;

/**
 * This class blueprints what a social event at the conference would need.
 * It is a sub-class of the Event class and inherits all the methods and attributes of that class.
 * It also has a few exclusive attributes and methods of its own
 * @author Chris Loftus, Faisal Rezwan and Owain Gibson
 * @version 1 30/03/2022
 */
public class Social extends Event {

    private boolean foodAndDrinkRequired;
    private boolean invitationOnly;

    /**
     * Constructor to create the object
     */
    public Social() {
    }

    /**
     * Constructor to create an instance of the social class
     * @param name holds the name of the social event
     * @param startDateTime holds the start date and time of the social event
     * @param endDateTime holds the end date and time of the social event
     */
    public Social(String name, Calendar startDateTime, Calendar endDateTime) {
        super(name, startDateTime, endDateTime);
    }

    /**
     * Method to check if food and drink is required for the event
     * @return returns the boolean value
     */
    public boolean isFoodAndDrinkRequired() {
        return foodAndDrinkRequired;
    }

    /**
     * Method to change the food and drink requirement for the social event
     * @param foodAndDrinkRequired holds the new decision
     */
    public void setFoodAndDrinkRequired(boolean foodAndDrinkRequired) {
        this.foodAndDrinkRequired = foodAndDrinkRequired;
    }

    /**
     * Method to check if the event is invitation only
     * @return returns the boolean value
     */
    public boolean isInvitationOnly() {
        return invitationOnly;
    }

    /**
     * Method to set the social event to invitation only or not
     * @param invitationOnly hold the new decision
     */
    public void setInvitationOnly(boolean invitationOnly) {
        this.invitationOnly = invitationOnly;
    }

    /**
     * Method to summarise the general characteristics of the event as well as the sub-class-exclusive characteristics as a string to the calling function
     * @return returns the summary
     */
    @Override
    public String toString() {
        return super.toString() + ", foodAndDrinkRequired=" + foodAndDrinkRequired + ", invitationOnly=" + invitationOnly;
    }

    /**
     * Writes out information about the Social event to the file
     *
     * @param outfile An open file
     * @throws IllegalArgumentException if outfile is null
     */
    public void save(PrintWriter outfile) {
        outfile.println("SOCIAL");
        super.save(outfile);

        outfile.println(foodAndDrinkRequired);
        outfile.println(invitationOnly);
    }

    /**
     * Reads in Social event specific information from the file
     * @param infile An open file
     * @throws IllegalArgumentException if infile is null
     */
    public void load(Scanner infile) {
        super.load(infile);

        foodAndDrinkRequired = infile.nextBoolean();
        invitationOnly = infile.nextBoolean();

    }

}
