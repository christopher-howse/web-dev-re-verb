import static spark.Spark.*;
import spark.Session;
import users.User;

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
            return ((User)request.session().attribute("user")).name;
        });

        get("/getIsAnonymous", "application/x-www-form-urlencoded", (request, response) ->
        {
            response.type("application/application/x-www-form-urlencoded");
            boolean anon = request.session().attribute("anon");
            System.out.println("Getting anon " + Boolean.toString(anon));
            return Boolean.toString(anon);
        });

        post("/setAnonymous", (request, response) ->
        {
            boolean anon = Boolean.parseBoolean(request.queryParams("anon"));
            System.out.println("Setting anon " + request.queryParams("anon"));
            request.session().attribute("anon", anon);
            return " ";
        });
    }
}
