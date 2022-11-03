import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * The main application class for the Conference. Has a command line menu.
 *
 * @author Chris Loftus, Faisal Rezwan and Owain Gibson
 * @version 3 (26th March 2022)
 */

public class ConferenceApp {

    private String filename;
    private Scanner scan;
    private Conference conference;

    /**
     * Notice how we can make this private, since we only call from main which
     * is in this class. We don't want this class to be used by any other class.
     */
    private ConferenceApp() {
        scan = new Scanner(System.in);
        System.out.print("Please enter the filename of conference information: ");
        filename = scan.nextLine();

        conference = new Conference();
    }

    /**
     * initialise() method runs from the main and reads from a file
     *
     * @throws IllegalArgumentException if the filename is null or empty and the file needs to be created again
     */
    private void initialise() throws IllegalArgumentException {
        System.out.println("Using file " + filename);

        try {
            conference.load(filename);
        } catch (FileNotFoundException e) {
            System.err.println("The file: " + " does not exist. Assuming first use and an empty file." +
                    " If this is not the first use then have you accidentally deleted the file?");

        } catch (IOException e) {
            System.err.println("An unexpected error occurred when trying to open the file " + filename);
            System.err.println(e.getMessage());

        } catch (InputMismatchException e) {
            System.err.println("The file seems to have been corrupted. Opening a copy of the file now");
            try {
                conference.load("copy" + filename);

            } catch (Exception anyException) {
                System.err.println("Copy file could not be loaded, resetting file");
                //since the file cannot be recovered, it is reset
                conference = new Conference();
            }
        }

    }

    /*
     * runMenu() method runs from the main and allows entry of data etc
     */

    /**
     * Method for running the menu for the user. It is private so that it cannot be called by mistake from other classes.
     * It gives the user the option to add an event, change the name of the conference, search for an event, remove an event,
     * add a venue, and print all the events in the conference, and also update general info about existing events (FLAIR)
     * It is private because it is only needed in THIS class and other classes won't call it by accident
     */
    private void runMenu() {
        String response;

        if (conference.getName() == null) { //lets the user name the conference if it doesn't already have one
            changeConferenceName();
        }
        do {
            printMenu();
            System.out.println("What would you like to do:");
            response = readOptionInput("1", "2", "3", "4", "5", "6", "7", "Q");
            switch (response) {
                case "1":
                    addEvent();
                    break;
                case "2":
                    changeConferenceName();
                    break;
                case "3":
                    searchForEvent();
                    break;
                case "4":
                    removeEvent();
                    break;
                case "5":
                    addVenue();
                    break;
                case "6":
                    printAll();
                    break;
                case "7":
                    updateExistingEvents();
                    break;
                case "Q":
                    break;
            }
        } while (!(response.equals("Q")));
    }

    /**
     * Method to add an event, it gives the user a choice between a talk or a social, or to go back to the main menu.
     * It is private because it is only needed in THIS class and other classes won't call it by accident
     */
    private void addEvent() {
        String response;
        System.out.println("What would you like to add?:");
        printEventOptions();
        response = readOptionInput("1", "2", "B");
        switch (response) {
            case "1":
                addTalk();
                break;

            case "2":
                addSocial();
                break;

            case "B":
                break;
        }
    }

    /**
     * This is a function to output the types of events that the user can apply options to.
     * It is private because it is only needed in THIS class and other classes won't call it by accident
     */
    private void printEventOptions() {
        System.out.println("1 -  a Talk");
        System.out.println("2 -  a Social Event");
        System.out.println("b -  I changed my mind, go back to the main menu");
    }

    /**
     * Method to print the main menu options to the user
     * It is private because it is only needed in THIS class and other classes won't call it by accident
     */
    private void printMenu() {
        System.out.println("1 -  add a new Event");
        System.out.println("2 -  change conference name ");
        System.out.println("3 -  search for an event");
        System.out.println("4 -  remove an event");
        System.out.println("5 -  add an venue");
        System.out.println("6 -  display everything");
        System.out.println("7 -  update general details about existing events");
        System.out.println("q -  Quit");
    }

    /**
     * Method for letting the user specify what speakers there will be for the talk.
     * It then passes that sub-class-specific information to the populateAndAddToConference method to fill in the general event info
     * It is private because it is only needed in THIS class and other classes won't call it by accident
     */
    private void addTalk() {
        System.out.println("Get speakers: ");
        ArrayList<Speaker> speakers = getSpeakers();

        Talk talk = new Talk();
        talk.setSpeakers(speakers);

        populateAndAddToConference(talk);
    }

