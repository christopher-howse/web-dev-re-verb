import com.google.gson.Gson;
import posts.PostManager;
import users.User;
import users.UserManager;
import utilityDtos.IdDto;

import static spark.Spark.*;

public class AdminSparkCalls
{
    public static void adminCall()
    {
        //temp
        UserManager userManager = new UserManager();

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
            return userManager.getUsers();
        }, new JsonTransformer());


        post("admin/getUserPosts", "application/json", (request, response) ->
        {
            Gson gson = new Gson();
            UsernameDto usernameDto = gson.fromJson(request.body(), UsernameDto.class);
            response.type("application/json");
            return PostManager.getPostsByUser(usernameDto.username);
        }, new JsonTransformer());

        post("admin/toggleUserRole", "application/json", (request, response) ->
        {
            Gson gson = new Gson();
            UsernameDto usernameDto = gson.fromJson(request.body(), UsernameDto.class);
            response.type("application/json");
            return UserManager.toggleUserRole(usernameDto.username);
        }, new JsonTransformer());

        post("admin/deletePost", "application/json", (request, response) ->
        {
            Gson gson = new Gson();
            IdDto idDto = gson.fromJson(request.body(), IdDto.class);
            response.type("application/json");
            return PostManager.deletePost(idDto.id);
        }, new JsonTransformer());
    }

    public class UsernameDto
    {
        public final String username;

        public UsernameDto(String username)
        {
            this.username = username;
        }
    }

}
