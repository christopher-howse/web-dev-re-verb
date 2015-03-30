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
                    "primary key (message_id)," +
                    "foreign key(username) references Users(username) ON UPDATE CASCADE)";

    private static String selectByUsername =
            "SELECT * FROM Messages WHERE username = ? AND Reply_link = 0";

    private static String selectById =
            "SELECT * FROM Messages WHERE message_id=?;";

    private static String selectAll =
            "SELECT * FROM Messages;";

    private static String selectReplies =
            "SELECT * FROM Messages WHERE reply_link = ?;";

    private static String selectByLocation =
            "SELECT " +
            "message_id, username, message_body, anon_flag, latitude, longitude, report_count, vote_count, reply_link, create_time " +
            "FROM Messages " +
            "WHERE create_time <= CURRENT_TIMESTAMP and Reply_link = 0 " +
            "ORDER BY create_time desc";

    private static String insertIntoMessages =
            "INSERT INTO Messages VALUES(NULL, ?, ?, ?, ?," +
                    "0, 0, ?, ?, ?)";

    private static String deleteMessage =
            "DELETE FROM Messages WHERE message_id = ?";

    private static String deleteMessageByUser =
            "DELETE FROM Messages WHERE message_id = ? AND username = ?";

    private static String getFavoritesByUserAndMessage =
            "SELECT * FROM Favorites WHERE message_id = ? AND username = ?";

    private static String selectFavoriteCount =
            "SELECT COUNT(*) AS favCount FROM Favorites WHERE message_id = ?";

    public PostManager() throws SQLException
    {
        try (
                Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement( createMessageTable );
        ) {
            stmt.setQueryTimeout(DatabaseManager.timeout);
            stmt.executeUpdate();
            System.out.println("Created message table");
        }
    }

    public Post getMessageById(int messageId, String username)
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
                boolean isFavorited = isMessageFavoritedByUser(username, rs.getInt("message_id"));
                boolean isUser = rs.getString("username").equals(username);
                boolean isAnon = (rs.getInt("anon_flag") != 0);
                result = new Post( isAnon ? "Anonymous" : rs.getString("username"), rs.getInt("message_id"), rs.getString("message_body"),rs.getString("create_time"),isFavorited, isUser, selectFavoriteCount(rs.getInt("message_id")));
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
                boolean isFavorited = isMessageFavoritedByUser(username, rs.getInt("message_id"));
                boolean isUser = rs.getString("username").equals(username);
                boolean isAnon = (rs.getInt("anon_flag") != 0);
                result.add(new Post(isAnon ? "Anonymous" : rs.getString("username"), rs.getInt("message_id"), rs.getString("message_body"),rs.getString("create_time"), isFavorited, isUser, selectFavoriteCount(rs.getInt("message_id"))));
            }
        } catch (SQLException e)
        {
            System.out.println("Could not get posts for user: " + username);
        }

        return result;
    }

    public ArrayList<Post> getPostsByLocation(String lat,String lon,String username)
    {
        ArrayList<Post> result = new ArrayList<Post>();
        try(
                Connection conn = DriverManager.getConnection(DatabaseManager.dbURL);
                PreparedStatement stmt = conn.prepareStatement( selectByLocation );
        ) {
            stmt.setQueryTimeout(DatabaseManager.timeout);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                double distance = 6371 * Math.acos(
                        Math.cos(Math.toRadians(Float.parseFloat(lat)))
                                * Math.cos(Math.toRadians(rs.getFloat("latitude")))
                                * Math.cos(Math.toRadians(rs.getFloat("longitude")) - Math.toRadians(Float.parseFloat(lon)))
                                + Math.sin(Math.toRadians(Float.parseFloat(lat)))
                                * Math.sin(Math.toRadians(rs.getFloat("latitude")))
                );

                if (distance <= 1)
                {
                    boolean isFavorited = isMessageFavoritedByUser(username, rs.getInt("message_id"));
                    boolean isAnon = (rs.getInt("anon_flag") != 0);
                    boolean isUser = rs.getString("username").equals(username);
                    result.add(new Post(isAnon ? "Anonymous" : rs.getString("username"), rs.getInt("message_id"), rs.getString("message_body"), rs.getString("create_time"), isFavorited, isUser, selectFavoriteCount(rs.getInt("message_id"))));
                }
            }
        } catch (SQLException e)
        {
            System.out.println("Could not get posts at: " + lat + "," + lon);
        }

        return result;

    }

    public ArrayList<Post> getReplies(int messageId, String username)
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
                boolean isFavorited = isMessageFavoritedByUser(username, rs.getInt("message_id"));
                boolean isUser = rs.getString("username").equals(username);
                boolean isAnon = (rs.getInt("anon_flag") != 0);
                result.add(new Post(isAnon ? "Anonymous" : rs.getString("username"), rs.getInt("message_id"), rs.getString("message_body"),rs.getString("create_time"),isFavorited, isUser, selectFavoriteCount(rs.getInt("message_id"))));
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
            System.out.println("Deleted message with post id: " + messageId);
            result.success = true;
        } catch (SQLException e)
        {
            System.out.println("Could not delete post with post id: " + messageId);
        }

        return result;
    }

    public static boolean deleteMessageByUser(int messageId, String username)
    {
        try (
                Connection conn = DriverManager.getConnection(DatabaseManager.dbURL);
                PreparedStatement stmt = conn.prepareStatement( deleteMessageByUser );
        ) {
            stmt.setQueryTimeout(DatabaseManager.timeout);
            stmt.setInt(1, messageId);
            stmt.setString(2, username);
            stmt.executeUpdate();
            System.out.println("Deleted message with post id: " + messageId);
            return true;
        } catch (SQLException e)
        {
            System.out.println("Could not delete post with post id: " + messageId);
            return false;
        }
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

    public boolean  isMessageFavoritedByUser(String username, int post_id)
    {
        try(
                Connection conn = DriverManager.getConnection(DatabaseManager.dbURL);
                PreparedStatement stmt = conn.prepareStatement( getFavoritesByUserAndMessage );
        ) {
            stmt.setQueryTimeout(DatabaseManager.timeout);
            stmt.setInt(1, post_id);
            stmt.setString(2, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e)
        {
            System.out.println("Could not get favorited post");
            return false;
        }

    }

    public int selectFavoriteCount(int post_id)
    {
        try (
                Connection conn = DriverManager.getConnection(DatabaseManager.dbURL);
                PreparedStatement stmt = conn.prepareStatement( selectFavoriteCount );
        ) {
            stmt.setQueryTimeout(DatabaseManager.timeout);
            stmt.setInt(1, post_id);

            ResultSet rs = stmt.executeQuery();
            return rs.getInt("favCount");
        } catch (SQLException e)
        {
            System.out.println("Could not get fav count");
            return -1;
        }
    }

}
