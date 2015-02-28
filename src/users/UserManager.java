package users;

import java.util.ArrayList;

public class UserManager
{
    public ArrayList<User> getUsers()
    {
        ArrayList<User> result = new ArrayList<User>();
        //TODO: Get users from database;
        //for now fake users
        result.add(new User("test_name", "test_password", "test_role"));
        result.add(new User("test_name2", "test_password", "test_role"));
        result.add(new User("test_name3", "test_password", "test_role"));
        return result;
    }
}
