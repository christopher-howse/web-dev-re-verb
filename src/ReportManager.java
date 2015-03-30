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

    private static String updateUserNumReports =
            "UPDATE Users SET num_reports=num_reports + ? WHERE username = ?";

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
        boolean result;
        try (
                Connection conn = DriverManager.getConnection(DatabaseManager.dbURL);
                PreparedStatement stmt = conn.prepareStatement( reportPost );
        ) {
            stmt.setQueryTimeout(DatabaseManager.timeout);
            stmt.setInt(1, post_id);
            stmt.setString(2, username);

            stmt.executeUpdate();
            System.out.println("Reporting post");
            result = true;
        } catch (SQLException e)
        {
            System.out.println("Could not report post");
            result = false;
        }

        if(result)
        {
            incrementUserNumReports(username);
        }

        return result;
    }

    public void incrementUserNumReports(String username)
    {
        try (
                Connection conn = DriverManager.getConnection(DatabaseManager.dbURL);
                PreparedStatement stmt = conn.prepareStatement(updateUserNumReports);
        ) {
            stmt.setQueryTimeout(DatabaseManager.timeout);
            stmt.setInt(1, 1);
            stmt.setString(2, username);
            stmt.executeUpdate();
            System.out.println("Incremented num_reports on user: " + username);
        } catch (SQLException e)
        {
            System.out.println("Could not increment num_reports on user: " + username);
        }
    }
}
