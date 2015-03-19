package users;

public class User {
    public final String handle;
    public final String name;
    public final String password;
    public final String role;
    public final String description;
    public final String picture;

    public User( String n, String p, String r ) {
        name = n;
        password = p;
        role = r;
        handle = "defaultHandle";
        description = "defaultDescription";
        picture = "defaultPic";
    }

    public User( String n, String p, String r, String h, String d ) {
        name = n;
        password = p;
        role = r;
        handle = h;
        description = d;
        picture = "defaultPic";
    }
}