    /**
     * Method for letting the user specify what speakers there will be for the talk.
     * It then passes that sub-class-specific information to the populateAndAddToConference method to fill in the general event info
     * It is private because it is only needed in THIS class and other classes won't call it by accident
     */
    private void addSocial() {
        System.out.println();
        boolean foodDrinkRequired = askFoodDrink();
        boolean inviteRequired = askInviteOnly();

        Social social = new Social();
        social.setFoodAndDrinkRequired(foodDrinkRequired);
        social.setInvitationOnly(inviteRequired);

        populateAndAddToConference(social);
    }

    /**
     * Adds event general data. This is common to all events. Then adds to the conference.
     * Also, checks if the dates entered are correct before adding them to the event
     * It is private because it is only needed in THIS class and other classes won't call it by accident
     */
    private void populateAndAddToConference(Event event) {
        System.out.println("Event name: ");
        String name = inputUserNaming();

        Calendar startDateTime;
        Calendar endDateTime;
        boolean badTime = true;
        do
        {                             //checking that the dates are put in the correct order (start dates/times come before end dates/times)
            System.out.println("Enter start time for event");
            startDateTime = getDateTime();
            System.out.println("Enter end time for event");
            endDateTime = getDateTime();
            if (startDateTime.before(endDateTime)) {
                badTime = false;
            } else {
                System.out.println("Start date and time must be BEFORE the end date and time");
            }
        } while (badTime);

        Venue venue;
        String answer;
        do { //reading general info about the event
            System.out.println("Is a data projector required?(Y/N)");
            answer = readOptionInput("Y", "N");

            boolean projectorRequired = answer.equals("Y");

            System.out.println("Enter venue name");
            String venueName = inputUserNaming();
            answer = "N";
            venue = conference.searchForVenue(venueName);
            if (venue != null) {
                try {
                    event.setName(name);
                    event.setDataProjectorRequired(projectorRequired);
                    event.setStartDateTime(startDateTime);
                    event.setEndDateTime(endDateTime);
                    event.setVenue(venue);
                    conference.addEvent(event);
                } catch (IllegalArgumentException e) { //comes from the setDataProjectRequired method only since dates have been checked prior
                    System.out.println("Selected venue does not have a data projector. Choose a different venue");
                    answer = "Y";
                }
            } else {
                System.out.println("Venue " + venue + " does not exist. Try a different venue? (Y/N)");
                answer = readOptionInput("Y", "N");
            }
        } while (answer.equals("Y"));
    }

    /**
     * FLAIR METHOD
     *
     * Method to provide a menu showing all the events in the conference file (uses obtainAllEvents()).
     * It then lists all the events and the user can then choose an event to perform an update on.
     * If there are no events in the conference, this is conveyed to the user, and they are returned to the main menu
     */
    private void updateExistingEvents() {
        System.out.println("What event would you like to update:");
        Event[] events = conference.obtainAllEvents();
        int numEvents = events.length;
        String input;


        //array to hold all the events that the user can choose to update. The last option holds the 'back' option, so it is one larger than the number of events
        String[] optionChoices = new String[numEvents + 1];
        optionChoices[numEvents] = "B";

        if (numEvents == 0) {
            System.out.println("There are no events in the conference to update");
            System.out.println();
            return;
        }

        for (int i = 0; i < numEvents; i++) { //adds the names of the events to the options string, so that it can be passed to the option checker function
            System.out.println(i + 1 + " -  " + events[i].getName());
            optionChoices[i] = String.valueOf(i + 1);
        }
        System.out.println("b -  I changed my mind, go back to the main menu");

        input = readOptionInput(optionChoices);

        if (input.equals("B")) { //return to main menu if user chooses to
            return;
        }

        Event chosenEvent = events[Integer.parseInt(input) - 1];
        performUpdate(chosenEvent);
    }

    /**
     * FLAIR METHOD
     *
     * Method to let the user perform a general info update on a given event
     * @param event holds the event to update
     */
    private void performUpdate(Event event) {
        String answer;
        System.out.println("What part of " + event.getName() + " would you like to update:");
        printUpdateOptions();
        answer = readOptionInput("1", "2", "3", "4", "5");
        try {
            switch (answer) {
                case "1":
                    System.out.println("Enter new name:");
                    answer = inputUserNaming();
                    event.setName(answer);
                    break;
                case "2":
                    System.out.println("Enter start date and time of venue:");
                    Calendar newStartTime = getDateTime();
                    event.setStartDateTime(newStartTime);
                    break;
                case "3":
                    System.out.println("Enter end date and time of venue:");
                    Calendar newEndTime = getDateTime();
                    event.setEndDateTime(newEndTime);
                    break;
                case "4":
                    System.out.println("Enter the name of the venue:");
                    String venueName = inputUserNaming();
                    Venue venue = conference.searchForVenue(venueName);
                    event.setVenue(venue);
                    break;
                case "5":
                    System.out.println("Is a data projector required for this event(Y/N)");
                    answer = readOptionInput("Y", "N");
                    event.setDataProjectorRequired(answer.equals("Y"));
                    break;
            }
        } catch (IllegalArgumentException e) { //if there is an error thrown from any of the updates, the message is shown to the user and they are returned to the main menu
            System.out.println(e.getMessage());
        }


    }

