import static spark.Spark.*;
import spark.Session;
/**
 * Created by Jake on 21/03/2015.
 */
public class MainFeedSparkCalls
{
    private DatabaseManager databaseManager;

    public MainFeedSparkCalls(DatabaseManager databaseManager)
    {
        this.databaseManager = databaseManager;
        mainFeedCall();
    }

    public void mainFeedCall()
    {
        post("/goHome", (request, response) ->
        {
            response.redirect("/auth/main-feed.html");
            return null;
        });

        post("/getUserPosition", (request, response) ->
        {
            float latitude = Float.parseFloat(request.queryParams("latitude"));
            float longitude = Float.parseFloat(request.queryParams("longitude"));
            request.session().attribute("latitude", latitude);
            request.session().attribute("longitude", longitude);
            return " ";
        });
    }
}
