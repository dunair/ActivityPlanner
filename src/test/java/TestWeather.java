import org.exp.activityplanner.ActivityPlanner;
import org.exp.activityplanner.Request;
import org.exp.activityplanner.Response;
import org.joda.time.DateTime;

public class TestWeather {
    public static void main(String[] args) {
        ActivityPlanner acc = new ActivityPlanner();
        Request req = new Request();
        req.setAction("email");
        req.setPreferences("nightlife");
        req.setCityName("London");


        long start = System.currentTimeMillis();
        Response res = acc.handleRequest(req, null);
        long end = System.currentTimeMillis();
        System.out.println(end - start);

        DateTime today = new DateTime();
        DateTime tomorrow = today.plusDays(1);
        int a = 0;
    }
}
