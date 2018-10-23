import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.junit.MockServerRule;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.verify.VerificationTimes;

import java.util.Vector;

import static org.junit.Assert.assertEquals;

public class MeetingManagerTest {
    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this, 8000);

    private MockServerClient mockServerClient;

    private Vector<Person> attendees;
    private int delta;
    private MeetingDBManagerMock dbManager;
    @Before
    public void setUp() {
        dbManager = new MeetingDBManagerMock();
        MeetingManager.getInstance().setDBManager(dbManager);
        attendees = new Vector<Person>();
        attendees.add(new PersonMock("malikeh", "malikehehghaghi@gmail.com"));
        attendees.add(new PersonMock("ali", "a.tabatabaei97@gmail.com"));
        attendees.add(new PersonMock("gholi", "gholi82@gmail.com"));
        delta = 5;
    }

    @Test
    public void shouldBookAMeetingIfResponseStatusIs200() throws Exception {
        // setting behaviour for test case
        mockServerClient.when(HttpRequest.request("/checkdate").withMethod("POST")).respond(HttpResponse.response().withStatusCode(200));

        int initialSize = dbManager.meetings.size();
        MeetingManager.getInstance().bookMeeting(attendees, delta);
        int secondSize = dbManager.meetings.size();

        // verify server has received exactly one request
        mockServerClient.verify(HttpRequest.request("/checkdate"), VerificationTimes.once());

        assertEquals( "number of added meetings", 1, secondSize - initialSize);
        assertEquals( "number of attendies in new meeting", 3, dbManager.meetings.lastElement().getAttendees().size());

    }

    @Test (expected = Exception.class)
    public void shouldNotBookAMeetingIfResponseStatusIs400() throws Exception {
        // setting behaviour for test case
        mockServerClient.when(HttpRequest.request("/checkdate").withMethod("POST")).respond(HttpResponse.response().withStatusCode(400));

        int initialSize = dbManager.meetings.size();
        MeetingManager.getInstance().bookMeeting(attendees, delta);
        int secondSize = dbManager.meetings.size();

        // verify server has received exactly one request
        mockServerClient.verify(HttpRequest.request("/checkdate"), VerificationTimes.once());

        assertEquals( "number of added meetings", 0, secondSize - initialSize);
    }

    @Test
    public void shouldNotifyAllUsersOfAllMeetings() throws Exception {
        // setting behaviour for test case
        mockServerClient.when(HttpRequest.request("/checkdate").withMethod("POST")).respond(HttpResponse.response().withStatusCode(200));

        Vector<Person> attendees2 = new Vector<Person>();
        attendees2.add(attendees.get(0));

        MeetingManager.getInstance().bookMeeting(attendees, delta);
        MeetingManager.getInstance().bookMeeting(attendees2, delta + 2);

        MeetingManager.getInstance().notifyMeetings();

        assertEquals( "number of notifies added", 2, ((PersonMock) attendees.get(0)).numOfCall);
        assertEquals( "number of notifies added", 1, ((PersonMock) attendees.get(1)).numOfCall);
        assertEquals( "number of notifies added", 1, ((PersonMock) attendees.get(2)).numOfCall);
    }

    @After
    public void tearDown() {
        MeetingManager.getInstance().setDBManager(new MeetingDBManager());
    }
}

