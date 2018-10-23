import java.util.Vector;

public class MeetingDBManagerMock implements DBManager {
    public Vector<Meeting> meetings = new Vector<Meeting>();
    public Vector<Meeting> getAllMeetings() {
        return meetings;
    }

    public void saveMeeting(Meeting meeting) {
        meetings.add(meeting);
    }
}