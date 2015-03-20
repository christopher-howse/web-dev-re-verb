import posts.Post;
import utilityDtos.BooleanDto;

import java.sql.*;
import java.util.ArrayList;

public class PostManager
{
    private static String createMessageTable =
            "CREATE TABLE IF NOT EXISTS Messages " +
                    "( message_id integer, user_id integer, message_body text, anon_flag integer," +
                    "location point, report_count integer, vote_count integer, reply_link integer, create_time datetime," +
                    "primary key (message_id))";

    private static String selectByUsername =
            "SELECT * FROM Messages WHERE username = ?";

    private static String insertIntoMessages =
            "INSERT INTO Messages VALUES(NULL, ?, ?, ?," +
                    "?, ?, ?, ?, ?)";

    private static String deleteMessage =
            "DELETE FROM Messages WHERE message_id = ?";

    public PostManager() throws SQLException
    {
        try (
                Connection conn = DriverManager.getConnection(DatabaseManager.dbURL);
                PreparedStatement stmt = conn.prepareStatement( createMessageTable );
        ) {
            stmt.setQueryTimeout(DatabaseManager.timeout);
            stmt.executeUpdate();
            System.out.println("Created message table");
        }
    }

    public ArrayList<Post> getPostsByUser(String username)
    {
        ArrayList<Post> result = new ArrayList<Post>();

        try (
                Connection conn = DriverManager.getConnection(DatabaseManager.dbURL);
                PreparedStatement stmt = conn.prepareStatement( selectByUsername );
        ) {
            stmt.setQueryTimeout(DatabaseManager.timeout);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
            {
                result.add(new Post(rs.getString("username"), rs.getInt("message_id"), rs.getString("message_body")));
            }
        } catch (SQLException e)
        {
            System.out.println("Could not get posts for user: " + username);
        }

        return result;
    }

    public static BooleanDto deleteMessage(int messageId)
    {
        BooleanDto result = new BooleanDto(false);

        try (
                Connection conn = DriverManager.getConnection(DatabaseManager.dbURL);
                PreparedStatement stmt = conn.prepareStatement( deleteMessage );
        ) {
            stmt.setQueryTimeout(DatabaseManager.timeout);
            stmt.setInt(1, messageId);
            stmt.executeUpdate();
            result.success = true;
        } catch (SQLException e)
        {
            System.out.println("Could not delete post with post id: " + messageId);
        }

        return result;
    }
}
