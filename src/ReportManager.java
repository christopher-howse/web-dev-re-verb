import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by christopherhowse on 15-03-20.
 */
public class ReportManager
{
    private static String createReportTable =
            "CREATE TABLE IF NOT EXISTS Reports" +
                    "(message_id integer, username text," +
                    "primary key(message_id, username), foreign key(username) references Users(username) ON UPDATE CASCADE," +
                    "foreign key(message_id) references Messages(message_id) ON UPDATE CASCADE)";

    private static String insertIntoReports =
            "INSERT INTO Reports VALUES(?,?)";

    private static String selectFromReports =
            "SELECT * FROM Reports WHERE message_id = ? AND username = ?";

    private static String deleteFromReports =
            "DELETE FROM Reports WHERE message_id = ? AND username = ?";

    private static String reportPost =
            "INSERT INTO Reports VALUES(?, ?)";

    public ReportManager() throws SQLException
    {
        try (
                Connection conn = DriverManager.getConnection(DatabaseManager.dbURL);
                PreparedStatement stmt = conn.prepareStatement( createReportTable );
        ) {
            stmt.setQueryTimeout(DatabaseManager.timeout);
            stmt.executeUpdate();
            System.out.println("Created reports table");
        }
    }

    public boolean reportPost(String username, int post_id)
    {
        try (
                Connection conn = DriverManager.getConnection(DatabaseManager.dbURL);
                PreparedStatement stmt = conn.prepareStatement( reportPost );
        ) {
            stmt.setQueryTimeout(DatabaseManager.timeout);
            stmt.setInt(1, post_id);
            stmt.setString(2, username);

            stmt.executeUpdate();
            System.out.println("Reporting post");
            return true;
        } catch (SQLException e)
        {
            System.out.println("Could not report post");
            return false;
        }
    }

}
