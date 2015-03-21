import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by christopherhowse on 15-03-20.
 */
public class FavoriteManager
{
    private static String createFavoriteTable =
            "CREATE TABLE IF NOT EXISTS Favorites" +
                    "(message_id integer, user_id integer," +
                    "primary key(message_id, user_id))";

    private static String insertIntoFavorites =
            "INSERT INTO Favorites VALUES(?,?)";

    private static String selectFromFavorites =
            "SELECT * FROM Favorites WHERE message_id = ? AND user_id = ?";

    private static String deleteFromFavorites =
            "DELETE FROM Favorites WHERE message_id = ? AND user_id = ?";

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
}
