package login;

import error.Error;
import users.*;
import static spark.Spark.*;
import spark.Session;

/**
 * Created by Jake on 28/02/2015.
 */
public class Login
{
    public static void login()
    {
        post("/login", (request, response) ->
        {
            String u = request.queryParams("user");
            String pw = request.queryParams("password");
            //TODO: display errors on login screen not new page
            if ( u == null || pw == null )
            {
                return Error.errorPage("Password or username not given");
            }
            User user = UserManager.getUser(u);
            if ( user == null )
            {
                response.redirect("/login.html");
                return null;
            }
            Session sess = request.session(true);
            if ( sess == null )
            {
                return Error.errorPage("Get a session, bitch!");
            }
            response.redirect("/auth/index.html");
            return null;
        });

        post("/signup", (request, response) ->
        {
            String u = request.queryParams("user");
            String pw = request.queryParams("password");
            //TODO: display errors on signup screen not new page
            if ( u == null || pw == null )
            {
                return Error.errorPage("Password or username not given");
            }
            User user = UserManager.getUser(u);
            if ( user != null )
            {
                response.redirect("/login.html?init=signup");
                return null;
            }
            //TODO: password strength test
            Session sess = request.session(true);
            if ( sess == null )
            {
                return Error.errorPage("Get a session, bitch!");
            }
            UserManager.createUser(u,pw);
            response.redirect("/auth/index.html");
            return null;
        });
    }
}
