package posts;

import java.util.ArrayList;

public class PostManager
{
    public static ArrayList<Post> getPostsByUser(String username)
    {
        ArrayList<Post> result = new ArrayList<Post>();
        //TODO: Get posts from database;
        //for now fake posts
        result.add(new Post("username", "post body"));
        result.add(new Post("username", "post body 2"));
        return result;
    }
}
