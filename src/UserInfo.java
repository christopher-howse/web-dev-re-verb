import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import users.User;
import users.UserManager;

import static spark.Spark.*;

/**
 * Created by Colin on 2/28/2015.
 */
public class UserInfo {

    public static void userCall()
    {
        before("/user/*", (request, response) ->
        {
            User user = Reverb.testAdmin;//request.session().attribute("user");
            if ( user == null ) {
                response.redirect("/not-signed-in.html");
                return;
            }

        });

        get("/user/info", (request, response) ->
        {
            response.redirect("user-account.html");
            return null;
        });

        get("/user/getUserInfo", "application/json", (request, response) ->
        {
            response.type("application/json");
            UserManager userManager = new UserManager();
            return userManager.getUsers().get(0);
        }, new JsonTransformer());

        post("/user/saveUserInfo", (request, response) -> {
            response.type("text/plain");
            try {
                Gson gson = new Gson();

                String b = request.body();
                // attempt to convert JSON to SumObject
                User obj = gson.fromJson(b, User.class);
                System.out.println(String.format("/saveUserInfo: name:%s, handle:%s, description:%s", obj.name, obj.handle, obj.description));
                return String.valueOf( obj.handle ); //TODO: Fix this
            }
            catch ( JsonParseException ex ) {
                System.out.println("/saveUserInfo: malformed values");
                halt(400, "malformed values");
            }
            return null;
        });

        post("/user/saveChangePassword", (request, response) -> {
            response.type("text/plain");
            String oldPassword = request.queryParams("oldPassword");
            String newPassword = request.queryParams("newPassword");
            //TODO:Check if old password matches
            //TODO:Set new password
            System.out.println("Changing Password");
            return newPassword; //TODO:Don't send back the password
        });
    }

}
