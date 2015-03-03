package users;

public class User implements Comparable
{
    public final String name;
    public final String password;
    public String role;

    public User( String n, String p, String r ) {
        name = n;
        password = p;
        role = r;
    }

    public void toggleRole()
    {
        if(this.role.equals("admin"))
        {
            this.role = "pleb";
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
