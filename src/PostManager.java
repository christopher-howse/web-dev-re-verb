import posts.Post;
import users.User;
import utilityDtos.BooleanDto;

import java.sql.*;
import java.util.ArrayList;

public class PostManager
{
    private static String createMessageTable =
            "CREATE TABLE IF NOT EXISTS Messages " +
                    "( message_id integer, message_body text, anon_flag integer," +
                    "latitude real, longitude real, report_count integer, vote_count integer, reply_link integer, create_time datetime, username text," +
                    "primary key (message_id))";

    private static String selectByUsername =
            "SELECT * FROM Messages WHERE username = ?";

    private static String selectById =
            "SELECT * FROM Messages WHERE message_id=?;";

    private static String selectAll =
            "SELECT * FROM Messages;";

    private static String selectReplies =
            "SELECT * FROM Messages WHERE reply_link = ?;";

    private static String selectByLocation =
            "SELECT " +
            "message_id, user_id, message_body, anon_flag, latitude, longitude, report_count, vote_count, reply_link, create_time,  (" +
        "6371 * acos (" +
        "cos ( radians(?) )" + //lat
        "* cos( radians( latitude ) )" +
        "* cos( radians( longitude ) - radians(?) )" + //long
        "+ sin ( radians(?) )" + //lat
        "* sin( radians( latitude ) )" +
        ")" +
        ") AS distance" +
    "FROM Messages" +
    "HAVING distance < 1 AND UNIX_TIMESTAMP(create_time) <= UNIX_TIMESTAMP(?) and Reply_link is NULL" + //time
    "ORDER BY create_time desc,distance"; //+
//    "LIMIT 25;";

    private static String insertIntoMessages =
            "INSERT INTO Messages VALUES(NULL, ?, ?, ?, ?," +
                    "0, 0, ?, ?, ?)";

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

    public Post getMessageById(int messageId)
    {
        Post result = new Post();

        try (
                Connection conn = DriverManager.getConnection(DatabaseManager.dbURL);
                PreparedStatement stmt = conn.prepareStatement( selectById );
        ) {
            stmt.setQueryTimeout(DatabaseManager.timeout);
            stmt.setInt(1, messageId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
            {
                result = new Post(rs.getString("username"), rs.getInt("message_id"), rs.getString("message_body"),rs.getString("create_time"));
            }
        } catch (SQLException e)
        {
            System.out.println("Could not get post by id: " + messageId);
        }

        return result;
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
                result.add(new Post(rs.getString("username"), rs.getInt("message_id"), rs.getString("message_body"),rs.getString("create_time")));
            }
        } catch (SQLException e)
        {
            System.out.println("Could not get posts for user: " + username);
        }

        return result;
    }

    public ArrayList<Post> getPostsByLocation(String lat,String lon,String time)
    {
        ArrayList<Post> result = new ArrayList<Post>();
        try(
                Connection conn = DriverManager.getConnection(DatabaseManager.dbURL);
                PreparedStatement stmt = conn.prepareStatement( selectAll );
        ) {
            stmt.setQueryTimeout(DatabaseManager.timeout);
//            stmt.setString(1, lat);
//            stmt.setString(2, lon);
//            stmt.setString(3, lat);
//            stmt.setString(4, time);
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
            {
                result.add(new Post(rs.getString("username"), rs.getInt("message_id"), rs.getString("message_body"),rs.getString("create_time")));
            }
        } catch (SQLException e)
        {
            System.out.println("Could not get posts at: " + lat + "," + lon + "," + time);
        }

        return result;

    }

    public ArrayList<Post> getReplies(int messageId)
    {
        ArrayList<Post> result = new ArrayList<Post>();
        try(
                Connection conn = DriverManager.getConnection(DatabaseManager.dbURL);
                PreparedStatement stmt = conn.prepareStatement( selectReplies );
        ) {
            stmt.setQueryTimeout(DatabaseManager.timeout);
            stmt.setInt(1, messageId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
            {
                result.add(new Post(rs.getString("username"), rs.getInt("message_id"), rs.getString("message_body"),rs.getString("create_time")));
            }
        } catch (SQLException e)
        {
            System.out.println("Could not get posts with reply id: " + messageId);
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

    public boolean sendPost(String username, String postContent, int anon, float latitude, float longitude, String time)
    {
        try (
                Connection conn = DriverManager.getConnection(DatabaseManager.dbURL);
                PreparedStatement stmt = conn.prepareStatement( insertIntoMessages );
        ) {
            stmt.setQueryTimeout(DatabaseManager.timeout);
            stmt.setString(1, postContent);
            stmt.setInt(2, anon);
            stmt.setFloat(3, latitude);
            stmt.setFloat(4, longitude);
            stmt.setInt(5, 0);
            stmt.setString(6, time);
            stmt.setString(7, username);

            stmt.executeUpdate();
            return true;

        } catch (SQLException e)
        {
            System.out.println("Could not send post to db");
            return false;
        }
    }

    public boolean sendReply(String username, String postContent, int anon, float latitude, float longitude, String time,int messageId)
    {
        try (
                Connection conn = DriverManager.getConnection(DatabaseManager.dbURL);
                PreparedStatement stmt = conn.prepareStatement( insertIntoMessages );
        ) {
            stmt.setQueryTimeout(DatabaseManager.timeout);
            stmt.setString(1, postContent);
            stmt.setInt(2, anon);
            stmt.setFloat(3, latitude);
            stmt.setFloat(4, longitude);
            stmt.setInt(5, messageId);
            stmt.setString(6, time);
            stmt.setString(7, username);

            stmt.executeUpdate();
            return true;

        } catch (SQLException e)
        {
            System.out.println("Could not send post to db");
            return false;
        }
    }
}
