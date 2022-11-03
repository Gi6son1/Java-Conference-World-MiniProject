import java.io.*;
import java.util.*;

/**
 * To model a Conference - a collection of talks
 *
 * @author Chris Loftus, Faisal Rezwan and Owain Gibson
 * @version 3 (26th March 2022)
 */
public class Conference {
    private String name;

    /**
     * These variables are from the List class rather than ArrayList because it allows me to change what type of list
     * they are in case, further down the line, I need to perform functions that ArrayLists may not be able to perform.
     * It's good practice
     */
    private List<Event> events;
    private List<Venue> venues;
    private int numVenues;

    /**
     * Creates a conference, only when constructing the object do I create an ArrayList objects for each variable.
     * Also sets the current number of venues to 0, since it is a new conference with no current venues or events
     */
    public Conference(){
        events = new ArrayList<>();
        venues = new ArrayList<>();
        numVenues = 0;
    }

    /**
     * This method gets the value for the name attribute. The purpose of the
     * attribute is: The name of the Conference e.g. "QCon London 2019"
     *
     * @return String The name of the conference
     */
    public String getName() {
        return name;
    }

    /**
     * This method sets the value for the name attribute. The purpose of the
     * attribute is: The name of the conference e.g. "QCon London 2019"
     *
     * @param name The name of the conference
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Method that enables a user to add a Talk to the Conference
     *
     * @param event A new talk
     */
    public void addEvent(Event event){
        events.add(event);
    }

    /**
     * Method to add a new venue to the conference
     *
     * @param venue Must be a unique name
     */
    public void addVenue(Venue venue){
        venues.add(venue);

        //increments number of venues by 1
        numVenues++;
    }

    /**
     * Enables a user to delete a Talk from the Conference.
     *
     * @param eventName The talk to remove
     * @return returns true or false at depending on if event exists or not
     */
    public boolean removeEvent(String eventName){
        // Search for the event by name
        Event which = null;
        for (Event event: events){
            if (eventName.equals(event.getName())){
                which = event;
                break;
            }
        }
        if (which != null){
            // if it exists, removes event and decrements the number of venues
            events.remove(which); // Requires that Event has an equals method
            System.out.println("removed " + which);
            numVenues--;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns an array of the events in the conference
     * @return An array of the correct size
     */
    public Event[] obtainAllEvents() {

        Event[] result = new Event[events.size()];
        result = events.toArray(result);
        return result;
    }

    /**
     * Searches for and returns the talk, if found
     * @param name The name of the talk
     * @return The talk or else null if not found
     */
    public Event searchForEvent(String name) {

        Event result = null;

        for (Event event: events){
            if (event.getName().equals(name)){
                result = event;
            }
        }
        return result;
    }

    /**
     * Searches for and returns the venue, if found
     * @param name The name of the venue
     * @return The venue or else null if not found
     */
    public Venue searchForVenue(String name) {

        Venue result = null;
        for (Venue v : venues){
            if (v.getName().equals(name)){
                result = v;
            }
        }
        return result;
    }

    /**
     * Method for summarising all the information about conference, in order of start time and date
     * @return String showing the summary, or a message saying there is no data in the conference if it's empty
     */
    public String toString() {

        if (events.isEmpty() && numVenues == 0){
            return "There is no event data in " + name;
        }

        Collections.sort(events);

        StringBuilder stringBuilder = new StringBuilder("Data in Conference " + name + " is: \n");
        for (Venue venue : venues){
            stringBuilder.append(venue);
            stringBuilder.append("\n");
        }
        for (Event event : events) {
            stringBuilder.append(event);
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    /**
     * Reads in Conference information from the file
     *
     * @param filename The file to read from
     * @throws IOException if there is an issue with the input/output stream
     * @throws FileNotFoundException if the filename does not match an existing file
     * @throws IllegalArgumentException if filename is null or empty
     * @throws InputMismatchException if the file has been tampered with in such a way that it is unreadable
     */
    public void load(String filename) throws FileNotFoundException, IOException, InputMismatchException{
        // Using try-with-resource. We will cover this in Seminar 8 and workshop 17, but
        // what it does is to automatically close the file after the try / catch ends.
        // This means we don't have to worry about closing the file.
		if (filename == null || filename.isEmpty()){
		    throw new IllegalArgumentException();
        }

        try (FileReader fr = new FileReader(filename);
             BufferedReader br = new BufferedReader(fr);
             Scanner infile = new Scanner(br)) {

            events.clear();
            venues.clear();
            numVenues = 0;

            // Use the delimiter pattern so that we don't have to clear end of line
            // characters after doing an infile.nextInt or infile.nextBoolean and can use infile.next
            infile.useDelimiter("\r?\n|\r"); // End of line characters on Unix or DOS

            name = infile.next();
            numVenues = infile.nextInt();
            for (int i=0; i<numVenues; i++){
                Venue venue = new Venue();
                venue.load(infile);
                venues.add(venue);
            }

            while (infile.hasNext()) {
                String type = infile.next();
                Event event = null;
                if (type.equals("TALK")){
                    event = new Talk();
                }
                else if(type.equals("SOCIAL")){
                    event = new Social();
                }

                // if the file has been tampered with in such a way that the definition of an event (TALK/SOCIAL) does not match its following contents, the whole event's section is skipped over
                try{
                    event.load(infile);

                    // Read the venue data
                    String venueName = infile.next();
                    Venue theVenue = searchForVenue(venueName);
                    event.setVenue(theVenue);

                    events.add(event);
                } catch (NullPointerException ignore){
                }

            }
        }
    }

    /**
     * Write out Conference information to the outfile
     *
     * @param outfileName The file to write to
     * @throws IOException if there is an issue reading or writing
     * @throws IllegalArgumentException if outfileName is null or empty
     */
    public void save(String outfileName) throws IOException {
        // Again using try-with-resource 
		// so that I don't need to close the file explicitly
		
        try (FileWriter fw = new FileWriter(outfileName);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter outfile = new PrintWriter(bw);) {

            outfile.println(name);

            //prints all the venues first and their information
            outfile.print(numVenues);
            for (Venue venue : venues){
                outfile.println();
                venue.save(outfile);
            }

            for (Event event : events) {
                outfile.println();
                event.save(outfile);

                Venue venue = event.getVenue();
                // Print rather than println so that we don't leave a blank line at the end
                outfile.print(venue.getName());

            }
        }
    }

}
