import java.sql.*;

/**
 * Created by christopherhowse on 15-03-20.
 */
public class FavoriteManager
{
    private static String createFavoriteTable =
            "CREATE TABLE IF NOT EXISTS Favorites" +
                    "(message_id integer, username text," +
                    "primary key(message_id, username), foreign key(username) references Users(username) ON UPDATE CASCADE," +
                    "foreign key(message_id) references Messages(message_id) ON UPDATE CASCADE)";

    private static String insertIntoFavorites =
            "INSERT INTO Favorites VALUES(?,?)";

    private static String selectFromFavorites =
            "SELECT * FROM Favorites WHERE message_id = ? AND username = ?";

    private static String deleteFromFavorites =
            "DELETE FROM Favorites WHERE message_id = ? AND username = ?";

    private static String favoritePost =
            "INSERT INTO Favorites VALUES(?, ?)";

    private static String unFavoritePost =
            "DELETE FROM Favorites WHERE message_id = ? AND username = ?";

    public FavoriteManager() throws SQLException
    {
        try (
                Connection conn = DriverManager.getConnection(DatabaseManager.dbURL);
                PreparedStatement stmt = conn.prepareStatement( createFavoriteTable );
        ) {
            stmt.setQueryTimeout(DatabaseManager.timeout);
            stmt.executeUpdate();
            System.out.println("Created favorites table");
        }
    }

    public boolean favoritePost(String username, int post_id)
    {
        try (
                Connection conn = DriverManager.getConnection(DatabaseManager.dbURL);
                PreparedStatement stmt = conn.prepareStatement( favoritePost );
        )
        {
            stmt.setQueryTimeout(DatabaseManager.timeout);
            stmt.setInt(1, post_id);
            stmt.setString(2, username);


            stmt.executeUpdate();
            return true;

        } catch (SQLException e)
        {
            System.out.println("Could not fav post in db");
            return false;
        }
    }

    public boolean unFavoritePost(String username, int post_id)
    {
        try (
                Connection conn = DriverManager.getConnection(DatabaseManager.dbURL);
                PreparedStatement stmt = conn.prepareStatement( unFavoritePost );
        ) {
            stmt.setQueryTimeout(DatabaseManager.timeout);
            stmt.setInt(1, post_id);
            stmt.setString(2, username);

            stmt.executeUpdate();
            System.out.println("Un-favoriting post");
            return true;
        } catch (SQLException e)
        {
            System.out.println("Could not un-favorite post");
            return false;
        }
    }
}
