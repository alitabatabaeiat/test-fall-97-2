import java.io.IOException;

public class PersonMock extends Person {
    public int numOfCall;
    public PersonMock(String name, String email) {
        super(name, email);
        numOfCall = 0;
    }

    @Override
    public void notifyUser() throws IOException {
        this.numOfCall++;
    }
}
