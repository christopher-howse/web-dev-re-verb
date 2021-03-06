package users;

public class User implements Comparable
{
    public String name;
    public String password;
    public String role;
    public String description;
    public final Integer num_reports;
    public final String accountStatus;

    public User( String n, String p, String r ) {
        name = n;
        password = p;
        role = r;
        description = "";
        num_reports = 0;
        accountStatus = "Active";
    }

    public User( String n, String p, String r, String d ) {
        name = n;
        password = p;
        role = r;
        description = d;
        num_reports = 0;
        accountStatus = "Active";
    }

    public User( String n, String p, String r, String d, Integer num_reports, String accountStatus ) {
        name = n;
        password = p;
        role = r;
        description = d;
        this.num_reports = num_reports;
        this.accountStatus = accountStatus;
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