    /**
     * FLAIR METHOD
     *
     * Method to print the all the update options for an event
     */
    private void printUpdateOptions() {
        System.out.println("1 -  update name");
        System.out.println("2 -  update start date and time");
        System.out.println("3 -  update end date and time");
        System.out.println("4 -  update venue");
        System.out.println("5 -  update isDataProjectorRequired");
    }

    /**
     * FLAIR METHOD
     *
     * This is a method for checking the user's input when selecting options that require a specific string input.
     * Since the function repeats until a valid input is received, it means the calling method is GUARANTEED a valid input to use, and does not require its own error checking.
     *
     * @param acceptableValues holds the list of acceptable values to be entered
     * @return returns the accepted input from the user
     */
    private String readOptionInput(String... acceptableValues) {
        String input;
        boolean badInput = true;
        do {
            input = scan.nextLine().toUpperCase();
            for (String value : acceptableValues) {
                if (input.equals(value)) {
                    badInput = false;
                    break;
                }
            }
            if (badInput) {
                System.out.println("Invalid answer, please try again");
            }

        } while (badInput);
        return input;
    }

    /**
     * FLAIR METHOD
     *
     * This is a method for checking the user's input when inputting strings
     * It's used so that the user cannot enter a string that, when stored in a conference file, interferes with the program's load function and causes errors
     *
     * @return returns the accepted string from the user
     */
    private String inputUserNaming() {
        String input = null;
        boolean badName = true;
        while (badName) {
            input = scan.nextLine();
            if (input.equals("TALK") || input.equals("SOCIAL")) {
                System.out.println("Unfortunately, the your input holds a string that has been reserved for the program's use only. Please re-input with a different string:");
            } else {
                badName = false;
            }
        }
        return input;
    }


    /**
     * Method for allowing the user to enter a date and then returns the date as a calendar object to the calling method
     * Also checks to make sure the date has been entered in the correct format (i.e. numbers)
     *
     * @return returns the date as a calendar object
     * It is private because it is only needed in THIS class and other classes won't call it by accident
     */
    private Calendar getDateTime() {
        Calendar result = Calendar.getInstance();
        int year = 0, month = 0, day = 0, hour = 0, minutes = 0;
        boolean badDate = true;
        do {
            System.out.println("On one line (numbers): year month day hour minutes");

            // Note that an ArrayIndexOutOfBoundsException is thrown if an
            // illegal value is entered. For simplicity, we will pretend that won't happen.

            try {
                year = scan.nextInt();
                // Note that months start from 0, so we have to subtract 1
                // when reading and then add 1 when displaying the result
                month = scan.nextInt() - 1;
                day = scan.nextInt();
                hour = scan.nextInt();
                minutes = scan.nextInt();

                badDate = false;
            } catch (InputMismatchException e) {
                System.err.println("Data entered for the times MUST be in number format ONLY");
            } finally {
                scan.nextLine(); // Clear the end of line character
            }

        } while (badDate);

        result.clear();
        result.set(year, month, day, hour, minutes);

        System.out.println("The date/time you entered was: " +
                result.get(Calendar.YEAR) + "/" +
                (result.get(Calendar.MONTH) + 1) + "/" +
                result.get(Calendar.DAY_OF_MONTH) + " " +
                result.get(Calendar.HOUR_OF_DAY) + ":" +
                result.get(Calendar.MINUTE));
        return result;
    }

    /**
     * Method to ask the user if the social event requires food and drink
     *
     * @return returns the answer as a boolean (true means yes, false means no)
     * It is private because it is only needed in THIS class and other classes won't call it by accident
     */
    private boolean askFoodDrink() {
        System.out.println("Will food and drink be required at the social event?(Y/N)");
        String answer = readOptionInput("Y", "N");
        return answer.equals("Y");
    }

    /**
     * Method to ask the user if the social event requires an invitation
     *
     * @return returns the answer as a boolean (true means yes, false means no)
     * It is private because it is only needed in THIS class and other classes won't call it by accident
     */
    private boolean askInviteOnly() {
        System.out.println("Will the social event be invite-only?(Y/N)");
        String answer = readOptionInput("Y", "N");
        return answer.equals("Y");
    }

