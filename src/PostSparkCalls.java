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
            boolean anon = request.session().attribute("anon");

            float latitude = request.session().attribute("latitude");
            float longitude = request.session().attribute("longitude");

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String curTime = (dateFormat.format(date));
            databaseManager.getPostMan().sendPost(user.name, postBody, anon ? 1 : 0, latitude, longitude, curTime);

            response.redirect("/auth/main-feed.html");

            return null;
        });

        post("/getMessage", "application/json", (request, response) ->
        {
            Gson gson = new Gson();
            String b = request.body();
            IdDto obj = gson.fromJson(b, IdDto.class);
            User user = request.session().attribute("user");
            response.type("application/json");
            return databaseManager.getPostMan().getMessageById(obj.id, user.name);
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
            boolean anon = request.session().attribute("anon");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String curTime = (dateFormat.format(date));
            float latitude = request.session().attribute("latitude");
            float longitude = request.session().attribute("longitude");
            databaseManager.getPostMan().sendReply(user.name, postBody, anon ? 1 : 0, latitude, longitude, curTime, id);

            response.redirect("/auth/main-feed.html");

            return null;
        });

        get("/getMessagesByLocation", "application/json", (request, response) ->
        {
//            Gson gson = new Gson();
            response.type("application/json");
            float latitude = request.session().attribute("latitude");
            float longitude = request.session().attribute("longitude");
            User user = request.session().attribute("user");
            String lat = String.valueOf(latitude);
            String lon = String.valueOf(longitude);
            return databaseManager.getPostMan().getPostsByLocation(lat, lon, user.name);
        }, new JsonTransformer());
        
        post("/getReplies", "application/json", (request, response) ->
        {
            Gson gson = new Gson();
            String b = request.body();
            IdDto obj = gson.fromJson(b, IdDto.class);
            User user = request.session().attribute("user");
            response.type("application/json");
            return databaseManager.getPostMan().getReplies(obj.id, user.name);
        }, new JsonTransformer());
        

        get("/getMessagesByUser", "application/json", (request, response) ->
        {
//            Gson gson = new Gson();
            response.type("application/json");
            User user = request.session().attribute("user");
            return databaseManager.getPostMan().getPostsByUser(user.name);
        }, new JsonTransformer());

        post("/favoritePost", "application/x-www-form-urlencoded", (request, response) ->
        {
            Session sess = request.session(true);
            if (sess == null) {
                return Error.errorPage("failed to get the session");
            }
            String id = request.queryParams("post_id");
            int post_id = Integer.parseInt(id);
            boolean favorite = Boolean.parseBoolean(request.queryParams("favorite"));
            User user = request.session().attribute("user");

            if (!favorite) {
                databaseManager.getFavMan().favoritePost(user.name, post_id);
            } else {
                databaseManager.getFavMan().unFavoritePost(user.name, post_id);
            }

            return databaseManager.getPostMan().selectFavoriteCount(post_id);
        });

        post("/reportPost", (request, response) ->
        {
            Session sess = request.session(true);
            if ( sess == null )
            {
                return Error.errorPage("failed to get the session");
            }
            String id = request.queryParams("post_id");
            int post_id = Integer.parseInt(id);
            User user = request.session().attribute("user");

            databaseManager.getReportMan().reportPost(user.name, post_id);

            response.redirect("/auth/main-feed.html");

            return " ";
        });

        post("/deletePost", (request, response) ->
        {
            Session sess = request.session(true);
            if ( sess == null )
            {
                return Error.errorPage("failed to get the session");
            }
            String id = request.queryParams("post_id");
            int post_id = Integer.parseInt(id);
            User user = request.session().attribute("user");

            databaseManager.getPostMan().deleteMessageByUser(post_id, user.name);

            return " ";
        });
    }
}
