import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import users.User;

import static spark.Spark.*;

/**
 * Created by Colin on 2/28/2015.
 */
public class UserInfo {

    private static DatabaseManager databaseManager;

    public UserInfo(DatabaseManager databaseManager)
    {
        this.databaseManager = databaseManager;
        userCall();
    }

    public static void userCall()
    {
        get("/auth/userInfo", (request, response) ->
        {
            response.redirect("/auth/user-account.html");
            return null;
        });

        get("/auth/getUserInfo", "application/json", (request, response) ->
        {
            response.type("application/json");
            User curUser = request.session().attribute("user");
            return curUser;
        }, new JsonTransformer());

        post("/auth/saveUserInfo", (request, response) -> {
            response.type("text/plain");
            try {
                Gson gson = new Gson();

                String b = request.body();
                // attempt to convert JSON to SumObject
                User obj = gson.fromJson(b, User.class);
                System.out.println(String.format("/saveUserInfo: name:%s, description:%s", obj.name, obj.description));

                User curUser = request.session().attribute("user");

                UserManager usrMgr = databaseManager.getUsrMan();
                if(usrMgr.updateUserInfo(obj.name, obj.description, curUser.name))
                {
                    curUser.name = obj.name;
                    curUser.description = obj.description;
                    //TODO: update user in usermanager
                }

                return String.valueOf( obj.name ); //TODO: Fix this
            }
            catch ( JsonParseException ex ) {
                System.out.println("/saveUserInfo: malformed values");
                halt(400, "malformed values");
            }
            return null;
        });

        post("/auth/saveChangePassword", (request, response) -> {
            response.type("text/plain");
            String oldPassword = request.queryParams("oldPassword");
            String newPassword = request.queryParams("newPassword");
            User curUser = request.session().attribute("user");
            if(!oldPassword.equals(curUser.password))
            {
                System.out.println("Old password is wrong"); //TODO: proper error
            }
            else
            {
                if(databaseManager.getUsrMan().updateUserPassword(newPassword, curUser.name))
                {
                    curUser.password = newPassword;
                    System.out.println("Changing Password");
                }
            }
            return newPassword; //TODO:Don't send back the password
        });
    }

}
