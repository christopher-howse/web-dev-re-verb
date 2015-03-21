import users.User;
import utilityDtos.BooleanDto;

import java.sql.*;
import java.util.ArrayList;

public class UserManager
{
    static {
        try {
            Class.forName("org.sqlite.JDBC");
        }
        catch( Exception ex ) {
            System.err.println( ex.getMessage() );
            System.exit( 0 );
        }
    }

    private static String createUserTable =
            "CREATE TABLE IF NOT EXISTS Users" +
                    "( user_id integer, username text unique, password text, about_me text, account_status text, role text," +
                    "primary key (user_id))";

    private static String updateAccountStatus =
            "UPDATE Users SET account_status = ? WHERE user_id = ?";

    private static String deleteUser =
            "DELETE FROM Users WHERE user_id = ?";

    private static String insertIntoUsers =
            "INSERT INTO Users VALUES(NULL,?,?,?,?,?)";

    private static String getUserRole =
            "SELECT role FROM Users WHERE user_id = ?";

    private static String updateUserRole =
            "UPDATE Users SET role = ? WHERE user_id = ?";

    private static String getUsers =
            "SELECT * FROM Users";

    private static String loginAttempt =
            "SELECT * FROM Users WHERE username = ? AND password = ?";

    private static String checkIfUserExists =
            "SELECT * FROM Users WHERE username = ?";


    public UserManager() throws SQLException
    {
        try (
                Connection conn = DriverManager.getConnection(DatabaseManager.dbURL);
                PreparedStatement stmt = conn.prepareStatement( createUserTable );
        ) {
            stmt.setQueryTimeout(DatabaseManager.timeout);
            stmt.executeUpdate();
            System.out.println("Created user table");
        }
    }

    public ArrayList<User> getUsers()
    {
        ArrayList<User> result = new ArrayList<>();

        try
        (
                Connection conn = DriverManager.getConnection(DatabaseManager.dbURL);
                PreparedStatement stmt = conn.prepareStatement(getUsers);
        )
        {
            stmt.setQueryTimeout(DatabaseManager.timeout);
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                result.add(new User(rs.getString("username"), rs.getString("password"), rs.getString("role")));
            }
        } catch (SQLException e)
        {
            System.out.println("Could not obtain users from database");
            e.printStackTrace();
        }

        return result;
    }

    public BooleanDto toggleUserRole(String username)
    {
        BooleanDto result = new BooleanDto(false);
        try
        (
                Connection conn = DriverManager.getConnection(DatabaseManager.dbURL);
                PreparedStatement stmt = conn.prepareStatement(getUserRole);
        )
        {
            stmt.setQueryTimeout(DatabaseManager.timeout);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if(rs.next())
            {
                System.out.println("Got user role");
                if(rs.getString("role") != null && rs.getString("role").equals("Admin"))
                {
                    result.success = setUserRole(username, "User");
                }
                else
                {
                    result.success = setUserRole(username, "Admin");
                }
            }
            else
            {
                System.out.println("Could not find user in database to toggle role");
                result.success = false;
            }
        } catch (SQLException e)
        {
            System.out.println("Could not obtain user role from database");
            e.printStackTrace();
            result.success = false;
        }
        return result;
    }

    private boolean setUserRole(String username, String role)
    {
        try (
                Connection conn = DriverManager.getConnection( DatabaseManager.dbURL );
                PreparedStatement stmt = conn.prepareStatement( updateUserRole );
        )
        {
            stmt.setQueryTimeout(DatabaseManager.timeout);
            stmt.setString(1, role);
            stmt.setString(2, username);
            stmt.executeUpdate();
            System.out.println("Set user role for: " + username + ", to: " + role);
            return true;
        } catch (SQLException e)
        {
            System.out.println("Could not set user role in database");
            return false;
        }
    }

    public User getUser(String userName)
    {
        return new User("test_name", "test_password", "test_role");
    }


    public User checkIfUserExists(String username)
    {
        User result = null;

        try
        (
                Connection conn = DriverManager.getConnection(DatabaseManager.dbURL);
                PreparedStatement stmt = conn.prepareStatement(checkIfUserExists);
        )
        {
            stmt.setQueryTimeout(DatabaseManager.timeout);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if(rs.next())
            {
                result = new User(rs.getString("username"), rs.getString("password"), rs.getString("role"));
                System.out.println("Found existing user in database");
            }
        } catch (SQLException e)
        {
            System.out.println("User already exists");
            e.printStackTrace();
        }

        return result;
    }

    public User loginUser(String username, String password)
    {
        User result = null;

        try
        (
                Connection conn = DriverManager.getConnection(DatabaseManager.dbURL);
                PreparedStatement stmt = conn.prepareStatement(loginAttempt);
        )
        {
            stmt.setQueryTimeout(DatabaseManager.timeout);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if(rs.next())
            {
                result = new User(rs.getString("username"), rs.getString("password"), rs.getString("role"));
                System.out.println("Login successful");
            }
            else
            {
                System.out.println("Login failed: incorrect login details");
            }
        } catch (SQLException e)
        {
            System.out.println("Could not log in user");
            e.printStackTrace();
        }

        return result;
    }

    //TODO: add error scenario
    public void createUser(String username, String password)
    {
        try
        (
                Connection conn = DriverManager.getConnection(DatabaseManager.dbURL);
                PreparedStatement stmt = conn.prepareStatement(insertIntoUsers);
        )
        {
            stmt.setQueryTimeout(DatabaseManager.timeout);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, "");              //about me default
            stmt.setString(4, "Active");        //account_status default
            stmt.setString(5, "User");          //role default
            stmt.executeUpdate();
            System.out.println("Created user");
        } catch (SQLException e)
        {
            System.out.println("User already exists");
            e.printStackTrace();
        }
    }
}
