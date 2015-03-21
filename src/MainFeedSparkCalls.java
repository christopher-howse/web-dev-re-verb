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
    }
}
