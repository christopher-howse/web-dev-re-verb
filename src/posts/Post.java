package posts;

public class Post
{
    public final String username;
    public final int postId;
    public final String postBody;
    public final String timeStamp;

    public Post(String username, int postId, String postBody, String timeStamp)
    {
        this.username = username;
        this.postId = postId;
        this.postBody = postBody;
        this.timeStamp = timeStamp;
    }
}
