import java.io.PrintWriter;
import java.util.Objects;
import java.util.Scanner;

/**
 * Class to blueprint what a venue would contain and how it should be interacted with
 * @author Chris Loftus, Faisal Rezwan and Owain Gibson
 * @version 3 30/03/2022
 */
public class Venue {
    private String name;
    private boolean hasDataProjector;

    /**
     * Constructor to create the object
     */
    public Venue(){}

    /**
     * Constructor to create a venue object
     * @param name holds the name of the venue
     */
    public Venue(String name){
        this.name = name;
    }

    /**
     * Method to return the name of the venue to the calling function
     * @return returns the name
     */
    public String getName() {
        return name;
    }

    /**
     * Method to change the name of a venue
     * @param name holds the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Method to check if the venue has a data projector
     * @return returns the boolean value
     */
    public boolean hasDataProjector() {
        return hasDataProjector;
    }

    /**
     * Method to change the venue to having/not having a data projector
     * @param hasDataProjector holds the new boolean
     */
    public void setHasDataProjector(boolean hasDataProjector) {
        this.hasDataProjector = hasDataProjector;
    }

    /**
     * Method for checking if the venue is the same as the one passed as a parameter.
     * It checks if there even is an object to compare and if there is, if it's the same class first. Then checks based off of the name
     * @param o holds the object to check equality with
     * @return returns the result of the method (true if the same, false if different)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Venue location = (Venue) o;
        return Objects.equals(name, location.name);
    }

    /**
     * Method to summarise the characteristics of the object and return them as a string to the calling function
     * @return returns the summary
     */
    @Override
    public String toString() {
        return "Venue{" +
                "name='" + name + '\'' +
                ", hasDataProjector=" + hasDataProjector +
                '}';
    }

    /**
     * Method to save the venue information to the file
     * @param outfile holds the object used to write the info
     */
    public void save(PrintWriter outfile) {
        if (outfile == null)
            throw new IllegalArgumentException("outfile must not be null");
        outfile.println(getName());
        outfile.print(hasDataProjector());
    }

    /**
     * Method to load data to the venue object
     * @param infile holds the scanner that will scan the info
     */
    public void load(Scanner infile) {
        if (infile == null) {
            throw new IllegalArgumentException("infile must not be null");
        }
        setName(infile.next());
        setHasDataProjector(infile.nextBoolean());
    }
}
