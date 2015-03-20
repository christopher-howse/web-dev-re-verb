import users.User;

import java.sql.SQLException;

import static spark.Spark.*;

public class Reverb
{
    public static final User testAdmin = new User("Chris", "password", "admin");

    public static void main(String[] args)
    {
        port(8008);

        try
        {
            DatabaseManager db = new DatabaseManager();

            externalStaticFileLocation("static");

            before("/", (request, response) ->
            {
                response.type("text/html; charset=utf-8");
            });

            before("/auth/*", (request, response) ->
            {
                User user = request.session().attribute("user");
                if(user == null)
                {
                    response.redirect("/not-signed-in.html");
                    return;
                }
            });

            before("/auth/user/*", (request, response) ->
            {
                User user = request.session().attribute("user");
                if(user == null)
                {
                    response.redirect("/not-signed-in.html");
                    return;
                }
                //else if user page isn't for the user in question
                //redirect to access forbidden page
            });

            // redirect / to /login.html
            get("/", (request, response) ->
            {
                response.redirect("/login.html");
                return null;
            });

            Login login = new Login(db);
            AdminSparkCalls adminSparkCalls = new AdminSparkCalls(db);

        } catch (SQLException e)
        {
            System.out.println("Could not establish the reverb database");
            e.printStackTrace();
        }

    }
}
