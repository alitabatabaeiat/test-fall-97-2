import java.io.IOException;
import java.util.Date;
import java.util.Vector;

public class Meeting {
    private Vector<Person> attendees;
    private Date date;

    public Meeting(Vector<Person> attendees, Date date){
        this.attendees = new Vector<Person>(attendees);
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public Vector<Person> getAttendees() {
        return attendees;
    }

    public void notifyUsers() throws IOException {
        for(Person attendee: attendees){
            attendee.notifyUser();
        }
    }

}
