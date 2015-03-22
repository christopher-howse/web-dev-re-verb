import error.Error;
import posts.*;
import static spark.Spark.*;
import spark.Session;
import users.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jake on 19/03/2015.
 */
public class PostSparkCalls
{

    private DatabaseManager databaseManager;

    public PostSparkCalls(DatabaseManager databaseManager)
    {
        this.databaseManager = databaseManager;
        posting();
    }
    public void posting()
    {
        post("/posting", (request, response) ->
        {
            Session sess = request.session(true);
            if ( sess == null )
            {
                return Error.errorPage("failed to get the session");
            }
            String postBody = request.queryParams("post");
            User user = request.session().attribute("user");

            //TODO: Add the post using post manager

            float latitude = request.session().attribute("latitude");
            float longitude = request.session().attribute("longitude");
            databaseManager.getPostMan().sendPost(user.name, postBody, 0 /*TODO: fix anon*/, latitude, longitude, "now");

            response.redirect("/auth/main-feed.html");

            return null;
        });
    }
}
