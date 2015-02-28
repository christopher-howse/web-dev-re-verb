import com.google.gson.Gson;
import posts.PostManager;
import users.User;
import users.UserManager;

import static spark.Spark.*;

/**
 * Created by christopherhowse on 15-02-28.
 */
public class AdminSparkCalls
{
    public static void adminCall()
    {
        before("/admin/*", (request, response) ->
        {
            User user = Reverb.testAdmin;//request.session().attribute("user");
            if ( user == null ) {
                response.redirect("/not-signed-in.html");
                return;
            }
            if ( !user.role.equals( "admin" ) ) {
                response.redirect("/not-admin.html");
                return;
            }
        });

        get("/admin/getUsers", "application/json", (request, response) ->
        {
            response.type("application/json");
            UserManager userManager = new UserManager();
            return userManager.getUsers();
        }, new JsonTransformer());

        /*
        post("admin/getUserPosts", "application/json", (request, response) ->
        {
            Gson gson = new Gson();
            String username = gson.fromJson(request.body(), User.class);
            response.type("application/json");
            PostManager.getPostsByUser(username)
            return userManager.getUsers();
        }, new JsonTransformer());*/
    }

}
