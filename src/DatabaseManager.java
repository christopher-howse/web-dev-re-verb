import org.sqlite.SQLiteConfig;

import java.sql.*;
import java.util.Properties;

/**
 * Created by christopherhowse on 15-03-19.
 */
public class DatabaseManager
{
    public static final String dbURL = "jdbc:sqlite:reverb.db";
    public static final int timeout = 5;

    public static final String enableForeignKeys = "PRAGMA foreign_key = ON;";

    private UserManager usrMan;
    private PostManager postMan;
    private FavoriteManager favMan;
    private ReportManager reportMan;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        }
        catch( Exception ex ) {
            System.err.println( ex.getMessage() );
            System.exit( 0 );
        }
    }

    public DatabaseManager() throws SQLException
    {
        usrMan = new UserManager();
        postMan = new PostManager();
        favMan = new FavoriteManager();
        reportMan = new ReportManager();

    }

    public static Connection getConnection() throws SQLException
    {
        SQLiteConfig config = new SQLiteConfig();
        config.enforceForeignKeys(true);
        Connection connection = DriverManager.getConnection(dbURL, config.toProperties());
        return connection;
    }

    public UserManager getUsrMan()
    {
        return usrMan;
    }

    public PostManager getPostMan()
    {
        return postMan;
    }

    public FavoriteManager getFavMan()
    {
        return favMan;
    }

    public ReportManager getReportMan()
    {
        return reportMan;
    }
}
