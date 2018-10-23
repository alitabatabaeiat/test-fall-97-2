import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class Person {
    private String name;
    private String email;


    public Person(String name, String email) {
        this.name = name;
        this.email = email;
    }


    public void notifyUser() throws IOException {
        String url = "http://mail.test.ut.ac.ir";
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");

            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            String urlParameters = String.format("email=%s&name=%s&type=notify", this.email, this.name);

            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String toString() {
        return String.format("%s|%s", this.name, this.email);
    }
}
