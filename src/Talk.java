import java.io.PrintWriter;
import java.util.*;

/**
 * This class blueprints what a talk at the conference would need.
 * It is a sub-class of the Event class and inherits all the methods and attributes of that class.
 * It also has a few exclusive attributes and methods of its own
 * @author Owain
 * @version 3 30/03/2022
 */
public class Talk extends Event{

    /**
     * This variable is from the List class rather than ArrayList because it allows me to change what type of list
     * it is in case, further down the line, I need to perform functions that ArrayLists may not be able to perform.
     * It's good practice
     */
    private List<Speaker> speakers = new ArrayList<>();

    /**
     * Constructor to create the object
     */
    public Talk(){
    }

    /**
     * Constructor for Talk
     * @param name The talk title
     * @param startDateTime When it starts
     * @param endDateTime When it ends
     */
    public Talk(String name, Calendar startDateTime, Calendar endDateTime) {
        super(name, startDateTime, endDateTime);
    }

    /**
     * Method to set the speakers for the talk
     * @param speakers holds the new list of speakers to add
     */
    public final void setSpeakers(List<Speaker> speakers) {
        // We make a true copy of the speakers ArrayList to make sure that we
        // don't break encapsulation: i.e. don't share object references with
        // other code
        if (speakers == null){
            throw new IllegalArgumentException("speakers must not be null");
        }
        this.speakers.clear();
        for (Speaker s : speakers) {
            Speaker copy = new Speaker(s.getName(), s.getPhone());
            this.speakers.add(copy);
        }
    }

    /**
     * Returns a copy of the speakers
     * @return A copy of the speakers as an array
     */
    public Speaker[] getSpeakers(){
        Speaker[] result = new Speaker[speakers.size()];
        result = speakers.toArray(result);
        return result;
    }

    /**
     * A basic implementation to just return all the data in string form.
     * CHANGE THIS TO USE StringBuffer
     * @return All the string data for the talk
     */
    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder(super.toString() + ", speakers=");

        for(Speaker speaker: speakers){
            stringBuilder.append(speaker);
        }
        return stringBuilder.toString();
    }


    /**
     * Reads in Talk specific information from the file
     * @param infile An open file
     * @throws IllegalArgumentException if infile is null
     */
    public void load(Scanner infile) {
        super.load(infile);

        int numSpeakers = infile.nextInt();
        Speaker speaker;
        speakers.clear();
        for (int i=0; i<numSpeakers; i++){
            String speakerName = infile.next();
            String phone = infile.next();
            speaker = new Speaker(speakerName, phone);
            speakers.add(speaker);
        }
    }

    /**
     * Writes out information about the Talk to the file
     * @param outfile An open file
     * @throws IllegalArgumentException if outfile is null
     */
    public void save(PrintWriter outfile) {
        outfile.println("TALK");
        super.save(outfile);

        outfile.println(speakers.size());
        for (Speaker speaker: speakers){
            outfile.println(speaker.getName());
            outfile.println(speaker.getPhone());
        }
    }

}
