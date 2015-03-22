import com.google.gson.Gson;
import users.User;
import utilityDtos.IdDto;

import static spark.Spark.*;

public class AdminSparkCalls
{
    private DatabaseManager databaseManager;

    public AdminSparkCalls(DatabaseManager databaseManager)
    {
        this.databaseManager = databaseManager;
        adminCall();
    }

    public void adminCall()
    {
        before("/admin/*", (request, response) ->
        {
            User user = Reverb.testAdmin;//request.session().attribute("user");
            if ( user == null ) {
                response.redirect("/login.html?error=Not Yet Logged In");
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
            return databaseManager.getUsrMan().getUsers();
        }, new JsonTransformer());

        //TODO: actually check if user is admin
        get("/admin/getAdmin", (request, response) ->
        {
            String adminType = "true";
            return adminType;
        });

        post("/goAdmin", (request, response) ->
        {
            response.redirect("/admin/index.html");
            return null;
        });

        post("admin/toggleSuspendUser", "application/json", (request, response) ->
        {
            Gson gson = new Gson();
            UsernameDto usernameDto = gson.fromJson(request.body(), UsernameDto.class);
            response.type("application/json");
            return databaseManager.getUsrMan().toggleUserSuspension(usernameDto.username);
        }, new JsonTransformer());

        post("admin/getUserPosts", "application/json", (request, response) ->
        {
            Gson gson = new Gson();
            UsernameDto usernameDto = gson.fromJson(request.body(), UsernameDto.class);
            response.type("application/json");
            return databaseManager.getPostMan().getPostsByUser(usernameDto.username);
        }, new JsonTransformer());

        post("admin/toggleUserRole", "application/json", (request, response) ->
        {
            Gson gson = new Gson();
            UsernameDto usernameDto = gson.fromJson(request.body(), UsernameDto.class);
            response.type("application/json");
            return databaseManager.getUsrMan().toggleUserRole(usernameDto.username);
        }, new JsonTransformer());

        post("admin/deletePost", "application/json", (request, response) ->
        {
            Gson gson = new Gson();
            IdDto idDto = gson.fromJson(request.body(), IdDto.class);
            response.type("application/json");
            return PostManager.deleteMessage(idDto.id);
        }, new JsonTransformer());

        post("admin/deleteUser", "application/json", (request, response) ->
        {
            Gson gson = new Gson();
            UsernameDto usernameDto = gson.fromJson(request.body(), UsernameDto.class);
            response.type("application/json");
            return databaseManager.getUsrMan().deleteUser(usernameDto.username);
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
