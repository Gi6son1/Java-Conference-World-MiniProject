import java.util.Objects;

/**
 * Class to blueprint what a speaker would look like
 * @author Chris Loftus, Faisal Rezwan and Owain Gibson
 * @version 3 (26th March 2022)
 */
public class Speaker {
    private String name;
    private String phone;

    /**
     * Constructor to create an instance of the class
     * @param name holds the speaker's name
     */
    public Speaker(String name){
        this.name = name;
    }

    /**
     * Constructor to create an instance of the class
     * @param name holds the name of the speaker
     * @param phone holds the phone number of the speaker
     */
    public Speaker(String name, String phone){
        this(name);
        this.phone = phone;

    }

    /**
     * Method to retrieve the name of the speaker
     * @return returns the name
     */
    public String getName() {
        return name;
    }

    /**
     * Method to set the name of the speaker
     * @param name holds the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Method to retrieve the phone number of the speaker
     * @return returns the phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Method to set the phone number of the speaker
     * @param phone holds the new number
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Override of the equals method, used to check if the speaker is the same as the one passed to it
     * @param o holds the object to compare
     * @return returns the result of the comparison (true if same, false if not)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Speaker speaker = (Speaker) o;
        return Objects.equals(name, speaker.name) &&
                Objects.equals(phone, speaker.phone);
    }

    /**
     * Override of the toString method, used to return a summary of the speaker's characteristics to the calling function
     * @return returns the summary
     */
    @Override
    public String toString() {
        return "Speaker{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
