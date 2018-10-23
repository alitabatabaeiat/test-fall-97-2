import java.sql.*;
import java.util.Vector;



public interface DBManager {
    public Vector<Meeting> getAllMeetings();
    public void saveMeeting(Meeting meeting);
}

class MeetingDBManager implements DBManager {
    public Vector<Meeting> getAllMeetings() {
        Vector<Meeting> meetings = new Vector<Meeting>();
        String url = "jdbc:sqlite:/tmp/sqlite/ImportantMeetings.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            String sql = "SELECT attendees, date_of_meeting FROM meetings";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery(sql);
            while (rs.next()) {
                Vector<Person> attendees = new Vector<Person>();
                for (String nameemail : rs.getString("attendees").split("\\s*,\\s*")) {
                    attendees.add(new Person(nameemail.split("\\s*\\|\\s*")[0], nameemail.split("\\s*\\|\\s*")[1]));
                }
                meetings.add(new Meeting(attendees, rs.getDate("date_of_meeting")));
            }
            return meetings;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void saveMeeting(Meeting meeting) {
        String url = "jdbc:sqlite:/tmp/sqlite/ImportantMeetings.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            String sql = "INSERT INTO meetings(attendees,date_of_meeting) VALUES(?,?)";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, String.join(meeting.getAttendees().toString(), ", "));
            pstmt.setDate(2, new Date(meeting.getDate().getTime()));
            pstmt.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

