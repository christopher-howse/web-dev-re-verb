package users;

public class User implements Comparable
{
    public final String handle;
    public final String name;
    public final String password;
    public String role;
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

    public void toggleRole()
    {
        if(this.role.equals("admin"))
        {
            this.role = "default";
        }
        else
        {
            this.role = "admin";
        }
    }

    @Override
    public int compareTo(Object o)
    {
        if(o instanceof User)
        {
            if(this.name.equals(((User) o).name)) return 0;
        }
        return -1;
    }
}
