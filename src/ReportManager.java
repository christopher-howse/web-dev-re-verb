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
                    "(message_id integer, user_id integer," +
                    "primary key(message_id, user_id))";

    private static String insertIntoReports =
            "INSERT INTO Reports VALUES(?,?)";

    private static String selectFromReports =
            "SELECT * FROM Reports WHERE message_id = ? AND user_id = ?";

    private static String deleteFromReports =
            "DELETE FROM Reports WHERE message_id = ? AND user_id = ?";

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
}
