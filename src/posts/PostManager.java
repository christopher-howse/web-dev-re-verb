package posts;

import utilityDtos.BooleanDto;

import java.util.ArrayList;

public class PostManager
{
    public static ArrayList<Post> getPostsByUser(String username)
    {
        ArrayList<Post> result = new ArrayList<Post>();
        //TODO: Get posts from database;
        //for now fake posts
        result.add(new Post("test username", 1, "post body"));
        result.add(new Post("test username", 2, "post body 2"));
        return result;
    }

    public static BooleanDto deletePost(int postId)
    {
        BooleanDto result = new BooleanDto(false);
        //TODO: delete post from database here
        result.success = true;
        return result;
    }
}
