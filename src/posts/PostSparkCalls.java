package posts;

import error.Error;
import posts.*;
import static spark.Spark.*;
import spark.Session;
import users.User;

/**
 * Created by Jake on 19/03/2015.
 */
public class PostSparkCalls
{
    public static void posting()
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

            response.redirect("/feed.html");

            return null;
        });
    }
}
