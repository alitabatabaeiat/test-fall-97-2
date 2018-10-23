import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Vector;

public class MeetingManager {
    private static MeetingManager ourInstance = new MeetingManager();
    private static String DATE_VALIDATOR_URL = "http://localhost:8000/checkdate";

    private DBManager dbManager = new MeetingDBManager();

    public static MeetingManager getInstance() {
        return ourInstance;
    }

    public void bookMeeting(Vector<Person> attendees, int day_delta) throws Exception {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, day_delta);
        if (!this.checkDate(c.getTime())) {
            throw new Exception();
        }
        Meeting meeting = new Meeting(attendees, c.getTime());
        this.saveMeeting(meeting);
    }

    public void setDateValidatorUrl(String url) {
        DATE_VALIDATOR_URL = url;
    }

    private boolean checkDate(java.util.Date date) {
        try {
            URL obj = new URL(MeetingManager.DATE_VALIDATOR_URL);

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("Accept", "*/*");
            con.setRequestMethod("POST");
            con.setDoOutput(true);

            OutputStreamWriter outputWriter = new OutputStreamWriter(con.getOutputStream());
            outputWriter.write(String.format("{date=%s}", date.getTime()));
            outputWriter.flush();
            outputWriter.close();

            return 200 == con.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void notifyMeetings() throws IOException {
        for (Meeting meeting : this.getAllMeetings()) {
            meeting.notifyUsers();
        }
    }

    private Vector<Meeting> getAllMeetings() {
        return dbManager.getAllMeetings();
    }

    private void saveMeeting(Meeting meeting) {
        dbManager.saveMeeting(meeting);
    }

    public void setDBManager(DBManager dbManager) {
        this.dbManager = dbManager;
    }

}
