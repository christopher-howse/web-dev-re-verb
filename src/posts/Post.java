package posts;

public class Post
{
    public final String username;
    public final int postId;
    public final String postBody;
    public final String timeStamp;
    public final boolean favorite;
    public final boolean isUser;
    public final int favCount;

    public Post(String username, int postId, String postBody, String timeStamp, boolean favorite, boolean isUser, int favCount)
    {
        this.username = username;
        this.postId = postId;
        this.postBody = postBody;
        this.timeStamp = timeStamp;
        this.favorite = favorite;
        this.isUser = isUser;
        this.favCount = favCount;
    }

    public Post()
    {
        this.username = null;
        this.postId = -1;
        this.postBody = null;
        this.timeStamp = null;
        this.favorite = false;
        this.isUser = false;
        this.favCount = 0;
    }
}