    /**
     * Method for allowing the user to enter the speakers for the talk and then returns them as an ArrayList object to the calling method
     *
     * @return returns the speakers in an arrayList
     * It is private because it is only needed in THIS class and other classes won't call it by accident
     */
    private ArrayList<Speaker> getSpeakers() {
        ArrayList<Speaker> speakers = new ArrayList<>();
        String answer;
        do {
            System.out.println("Enter on separate lines: speaker-name speaker-phone");
            String speakerName = inputUserNaming();
            String speakerPhone = inputUserNaming(); //won't check if phone number is actually a full number - this is because number could include ()+ characters or a space, and these aren't numbers
            Speaker speaker = new Speaker(speakerName, speakerPhone);
            speakers.add(speaker);
            System.out.println("Another owner (Y/N)?");
            answer = readOptionInput("Y", "N");
        } while (!answer.equals("N"));
        return speakers;
    }

    /**
     * Method to allow the user to change the name of the conference
     * It is private because it is only needed in THIS class and other classes won't call it by accident
     */
    private void changeConferenceName() {
        System.out.println("Choose a new name for the conference:");
        String name = inputUserNaming();
        conference.setName(name);
    }

    /**
     * Method to allow the user to search for an event.
     * If the search yields no results, they receive a message saying that the event was not found
     * It is private because it is only needed in THIS class and other classes won't call it by accident
     */
    private void searchForEvent() {
        System.out.println("Which event do you want to search for");
        String name = scan.nextLine();
        Event event = conference.searchForEvent(name);
        if (event != null) {
            System.out.println(event);
        } else {
            System.out.println("Could not find event: " + name);
        }
    }

    /**
     * Method to let the user remove an event from the conference
     * Once removed, a confirmation message is sent.
     * If no such event exists, this is conveyed in a message instead.
     */
    private void removeEvent() {
        System.out.println("Which event do you want to remove");
        String eventToBeRemoved;
        eventToBeRemoved = scan.nextLine();
        if (conference.removeEvent(eventToBeRemoved)) {
            System.out.println(eventToBeRemoved + "has been removed");
        } else {
            System.out.println("No such event exists, did you get the name right?");
        }

    }

    /**
     * Method to let the user add an event to the conference
     * If the event name already exists, this is conveyed in a message to the user.
     * No need to throw an exception, just a message is sufficient.
     */
    private void addVenue() {
        Venue venue;
        String venueName;
        boolean tryAgain;
        do {
            tryAgain = false;
            System.out.println("Enter the venue name");
            venueName = inputUserNaming();
            venue = conference.searchForVenue(venueName);
            if (venue != null) {
                System.out.println("This venue already exists. Give it a different name");
                tryAgain = true;
            }
        } while (tryAgain);

        System.out.println("Does it have a data projector?(Y/N)");
        String answer = readOptionInput("Y", "N");
        boolean hasDataProjector = answer.equals("Y");

        venue = new Venue(venueName);
        venue.setHasDataProjector(hasDataProjector);

        conference.addVenue(venue);
        System.out.println("Event " + venueName + " has been added");
    }


    /**
     * Method to print all the events in the conference. Since they use the conference class's toString method,
     * I decided it was best to sort them in that method instead. That way, they are both outputted in a more user-friendly way to the user AND are sorted
     */
    private void printAll() {
        //uses the conference class's toString method to sort the events and arrange them in a user-friendly format
        System.out.println(conference);
    }


    /**
     * save() method runs from the main and writes back to file
     */
    private void save() {
        try {
            conference.save(filename);

            //also creates a copy of the file in case the first one gets corrupted.
            conference.save("copy" + filename); //performs save function again rather than files.copy in case the first file save got corrupted

        } catch (IOException e) {
            System.err.println("Problem when trying to write to file: " + filename);
        }

    }

    // /////////////////////////////////////////////////
    public static void main(String args[]) {
        System.out.println("**********HELLO***********");
        ConferenceApp app;
        while (true) {
            try {
                app = new ConferenceApp();
                app.initialise();
                break;
            } catch (IllegalArgumentException e) { //if the user enters an invalid filename, they are told to enter a valid one instead
                System.out.println("The filename cannot be null or empty, please enter a valid filename:");
            }
        }
        app.runMenu();
        // MAKE A BACKUP COPY OF conf.txt JUST IN CASE YOU CORRUPT IT
        app.save();

        System.out.println("***********GOODBYE**********");
    }


}
