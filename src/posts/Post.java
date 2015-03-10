package posts;

public class Post
{
    public final String username;
    public final int postId;
    public final String postBody;

    public Post(String username, int postId, String postBody)
    {
        this.username = username;
        this.postId = postId;
        this.postBody = postBody;
    }
}
