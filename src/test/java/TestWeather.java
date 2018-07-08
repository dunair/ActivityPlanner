import org.exp.activityplanner.ActivityPlanner;
import org.exp.activityplanner.Request;

/**
 * Created by a-7775 on 7/8/18.
 */
public class TestWeather {
    public static void main(String[] args) {
        ActivityPlanner acc = new ActivityPlanner();
        Request req = new Request();
        req.setCityName("Cochin");
        acc.handleRequest(req, null);
    }
}
