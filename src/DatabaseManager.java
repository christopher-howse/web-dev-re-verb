import java.sql.*;

/**
 * Created by christopherhowse on 15-03-19.
 */
public class DatabaseManager
{
    public static final String dbURL = "jdbc:sqlite:reverb.db";
    public static final int timeout = 5;

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
