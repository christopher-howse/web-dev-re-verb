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
            if ( u == "" || pw == "" )
            {
                response.redirect("/login.html?error=Username or password not given");
                return null;
            }
            User user = UserManager.getUser(u);
            if ( user != null )
            {
                response.redirect("/login.html?error=Username or password incorrect");
                return null;
            }
            if ( user.password != pw )
            {
                response.redirect("/login.html?error=Username or password incorrect");
                return null;
            }
            Session sess = request.session(true);
            if ( sess == null )
            {
                return Error.errorPage("Get a session, bitch!");
            }
            User currentUser = request.session().attribute("user");
            response.redirect("/auth/index.html");
            return null;
        });

        post("/signup", (request, response) ->
        {
            String u = request.queryParams("user");
            String pw = request.queryParams("password");
            //TODO: display errors on signup screen not new page
            if ( u == "" || pw == "" )
            {
                response.redirect("/login.html?init=signup&error=Username or password not given");
                return null;
            }
            User user = UserManager.getUser(u);
            if ( user != null )
            {
                response.redirect("/login.html?init=signup&error=Username already exists");
                return null;
            }
            //TODO: password strength test
            Session sess = request.session(true);
            if ( sess == null )
            {
                return Error.errorPage("Get a session, bitch!");
            }
            UserManager.createUser(u,pw);
            User currentUser = request.session().attribute("user");
            currentUser = user;
            response.redirect("/auth/index.html");
            return null;
        });
    }
}
