import error.Error;
import users.*;
import static spark.Spark.*;
import spark.Session;

/**
 * Created by Jake on 28/02/2015.
 */
public class Login
{
    private DatabaseManager databaseManager;

    public Login(DatabaseManager databaseManager)
    {
        this.databaseManager = databaseManager;
        login();
    }

    public void login()
    {
        post("/login", (request, response) ->
        {
            String u = request.queryParams("user");
            String pw = request.queryParams("password");
            //TODO: display errors on login screen not new page
            if ( u.isEmpty() || pw.isEmpty() )
            {
                response.redirect("/login.html?error=Username or password not given");
                return null;
            }
            User user = databaseManager.getUsrMan().loginUser(u, pw);
            if ( user == null )
            {
                response.redirect("/login.html?error=Username or password incorrect");
                return null;
            }
            if ( !user.password.equals(pw) )
            {
                response.redirect("/login.html?error=Username or password incorrect");
                return null;
            }
            Session sess = request.session(true);
            if ( sess == null )
            {
                return Error.errorPage("Get a session, please!");
            }
            request.session().attribute("user", user);
            request.session().attribute("anon", false);
            response.redirect("/auth/main-feed.html");
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
            User user = databaseManager.getUsrMan().checkIfUserExists(u);
            if ( user != null )
            {
                response.redirect("/login.html?init=signup&error=Username already exists");
                return null;
            }
            //TODO: password strength test
            Session sess = request.session(true);
            if ( sess == null )
            {
                return Error.errorPage("Get a session, please!");
            }
            databaseManager.getUsrMan().createUser(u,pw);
            user = databaseManager.getUsrMan().loginUser(u, pw);
            request.session().attribute("user", user);
            request.session().attribute("anon", false);
            response.redirect("/auth/main-feed.html");
            return null;
        });

        post("/logout", (request, response) ->
        {
            Session sess = request.session(true);
            if ( sess == null )
            {
                return Error.errorPage("Get a session, please!");
            }
            request.session().attribute("user", null);
            response.redirect("login.html");
            return Error.errorPage("Get a session, please!");
        });
    }
}
