package login;

import error.Error;
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

            if ( u == null || pw == null )
            {
                return Error.errorPage("password or username not given");
            }
            if ( u.equals("foo") && pw.equals("bar") )
            {
                Session sess = request.session(true);
                if ( sess == null )
                {
                    return Error.errorPage("get a session, bitch!");
                }
                sess.attribute("user", u);
            }
            response.redirect("/auth/main.html");
            return null;
        });
    }
}
