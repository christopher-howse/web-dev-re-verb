package users;

public class User {
    public final String name;
    public final String password;
    public final String role;

    public User( String n, String p, String r ) {
        name = n;
        password = p;
        role = r;
    }
}
