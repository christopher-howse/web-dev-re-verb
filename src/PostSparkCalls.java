import com.google.gson.Gson;
import error.Error;
import posts.*;
import static spark.Spark.*;
import spark.Session;
import users.User;
import utilityDtos.IdDto;

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

        post("/getMessage", "application/json", (request, response) ->
        {
            Gson gson = new Gson();
            String b = request.body();
            IdDto obj = gson.fromJson(b, IdDto.class);
            response.type("application/json");
            return databaseManager.getPostMan().getMessageById(obj.id);
        }, new JsonTransformer());

        post("/reply", (request, response) ->
        {
            Session sess = request.session(true);
            if ( sess == null )
            {
                return Error.errorPage("failed to get the session");
            }
            String postBody = request.queryParams("reply");
            User user = request.session().attribute("user");
            String messageId = request.queryParams("id");
            int id = Integer.parseInt(messageId);
            //TODO: Add the post using post manager

            float latitude = request.session().attribute("latitude");
            float longitude = request.session().attribute("longitude");
            databaseManager.getPostMan().sendReply(user.name, postBody, 0 /*TODO: fix anon*/, latitude, longitude, "now", id);

            response.redirect("/auth/main-feed.html");

            return null;
        });

        get("/getMessagesByLocation", "application/json", (request, response) ->
        {
//            Gson gson = new Gson();
            response.type("application/json");
            float latitude = request.session().attribute("latitude");
            float longitude = request.session().attribute("longitude");
            String lat = String.valueOf(latitude);
            String lon = String.valueOf(longitude);
            Date now = new Date();
            long unix = now.getTime() / 1000;
            String time = String.valueOf(unix);
            return databaseManager.getPostMan().getPostsByLocation(lat,lon,time);
        }, new JsonTransformer());

        post("/getReplies", "application/json", (request, response) ->
        {
            Gson gson = new Gson();
            String b = request.body();
            IdDto obj = gson.fromJson(b, IdDto.class);
            response.type("application/json");
            return databaseManager.getPostMan().getReplies(obj.id);
        }, new JsonTransformer());
    }
}
